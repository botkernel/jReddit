import com.omrlnr.jreddit.*;
import com.omrlnr.jreddit.utils.Utils;

import java.util.*;

/**
 *
 * A simple example that lists messages 
 * 
 */
public class MessageLister {

    private static final Map<String, Messages.MessageType> TYPE_MAP =
                            new HashMap<String, Messages.MessageType>();
    static {
        TYPE_MAP.put("INBOX",   Messages.MessageType.INBOX );
        TYPE_MAP.put("UNREAD",  Messages.MessageType.UNREAD );
        TYPE_MAP.put("SENT",    Messages.MessageType.SENT );
    }

    /**
     *
     * Usage:
     * ./runExample.sh MessageLister <user> <pass> <op> <type>
     *
     * e.g.
     * ./runExample.sh MessageLister joesmith secret123 get INBOX
     *
     */
    public static void main(String[] args) throws Exception {

        String username     = args[0];
        String password     = args[1];

        String op           = args[2];

        User user = new User(username, password);
        user.connect();

        if(op.equals("get")) {
            String typeStr      = args[3];

            Messages.MessageType type = TYPE_MAP.get(typeStr);
            if(type == null) {
                System.out.println("ERROR Cannot find type: " + typeStr);
                return;
            }
            
            List<Message> messages = Messages.getMessages(user, type);
            for(Message message: messages) {
                System.out.println(message);
            }

            return;
        }

        if(op.equals("markAsRead")) {
            String id = args[3];
            Messages.markAsRead(user, id);
        }

    }


}
