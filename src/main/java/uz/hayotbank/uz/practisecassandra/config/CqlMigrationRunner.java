package uz.hayotbank.uz.practisecassandra.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CassandraMigrationRunner implements ApplicationRunner {

    private final CqlSession adminSession;

    public CassandraMigrationRunner(@Qualifier("adminSession") CqlSession adminSession) {
        this.adminSession = adminSession;
    }

    @Override
    public void run(ApplicationArguments args) {
        // 1. Create keyspace
        adminSession.execute("""
            CREATE KEYSPACE IF NOT EXISTS my_keyspace
            WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
        """);

        // 2. Create table in that keyspace
        adminSession.execute("""
            CREATE TABLE IF NOT EXISTS my_keyspace.state_dict (
                code text,
                actor text,
                lang text,
                title text,
                message text,
                imageUrl text,
                PRIMARY KEY ((code, actor), lang)
            );
        """);

        System.out.println("✔ Keyspace and table created successfully.");
    }
}
