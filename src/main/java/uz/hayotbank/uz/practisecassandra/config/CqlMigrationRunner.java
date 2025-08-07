package uz.hayotbank.uz.practisecassandra.config;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

@Component
public class CqlMigrationRunner implements CommandLineRunner {

    private final CqlSession session;
    private final ResourcePatternResolver resolver;
    private final MessageSource messageSource;

    public CqlMigrationRunner(CqlSession session, ResourceLoader resourceLoader, MessageSource messageSource) {
        this.session = session;
        this.resolver = (ResourcePatternResolver) resourceLoader;
        this.messageSource = messageSource;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create migration history table with author
        session.execute("""
                    CREATE TABLE IF NOT EXISTS schema_migration (
                        filename text PRIMARY KEY,
                        executed_at timestamp,
                        author text
                    );
                """);

        // Load and sort files
        Resource[] resources = resolver.getResources("classpath:liquibase/changelog/*.cql");
        Arrays.sort(resources, Comparator.comparing(Resource::getFilename));

        for (Resource resource : resources) {
            String filename = resource.getFilename();

            // Extract author from filename: 001-something_by_author.cql
            String author = "unknown";
            if (filename != null && filename.contains("_by_")) {
                int start = filename.indexOf("_by_") + 4;
                int end = filename.lastIndexOf(".cql");
                author = filename.substring(start, end);
            }

            // Check if file was applied
            ResultSet result = session.execute(
                    SimpleStatement.newInstance("SELECT filename FROM schema_migration WHERE filename = ?", filename)
            );

            if (!result.iterator().hasNext()) {
                // Run CQL
                String cql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                session.execute(cql);

                // Record with author
                session.execute(
                        SimpleStatement.newInstance(
                                "INSERT INTO schema_migration (filename, executed_at, author) VALUES (?, toTimestamp(now()), ?)",
                                filename, author
                        )
                );

                System.out.println("✅ Executed CQL: " + filename + " by " + author);
            } else {
                System.out.println("⏭️ Skipped (already applied): " + filename);
            }
        }

        System.out.println("✅ All pending CQL migrations executed.");
    }
}