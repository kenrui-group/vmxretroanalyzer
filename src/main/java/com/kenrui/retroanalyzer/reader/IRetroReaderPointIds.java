package com.kenrui.retroanalyzer.reader;

import com.google.common.collect.Multimap;
import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;

import java.util.List;

public interface IRetroReaderPointIds {
    public long getLinesRead();

    public List<TimePointId> getListOfPointIds();
}
