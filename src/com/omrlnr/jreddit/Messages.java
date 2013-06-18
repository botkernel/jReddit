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
 * This class contains methods for handling reddit messages.
 *
 * @author <a href="https://github.com/jasonsimpson">Jason Simpson</a>
 *
 */
public class Messages {


    /**
     * This function returns a list of messages
     * 
     * @param user              The user
     *
     * @return A list containing messages
     *
     * @throws IOException      If connection fails
     * @throws ParseException   If JSON parsing fails
     */
    public static List<Message> getMessages( User user ) 
                                    throws IOException, ParseException {

        ArrayList<Message> messages = new ArrayList<Message>();

        String urlString = "http://www.reddit.com/message/inbox.json";

        URL url = new URL(urlString);

        JSONObject jsonObject = (JSONObject)Utils.get(url, user.getCookie());

        //
        // DEBUG
        //
        // System.out.println(Utils.getJSONDebugString(jsonObject));

        JSONObject data = (JSONObject)jsonObject.get("data");
        JSONArray children = (JSONArray)data.get("children");

        for (int i = 0; i < children.size(); i++) {
            JSONObject jsonData = (JSONObject)children.get(i);
            messages.add(new Message(jsonData));
        }

        return messages;
    }

}

