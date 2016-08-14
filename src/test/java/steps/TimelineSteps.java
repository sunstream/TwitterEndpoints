package steps;

import api.request.TimelineService;
import api.response.ResponseParser;
import models.TestUser;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import static api.request.TimelineService.*;

public class TimelineSteps {
    private TimelineService timeline;
    private final ResponseParser responseParser = new ResponseParser();

    private TimelineService getTimelineService(){
        if (timeline == null){
            timeline = new TimelineService();
        }
        return timeline;
    }

    public HttpResponse requestTimeline(TestUser testUser, int count){
        return getTimelineService().requestUserTimeline(testUser, count);
    }

    public HttpResponse requestLastTweet(TestUser testUser){
        int tweetsCount = 1;
        return requestTimeline(testUser, tweetsCount);
    }

    public HttpResponse requestLastTweetByUserId(TestUser testUser){
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(TIMELINE_OWNER_USER_ID_KEY, testUser.getUserId());
        return getTimelineService().requestLatestTweet(testUser, urlParams);
    }

    public HttpResponse requestTimelineOfDifferentUser(String timelineOwnerName, TestUser requester, int count){
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(TIMELINE_OWNER_USERNAME_KEY, timelineOwnerName);
        urlParams.put(TWEETS_COUNT_KEY, count + "");
        return getTimelineService().requestUserTimeline(requester, urlParams);
    }

    public HttpResponse requestTimelineSince(String sinceId, TestUser testUser, int count){
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(TIMELINE_OWNER_USERNAME_KEY, testUser.getUsername());
        urlParams.put(TIMELINE_SINCE_ID_KEY, sinceId);
        urlParams.put(TWEETS_COUNT_KEY, count + "");
        return getTimelineService().requestUserTimeline(testUser, urlParams);
    }

    public HttpResponse requestTimelineBefore(String maxId, TestUser testUser, int count){
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(TIMELINE_OWNER_USERNAME_KEY, testUser.getUsername());
        urlParams.put(TIMELINE_MAX_ID_KEY, maxId);
        urlParams.put(TWEETS_COUNT_KEY, count + "");
        return getTimelineService().requestUserTimeline(testUser, urlParams);
    }

    public HttpResponse requestTimelineOfDifferentUserBefore(String maxId, String timelineOwner,
                                                             TestUser testUser, int count){
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(TIMELINE_OWNER_USERNAME_KEY, timelineOwner);
        urlParams.put(TIMELINE_MAX_ID_KEY, maxId);
        urlParams.put(TWEETS_COUNT_KEY, count + "");
        return getTimelineService().requestUserTimeline(testUser, urlParams);
    }
}
