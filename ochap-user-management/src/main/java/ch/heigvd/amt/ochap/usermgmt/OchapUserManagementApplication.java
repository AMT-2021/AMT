package ch.heigvd.amt.ochap.usermgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class OchapUserManagementApplication extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
    return app.sources(OchapUserManagementApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(OchapUserManagementApplication.class, args);
  }
}
