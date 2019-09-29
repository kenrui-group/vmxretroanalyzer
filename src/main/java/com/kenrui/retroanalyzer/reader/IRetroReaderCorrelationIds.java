package com.kenrui.retroanalyzer.reader;

import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;

import java.util.List;

public interface IRetroReaderCorrelationIds {

    public List<TimeCorrelationId> getListOfCorrelationIdsForOneTickToMultipleQuotes();

    public List<TimeCorrelationId> getListOfCorrelationIdsForOneTickToOneQuote();

    //    public List<TimeCorrelationId> getListOfCorrelationIdsForDifferentTicksToSameQuote();
    public List<TimeCorrelationId> getListOfCorrelationIdsForDifferentTicksToSameQuote();

    public List<TimeCorrelationId> getListOfCorrelationIdsForOneTickToNoQuote();

    public long getLinesRead();
}
