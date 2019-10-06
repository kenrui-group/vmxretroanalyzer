package com.kenrui.retroanalyzer.database.repositories;

import com.kenrui.retroanalyzer.database.entities.Point;
import com.kenrui.retroanalyzer.database.entities.RepeatingCorrelationIdGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatingCorrelationGroupsRepository extends JpaRepository<RepeatingCorrelationIdGroups, Long> {
}
