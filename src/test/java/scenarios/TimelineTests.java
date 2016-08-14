package scenarios;

import api.response.ResponseParser;
import asserts.ResponseAsserts;
import asserts.TweetAsserts;
import models.TestUser;
import models.Tweet;
import org.apache.http.HttpResponse;
import org.junit.Test;
import steps.StatusUpdateSteps;
import asserts.TimelineAsserts;
import steps.TimelineSteps;

import java.util.ArrayList;
import java.util.List;

public class TimelineTests {
    private final TimelineSteps timelineSteps = new TimelineSteps();
    private final StatusUpdateSteps statusUpdateSteps = new StatusUpdateSteps();

    private final TimelineAsserts timelineAsserts = new TimelineAsserts();
    private final TweetAsserts tweetAsserts = new TweetAsserts();
    private final ResponseAsserts responseAsserts = new ResponseAsserts();

    private final TestUser defaultUser = TestUser.DEFAULT;
    private final ResponseParser responseParser = new ResponseParser();

    private static final int DEFAULT_TWEETS_COUNT = 5;
    private static final int MAX_TWEETS_PER_REQUEST = 200;
    private static final int MAX_HISTORY_SIZE_ALLOWED = 3200;
    private static final String USER_WITH_MANY_TWEETS = "youtube";

    @Test
    public void should_ReturnExpectedTweetsCountInTimeline_WhenTotalCountIsHigher(){
        final int expectedCount = DEFAULT_TWEETS_COUNT;
        HttpResponse timelineResponse = timelineSteps.requestTimeline(defaultUser, expectedCount);
        List<Tweet> tweets = responseParser.getTweetsFromResponse(timelineResponse);
        timelineAsserts.verifyTweetsCount(tweets, expectedCount);
    }

    @Test
    public void should_ReturnLatestFeed_ByUserId(){
        String latestStatusUpdate = statusUpdateSteps.postArbitraryStatusUpdate(defaultUser);
        HttpResponse lastTweetResponse = timelineSteps.requestLastTweetByUserId(defaultUser);
        Tweet lastTweet = responseParser.getTweetsFromResponse(lastTweetResponse).get(0);
        tweetAsserts.verifyTweetText(lastTweet, latestStatusUpdate);
    }

    @Test
    public void should_ReturnLatestFeed_ByScreenName(){
        String latestStatusUpdate = statusUpdateSteps.postArbitraryStatusUpdate(defaultUser);
        HttpResponse lastTweetResponse = timelineSteps.requestLastTweet(defaultUser);
        Tweet lastTweet = responseParser.getTweetsFromResponse(lastTweetResponse).get(0);
        tweetAsserts.verifyTweetText(lastTweet, latestStatusUpdate);
    }

    @Test
    public void should_ReturnProtectedTimeline_WhenRequestedByApprovedFollower(){
        TestUser userRequestingTimeline = TestUser.APPROVED_FOLLOWER;
        String timelineOwnerName = TestUser.PROTECTED.getUsername();
        HttpResponse timelineResponse = timelineSteps
                .requestTimelineOfDifferentUser(timelineOwnerName, userRequestingTimeline, DEFAULT_TWEETS_COUNT);
        int successResponseCode = 200;
        responseAsserts.verifyRequestSuccessful(timelineResponse, successResponseCode);
    }

    @Test
    public void should_NotReturnProtectedTimeline_WhenRequestedByPendingFollower(){
        TestUser userRequestingTimeline = TestUser.FOLLOWER_PENDING;
        String timelineOwnerName = TestUser.PROTECTED.getUsername();
        HttpResponse timelineResponse = timelineSteps
                .requestTimelineOfDifferentUser(timelineOwnerName, userRequestingTimeline, DEFAULT_TWEETS_COUNT);
        int failureResponseCode = 401;
        responseAsserts.verifyRequestFailed(timelineResponse, failureResponseCode);
    }

    @Test
    public void should_NotReturnProtectedTimeline_WhenRequestedByUnsubscribedUser(){
        TestUser userRequestingTimeline = TestUser.UNRELATED_USER;
        String timelineOwnerName = TestUser.PROTECTED.getUsername();
        HttpResponse timelineResponse = timelineSteps
                .requestTimelineOfDifferentUser(timelineOwnerName, userRequestingTimeline, DEFAULT_TWEETS_COUNT);
        int failureResponseCode = 401;
        responseAsserts.verifyRequestFailed(timelineResponse, failureResponseCode);
    }

