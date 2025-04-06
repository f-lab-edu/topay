package com.topay.user.controller.dto.request;

import com.topay.common.utils.encryption.EncryptionService;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LoginRequest {
    private EncryptionService encryptionService;

    private String email;
    private String password;
    private String token;

}
