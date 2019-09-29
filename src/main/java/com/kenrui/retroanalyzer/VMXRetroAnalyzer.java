package com.kenrui.retroanalyzer;

import com.kenrui.retroanalyzer.reader.RetroReaderPointIds;

public class VMXRetroAnalyzer {

    private RetroReaderPointIds point25;
    private RetroReaderPointIds point5wa;

    public VMXRetroAnalyzer() {
//        point25 = new RetroReaderPointIds("C:\\Users\\Titus\\Desktop\\point25.txt", ",", "Point 25", "OUTPUT_EVENT_ID");
//        point5wa = new RetroReaderPointIds("C:\\Users\\Titus\\Desktop\\point5wa.txt", ",", "Point 5wa", "STD_TIME_INFO");

        point25.parseFile();
        point5wa.parseFile();
    }

    public void correlate() {
        point25.getDecodedMessages();
    }

    public void main(String[] args) {
        VMXRetroAnalyzer vmxRetroAnalyzer = new VMXRetroAnalyzer();
        correlate();
    }
}
