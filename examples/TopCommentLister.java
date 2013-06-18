import com.omrlnr.jreddit.*;
import com.omrlnr.jreddit.utils.Utils;

import java.util.List;

/**
 *
 * A simple example that lists submissions in a subreddit
 * 
 */
public class TopCommentLister {

    public static void main(String[] args) throws Exception {

        String username     = args[0];
        String password     = args[1];
        String subreddit    = args[2];

        User user = new User(username, password);
        user.connect();
        
        List<Submission> submissions = Submissions.getSubmissions(
                                                user,
                                                subreddit );

        for(Submission submission: submissions) {
            System.out.println(submission);
            List<Comment> comments = Comments.getComments(
                                                submission.getId(),
                                                user );
            if(comments.size() > 0) {
                System.out.println("   " + comments.get(0));
            } else {
                System.out.println("    **** No comments");
            }
        }
    }

}
