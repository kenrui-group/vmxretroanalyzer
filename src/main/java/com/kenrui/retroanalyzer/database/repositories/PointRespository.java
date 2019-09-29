package com.kenrui.retroanalyzer.database.repositories;

import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;
import com.kenrui.retroanalyzer.database.entities.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.NamedNativeQuery;
import java.util.List;

public interface PointRespository extends JpaRepository<Point, Long> {
    List<TimePointId> findPointIds();
}
