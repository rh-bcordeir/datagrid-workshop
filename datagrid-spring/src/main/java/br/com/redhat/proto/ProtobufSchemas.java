package br.com.redhat.proto;

import org.infinispan.protostream.SerializationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProtobufSchemas {
    @Bean
    public SerializationContextInitializer movieSchema() {
        return new MovieSchemaImpl();
    }
}

