import com.omrlnr.jreddit.*;
import com.omrlnr.jreddit.VoteManager.VoteType;
import com.omrlnr.jreddit.utils.Utils;

import java.util.*;

/**
 *
 * An example to vote on a Votable item.
 * 
 */
public class VoteTest {

    private static final Map<String, VoteType> TYPE_MAP =
                                        new HashMap<String, VoteType>();
    static {
        TYPE_MAP.put("UPVOTE",      VoteType.UPVOTE);
        TYPE_MAP.put("DOWNVOTE",    VoteType.DOWNVOTE);
        TYPE_MAP.put("UNVOTE",      VoteType.UNVOTE);
    }

    /**
     *
     * Usage:
     * ./runExample.sh VoteTest <user> <pass> <vote-type> <fullename>
     *
     * e.g.
     * ./runExample.sh VoteTest joesmith secret123 UPVOTE t1_1glgde
     *
     *
     */
    public static void main(String[] args) throws Exception {

        String username     = args[0];
        String password     = args[1];
        String voteStr      = args[2];
        String fullname     = args[3];

        VoteType type = TYPE_MAP.get(voteStr);
        if(type == null) {
            System.out.println("Unknown vote type " + voteStr);
            return;
        }

        // Login in user
        User user = new User(username, password);
        user.connect();
       
        // Get the item.
        Comment comment = Comments.getComment( user, fullname );

        VoteManager.vote( user, comment, type );

    }


}
