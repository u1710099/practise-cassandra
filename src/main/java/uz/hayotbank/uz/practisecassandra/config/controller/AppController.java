package uz.hayotbank.uz.practisecassandra.config.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.hayotbank.uz.practisecassandra.entity.StateDict;
import uz.hayotbank.uz.practisecassandra.repository.StateDictRepository;

@RestController
@RequestMapping("/api/states")
public class AppController {

    private final StateDictRepository repository;

    public AppController(StateDictRepository repository) {
        this.repository = repository;
    }


    @GetMapping
    public Flux<StateDict> getAllStates() {
        return repository.findAll();
    }

    @GetMapping("/{code}/{actor}/{lang}")
    public Mono<ResponseEntity<StateDict>> getState(
            @PathVariable String code,
            @PathVariable String actor,
            @PathVariable String lang) {
        return repository.findTopByIdCodeAndIdActorAndIdLang(code, actor, lang)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public Flux<StateDict> searchStates(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) String lang) {

        if (code != null && actor != null && lang != null) {
            return repository.findTopByIdCodeAndIdActorAndIdLang(code, actor, lang).flux();
        } else if (code != null && actor != null) {
            return repository.findByIdCodeAndIdActor(code, actor);
        } else if (code != null) {
            return repository.findByCode(code);
        }
        else if (lang !=null){
            return repository.findByLang(lang);
        }
        else {
            return repository.findAll();
        }
    }

    @PostMapping
    public Mono<StateDict> createState(@RequestBody StateDict stateDict) {
        return repository.save(stateDict);
    }

    @PutMapping("/{code}/{actor}/{lang}")
    public Mono<ResponseEntity<StateDict>> updateState(
            @PathVariable String code,
            @PathVariable String actor,
            @PathVariable String lang,
            @RequestBody StateDict stateDict) {
        return repository.findTopByIdCodeAndIdActorAndIdLang(code, actor, lang)
                .flatMap(existing -> repository.save(stateDict).map(ResponseEntity::ok))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
