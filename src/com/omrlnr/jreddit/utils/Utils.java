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
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.omrlnr.jreddit.*;


/**
 *
 * This class contains (or will contain) various utilities for jReddit.
 *
 * @author <a href="http://www.omrlnr.com">Omer Elnour</a>
 * @author <a hred="https://github.com/jasonsimpson">Jason Simpson</a>
 * 
 */
public class Utils {

    //
    // TODO make this configurable. 
    // (In order to accommodate multiple threads.)
    //
    private static int SLEEP_TIME = 3000;

    private static Object lock = new Object();

    public static final String USER_AGENT = 
                                    "jReddit Java API user agent v0.02";

    private static String userAgent = USER_AGENT;

    public static String getUserAgent(User user) {
        //
        // If no agent set, supply a default one.
        //
        String ret = user.getUserAgent();
        if(ret == null) {
            ret = USER_AGENT;
        }
        return ret;
    }

    /**
     * 
     * This function submits a POST request and returns a JSON object response
     * 
     */
    public static JSONObject post(  String apiParams, 
                                    URL url, 
                                    User user )
                                        throws IOException {
        synchronized(lock) {

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

            HttpURLConnection connection = 
                                (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Content-Length",
                String.valueOf(apiParams.length()));
            connection.setRequestProperty("cookie", "reddit_session=" + user.getCookie() );
            connection.setRequestProperty("User-Agent", getUserAgent(user) );
            DataOutputStream wr = 
                        new DataOutputStream(connection.getOutputStream());

            wr.writeBytes(apiParams);
            wr.flush();
            wr.close();

            try {

                JSONParser parser = new JSONParser();
                Object object = 
                    parser.parse(new BufferedReader(new InputStreamReader(
                        connection.getInputStream())).readLine());
                JSONObject jsonObject = (JSONObject) object;
                return jsonObject;

            } catch(IOException ioe) {

                // Handle 403 ... 
                //      IOException can be thrown from 
                //      HttpURLConnection.getInputStream(). 
                //      This seems to indicate a ban.
                //
                
                String msg = ioe.getMessage();
                if(msg.indexOf(
                    "Server returned HTTP response code: 403") != -1) {
                    throw new BannedUserException(msg);
                }
                throw ioe;


            } catch(ParseException pe) {
                //
                // pe.printStackTrace();

                throw new IOException("Error parsing POST response.", pe);
            }
        }
    }

    /**
     * This function submits a GET request and returns a JSON object response
     */
    public static Object get(   URL url, 
                                User user)
                                        throws IOException {
        return get("", url, user);
    }

    /**
     * This function submits a GET request and returns a JSON object response
     */
    public static Object get(   String apiParams, 
                                URL url, 
                                User user)
                                    throws IOException {
        synchronized(lock) {

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


            HttpURLConnection connection = 
                                (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("cookie", "reddit_session=" + user.getCookie());
            connection.setRequestProperty("User-Agent", getUserAgent(user) );

            try {
                JSONParser parser = new JSONParser();
                Object object = 
                    parser.parse(new BufferedReader(new InputStreamReader(
                        connection.getInputStream())).readLine());

                // JSONObject jsonObject = (JSONObject) object;
                // return jsonObject;

                return object;

            } catch(IOException ioe) {

                // Handle 403 ... 
                //      IOException can be thrown from 
                //      HttpURLConnection.getInputStream(). 
                //      This seems to indicate a ban.
                //
                
                String msg = ioe.getMessage();
                if(msg.indexOf(
                    "Server returned HTTP response code: 403") != -1) {
                    throw new BannedUserException(msg);
                }
                throw ioe;

            } catch(ParseException pe) {
                //
                // pe.printStackTrace();
    
                throw new IOException("Error parsing GET response.", pe);
            }
        }
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
     *
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
        
        // System.out.println("handleResponseErrors()");
        // System.out.println("object.toJSONString(): " + 
        //                              object.toJSONString());

        //
        // Attempt to handle errors by reading the json objects
        //
        JSONObject data = (JSONObject)object.get("json");
        if(data != null) {
            JSONArray array = (JSONArray)data.get("errors");

            //
            // Inspect each tuple in the array. It should indicate 
            // an error condition.
            //
            for (int i = 0; i < array.size(); i++) {
                JSONArray tuple = (JSONArray)array.get(i);
                String error = (String)tuple.get(0);
                String message = (String)tuple.get(1);
   
                if(error.equals("RATELIMIT")) {
                    throw new RateLimitException(error + " " + message);
                }

                if(error.equals("DELETED_COMMENT")) {
                    throw new DeletedCommentException(error + " " + message);
                }
    
                //
                // TODO There is additional data here. Not sure how to
                // interpret this just yet. 
                //
                // E.g. "ratelimit" which appears to map back to
                //      "ratelimit:     134.33498" in the parent object.
                //
                String additionalData = (String)tuple.get(2);

                //
                // TODO Chain all errors?
                //
                throw new IOException(error + " " + message);
            }
        }

        //
        // Fall back to legacy error handling.
        //
        if (object.toJSONString().contains(".error.USER_REQUIRED")) {

            throw new IOException(
                    "User is not logged in. " +
                    "User must be logged in to perform this operation.");
                    

        } else if (object.toJSONString().contains(
                        ".error.QUOTA_FILLED")) {

            throw new IOException("Quota filled. ");

        } else if (object.toJSONString().contains(
                        ".error.RATELIMIT.field-ratelimit") ) {

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
