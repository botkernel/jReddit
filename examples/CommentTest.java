import com.omrlnr.jreddit.*;
import com.omrlnr.jreddit.utils.Utils;

import java.util.*;

/**
 *
 * A simple example that submits a comment 
 * 
 */
public class CommentTest {

    public static void main(String[] args) throws Exception {

        String username     = args[0];
        String password     = args[1];

        String op           = args[2];
        String thingId      = args[3];
        String text         = args[4];

        User user = new User(username, password);
        user.connect();
   
        if(op.equals("post")) {

            String id = Comments.comment(   user,
                                            thingId,
                                            text );
        
            System.out.println("Posted " + id);

        } else if(op.equals("edit")) {

            Comments.editComment(   user,
                                    thingId,
                                    text );
        
        }

    }


}
