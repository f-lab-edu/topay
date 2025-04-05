package com.topay.common.utils.encrytion;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;

/**
 * 입력 문자열을 SHA-256 해시 알고리즘으로 암호화한다.
 * <p>
 * 1. 문자열을 바이트 배열로 변환 후, SHA-256 해시 계산
 * 2. 계산된 해시 바이트 배열을 16진수 문자열로 변환하여 반환
 * 3. 암호화 도중 예외 발생 시 원본 문자열 반환
 */
@Component
public class SHA256EncryptionService implements EncryptionService {

    @Override
    public String encrypt(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] passBytes = s.getBytes();
            md.reset();

            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();

            for (byte b : digested) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            return s;
        }
    }
    
}
