import com.omrlnr.jreddit.Submission;
import com.omrlnr.jreddit.Submissions;
import com.omrlnr.jreddit.Submissions.SubmissionType;
import com.omrlnr.jreddit.User;

import com.omrlnr.jreddit.utils.Utils;

import java.util.*;

/**
 *
 * A simple example that lists submissions in a subreddit
 * 
 */
public class SubmissionTest {

    private static final Map<String, SubmissionType> TYPE_MAP = 
                                new HashMap<String, SubmissionType>();
    static {
        TYPE_MAP.put("link", SubmissionType.LINK);
        TYPE_MAP.put("text", SubmissionType.TEXT);
    }

    public static void main(String[] args) throws Exception {

        String username     = args[0];
        String password     = args[1];

        String typeArg      = args[2];

        String title        = args[3];
        String content      = args[4];
        String subreddit    = args[5];

        User user = new User(username, password);
        user.connect();
   
        SubmissionType type = TYPE_MAP.get(typeArg);

        System.out.println("Type is " + type);
        System.out.println("Subreddit is " + subreddit);

        Submissions.submit( user,
                            type,
                            title,
                            content,
                            subreddit );

    }

}
