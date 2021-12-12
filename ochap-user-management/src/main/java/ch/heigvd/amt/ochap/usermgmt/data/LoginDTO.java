package ch.heigvd.amt.ochap.usermgmt.data;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class LoginDTO {
  @NotEmpty(message = "Username cannot be empty.")
  private String username;

  @NotEmpty(message = "Password cannot be empty.")
  private String password;
}
