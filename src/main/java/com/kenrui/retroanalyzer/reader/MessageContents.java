package com.kenrui.retroanalyzer.reader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class MessageContents {

    private Map<String, String> fieldValuePairs;

    public MessageContents() {
        this.fieldValuePairs = new HashMap();
    }

    public void addFieldValuePair(String field, String value) {
        fieldValuePairs.put(field, value);
    }

    public String getField(String field) {
        return fieldValuePairs.get(field);
    }

    public boolean hasValue(String value) {
        return fieldValuePairs.containsValue(value);
    }

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(fieldValuePairs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}
