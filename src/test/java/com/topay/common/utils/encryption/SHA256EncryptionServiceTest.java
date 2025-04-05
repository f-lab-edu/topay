package com.topay.common.utils.encryption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SHA256EncryptionServiceTest {
    private SHA256EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new SHA256EncryptionService();
    }

    @Test
    @DisplayName("SHA-256 해시 암호화 테스트 - 일반 문자열")
    void testEncryptWithValidInput() {
        // given
        String input = "password";
        String expected = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

        // when
        String actual = encryptionService.encrypt(input);

        // then
        assertEquals(expected, actual, "SHA-256 해시가 예상 결과와 일치해야 한다.");
    }

    @Test
    @DisplayName("SHA-256 해시 암호화 테스트 - 빈 문자열")
    void testEncryptWithEmptyString() {
        // given
        String input = "";
        String expected = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

        // when
        String actual = encryptionService.encrypt(input);

        // then
        assertEquals(expected, actual, "빈 문자열에 대한 SHA-256 해시가 예상 결과와 일치해야 한다.");
    }

    @Test
    @DisplayName("SHA-256 해시 암호화 테스트 - null 입력")
    void testEncryptWithNull() {
        // given
        String input = null;

        // when
        String actual = encryptionService.encrypt(input);

        // then
        assertNull(actual, "null 입력 시, 결과도 null이어야 한다.");
    }

}