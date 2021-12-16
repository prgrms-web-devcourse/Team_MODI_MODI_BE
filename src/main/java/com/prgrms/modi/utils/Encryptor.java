package com.prgrms.modi.utils;

import com.google.common.primitives.Longs;
import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

public class Encryptor {

    private static final String KeysetFileName = "keyset.json";

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

    private static KeysetHandle readKeysetFromFile() throws IOException, GeneralSecurityException {
        File keysetFile = new File(KeysetFileName);
        return CleartextKeysetHandle.read(JsonKeysetReader.withFile(keysetFile));
    }

    private static String getCipherBase64(Aead aead, String plaintext, Long associatedData)
        throws GeneralSecurityException {
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] associatedDataBytes = Longs.toByteArray(associatedData);
        byte[] cipher = aead.encrypt(plaintextBytes, associatedDataBytes);

        return Base64.getEncoder().encodeToString(cipher);
    }

}
