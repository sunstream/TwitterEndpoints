package api.request;

import models.TestUser;
import org.apache.http.HttpResponse;
import utils.Base64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public final class StatusUpdateService extends TwitterEndpoint {
    public static final String TEXT_KEY = "status";
    public static final String REPLY_TO_KEY = "in_reply_to_status_id";

    public HttpResponse postUpdate(TestUser testUser, String text){
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(TEXT_KEY, encodeForUrl(text));
        return postUpdate(testUser, urlParams);
    }

    public HttpResponse postUpdate(TestUser testUser, Map<String, String>urlParams){
        setUrl("https://api.twitter.com/1.1/statuses/update.json");
        setMethodName(POST_REQUEST);
        setUrlParams(urlParams);
        setTestUser(testUser);
        return sendRequest();
    }

    public String encodeForUrl(String text){
        try {
            return URLEncoder.encode(text, Base64Encoder.ENCODING);
        } catch (UnsupportedEncodingException e) {
            fail("Cannot encode text to "+Base64Encoder.ENCODING+": "+text);
        }
        return null;
    }
}
