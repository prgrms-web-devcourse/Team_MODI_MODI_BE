package com.prgrms.modi.utils;

import com.google.common.primitives.Longs;
import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.subtle.Base64;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class Decryptyor {

    private static final String KeysetFileName = "keyset.json";

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
        File keysetFile = new File(KeysetFileName);
        return CleartextKeysetHandle.read(JsonKeysetReader.withFile(keysetFile));
    }

    private static String getOriginalText(Aead aead, String cipherBase64, Long associatedData)
        throws GeneralSecurityException {
        byte[] cipher = Base64.decode(cipherBase64);
        byte[] associatedDataBytes = Longs.toByteArray(associatedData);
        byte[] decipher = aead.decrypt(cipher, associatedDataBytes);

        return new String(decipher);
    }

}
