package uz.hayotbank.uz.practisecassandra.repository;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.hayotbank.uz.practisecassandra.entity.StateDict;
import uz.hayotbank.uz.practisecassandra.entity.StateId;
@Repository
public interface StateDictRepository extends ReactiveCassandraRepository<StateDict, StateId> {
    Mono<StateDict> findTopByIdCodeAndIdActorAndIdLang(String idCode, String idActor, String idLang);
    Flux<StateDict> findByIdCodeAndIdActor(String code, String actor);
    @Query("SELECT * FROM state_dict WHERE code = ?0 ALLOW FILTERING")
    Flux<StateDict> findByCode(String code);

    @Query("SELECT * FROM state_dict WHERE lang = ?0 ALLOW FILTERING")
    Flux<StateDict> findByLang(String lang);
}
