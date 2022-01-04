package com.prgrms.modi.common.configuration;

import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.integration.awskms.AwsKmsClient;
import java.security.GeneralSecurityException;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TinkerConfiguration {

    @Bean
    public void registerAeadConfig() throws GeneralSecurityException {
        AeadConfig.register();
    }

}
