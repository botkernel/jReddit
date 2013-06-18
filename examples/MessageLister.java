import com.omrlnr.jreddit.*;
import com.omrlnr.jreddit.utils.Utils;

import java.util.List;

/**
 *
 * A simple example that lists messages 
 * 
 */
public class MessageLister {

    /**
     *
     * Usage:
     * ./runExample.sh MessageLister <user> <pass> 
     *
     * e.g.
     * ./runExample.sh MessageLister joesmith secret123 
     *
     */
    public static void main(String[] args) throws Exception {

        String username     = args[0];
        String password     = args[1];

        User user = new User(username, password);
        user.connect();
        
        List<Message> messages = Messages.getMessages(user);
        for(Message message: messages) {
            System.out.println(message);
        }
    }


}
