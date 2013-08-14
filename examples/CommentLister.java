import com.omrlnr.jreddit.Comment;
import com.omrlnr.jreddit.Comments;
import com.omrlnr.jreddit.Submission;
import com.omrlnr.jreddit.Submissions;
import com.omrlnr.jreddit.User;

import com.omrlnr.jreddit.utils.Utils;

import java.util.List;

/**
 *
 * A simple example that lists comments replying to a submission
 * 
 */
public class CommentLister {

    /**
     *
     * Usage:
     * ./runExample.sh CommentLister <user> <pass> <articleid>
     *
     * e.g.
     * ./runExample.sh CommentLister joesmith secret123 1xlgde
     * ./runExample.sh CommentLister joesmith secret123 t3_1llgde
     *
     */
    public static void main(String[] args) throws Exception {

        String username     = args[0];
        String password     = args[1];

        User user = new User(username, password);
        user.connect();

        List<Comment> comments = null;

        if(args.length == 3) {
            String article = args[2];
            Comment comment = Comments.getComment(  user, 
                                                    article);
            System.out.println("Comment: " + comment);
            return;
        }

        if(args.length == 4) {
            String targetuser = args[2];
            int limit = Integer.parseInt(args[3]);
            comments = Comments.getUserComments(    user, 
                                                    targetuser,
                                                    limit );
        }

        System.out.println("Found comments: " + comments.size());

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
