package com.omrlnr.jreddit.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;

import java.lang.Thread;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * This class contains (or will contain) various utilities for jReddit.
 * 
 */
public class Utils {

    private static int SLEEP_TIME = 2000;

    private static final String USER_AGENT = 
                                    "jReddit Java API user agent v0.02";

    /**
     * 
     * This function submits a POST request and returns a JSON object response
     * 
     */
    public static JSONObject post(  String apiParams, 
                                    URL url, 
                                    String cookie )
                                        throws IOException, ParseException {

        //
        // Adhere to API rules....
        // (Make this configurable)
        //        
        try {
            Thread.sleep(SLEEP_TIME);
        } catch(InterruptedException ie) {
            ie.printStackTrace();
            return null;
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Content-Length",
                String.valueOf(apiParams.length()));
        connection.setRequestProperty("cookie", "reddit_session=" + cookie);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(apiParams);
        wr.flush();
        wr.close();

        JSONParser parser = new JSONParser();
        Object object = parser.parse(new BufferedReader(new InputStreamReader(
                connection.getInputStream())).readLine());
        JSONObject jsonObject = (JSONObject) object;

        return jsonObject;
    }

    /**
     * This function submits a GET request and returns a JSON object response
     */
    public static Object get(   URL url, 
                                String cookie)
                                        throws IOException, ParseException {
        return get("", url, cookie);
    }

    /**
     * This function submits a GET request and returns a JSON object response
     */
    public static Object get(   String apiParams, 
                                URL url, 
                                String cookie)
                                    throws IOException, ParseException {

        //
        // Adhere to API rules....
        // (Make this configurable)
        //        
        try {
            Thread.sleep(SLEEP_TIME);
        } catch(InterruptedException ie) {
            ie.printStackTrace();
            return null;
        }


        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("cookie", "reddit_session=" + cookie);
        connection.setRequestProperty("User-Agent", USER_AGENT);

        JSONParser parser = new JSONParser();
        Object object = parser.parse(new BufferedReader(new InputStreamReader(
                connection.getInputStream())).readLine());

        // JSONObject jsonObject = (JSONObject) object;
        // return jsonObject;

        return object;
    }

    /**
     *
     * Get a somewhat more human readable version of the JSON string.
     *
     */
    public static String getJSONDebugString(Object obj) {
        return getJSONDebugString(obj, "");
    }

    /**
     *
     * Get a somewhat more human readable version of the JSON string.
     *
     */
    public static String getJSONDebugString(Object obj, String indent) {

        String ret = "";

        //
        // Handle hashtable
        //
        if(obj instanceof HashMap) {
            ret += indent + "{\n";
            HashMap hash = (HashMap)obj;
            Iterator it = hash.keySet().iterator();
            while(it.hasNext()) {
                String key = (String)it.next();
                ret += indent + key + ": ";
                Object val = hash.get(key);
                ret += indent + getJSONDebugString(val, indent + "    ");
            }
            ret += indent + "}\n";
            return ret;
        }

        //
        // Handle array
        //
        if(obj instanceof ArrayList) {
            ret += indent + "[\n";
            ArrayList list = (ArrayList)obj;
            for(int i = 0; i < list.size(); i++) {
                Object val = list.get(i); 
                ret += indent + getJSONDebugString(val, indent + "    ");
            }
            ret += indent + "]\n";
            return ret;
        }

        //
        // No hashtable or array so this should be a primitive...
        //
        return ((obj == null) ? "null" : obj.toString()) + "\n";

    }

    /**
     *
     * Handle any error response to a GET or POST request 
     *          for any error conditions returned.
     *  TODO need more thought on how to handle this stuff.
     *          Moving it here for now for a first pass refactor.
     * 
     * @param object    A JSONObject reponse returned from a GET or POST 
     *                  call to the reddit API
     *
     * @throws IOException  An exception with a message indicating the type
     *                      of error encountered, if any.
     */
    public static void handleResponseErrors(JSONObject object) 
                                                    throws IOException {

        if (object.toJSONString().contains(".error.USER_REQUIRED")) {

            throw new IOException(
                    "User is not logged in. " +
                    "User must be logged in to perform this operation.");
                    

        } else if (object.toJSONString().contains(
                        ".error.QUOTA_FILLED")) {

            throw new IOException("Quota filled. ");

        } else if (object.toJSONString().contains(
                        ".error.RATELIMIT.field-ratelimit")) {

            throw new IOException(
                    "Rate limit reached. " +
                    "'You are doing that too much.' Try again later.");

        } else if (object.toJSONString().contains(
                            ".error.ALREADY_SUB.field-url")) {

            throw new IOException(
                    "The link you are attempting to submit " +
                    "has already been submitted.");

        } else if (object.toJSONString().contains(
                    ".error.BAD_CAPTCHA.field-captcha")) {

            throw new IOException(
                    "This operation requires a captcha response.");

        } else {

            //
            // Success.
            //
            // Throw nothing.
            //

            // System.out.println("Link submitted to "
            //        + ((JSONArray) ((JSONArray) ((JSONArray) object
            //                .get("jquery")).get(16)).get(3)).get(0));
        }

    }


}
