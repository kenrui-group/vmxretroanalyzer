package com.kenrui.retroanalyzer.reader;

import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;
import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;
import com.kenrui.retroanalyzer.database.entities.Correlation;
import com.kenrui.retroanalyzer.database.entities.Point;

import java.util.List;
import java.util.Map;

public interface IRetroReaderCorrelationIds {

    public List<TimeCorrelationId> getListOfCorrelationIdsForOneTickToMultipleQuotes();

    public List<TimeCorrelationId> getListOfCorrelationIdsForOneTickToOneQuote();

    public List<TimeCorrelationId> getListOfCorrelationIdsForDifferentTicksToSameQuote();

    public List<TimeCorrelationId> getListOfCorrelationIdsForOneTickToNoQuote();

    public long getLinesRead();

    public Map<TimeCorrelationId, TimePointId> correlatePoints();
}
