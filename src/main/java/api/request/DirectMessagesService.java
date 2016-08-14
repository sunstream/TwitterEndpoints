package api.request;

import models.TestUser;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public final class DirectMessagesService extends TwitterEndpoint {
    private static final int DEFAULT_COUNT = 5;
    public HttpResponse getDirectMessages(TestUser testUser){
        return getDirectMessages(testUser, DEFAULT_COUNT);
    }

    public HttpResponse getDirectMessages(TestUser testUser, int count){
        setMethodName(GET_REQUEST);
        setUrl("https://api.twitter.com/1.1/direct_messages.json");
        Map<String,String> urlParams = new HashMap<>();
        urlParams.put("count", count+"");
        setUrlParams(urlParams);
        setTestUser(testUser);
        return sendRequest();
    }
}
