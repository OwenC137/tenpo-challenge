package com.romero.tenpochallenge.data.repository;

import com.romero.tenpochallenge.data.model.RequestData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RequestDataRepository extends ReactiveCrudRepository<RequestData, Long> {
    Flux<RequestData> findAllBy(Pageable pageable);
}
