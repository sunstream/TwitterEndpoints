package scenarios;

import api.request.StatusUpdateService;
import api.response.ResponseParser;
import asserts.ResponseAsserts;
import asserts.TweetAsserts;
import models.TestUser;
import models.Tweet;
import org.apache.http.HttpResponse;
import org.junit.Test;
import steps.StatusUpdateSteps;
import steps.TimelineSteps;

public class StatusUpdateTests {
    private static final int MAX_CHAR_COUNT = 140;

    private final TimelineSteps timelineSteps = new TimelineSteps();
    private final StatusUpdateSteps statusUpdateSteps = new StatusUpdateSteps();

    private final TweetAsserts tweetAsserts = new TweetAsserts();
    private final ResponseAsserts responseAsserts = new ResponseAsserts();

    private final TestUser defaultUser = TestUser.DEFAULT;
    private final ResponseParser responseParser = new ResponseParser();

    @Test
    public void should_PostTextUpToMaxCharCount_WhenStatusUpdated(){
        String text = statusUpdateSteps.generateRandomPostText(MAX_CHAR_COUNT);
        statusUpdateSteps.postUpdate(defaultUser, text);
        HttpResponse response = timelineSteps.requestLastTweet(defaultUser);
        Tweet lastTweet = responseParser.getTweetsFromResponse(response).get(0);
        tweetAsserts.verifyTweetText(lastTweet, text);
    }

    @Test
    public void should_RejectStatusUpdate_WhenLengthExceededLimit(){
        String text = statusUpdateSteps.generateRandomPostText(MAX_CHAR_COUNT+1);
        HttpResponse response = statusUpdateSteps.postUpdate(defaultUser, text);
        int expectedErrorCode = 403;
        responseAsserts.verifyRequestFailed(response, expectedErrorCode);
    }

    @Test
    public void should_LinkReplyToOriginalTweet_WhenRepliedToOwnTweet(){
        String text = statusUpdateSteps.generateArbitraryPostText();
        HttpResponse origTweetResponse = statusUpdateSteps.postUpdate(defaultUser, text);
        Tweet origTweet = responseParser.getSingleTweetFromResponse(origTweetResponse);

        text = statusUpdateSteps.generateArbitraryPostText();
        HttpResponse replyTweetResponse = statusUpdateSteps.postUpdateToPost(
                origTweet, defaultUser, text);
        responseAsserts.verifyResponseContainsField(replyTweetResponse,
                StatusUpdateService.REPLY_TO_KEY,
                origTweet.getId());
    }
}
