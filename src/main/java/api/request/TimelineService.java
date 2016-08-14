package api.request;

import models.TestUser;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public final class TimelineService extends TwitterEndpoint {

    public static final String TIMELINE_OWNER_USERNAME_KEY = "screen_name";
    public static final String TIMELINE_OWNER_USER_ID_KEY = "user_id";
    public static final String TIMELINE_SINCE_ID_KEY = "since_id";
    public static final String TIMELINE_MAX_ID_KEY = "max_id";
    public static final String TWEETS_COUNT_KEY = "count";

    public HttpResponse requestLatestTweet(TestUser testUser) {
        return requestUserTimeline(testUser, 1);
    }

    public HttpResponse requestLatestTweet(TestUser testUser, Map<String,String> urlParams){
        urlParams.put(TWEETS_COUNT_KEY, "1");
        return requestUserTimeline(testUser, urlParams);
    }

    public HttpResponse requestUserTimeline(TestUser testUser, int count) {
        Map<String, String> params = new HashMap<>();
        params.put(TIMELINE_OWNER_USERNAME_KEY, testUser.getUsername());
        params.put(TWEETS_COUNT_KEY, count + "");
        return requestUserTimeline(testUser, params);
    }

    public HttpResponse requestUserTimeline(TestUser testUser, Map<String, String> urlParams){
        setUrl("https://api.twitter.com/1.1/statuses/user_timeline.json");
        setMethodName(GET_REQUEST);
        setUrlParams(urlParams);
        setTestUser(testUser);
        return sendRequest();
    }
}
