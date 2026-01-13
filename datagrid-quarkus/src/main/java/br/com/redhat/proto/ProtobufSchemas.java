package br.com.redhat.proto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.infinispan.protostream.SerializationContextInitializer;

@ApplicationScoped
public class ProtobufSchemas {
  @Produces
  SerializationContextInitializer movieSchema() {
    return new MovieSchemaImpl();
  }
}
