import com.omrlnr.jreddit.*;
import com.omrlnr.jreddit.Submissions.ListingType;
import com.omrlnr.jreddit.utils.Utils;

import java.util.*;

/**
 *
 * A simple example that lists submissions in a subreddit
 * 
 */
public class SubmissionLister {

    /** 
     * Sort type will be one of these.
     *
     * Map params user can specify as Strings on the command line
     * back into types used by the API.
     */ 
    private static final Map<String, ListingType> TYPE_MAP = 
                                        new HashMap<String, ListingType>();
    static {
        TYPE_MAP.put("HOT",             ListingType.HOT);
        TYPE_MAP.put("NEW",             ListingType.NEW);
        TYPE_MAP.put("RISING",          ListingType.RISING);
        TYPE_MAP.put("CONTROVERSIAL",   ListingType.CONTROVERSIAL);
        TYPE_MAP.put("TOP",             ListingType.TOP);
    }

    /**
     *
     * Usage:
     *
     * ./runExample.sh SubmissionLister <user> <pass> 
     *                                  <subreddit> <type> <limit> [fullname]
     *
     * Example:
     * ./runExample.sh SubmissionLister joesmith secret123 pics TOP 5 
     * or
     * ./runExample.sh SubmissionLister joesmith secret123 pics TOP 5 t3_1gjx22
     *
     */
    public static void main(String[] args) throws Exception {

        String username     = args[0];
        String password     = args[1];
        String subreddit    = args[2];
        String typeStr      = args[3];

        //
        // Specify a limit (number of results to return.)
        // E.g. 100
        //
        int limit = Integer.parseInt(args[4]);

        String before   = null;
        String after    = null;

        //
        // Optionally allow an "after" parameter.
        // To use this, specify the last fullname of an item in the
        // list in order to retrieve the next page of list items.
        //
        // E.g. "t3_1gl6dm"
        //
        if(args.length > 5) {
            after = args[5];
        }

        User user = new User(username, password);
        user.connect();
        
        ListingType type = TYPE_MAP.get(typeStr);
        if(type == null) {
            System.out.println("ERROR Cannot find type " + typeStr);
            return;
        }

        List<Submission> submissions = Submissions.getSubmissions(
                                                        user,
                                                        subreddit,
                                                        type,
                                                        limit,
                                                        before,
                                                        after   );

        for(Submission submission: submissions) {
            System.out.println(submission);
        }

        System.out.println("Total submissions: " + submissions.size());
    }

}
