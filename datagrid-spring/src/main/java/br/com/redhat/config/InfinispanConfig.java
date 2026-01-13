package br.com.redhat.config;

import br.com.redhat.proto.Movie;
import br.com.redhat.proto.MovieList;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.protostream.SerializationContextInitializer;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class InfinispanConfig {

    @Value("${infinispan.hosts}")
    private String hosts;

    @Value("${infinispan.use-auth:true}")
    private boolean useAuth;

    @Value("${infinispan.username:developer}")
    private String username;

    @Value("${infinispan.password}")
    private String password;

    private RemoteCacheManager cacheManager;

    @Bean
    public RemoteCacheManager remoteCacheManager(SerializationContextInitializer schemaInitializer) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServers(hosts);
        
        if (useAuth) {
            builder.security()
                    .authentication()
                    .username(username)
                    .password(password)
                    .realm("default")
                    .saslMechanism("DIGEST-MD5");
        }

        builder.addContextInitializer(schemaInitializer);

        this.cacheManager = new RemoteCacheManager(builder.build());
        this.cacheManager.start();
        return this.cacheManager;
    }

    @PreDestroy
    public void destroy() {
        if (cacheManager != null) {
            cacheManager.stop();
        }
    }

    @Bean
    @DependsOn("remoteCacheManager")
    public RemoteCache<String, MovieList> movieListCache(RemoteCacheManager cacheManager) {
        return cacheManager.getCache("movies");
    }

    @Bean
    @DependsOn("remoteCacheManager")
    public RemoteCache<String, Movie> movieCache(RemoteCacheManager cacheManager) {
        return cacheManager.getCache("movies");
    }
}

