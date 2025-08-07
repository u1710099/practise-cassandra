package uz.hayotbank.uz.practisecassandra.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.*;
import org.springframework.data.cassandra.ReactiveSession;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.cql.session.DefaultBridgedReactiveSession;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import java.net.InetSocketAddress;

@Configuration
@EnableReactiveCassandraRepositories(basePackages = "uz.hayotbank.uz.practisecassandra.repository")
public class CassandraConfig {

//    @Bean(name = "adminSession")
//    public CqlSession adminSession() {
//        return CqlSession.builder()
//                .withLocalDatacenter("datacenter1")
//                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
//                .build();
//    }

//    @Bean
//    @Primary
//    @Lazy // ⬅ Lazy initialization prevents premature connection
//    public CqlSession sessionWithKeyspace() {
//        return CqlSession.builder()
//                .withKeyspace("my_keyspace")
//                .withLocalDatacenter("datacenter1")
//                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
//                .build();
//    }

    @Bean
    public ReactiveCassandraTemplate reactiveCassandraTemplate(CqlSession cqlSession,
                                                               CassandraConverter converter) {
        ReactiveSession reactiveSession = new DefaultBridgedReactiveSession(cqlSession);
        return new ReactiveCassandraTemplate(reactiveSession, converter);
    }


}
