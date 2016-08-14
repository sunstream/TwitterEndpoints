package steps;

import api.request.StatusUpdateService;
import models.TestUser;
import models.Tweet;
import org.apache.http.HttpResponse;
import utils.PostGenerator;

import java.util.HashMap;
import java.util.Map;

import static api.request.StatusUpdateService.*;

public class StatusUpdateSteps {
    private final PostGenerator postGenerator = new PostGenerator();
    private StatusUpdateService statusUpdate;

    public HttpResponse postUpdate(TestUser testUser, String text){
        return getStatusUpdateService().postUpdate(testUser, text);
    }

    public HttpResponse postUpdateToPost(Tweet origTweet, TestUser testUser, String text){
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(TEXT_KEY, getStatusUpdateService().encodeForUrl(text));
        urlParams.put(REPLY_TO_KEY, origTweet.getId());
        return getStatusUpdateService().postUpdate(testUser, urlParams);
    }

    public String postArbitraryStatusUpdate(TestUser testUser){
        String text = generateArbitraryPostText();
        getStatusUpdateService().postUpdate(testUser, text);
        return text;
    }

    public void postArbitraryStatusUpdates(TestUser testUser, int updatesCount){
        for (int i=0; i< updatesCount; i++){
            postArbitraryStatusUpdate(testUser);
        }
    }

    public String generateArbitraryPostText(){
        return postGenerator.generateArbitraryPost();
    }

    public String generateRandomPostText(int charCount){
        return postGenerator.generateRandomPost(charCount);
    }

    private StatusUpdateService getStatusUpdateService(){
        if (statusUpdate == null){
            statusUpdate = new StatusUpdateService();
        }
        return statusUpdate;
    }
}
