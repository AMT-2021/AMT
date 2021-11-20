package ch.heigvd.amt.ochap.usermgmt.data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TokenDTO {
  @NotEmpty
  String token;

  @NotNull
  AccountInfoDTO account;
}
