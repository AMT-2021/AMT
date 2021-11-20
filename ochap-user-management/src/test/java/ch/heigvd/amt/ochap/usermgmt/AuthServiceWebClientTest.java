package ch.heigvd.amt.ochap.usermgmt;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@SpringBootTest
@ContextConfiguration(initializers = AuthServiceWebClientTest.ContextInitializer.class)
public class AuthServiceWebClientTest {
  static final MockWebServer mockWebServer = new MockWebServer();
  static final String authMountPoint = "/auth";

  public static class ContextInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableApplicationContext,
          "authServiceBaseUrl=" + mockWebServer.url(authMountPoint).toString());
    }
  }

  @Value("${authServiceBaseUrl}")
  String loadedBaseUrl;

  @Test
  void contextHasAuthValues() {
    StringUtils.hasLength(loadedBaseUrl);
    assertTrue("Context loads baseUrl", StringUtils.hasLength(loadedBaseUrl));
  }

  @Autowired
  @Qualifier("AuthServiceWebClient")
  WebClient underTest;

  @Test
  void authServiceWebClientSetsBaseUrlAndJwt() throws Exception {
    String expected = "Success";
    mockWebServer.enqueue(new MockResponse().setBody(expected));
    assertEquals(expected,
        underTest.get().uri("/path").retrieve().bodyToMono(String.class).block());

    var req = mockWebServer.takeRequest();
    assertEquals(authMountPoint + "/path", req.getPath());
  }
}
