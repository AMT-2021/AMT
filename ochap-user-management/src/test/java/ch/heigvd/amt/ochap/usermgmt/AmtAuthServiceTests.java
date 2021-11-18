package ch.heigvd.amt.ochap.usermgmt;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.heigvd.amt.ochap.usermgmt.data.AccountInfoDTO;
import ch.heigvd.amt.ochap.usermgmt.data.TokenDTO;
import ch.heigvd.amt.ochap.usermgmt.service.AmtAuthService;

@EnabledIfSystemProperty(named = "authServiceBaseUrl", matches = "\\S+",
    disabledReason = "Test cannot be run if Auth Service is not known.")
@SpringBootTest
public class AmtAuthServiceTests {
  @Autowired
  private AmtAuthService amtAuthServer;

  @Test
  void createdUsersCanLogin() throws Exception {
    var tsFormat = DateTimeFormatter.ofPattern("yyMMdd-HHmmssSS");
    var username = "test-ochap-" + LocalDateTime.now().format(tsFormat);
    var password = "OChap$" + UUID.randomUUID().toString();

    AccountInfoDTO regResult = amtAuthServer.register(username, password).block();
    assertNotNull(regResult.getId());
    assertEquals(username, regResult.getUsername());

    TokenDTO loginResult = amtAuthServer.login(username, password).block();
    assertNotNull(loginResult.getToken());
    assertEquals(regResult, loginResult.getAccount());
  }
}
