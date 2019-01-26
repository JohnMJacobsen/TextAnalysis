package edu.sdsu.cs;

/**
 * Daniel Valoria
 * John Jacobsen
 * CS 310-01
 * Program 1
 */

import java.io.*;
import java.util.*;

public class App {
    public static void main(String[] args) {

        //Get Files
        File f = new File("src/main/java/edu/sdsu/cs");
        File[] fileArray = f.listFiles();

        String[] statsFormat      = {
                "Length of longest line in file: ",
                "Average line length: ",
                "Number of unique space-delineated tokens (case-sensitive): ",
                "Number of unique space--delineated tokens (case-insensitive): ",
                "Number of all space-delineated tokens in file: ",
                "Most frequently occurring token(s): ",
                "Count of most frequently occurring token (case-insensitive): ",
                "100 most frequent tokens with their counts (case-insensitive): ",
                "10 least frequent tokens with their counts (case-insensitive): ",
        };

        //Create new file
        File[] outputFile = new File[fileArray.length];

        try {
            for (int i = 0; i < fileArray.length; ++i) {
                if (fileArray[i].getName().contains(".java") || fileArray[i].getName().contains(".txt")) {
                    System.out.println("Success");

                    BufferedReader br = new BufferedReader(new FileReader(fileArray[i]));
                    outputFile[i] = new File(fileArray[i].getName() + ".stats");

                    //Format File
                    Formatter x = new Formatter(outputFile[i]);

                    int lineCount = 0;
                    int lengthLongestLine = 0;
                    int numAllTokens = 0;
                    int avgLineLength = 0;
                    String textLine = "";

                    String caseSensitiveToken = null;
                    String caseInsensitiveToken = null;

                    ArrayList<String> tokenList = new ArrayList<String>();
                    ArrayList<Integer> tokenCounts = new ArrayList<Integer>();

                    ArrayList<String> sensitiveTokenList = new ArrayList<String>();
                    ArrayList<Integer> sensitiveTokenCounts = new ArrayList<Integer>();

                    String[] fileStats = {
                            "fill", "fill", "fill", "fill", "fill", "fill", "fill", "fill", "fill"
                    };

                    while ((textLine = br.readLine()) != null) {
                        lineCount++;

                        if (textLine.length() > lengthLongestLine) {
                            lengthLongestLine = textLine.length();

                        }

                        numAllTokens += textLine.length();

                    }
                    avgLineLength = numAllTokens / lineCount;

                    fileStats[0] = Integer.toString(lengthLongestLine);
                    fileStats[1] = Integer.toString(avgLineLength);

                    try (Scanner scan = new Scanner(new FileInputStream(fileArray[i]))) {
                        while (scan.hasNext()) {
                            String token = scan.next();
                            caseSensitiveToken = token;
                            caseInsensitiveToken = token.toLowerCase();

                            if (!tokenList.contains(caseInsensitiveToken)) {
                                tokenList.add(tokenList.size(), caseInsensitiveToken);

                                tokenCounts.add(tokenCounts.size(), 1);
                            } else {
                                tokenCounts.set(tokenList.indexOf(caseInsensitiveToken), tokenCounts.get(tokenList.indexOf(caseInsensitiveToken)) + 1);
                            }

                            if (!sensitiveTokenList.contains(caseSensitiveToken)) {
                                sensitiveTokenList.add(sensitiveTokenList.size(), caseSensitiveToken);

                                sensitiveTokenCounts.add(sensitiveTokenCounts.size(), 1);
                            } else {
                                sensitiveTokenCounts.set(sensitiveTokenList.indexOf(caseSensitiveToken), sensitiveTokenCounts.get(sensitiveTokenList.indexOf(caseSensitiveToken)) + 1);
                            }

                        }

                        fileStats[2] = Integer.toString(tokenList.size());

                        fileStats[3] = Integer.toString(sensitiveTokenList.size());

                        fileStats[4] = Integer.toString(numberOfAllSpaceDelineatedTokens(tokenCounts));

                        fileStats[5] = mostFrequentlyOccurringToken(tokenCounts, tokenList);

                        fileStats[6] = Integer.toString(mostFrequentlyOccurringTokenCount(tokenCounts));

                        fileStats[7] = tenMostFrequentlyOccurringTokens(tokenCounts, tokenList);

                        fileStats[8] = tenLeastFrequentlyOccurringTokens(tokenCounts, tokenList);

                    } catch (FileNotFoundException e) {
                        System.out.println("File not found!");
                        System.out.println(e);
                    }
                    for (int index = 0; index < fileStats.length; ++index) {
                        x.format(statsFormat[index] + fileStats[index] + "\n");
                    }

                    x.close();
                } else {
                    System.out.println("Does not end in .java or .txt");
                }
            }
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e);
            System.exit(0);
        }
    }

    static int numberOfAllSpaceDelineatedTokens(ArrayList<Integer> editableTokenCounts) {
        int totalTokens = 0;
        for (Integer uses : editableTokenCounts) {
            totalTokens += uses;
        }
        return totalTokens;
    }

    static String mostFrequentlyOccurringToken(ArrayList<Integer> editableTokenCounts, ArrayList<String> editableTokenList) {
        int mostFrequent = mostFrequentlyOccurringTokenCount(editableTokenCounts);
        int desiredStringIndex = editableTokenCounts.indexOf(mostFrequent);
        return editableTokenList.get(desiredStringIndex);
    }

    static int mostFrequentlyOccurringTokenCount(ArrayList<Integer> editableTokenCounts) {
        int mostFrequent = 0;
        for (Integer uses : editableTokenCounts) {
            if (uses > mostFrequent) {
                mostFrequent = uses;
            }
        }
        return mostFrequent;
    }

    static String tenMostFrequentlyOccurringTokens(ArrayList<Integer> editableTokenCounts, ArrayList<String> editableTokenList) {
        String outputString = "\n";
        for (int i = 1; i <= 100; ++i) {
            int currentMaxCount = mostFrequentlyOccurringTokenCount(editableTokenCounts);
            String currentMaxToken = mostFrequentlyOccurringToken(editableTokenCounts, editableTokenList);

            outputString += "" + i + ": " + currentMaxToken + " - " + currentMaxCount + "\n";
            editableTokenList.remove(editableTokenList.indexOf(currentMaxToken));
            editableTokenCounts.remove(editableTokenCounts.indexOf(currentMaxCount));
        }
        return outputString;
    }

    static String tenLeastFrequentlyOccurringTokens(ArrayList<Integer> editableTokenCounts, ArrayList<String> editableTokenList) {
        String outputString = "\n";
        for (int i = 1; i <= 100; i++) {
            int currentMinCount = editableTokenCounts.get(0);
            String currentMinToken = editableTokenList.get(0);
            for (Integer uses : editableTokenCounts) {
                if (uses < currentMinCount) {
                    currentMinCount = uses;
                }
            }
            currentMinToken = editableTokenList.get(currentMinCount);
            outputString += "" + i + ": " + currentMinToken + " - " + currentMinCount + "\n";
            editableTokenList.remove(editableTokenList.indexOf(currentMinToken));
            editableTokenCounts.remove(editableTokenCounts.indexOf(currentMinCount));
        }
        return outputString;
    }
}