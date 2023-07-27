package com.romero.tenpochallenge.data.repository;

import com.romero.tenpochallenge.data.model.RequestData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestDataRepository extends ReactiveCrudRepository<RequestData, Long> {
}
