package com.kenrui.retroanalyzer.reader;

import java.util.HashMap;
import java.util.Map;

public class DecodedMessages {

    private Map<String, MessageContents> decodedMessages;

    public DecodedMessages() {
        this.decodedMessages = new HashMap<>();
    }

    public void addNewMessage(String keyValueForThisLine, MessageContents messageContents) {
        decodedMessages.put(keyValueForThisLine, messageContents);
    }

    public boolean hasKeyValueForTheLine(String keyValueForThisLine) {
        return decodedMessages.containsKey(keyValueForThisLine);
    }
}
