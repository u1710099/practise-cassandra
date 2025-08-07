package uz.hayotbank.uz.practisecassandra.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Table("state_dict")
public record StateDict(
        @PrimaryKey
        StateId id,
        String title,
        String message,
        String imageUrl
) implements Serializable {
}
