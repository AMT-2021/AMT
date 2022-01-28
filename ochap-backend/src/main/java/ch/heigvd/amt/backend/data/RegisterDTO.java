package ch.heigvd.amt.backend.data;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterDTO {
  @Size(min = 4, message = "Username must have at least 4 characters.")
  private String username;
  @Size(min = 4, message = "Password must have at least 4 characters.")
  private String password;
}
