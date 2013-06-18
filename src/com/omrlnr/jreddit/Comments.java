package com.omrlnr.jreddit;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.omrlnr.jreddit.User;
import com.omrlnr.jreddit.utils.Utils;

/**
 * This class contains methods for handling reddit comments.
 * 
 * @author <a href="https://github.com/jasonsimpson">Jason Simpson</a>
 *
 */
public class Comments {

    /**
     * This function returns a list of comments
     * 
     * @param articleId         The id of the link/article/submission
     * @param user              The user
     *
     * @return A list containing Comments
     *
     * @throws IOException      If connection fails
     * @throws ParseException   If JSON parsing fails
     */
    public static List<Comment> getComments(    String articleId, 
                                                User user ) 
                                    throws IOException, ParseException {

        ArrayList<Comment> comments = new ArrayList<Comment>();

        String urlString = "http://www.reddit.com/comments/" + articleId;
        urlString += ".json";

        URL url = new URL(urlString);

        JSONArray array = (JSONArray)Utils.get(url, user.getCookie());
        
        if(array.size() > 0) { 
           
            //
            // DEBUG (Can we ignore the item at index 0,
            // as this represents the Submission to which these
            // are replies?
            //
            // JSONObject submissionJson = (JSONObject)array.get(0);
            // System.out.println("Submission: " + submissionJson); 

            JSONObject replies = (JSONObject)array.get(1);
            JSONObject data = (JSONObject)replies.get("data");
            JSONArray children = (JSONArray)data.get("children");

            for (int i = 0; i < children.size(); i++) {
                JSONObject jsonData = (JSONObject)children.get(i);
                comments.add(new Comment(jsonData));
            }
        }

        return comments;
    }


    /**
     *
     * Submit a comment
     *
     * @param user      A logged in user.
     * @param fullname  The fullname of a thing we are replying to.
     *                  E.g. a link submission, or a comment.
     * @param text      The text of the reply.
     */
    public static void comment( User user,
                                String fullname,
                                String text )
                                    throws IOException, ParseException {

        text = URLEncoder.encode(text, "UTF-8");
        
        JSONObject ret = Utils.post(
                "api_type=json" + "&" +
                "thing_id=" + fullname + "&" +
                "text=" + text + "&" +
                "uh=" + user.getModhash(),
            new URL("http://www.reddit.com/api/comment"), 
            user.getCookie());

        //
        // DEBUG print the response
        //
        // System.out.println("Comment posted.");
        // System.out.println(Utils.getJSONDebugString(ret));


        //
        // Throw any exceptions if necessary.
        //
        Utils.handleResponseErrors(ret);


    }

}
