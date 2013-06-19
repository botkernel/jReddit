import com.omrlnr.jreddit.Comment;
import com.omrlnr.jreddit.Comments;
import com.omrlnr.jreddit.Submission;
import com.omrlnr.jreddit.Submissions;
import com.omrlnr.jreddit.User;

import com.omrlnr.jreddit.utils.Utils;

import java.util.List;

/**
 *
 * An example to retrieve a single comment given its fullname
 * 
 */
public class GetSingleComment {

    /**
     *
     * Usage:
     * ./runExample.sh CommentLister <user> <pass> <fullename>
     *
     * e.g.
     * ./runExample.sh CommentLister joesmith secret123 t1_1glgde
     *
     *
     */
    public static void main(String[] args) throws Exception {

        String username     = args[0];
        String password     = args[1];
        String fullname     = args[2];

        User user = new User(username, password);
        user.connect();
        
        Comment comment = Comments.getComment( user, fullname );

        System.out.println(comment);

        //
        // Not sure if the API will actually return child comments for this.
        //
        List<Comment> replies = comment.getReplies();
        if(replies.size() > 0) {
            printAllComments(replies, "    ");
        }
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
