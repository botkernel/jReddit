import com.omrlnr.jreddit.Comment;
import com.omrlnr.jreddit.Comments;
import com.omrlnr.jreddit.Submission;
import com.omrlnr.jreddit.Submissions;
import com.omrlnr.jreddit.User;

import com.omrlnr.jreddit.utils.Utils;

import java.util.List;

/**
 *
 * A simple example that lists submissions in a subreddit
 * 
 */
public class CommentLister {

    /**
     *
     * Usage:
     * ./runExample.sh CommentLister <user> <pass> <articleid>
     *
     * e.g.
     * ./runExample.sh CommentLister joesmith secret123 1glgde
     *
     * For some reason, the article ID cannot be specified as
     * a fullname (t3_xxxx) but rather needs to be just the
     * id without the type portion. 
     *
     * I'm wondering if this should be normalized by the API.
     * I.e. hide this from the user. Accept a fullname with tY_xxxxx
     *      and remove "tY_" from within the API impl. 
     *
     */
    public static void main(String[] args) throws Exception {

        String username     = args[0];
        String password     = args[1];
        String article      = args[2];

        User user = new User(username, password);
        user.connect();
        
        List<Comment> comments = Comments.getComments(
                                                article,
                                                user );

        printAllComments(comments, "");
    }

    /** 
     *
     * Recursively print comments and their replies.
     *
     */
    private static void printAllComments(   List<Comment> comments, 
                                            String indent ) {
        for(Comment comment: comments) {
            System.out.println(comment.toString(indent));
            List<Comment> replies = comment.getReplies();
            if(replies.size() > 0) {
                printAllComments(replies, indent + "    ");
            }
        }
    }


}
