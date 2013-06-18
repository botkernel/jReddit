import com.omrlnr.jreddit.Submission;
import com.omrlnr.jreddit.Submissions;
import com.omrlnr.jreddit.User;

import com.omrlnr.jreddit.utils.Utils;

/**
 * A simple bot that upvotes every new submission in a list of subreddits.
 * 
 * @author Omer Elnour
 */
public final class GenerousBot {
	public static void main(String[] args) throws Exception {
		String[] subreddits = { "programming", "funny", "wtf", "java",
				"todayilearned", "redditdev" };

		User user = new User("username", "password");
		user.connect();

		for (int i = 0; i < subreddits.length; i++) {
			for (Submission submission : Submissions.getSubmissions(user, 
                                                        subreddits[i], 
                                                        Submissions.NEW) ) {

                // 
                // TODO fix this
                //
				// submission.upVote();
			}
		}
	}
}
