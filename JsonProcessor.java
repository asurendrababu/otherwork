package com.csc.hzlcstTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonProcessor {


    static String sampleJson = "";
    static List<Integer> matchedIds = new ArrayList<Integer>();

    static Pattern idMatchingPattern = Pattern.compile("\"id\": ([0-9]+)");
    public static void main(String[] args) {

        JsonProcessor jsonParser = new JsonProcessor();

        sampleJson = jsonParser.readInputJson();
        int eleStartIndex=0;
        int eleEndIndex=0;


        String jsonElementsPart = sampleJson.substring(sampleJson.indexOf("[")+1, sampleJson.indexOf("]"));
        while(eleStartIndex >= 0) {
            eleStartIndex = jsonElementsPart.indexOf("{",eleStartIndex);
            if (eleStartIndex >= 0) {
                eleEndIndex = jsonElementsPart.indexOf("}", eleStartIndex);
                String jsonElement = jsonElementsPart.substring(eleStartIndex+1, eleEndIndex);
                eleStartIndex=eleEndIndex+1;
                Integer matchedID = jsonParser.getMatchingID(jsonElement, "\"label\"");
                if (matchedID != -1) {
                    matchedIds.add(matchedID);
                }
            }
        }
        System.out.println("Matched IDs: " + matchedIds.stream().map(Object::toString).collect(Collectors.joining(",")));
        System.out.println("Total: " + matchedIds.stream().reduce(0,Integer::sum));
    }

    private Integer getMatchingID(String elementData, String matchingValue) {
        if (elementData.contains( matchingValue )) {
            Matcher matcherForId = idMatchingPattern.matcher(elementData);
            if (matcherForId.find()) {
                return Integer.parseInt( matcherForId.group(1));
            }
        }
        return -1;
    }

    private String readInputJson()  {
        try {
            InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(reader);
            String line;
            line = in.readLine();
            return line;
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return "";
    }
}
