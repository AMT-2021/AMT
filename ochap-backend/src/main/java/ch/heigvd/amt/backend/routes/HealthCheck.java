package ch.heigvd.amt.backend.routes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//TODO NGY - purpose of this route ?
public class HealthCheck {
  @GetMapping("/health")
  public String health() {
    return "OK";
  }
}
