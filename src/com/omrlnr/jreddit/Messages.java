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
 * This class contains methods for handling reddit messages.
 *
 * @author <a href="https://github.com/jasonsimpson">Jason Simpson</a>
 *
 */
public class Messages {

    public enum MessageType {
        INBOX,
        UNREAD,
        SENT
    }

    public static List<Message> getMessages( User user ) 
                                                throws IOException {

        return getMessages( user, MessageType.INBOX );
    }

    /**
     * This function returns a list of messages
     * 
     * @param user              The user
     *
     * @return A list containing messages
     *
     * @throws IOException      If connection fails
     */
    public static List<Message> getMessages(    User user,
                                                MessageType messageType) 
                                            throws IOException {

        ArrayList<Message> messages = new ArrayList<Message>();

        String urlString = "http://www.reddit.com/message/";

        switch(messageType) {
            case UNREAD:
                urlString += "unread";
                break;
            case INBOX:
                urlString += "inbox";
                break;
            case SENT:
                urlString += "sent";
                break;
        }
        
        urlString += ".json";

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

    /**
     * 
     * Mark a message as read.
     *
     */
    public static void markAsRead(  User user, 
                                    Message message ) 
                                throws IOException {
        markAsRead(user, message.getName());
    }

    /**
     *
     * Mark a message as read.
     *
     */
    public static void markAsRead(  User user,
                                    String fullname ) 
                                throws IOException {

        String urlString = "http://www.reddit.com/api/read_message/";

        // urlString += ".json";

        URL url = new URL(urlString);

        JSONObject jsonObject = (JSONObject)Utils.post(
                                    "id=" + fullname + "&" +
                                    "uh=" + user.getModhash(),
                                    url, 
                                    user.getCookie());

        //
        // DEBUG
        //
        // System.out.println(Utils.getJSONDebugString(jsonObject));

    }


}

