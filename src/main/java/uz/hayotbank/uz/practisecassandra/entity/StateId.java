package uz.hayotbank.uz.practisecassandra.entity;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@PrimaryKeyClass
public record StateId(
        @PrimaryKeyColumn(name = "code", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
        String code,
        @PrimaryKeyColumn(name = "actor", type = PrimaryKeyType.PARTITIONED, ordinal = 1)
        String actor,
        @PrimaryKeyColumn(name = "lang", type = PrimaryKeyType.CLUSTERED, ordinal = 2)
        String lang
) implements Serializable {
}
