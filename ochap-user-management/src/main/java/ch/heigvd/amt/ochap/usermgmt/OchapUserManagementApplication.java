package ch.heigvd.amt.ochap.usermgmt;

import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@SpringBootApplication
public class OchapUserManagementApplication extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
    return app.sources(OchapUserManagementApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(OchapUserManagementApplication.class, args);
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
