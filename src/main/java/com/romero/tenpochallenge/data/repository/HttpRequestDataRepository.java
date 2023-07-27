package com.romero.tenpochallenge.data.repository;

import com.romero.tenpochallenge.data.model.RequestData;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpRequestDataRepository extends ReactiveSortingRepository<RequestData, Long> {
}