    @Test
    public void should_ReturnTopLimitTweets_WhenRequestedMore(){
        String timelineOwnerName = "youtube";
        int tweetsCount = MAX_TWEETS_PER_REQUEST + 1;
        HttpResponse timelineResponse = timelineSteps
                .requestTimelineOfDifferentUser(timelineOwnerName, defaultUser, tweetsCount);
        List<Tweet> tweets = responseParser.getTweetsFromResponse(timelineResponse);
        timelineAsserts.verifyTweetsCount(tweets, MAX_TWEETS_PER_REQUEST);
    }

    @Test
    public void should_ReturnPosteriorTweets_WhenStartPointSpecified(){
        HttpResponse response = timelineSteps.requestLastTweet(defaultUser);
        Tweet lastTweet = responseParser.getTweetsFromResponse(response).get(0);
        String lastTweetId = lastTweet.getId();

        int posteriorTweetsCount = 2;
        statusUpdateSteps.postArbitraryStatusUpdates(defaultUser, posteriorTweetsCount);
        response = timelineSteps.requestTimelineSince(lastTweetId, defaultUser, MAX_TWEETS_PER_REQUEST);
        List<Tweet> tweets = responseParser.getTweetsFromResponse(response);
        timelineAsserts.verifyTweetsCount(tweets, posteriorTweetsCount);
    }

    @Test
    public void should_ReturnNoTweets_WhenStartPointIsLastOne(){
        HttpResponse response = timelineSteps.requestLastTweet(defaultUser);
        Tweet lastTweet = responseParser.getTweetsFromResponse(response).get(0);
        String lastTweetId = lastTweet.getId();
        int expectedTweetsCount = 0;
        response = timelineSteps.requestTimelineSince(lastTweetId, defaultUser, MAX_TWEETS_PER_REQUEST);
        List<Tweet> tweets = responseParser.getTweetsFromResponse(response);
        timelineAsserts.verifyTweetsCount(tweets, expectedTweetsCount);
    }

    @Test
    public void should_ReturnPriorFeeds_WhenEndPointIsSpecified(){
        HttpResponse response = timelineSteps.requestTimeline(defaultUser, 2);
        List<Tweet> lastTweets = responseParser.getTweetsFromResponse(response);
        String nextToLastTweetId = lastTweets.get(1).getId();

        response = timelineSteps.requestTimelineBefore(nextToLastTweetId, defaultUser, DEFAULT_TWEETS_COUNT);
        List<Tweet> tweets = responseParser.getTweetsFromResponse(response);
        Tweet lastReturnedTweet = tweets.get(0);
        tweetAsserts.verifyTweetId(lastReturnedTweet, nextToLastTweetId);
    }

    @Test
    public void should_ReturnUpToSpecifiedMostRecentTweetsNumber(){
        //? This method can only return up to 3,200 of a user’s most recent Tweets
        HttpResponse response = timelineSteps.requestTimelineOfDifferentUser(USER_WITH_MANY_TWEETS,
                defaultUser, MAX_TWEETS_PER_REQUEST);
        List<String> allTweetIds = new ArrayList<>();
        List<String> idsFromLastResponse = responseParser.getTweetIdsFromResponse(response);
        allTweetIds.addAll(idsFromLastResponse);
        while (idsFromLastResponse.size() == MAX_TWEETS_PER_REQUEST
                || allTweetIds.size() <= MAX_HISTORY_SIZE_ALLOWED ){
            String oldestTweetId = idsFromLastResponse.get(MAX_TWEETS_PER_REQUEST - 1);
            response = timelineSteps.requestTimelineOfDifferentUserBefore(
                    oldestTweetId, USER_WITH_MANY_TWEETS,
                    defaultUser, MAX_TWEETS_PER_REQUEST);
            idsFromLastResponse = responseParser.getTweetIdsFromResponse(response);
            allTweetIds.addAll(idsFromLastResponse);
        }
        timelineAsserts.verifyMaxAllowedHistorySize(allTweetIds, MAX_HISTORY_SIZE_ALLOWED);
    }

    //trim user tests
    //exclude replies test
    //bla bla bla all other parameters tests

}
