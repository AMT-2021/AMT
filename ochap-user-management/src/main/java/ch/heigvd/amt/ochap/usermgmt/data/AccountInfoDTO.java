package ch.heigvd.amt.ochap.usermgmt.data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountInfoDTO {
  @NotNull
  Long id;

  @NotEmpty
  String username;

  @NotEmpty
  String role;
}
