package com.omrlnr.jreddit;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import com.omrlnr.jreddit.utils.Utils;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * This class represents a reddit comment
 *
 * @author <a href="https://github.com/jasonsimpson">Jason Simpson</a>
 * 
 */
public class Comment extends Thing {

    public Comment(JSONObject jsonObj) {
        super(jsonObj);
    }

    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        String thing = super.toString(indent);
        
        if(getKind().equals(KIND_MORE)) {
            return thing +
                    indent + "    ...more...\n";
        }

        return thing + 
            indent + "   Comment:    "   + getBody()     + "\n" +
            indent + "       author: "   + getAuthor()   + "\n" +
            indent + "       up: "       + getUpVotes()  + "\n" +
            indent + "       down: "     + getDownVotes() + "\n";
            // indent + Utils.getJSONDebugString(_data, indent);

    }

    public String getBody() { 
        return (String)((JSONObject)_data.get("data")).get("body");
    }

    public long getUpVotes() { 
        return (Long)((JSONObject)_data.get("data")).get("ups");
    }

    public long getDownVotes() { 
        return (Long)((JSONObject)_data.get("data")).get("downs");
    }

    public long getScore() { 
        return (Long)((JSONObject)_data.get("data")).get("score");
    }

    public String getAuthor() { 
        return (String)((JSONObject)_data.get("data")).get("author");
    }

    /**
     * Get the replies to this comment.
     */
    public List<Comment> getReplies() {
        List<Comment> ret = new ArrayList();
        
        JSONObject data = (JSONObject)_data.get("data");

        //
        // DEBUG
        //
        // System.out.println(Utils.getJSONDebugString(data));

        //
        // Need to check type of "replies" property.
        // This is annoying. Rather than an empty object, if no replies
        // are present the JSON tools treat this as a String. Therefore
        // need to check instanceof on the "replies" property.
        //
        Object repliesObj = data.get("replies");
        if(repliesObj instanceof JSONObject) {

            JSONObject replies = (JSONObject)repliesObj;
            JSONObject replyData = (JSONObject)replies.get("data");
            JSONArray children = (JSONArray)replyData.get("children");
    
            for (int i = 0; i < children.size(); i++) {
                JSONObject jsonData = (JSONObject)children.get(i);
                Comment comment = new Comment(jsonData);
    
                ret.add(new Comment(jsonData));
            }
        }

        return ret;
    }

}
