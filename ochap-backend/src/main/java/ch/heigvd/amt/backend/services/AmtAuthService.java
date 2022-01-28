package ch.heigvd.amt.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import ch.heigvd.amt.backend.data.AccountInfoDTO;
import ch.heigvd.amt.backend.data.TokenDTO;
import ch.heigvd.amt.backend.services.AmtAuthService.IncorrectCredentialsException;
import ch.heigvd.amt.backend.services.AmtAuthService.UnacceptableRegistrationException;
import ch.heigvd.amt.backend.services.AmtAuthService.UsernameAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@Component
public class AmtAuthService {
  @Autowired
  public AmtAuthService(@Qualifier("AuthServiceWebClient") WebClient httpClient) {
    this.httpClient = httpClient;
  }

  public static class IncorrectCredentialsException extends Exception {
  }

  public static class UsernameAlreadyExistsException extends Exception {
  }

  @AllArgsConstructor
  @NoArgsConstructor
  public static class UnacceptableRegistrationException extends Exception {
    @Getter
    private List<PropertyError> errors;
  }

  final WebClient httpClient;

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

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PropertyError {
    private String property;
    private String message;
  }

  public Mono<TokenDTO> login(String username, String password) {
    var cmd = new AuthLoginCommand(username, password);
    return httpClient.post().uri("/auth/login").contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON).body(Mono.just(cmd), AuthLoginCommand.class).retrieve()
        .onStatus(HttpStatus.FORBIDDEN::equals, r -> Mono.error(IncorrectCredentialsException::new))
        .bodyToMono(TokenDTO.class);
  }

  public Mono<AccountInfoDTO> register(String username, String password) {
    var cmd = new AccountRegisterCommand(username, password);
    return httpClient.post().uri("/accounts/register").contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON).body(Mono.just(cmd), AccountRegisterCommand.class)
        .retrieve()
        .onStatus(HttpStatus.CONFLICT::equals, r -> Mono.error(UsernameAlreadyExistsException::new))
        .onStatus(HttpStatus.UNPROCESSABLE_ENTITY::equals,
            r -> r.bodyToMono(UnacceptableRegistrationException.class).flatMap(Mono::error))
        .bodyToMono(AccountInfoDTO.class);
  }
}
