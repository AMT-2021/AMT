package ch.heigvd.amt.backend;

import java.io.IOException;// TODO Review NGY - need compiler option
import java.time.Instant;// TODO Review NGY - unused import statement
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {
  @Autowired // TODO NGY Field injection is not recommended
  JWTVerifier jwtVerifier;

  // TODO NGY method's name to review... and why not using try catch bloc in the corps methods to
  // deal with exception
  private void trySetJwtBasedAuthentication(HttpServletRequest request) {
    var cookies = request.getCookies();
    if (cookies == null)
      return;

    var authCookie = Arrays.stream(cookies).filter(c -> "token".equals(c.getName())).findAny()
        .map(c -> c.getValue());
    if (authCookie.isEmpty())
      return;

    DecodedJWT jwt = jwtVerifier.verify(authCookie.get());

    var issued = ZonedDateTime.ofInstant(jwt.getIssuedAt().toInstant(), ZoneOffset.UTC);
    var expires = ZonedDateTime.ofInstant(jwt.getExpiresAt().toInstant(), ZoneOffset.UTC);
    var now = ZonedDateTime.now();

    if (now.isBefore(issued) || now.isAfter(expires)) {
      return; // Token not yet / no longer valid.//TODO NGY How the client is informed about this
              // issue ?
    }

    String subject = jwt.getSubject();
    String role = jwt.getClaim("role").asString();

    var auth = new PreAuthenticatedAuthenticationToken(subject, null,
        Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      trySetJwtBasedAuthentication(request);
    } catch (RuntimeException e) {
      // Silently ignore JWT token erros: If a route has security requirements.
    }
    filterChain.doFilter(request, response);
  }
}
