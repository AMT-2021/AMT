package ch.heigvd.amt.backend;

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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.InputMismatchException;

@SpringBootApplication
@EnableWebSecurity
public class AmtBackendApplication extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
    return app.sources(AmtBackendApplication.class);
  }

  public static void main(String[] args) {
    /*
     * // créer le dossier uploads/ Path uploadDir = Paths.get("hatPhotos"); try {
     * Files.createDirectories(uploadDir); } catch (Exception e) { e.printStackTrace(); } Path
     * filePath = uploadDir.resolve("default.png"); // get image from ressource try { File file =
     * ResourceUtils.getFile("classpath:img/default.png"); Files.copy(file.toPath(), filePath,
     * StandardCopyOption.REPLACE_EXISTING); } catch (IOException e) { e.printStackTrace(); }
     */
    SpringApplication.run(AmtBackendApplication.class, args);
  }

  @Bean
  public JWTVerifier jwtParser(@Value("${authServiceJwtSecret}") String secret) throws Exception {
    return JWT.require(Algorithm.HMAC256(secret)).build();
  }

  @Configuration
  public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    public JwtFilter jwtFilter;

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

  @Bean
  public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver,
      SpringSecurityDialect sec) {
    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
    templateEngine.addDialect(sec); // "sec" tags
    return templateEngine;
  }
}
