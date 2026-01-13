package br.com.redhat.config;

import br.com.redhat.dto.MovieSchema;
import br.com.redhat.dto.MovieSchemaImpl;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.spring.starter.remote.InfinispanRemoteCacheCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class InfinispanConfig {

    @Bean
    public MovieSchema moviesSchema() {
        return new MovieSchemaImpl();
    }

    @Bean
    public InfinispanRemoteCacheCustomizer protostreamCustomizer(MovieSchema schema) {
        return (ConfigurationBuilder builder) -> builder.addContextInitializer(schema);
    }
}

