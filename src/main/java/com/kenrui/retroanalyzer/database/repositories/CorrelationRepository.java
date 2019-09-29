package com.kenrui.retroanalyzer.database.repositories;

import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;
import com.kenrui.retroanalyzer.database.entities.Correlation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorrelationRepository extends JpaRepository<Correlation, Long> {
    List<TimeCorrelationId> findCorrelationIdsForOneTickToMultipleQuotes();

    List<TimeCorrelationId> findCorrelationIdsForOneTickToOneQuote();

    List<TimeCorrelationId> findCorrelationIdsForDifferentTicksToSameQuote();

    List<TimeCorrelationId> findCorrelationIdsForOneTickToNoQuote();

    List<Object[]> findCorrelatedPoints();

}
