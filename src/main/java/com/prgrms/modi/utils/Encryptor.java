package com.prgrms.modi.utils;

import com.google.common.primitives.Longs;
import com.google.crypto.tink.Aead;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.KmsAeadKeyManager;
import com.google.crypto.tink.integration.awskms.AwsKmsClient;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Encryptor {

    private static final String KEYSET_PATH = "Team_MODI_MODI_BE_KEYSET/encrypted-keyset.json";

    private static String arn;

    @Value("${tink.kms-arn}")
    public void setArn(String arn) {
        Encryptor.arn = arn;
    }

    public static String encrypt(String plaintext, Long associatedData) {
        try {
            KeysetHandle keysetHandle = readKeysetFromFile();
            Aead aead = keysetHandle.getPrimitive(Aead.class);
            String cipherBase64 = getCipherBase64(aead, plaintext, associatedData);

            return cipherBase64;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("encryption failed", e);
        } catch (IOException e) {
            throw new RuntimeException("keyset file read failed", e);
        }
    }

    public static String decrypt(String cipherBase64, Long associatedData) {
        try {
            KeysetHandle keysetHandle = readKeysetFromFile();
            Aead aead = keysetHandle.getPrimitive(Aead.class);
            String decipher = getOriginalText(aead, cipherBase64, associatedData);

            return decipher;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("decryption failed", e);
        } catch (IOException e) {
            throw new RuntimeException("keyset file read failed", e);
        }
    }

    private static KeysetHandle readKeysetFromFile() throws IOException, GeneralSecurityException {
        AwsKmsClient.register(Optional.of(arn), Optional.empty());
        KeysetHandle handle = KeysetHandle.generateNew(KmsAeadKeyManager.createKeyTemplate(arn));
        Aead kekAead = handle.getPrimitive(Aead.class);
        InputStream keysetStream = ClassLoader.getSystemClassLoader().getResourceAsStream(KEYSET_PATH);

        return KeysetHandle.read(JsonKeysetReader.withInputStream(keysetStream), kekAead);
    }

    private static String getCipherBase64(Aead aead, String plaintext, Long associatedData)
        throws GeneralSecurityException {
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] associatedDataBytes = Longs.toByteArray(associatedData);
        byte[] cipher = aead.encrypt(plaintextBytes, associatedDataBytes);

        return Base64.getEncoder().encodeToString(cipher);
    }

    private static String getOriginalText(Aead aead, String cipherBase64, Long associatedData)
        throws GeneralSecurityException {
        byte[] cipher = com.google.crypto.tink.subtle.Base64.decode(cipherBase64);
        byte[] associatedDataBytes = Longs.toByteArray(associatedData);
        byte[] decipher = aead.decrypt(cipher, associatedDataBytes);

        return new String(decipher);
    }

}
