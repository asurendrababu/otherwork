package com.csc.hzlcstTest;

/**
 * Iterate through each line of input.
 *
 * Determine if two URIs match. For the purpose of this challenge, you should use a case-sensitive octet-by-octet comparison of the entire URIs, with these exceptions:

 1. A port that is empty or not given is equivalent to the default port of 80
 2. Comparisons of host names MUST be case-insensitive
 3. Comparisons of scheme names MUST be case-insensitive
 4. Characters are equivalent to their % HEX HEX encodings. (Other than typical reserved characters in urls like , / ? : @ & = + $ #)
 Input:
 Your program should read lines from standard input. Each line contains two urls delimited by a semicolon.
 Output:
 Print out True/False if the URIs match.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.StringTokenizer;

public class UrlMatcher{
    public static void main(String[] args)  throws IOException {

        UrlMatcher urlMatcher = new UrlMatcher();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = "";
        do {
            inputLine = reader.readLine();
            if (inputLine.isEmpty()) break;
            StringTokenizer stURLsplitter = new StringTokenizer(inputLine, ";");
            String strFirstURL = stURLsplitter.nextToken(); // "https://www.abc.com/employees/2323?ename=su re&enum=234";
            String strSecnondURL= (stURLsplitter.hasMoreTokens()) ? stURLsplitter.nextToken() : ""; // "https://www.ABC.com/employees/2323?ename=su%20re&enum=234";

            try {
                URL firstURL = new URL(strFirstURL);
                URL secondURL = new URL(strSecnondURL);
                System.out.println((urlMatcher.compareURLs(firstURL, secondURL) ? " True " : " False"));
            } catch (MalformedURLException mex) {
                System.out.println("  atleast One Of The URLs is malformed.");
            }
        } while (true);
    }

    private boolean compareURLs(URL fUrl, URL sUrl) {

        if ( fUrl.getProtocol().compareToIgnoreCase(sUrl.getProtocol()) != 0) {
            return false;
        }
        if ( fUrl.getHost().compareToIgnoreCase(sUrl.getHost()) != 0) {
            return false;
        }
        int FirstPort = fUrl.getPort() == -1 ? fUrl.getDefaultPort() : fUrl.getPort();
        int secondPort = sUrl.getPort() == -1 ? sUrl.getDefaultPort() : sUrl.getPort();
        if ( FirstPort != secondPort) {
            return false;
        }
        if ( URLDecoder.decode(fUrl.getPath()).compareTo(URLDecoder.decode(sUrl.getPath())) != 0) {
            return false;
        }
        return true;
    }
}
