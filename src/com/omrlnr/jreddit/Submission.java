package com.omrlnr.jreddit;

import org.json.simple.JSONObject;

import com.omrlnr.jreddit.utils.Utils;

/**
 *
 * This class represents a reddit Submission or "Article"
 *
 * @author <a href="https://github.com/jasonsimpson">Jason Simpson</a>
 * 
 */
public class Submission extends Thing implements Votable {

    public Submission(JSONObject data) {
        super(data);
    }

    public String toString() {
        String thing = super.toString();
        return thing +
                "   Submission:   "   + getTitle()        + "\n" +
                "       author:   "   + getAuthor()       + "\n" +
                // "       url:      "   + getUrl()          + "\n" +
                "       score:    "   + getScore() + 
                                        " (" 
                                            + getUpVotes() + "/" 
                                            + getDownVotes() + ")" + "\n" + 
                "       comments: " + getNumComments() + 
                " selfpost: " + isSelfPost() + "\n";
                // Utils.getJSONDebugString(_data);

    }

    public String getUrl() { 
        return (String)((JSONObject)_data.get("data")).get("url");
    }

    public long getNumComments() { 
        return (Long)((JSONObject)_data.get("data")).get("num_comments");
    }

    public long getUpVotes() { 
        return (Long)((JSONObject)_data.get("data")).get("ups");
    }

    public String getAuthor() { 
        return (String)((JSONObject)_data.get("data")).get("author");
    }

    public String getTitle() { 
        return (String)((JSONObject)_data.get("data")).get("title");
    }

    public String getSelftext() { 
        return (String)((JSONObject)_data.get("data")).get("selftext");
    }
    
    public boolean isSelfPost() {
        // return (Boolean)((JSONObject)_data.get("data")).get("is_self");

        Boolean b = (Boolean)((JSONObject)_data.get("data")).get("is_self");
        if(b == null) {
            return false;
        }

        return b.booleanValue();
    }

    //
    // Methods to implement Votable
    //

    public long getDownVotes() { 
        return (Long)((JSONObject)_data.get("data")).get("downs");
    }

    public long getScore() { 
        return (Long)((JSONObject)_data.get("data")).get("score");
    }

    public Boolean getLikes() {
        return (Boolean)((JSONObject)_data.get("data")).get("likes");
    }



}
