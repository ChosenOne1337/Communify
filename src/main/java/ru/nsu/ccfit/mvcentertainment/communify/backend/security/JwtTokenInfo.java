package ru.nsu.ccfit.mvcentertainment.communify.backend.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenInfo {

    private Long userId;
    private String userName;
    private Long expirationDateMillis;

}
