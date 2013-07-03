package com.omrlnr.jreddit;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

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
public class VoteManager {

    public enum VoteType {
        UPVOTE,
        DOWNVOTE,
        UNVOTE
    }

    /**
     * Vote on a Voteable item.
     *
     * @param user      User to perform the voting
     * @param votable   The thing on which to vote.
     * @param type      The type of vote, one of VoteType.
     */
    public static void vote(    User user, 
                                Votable votable, 
                                VoteType type)
                                        throws IOException {
        int dir = 0;

        switch(type) {
            case UPVOTE:
                dir = 1;
                break;
            case DOWNVOTE:
                dir = -1;
                break;
            case UNVOTE:
                dir = 0;
                break;
        }

        JSONObject ret = Utils.post(
                "dir=" + dir + "&" +
                "id=" + votable.getName() + "&" +
                "uh=" + user.getModhash(),
            new URL("http://www.reddit.com/api/vote"), 
            user );

        //
        // Handle any errors returned.
        //
        Utils.handleResponseErrors(ret);

    }


}
