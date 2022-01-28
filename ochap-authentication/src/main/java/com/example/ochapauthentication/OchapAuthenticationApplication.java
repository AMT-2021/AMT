package com.example.ochapauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;



@SpringBootApplication
public class OchapAuthenticationApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
    return app.sources(OchapAuthenticationApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(OchapAuthenticationApplication.class, args);
  }

}
