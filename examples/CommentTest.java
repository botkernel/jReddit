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
        String thingId      = args[2];
        String text         = args[3];

        User user = new User(username, password);
        user.connect();
   
        Comments.comment( user,
                          thingId,
                          text );

    }


}
