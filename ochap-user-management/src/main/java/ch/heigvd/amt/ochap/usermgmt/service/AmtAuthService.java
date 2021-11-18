package ch.heigvd.amt.ochap.usermgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import ch.heigvd.amt.ochap.usermgmt.data.AccountInfoDTO;
import ch.heigvd.amt.ochap.usermgmt.data.LoginDTO;
import ch.heigvd.amt.ochap.usermgmt.data.RegisterDTO;
import ch.heigvd.amt.ochap.usermgmt.data.TokenDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Mono;

@Component
public class AmtAuthService {
  public static class IncorrectCredentialsException extends Exception {
  }

  public static class UsernameAlreadyExistsException extends Exception {
  }

  @Autowired
  @Qualifier("AuthServiceWebClient")
  WebClient httpClient;

  @Data
  @AllArgsConstructor
  private static class AuthLoginCommand {
    String username;
    String password;
  }

  @Data
  @AllArgsConstructor
  private static class AccountRegisterCommand {
    String username;
    String password;
  }

  public Mono<TokenDTO> login(String username, String password)
      throws IncorrectCredentialsException {
    var cmd = new AuthLoginCommand(username, password);
    return httpClient.post().uri("/auth/login").contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON).body(Mono.just(cmd), AuthLoginCommand.class).retrieve()
        .onStatus(HttpStatus.FORBIDDEN::equals, r -> Mono.error(IncorrectCredentialsException::new))
        .bodyToMono(TokenDTO.class);
  }

  public Mono<AccountInfoDTO> register(String username, String password)
      throws UsernameAlreadyExistsException {
    var cmd = new AccountRegisterCommand(username, password);
    return httpClient.post().uri("/accounts/register").contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON).body(Mono.just(cmd), AccountRegisterCommand.class)
        .retrieve()
        .onStatus(HttpStatus.CONFLICT::equals, r -> Mono.error(UsernameAlreadyExistsException::new))
        .bodyToMono(AccountInfoDTO.class);
  }
}
