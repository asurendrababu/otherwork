package com.csc.hzlcstTest;

/*
 *
 * Credits: This challenge has appeared in a past ACM competition.

Some message encoding schemes require that an encoded message be sent in two parts. The first part, called the header, contains the characters that can be present in the message.
The second part contains a pattern that represents the message.You must write a program that can decode messages under such a scheme.

The heart of the encoding scheme for your program is a sequence of "key" strings of 0's and 1's as follows:
0,00,01,10,000,001,010,011,100,101,110,0000,0001,. . .,1011,1110,00000, . . .

The first key in the sequence is of length 1, the next 3 are of length 2, the next 7 of length 3, the next 15 of length 4, etc. If two adjacent keys have the same length, the second can be obtained from the first by adding 1 (base 2). Notice that there are no keys in the sequence that consist only of 1's.

The keys are mapped to the characters in the header in order. That is, the first key (0) is mapped to the first character in the header, the second key (00) to the second character in the header,
the kth key is mapped to the kth character in the header. For example, suppose the header is:

AB#TANCnrtXc
Then 0 is mapped to A, 00 to B, 01 to #, 10 to T, 000 to A, ..., 110 to X, and 0000 to c.

The encoded message contains only 0's and 1's and possibly carriage returns, which are to be ignored. The message is divided into segments.
 The first 3 digits of a segment give the binary representation of the length of the keys in the segment. For example, if the first 3 digits are 010,
  then the remainder of the segment consists of keys of length 2 (00, 01, or 10). The end of the segment is a string of 1's which is the same length as the length of the keys in the segment.
   So a segment of keys of length 2 is terminated by 11. The entire encoded message is terminated by 000 (which would signify a segment in which the keys have length 0).
   The message is decoded by translating the keys in the segments one-at-a-time into the header characters to which they have been mapped.
Input:
Your program should read lines of text from standard input. Ecah line consists of a header and a message.
The length of the header is limited only by the fact that key strings have a maximum length of 7 (111 in binary). Note that neither 0 nor 1 will occur in the header.
If there are multiple copies of a character in a header, then several keys will map to that character. The encoded message contains only 0's and 1's,
and it is a legitimate encoding according to the described scheme. That is, the message segments begin with the 3-digit length sequence and end with the appropriate sequence of 1's.
The keys in any given segment are all of the same length, and they all correspond to characters in the header. The message is terminated by 000.
Your program should accept the first argument as the filename and read the contents of this file as the test data, according to the conditions above.
Output:
For each data set, print to standard output the decoded message, one message per line.
 *
 *
 *
input: $#**\0100000101101100011100101000
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewMessageDecoder {

    public static final String MESSAGE_TERMINATOR = "000";
    static String[] CHAR_MAPPING = "0,00,01,10,000,001,010,011,100,101,110,0000,0001,0010,0011,0100,0101,0110,0111,1001,1010,1011,1100,1101,1110,00000,00001,00010,00011,00100,00101,00110,00111,01001,01010,01011,01100,01101,01110,10000,10001,10010,10011,10100,10101,10110,10111,11001,11010,11011,11100,11101,11110,000000,000001,000010,000011,000100,000101,000110,000111,001001,001010,001011,001100,001101,001110,010000,010001,010010,010011,010100,010101,010110,010111,011001,011010,011011,011100,011101,011110,100000,100001,100010,100011,100100,100101,100110,100111,101001,101010,101011,101100,101101,101110,110000,110001,110010,110011,110100,110101,110110,110111,111001,111010,111011,111100,111101,111110,0000000,0000001,0000010,0000011,0000100,0000101,0000110,0000111,0001001,0001010,0001011,0001100,0001101,0001110,0010000,0010001,0010010,0010011,0010100,0010101,0010110,0010111,0011001,0011010,0011011,0011100,0011101,0011110,0100000,0100001,0100010,0100011,0100100,0100101,0100110,0100111,0101001,0101010,0101011,0101100,0101101,0101110,0110000,0110001,0110010,0110011,0110100,0110101,0110110,0110111,0111001,0111010,0111011,0111100,0111101,0111110,1000000,1000001,1000010,1000011,1000100,1000101,1000110,1000111,1001001,1001010,1001011,1001100,1001101,1001110,1010000,1010001,1010010,1010011,1010100,1010101,1010110,1010111,1011001,1011010,1011011,1011100,1011101,1011110,1100000,1100001,1100010,1100011,1100100,1100101,1100110,1100111,1101001,1101010,1101011,1101100,1101101,1101110,1110000,1110001,1110010,1110011,1110100,1110101,1110110,1110111,1111001,1111010,1111011,1111100,1111101,1111110".split(",");
    static int KEY_LENGTH_CHAR_COUNT=3;
    static Pattern headerPattern = Pattern.compile("([^0-9]+)");
    static Pattern messagePattern = Pattern.compile("([0-9\n]+)", Pattern.MULTILINE);

    public static void main(String[] args) {
        NewMessageDecoder messageDecoder = new NewMessageDecoder();

        String input = messageDecoder.readFileDataIntoString();
        String header = messageDecoder.getHeader(input);
        String message = messageDecoder.getMessage(input);
        messageDecoder.parseMessage(header, message);
    }

    private void parseMessage(String hdr, String msg) {
        StringBuilder outputMessage= new StringBuilder();
        Map<String,String> headerMap = getHeaderMapping(hdr);
        while (!msg.equals("")){
            if (MESSAGE_TERMINATOR.equals(msg)) {
                msg = "";
            }
            else{
                int keyLength = Integer.parseInt(msg.substring(0, KEY_LENGTH_CHAR_COUNT), 2);
                msg = msg.substring(KEY_LENGTH_CHAR_COUNT);
                msg = getSegmentTokens(msg, keyLength, headerMap, outputMessage);
            }
        }
        System.out.println(outputMessage.toString());
    }

    private String getSegmentTokens(String segmentData, int keyLength, Map<String,String> headerMap, StringBuilder outputMessage) {
        String segDelimiter = getSegmentDelimiter(keyLength);
        String currentKey;
        while ( !(currentKey=segmentData.substring(0,keyLength)).equals(segDelimiter) ){
            segmentData = segmentData.substring(keyLength);
            outputMessage.append(headerMap.get(currentKey));
        }
        segmentData = segmentData.substring(keyLength);
        return segmentData;
    }

    private String getSegmentDelimiter(int length) {
        String[] testStr = new String[length];
        Arrays.fill(testStr,"1");
        return (String.join("",testStr));
    }

    private String getMessage(String input) {
        Matcher msgMatcher = messagePattern.matcher(input);
        return (msgMatcher.find()) ? msgMatcher.group(0).replaceAll("\n","") : "";
    }

    private Map<String, String> getHeaderMapping(String header) {
        Map<String,String> headerMapping = new LinkedHashMap<String, String>();
        int index=0;
        for (char c : header.toCharArray()) {
            headerMapping.put(CHAR_MAPPING[index++], String.valueOf(c));
        }
        return headerMapping;
    }

    private String getHeader(String input) {
        Matcher headerMatcher = headerPattern.matcher(input);

        return (headerMatcher.find()) ? headerMatcher.group(1).replaceAll("\n","") : "";

    }

    private String readFileDataIntoString() {
        try {
            InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(reader);
            String line;
            return (line = in.readLine());
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return "";
    }
}
