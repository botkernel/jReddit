package com.omrlnr.jreddit;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

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

    public static final int HOT = 0;
    public static final int NEW = 1;

    public static final int FRONTPAGE = 0;

    public enum SubmissionType {
        TEXT, 
        LINK 
    };

    /**
     * This function returns a list containing the submissions on a given
     * subreddit and page. 
     * 
     * @param subRedditName     The subreddit's name
     * @param type              HOT or NEW and some others to come
     * @param page              TODO this
     * @param user              The user
     *
     * @return The list containing submissions
     *
     * @throws IOException      If connection fails
     * @throws ParseException   If JSON parsing fails
     */
    public static List<Submission> getSubmissions(
                                            String subRedditName,
                                            int type, 
                                            int page, 
                                            User user) 
                                    throws IOException, ParseException {

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
        }

        /**
         * TODO Implement Pages
         */

        urlString += ".json";
        url = new URL(urlString);

        JSONObject object = (JSONObject)Utils.get(url, user.getCookie());
        JSONObject data = (JSONObject)object.get("data");
        
        String before = (String)data.get("before");
        String after = (String)data.get("after");
        JSONArray array = (JSONArray)data.get("children");

        //
        // TODO a good way to do apging through the API...
        // Might have to just expose this to the caller, ug. 
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
                                SubmissionType type,
                                String title, 
                                String content,
                                String subreddit ) 
                                    throws IOException, ParseException {

        title   = URLEncoder.encode(title, "UTF-8");
        content = URLEncoder.encode(content, "UTF-8");
        
        JSONObject ret = Utils.post(
                "title=" + title + "&" + 
                (type.equals(SubmissionType.TEXT) ? "text" : "url") + "=" + 
                    content + "&" +
                "sr=" + subreddit + "&" +
                "kind=" +
                (type.equals(SubmissionType.TEXT) ? "self" : "link" ) + "&" +
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
