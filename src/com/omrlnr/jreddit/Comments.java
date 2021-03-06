package com.omrlnr.jreddit;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
     * This function returns a single comment, given a fullname
     *
     * @param user              The user
     * @param fullname          The fullname of the comment.
     *
     * @return The comment with the specified fullname
     *
     */
    public static Comment getComment(User user, String fullname)
                                    throws IOException {

        String urlString = "http://www.reddit.com/api/info.json?id=" + fullname;
        URL url = new URL(urlString);

        JSONObject obj = (JSONObject)Utils.get(url, user);
        JSONObject data = (JSONObject)obj.get("data");
        JSONArray children = (JSONArray)data.get("children");
       
        //
        // Not sure why this can be empty sometimes...
        //
        if(children.size() > 0) {
            JSONObject jsonData = (JSONObject)children.get(0);
            return new Comment(jsonData);
        }

        return null;
    }

    /**
     * This function returns a list of comments given a Submission
     * 
     * @param user              The user
     * @param submission        A submission
     *
     * @return A list containing Comments
     *
     * @throws IOException      If connection fails
     */ 
    public static List<Comment> getComments(    User user,
                                                Submission submission )
                                            throws IOException {
        return getComments( user, submission.getId() );
    }

    /**
     * This function returns a list of comments
     * 
     * @param user              The user
     * @param articleId         The id of the link/article/submission
     *
     * @return A list containing Comments
     *
     * @throws IOException      If connection fails
     */
    public static List<Comment> getComments(    User user,
                                                String articleId )
                                            throws IOException {

        if(articleId.startsWith(Thing.KIND_LINK+"_")) {
            //
            // Fix this for them. The caller should be able to pass in
            // a fullname here.
            //
            articleId = articleId.substring( (Thing.KIND_LINK+"_").length() );

            // DEBUG
            // System.out.println(
            // "Article ID changed fullname to " + articleId);
        }

        ArrayList<Comment> comments = new ArrayList<Comment>();

        String urlString = "http://www.reddit.com/comments/" + articleId;
        urlString += ".json";

        URL url = new URL(urlString);

        JSONArray array = (JSONArray)Utils.get(url, user);
        
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
     * This function returns a list of comments
     * 
     * @param user              The user we are using (logged in user.)
     * @param username          The username of the user for which we are
     *                          retreiving comment history.
     *
     * @return A list containing Comments
     *
     * @throws IOException      If connection fails
     */
    public static List<Comment> getUserComments(    
                                                User user,
                                                String username, 
                                                int limit )
                                            throws IOException {

        ArrayList<Comment> comments = new ArrayList<Comment>();

        String urlString =  "http://www.reddit.com/user/" + username + 
                            "/comments.json";
        urlString += "?";
        urlString += "limit=" + limit;

        URL url = new URL(urlString);

        JSONObject object = (JSONObject)Utils.get(url, user);
       
        // DEBUG
        // System.out.println("Returned user comments " + 
        //                      object.toJSONString());

        JSONObject data = (JSONObject)object.get("data");
        JSONArray children = (JSONArray)data.get("children");

        for (int i = 0; i < children.size(); i++) {
            JSONObject jsonData = (JSONObject)children.get(i);
            comments.add(new Comment(jsonData));
        }

        return comments;
    }


    /**
     * Submit a comment
     *
     * @param user      A logged in user.
     * @param thing     The thing we are replying to.
     *                      Should probably be a Comment or a Submission
     *                      Should maybe add an interface called Repliable
     * @param text      The text of the reply.
     *
     */
    public static String comment(   User user,
                                    Thing thing,
                                    String text )

                                    throws IOException {
        return comment(user, thing.getName(), text);
    }

    /**
     *
     * Submit a comment
     *
     * @param user      A logged in user.
     * @param fullname  The fullname of a thing we are replying to.
     *                  E.g. a link submission, or a comment.
     * @param text      The text of the reply.
     *
     * @return  The id of the posted comment.
     */
    public static String comment(   User user,
                                    String fullname,
                                    String text )
                                                    throws IOException {

        text = URLEncoder.encode(text, "UTF-8");
        
        JSONObject ret = Utils.post(
                "api_type=json" + "&" +
                "thing_id=" + fullname + "&" +
                "text=" + text + "&" +
                "uh=" + user.getModhash(),
            new URL("http://www.reddit.com/api/comment"), 
            user );

        //
        // DEBUG print the response
        //
        // System.out.println("Comment posted.");
        // System.out.println(Utils.getJSONDebugString(ret));
       
        //
        // Throw any exceptions if necessary.
        //
        Utils.handleResponseErrors(ret);

        JSONObject json = (JSONObject)ret.get("json");
        JSONObject data = (JSONObject)json.get("data");
        JSONArray things = (JSONArray)data.get("things");
        JSONObject jsonData = (JSONObject)things.get(0);

        Thing t = new Thing(jsonData);
       
        // DEBUG
        // System.out.println("Thing " + t);

         //
        // Return the ID of the posted comment.
        //
        return t.getId();
    }

    /**
     *
     * Edit a comment
     *
     * @param user      A logged in user.
     * @param id        The id (fullname) of a thing we are replying to.
     *                  E.g. a comment id (t1_cbmu8dr)
     * @param text      The text of the reply.
     *
     */
    public static void editComment( User user,
                                    String id,
                                    String text )
                                                    throws IOException {

        text = URLEncoder.encode(text, "UTF-8");
        
        JSONObject ret = Utils.post(
                "api_type=json" + "&" +
                "thing_id=" + id + "&" +
                "text=" + text + "&" +
                "uh=" + user.getModhash(),
            new URL("http://www.reddit.com/api/editusertext"), 
            user );

        //
        // DEBUG print the response
        //
        // System.out.println("Comment edited.");
        // System.out.println(Utils.getJSONDebugString(ret));

        //
        // Throw any exceptions if necessary.
        //
        Utils.handleResponseErrors(ret);

    }



}
