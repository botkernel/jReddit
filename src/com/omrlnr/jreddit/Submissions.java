package com.omrlnr.jreddit;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.omrlnr.jreddit.User;
import com.omrlnr.jreddit.utils.Utils;

/**
 * This class offers some submission utilties.
 * 
 * @author <a href="http://www.omrlnr.com">Omer Elnour</a>
 * @author <a href="https://github.com/jasonsimpson">Jason Simpson</a>
 *
 */
public class Submissions {

    /**
     *
     * Types for retreiving subreddit content
     * sort criteria. (Though the API has a separate parameter for sort,
     * that is not clear.)
     *
     */ 
    public enum ListingType {
        HOT,
        NEW,
        RISING,
        CONTROVERSIAL,
        TOP
    }

    //
    // deprecate / delete these
    // public static final int HOT             = 0;
    // public static final int NEW             = 1;
    // public static final int RISING          = 2;
    // public static final int CONTROVERSIAL   = 3;
    // public static final int TOP             = 4;

    /** 
     *  Default limit (returned results per "page" for a list request.)
     */ 
    public static final int DEFAULT_LIMIT   = 25;

    /**
     *
     * Used when sibmitting a link or text self post to indicate the
     * submission type.
     *
     */
    public enum SubmissionType {
        TEXT, 
        LINK 
    };

    /**
     * This function returns a list containing the submissions on a given
     * subreddit and page. 
     * 
     * @param user              The user
     * @param subRedditName     The subreddit's name
     * @param type              HOT or NEW and some others to come
     *
     * @return The list containing submissions
     *
     * @throws IOException      If connection fails
     */
    public static List<Submission> getSubmissions(  User user,
                                                    String subRedditName )
                                                throws IOException {

        return getSubmissions(  user, 
                                subRedditName, 
                                ListingType.HOT, 
                                DEFAULT_LIMIT,
                                (String)null, 
                                (String)null  );
    }

    /**
     * This function returns a list containing the submissions on a given
     * subreddit and page. 
     * 
     * @param user              The user
     * @param subRedditName     The subreddit's name
     * @param type              HOT or NEW and some others to come
     * @param before            fullname of a thing
     * @param after             fillname of a thing
     *
     * @return The list containing submissions
     *
     * @throws IOException      If connection fails
     */
    public static List<Submission> getSubmissions(
                                            User user,
                                            String subRedditName,
                                            ListingType type, 
                                            int limit,
                                            String before,
                                            String after )
                                        throws IOException {

        ArrayList<Submission> submissions = new ArrayList<Submission>();
        URL url;
        String urlString = "http://www.reddit.com/r/" + subRedditName;

        //
        // TODO fix "type"
        // Make this not a primitive. 
        //
        switch (type) {
            case NEW:
                urlString += "/new";
                break;
            case HOT:
                urlString += "/hot";
                break;
            case RISING:
                urlString += "/rising";
                break;
            case CONTROVERSIAL:
                urlString += "/controversial";
                break;
            case TOP:
                urlString += "/top";
                break;
        }

        urlString += ".json";
        urlString += "?";

        //
        // Add additional parameters
        // for pagination, limit, sort, etc.
        //
        urlString +=    "after=" + after + "&" +
                        "limit=" + limit + "&";

        url = new URL(urlString);

        JSONObject object = (JSONObject)Utils.get(  url, 
                                                    user.getCookie());

        JSONObject data = (JSONObject)object.get("data");
        JSONArray array = (JSONArray)data.get("children");

        //
        // TODO We could attempt to return these as part of pagination 
        // navigation to the caller.
        //
        // Note as you become familiar with the API, I think 
        // you realize this is not necessary to capture in its own 
        // datastructure, as the caller can use the fullname of any
        // object in a collection to get the next set (next page) of items 
        //
        // If that is the case, we can ignore this TODO and delete this stuff.
        //
        // String before = (String)data.get("before");
        // String after = (String)data.get("after");

        //
        // Debug
        //
        // System.out.println("Before  " + before);
        // System.out.println("After   " + after);


        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonData = (JSONObject)array.get(i);
            submissions.add(new Submission(jsonData));
        }

        return submissions;
    }

    /**
     *
     * Submit a Submission to a subreddit.
     *
     */
    public static void submit(  User user,
                                SubmissionType submissionType,
                                String title, 
                                String content,
                                String subreddit ) 
                                    throws IOException {

        title   = URLEncoder.encode(title, "UTF-8");
        content = URLEncoder.encode(content, "UTF-8");
        
        JSONObject ret = Utils.post(
                "title=" + title + "&" + 
                (submissionType.equals(SubmissionType.TEXT) ? 
                    "text" : "url") + "=" + content + "&" +
                "sr=" + subreddit + "&" +
                "kind=" + (submissionType.equals(SubmissionType.TEXT) ? 
                            "self" : "link" ) + "&" +
                "uh=" + user.getModhash(),
            new URL("http://www.reddit.com/api/submit"), 
            user.getCookie());


        //
        // DEBUG print the response
        //
        // System.out.println("Submission posted.");
        // System.out.println(Utils.getJSONDebugString(ret));


        //
        // Throw any exceptions if necessary.
        //
        Utils.handleResponseErrors(ret);


    }


}
