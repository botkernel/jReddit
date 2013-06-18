import com.omrlnr.jreddit.*;
import com.omrlnr.jreddit.utils.Utils;

import java.util.*;

/**
 *
 * A simple example that lists submissions in a subreddit
 * 
 */
public class SubmissionLister {

    private static final Map<String, Integer> TYPE_MAP = 
                                        new HashMap<String, Integer>();
    static {
        TYPE_MAP.put("HOT",             Submissions.HOT);
        TYPE_MAP.put("NEW",             Submissions.NEW);
        TYPE_MAP.put("RISING",          Submissions.RISING);
        TYPE_MAP.put("CONTROVERSIAL",   Submissions.CONTROVERSIAL);
        TYPE_MAP.put("TOP",             Submissions.TOP);
    }

    public static void main(String[] args) throws Exception {

        String username     = args[0];
        String password     = args[1];
        String subreddit    = args[2];
        String typeStr      = args[3];

        int limit = Integer.parseInt(args[4]);

        String before   = null;
        String after    = null;

        if(args.length > 5) {
            after = args[5];
        }

        User user = new User(username, password);
        user.connect();
        
        Integer type = TYPE_MAP.get(typeStr);
        if(type == null) {
            System.out.println("ERROR Cannot find type " + typeStr);
            return;
        }

        List<Submission> submissions = Submissions.getSubmissions(
                                                        user,
                                                        subreddit,
                                                        type.intValue(),
                                                        limit,
                                                        before,
                                                        after   );

        for(Submission submission: submissions) {
            System.out.println(submission);
        }

        System.out.println("Total submissions: " + submissions.size());
    }

}
