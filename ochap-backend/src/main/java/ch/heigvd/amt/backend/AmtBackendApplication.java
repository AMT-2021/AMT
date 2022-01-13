package ch.heigvd.amt.backend;

import java.net.URL;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

@SpringBootApplication
@EnableWebSecurity
public class AmtBackendApplication extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
    return app.sources(AmtBackendApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(AmtBackendApplication.class, args);
  }

  @Bean
  public JWTVerifier jwtParser(@Value("${authServiceJwtSecret}") String secret) {
    return JWT.require(Algorithm.HMAC256(secret)).build();
  }

  @Configuration
  public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    public final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfiguration(JwtFilter jwtFilter) {
      this.jwtFilter = jwtFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      // @formatter:off
      http
        .authorizeRequests()
          .antMatchers("/**/*").permitAll()
          .and()
        .formLogin().disable()
        .httpBasic().disable()
        .logout().disable()
        .csrf().disable()
        .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
      // @formatter:on
      http.addFilterBefore(jwtFilter, BasicAuthenticationFilter.class);
    }
  }

  @Configuration
  @EnableGlobalMethodSecurity(jsr250Enabled = true)
  public static class SecurityRoleFilter {
  }

  @Bean
  public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver,
      SpringSecurityDialect sec) {
    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
    templateEngine.addDialect(sec); // "sec" tags
    return templateEngine;
  }

  @Bean("AuthServiceWebClient")
  public WebClient authServiceWebClient(@Value("${authServiceBaseUrl}") String baseUrl)
      throws Exception {
    URL parsed = new URL(baseUrl); // Validates the specified URL is valid.
    if (!StringUtils.hasLength(parsed.getProtocol()) || !StringUtils.hasLength(parsed.getHost()))
      throw new Exception(
          "The specified base URL for the authentication service is invalid: " + parsed.toString());
    return WebClient.builder().uriBuilderFactory(new DefaultUriBuilderFactory(baseUrl)).build();
  }
}
