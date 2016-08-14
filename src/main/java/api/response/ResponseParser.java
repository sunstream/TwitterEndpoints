package api.response;

import models.Tweet;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.CommonErrorMessages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

public final class ResponseParser implements CommonErrorMessages {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final JSONParser jsonParser = new JSONParser();
    public void printResponseStatus(HttpResponse response){
        System.out.println(response.getStatusLine());
    }

    public void printResponseContent(HttpResponse response){
        String responseString = getEntityAsString(response);
        logger.info(responseString);
    }

    public JSONArray getResponseAsJSONArray(HttpResponse response){
        JSONArray responseArray = null;
        String responseString = getEntityAsString(response);
        try {
            responseArray = (JSONArray)jsonParser.parse(responseString);
        } catch (ParseException e) {
            fail(byException(e)
                    + "Cannot parse HTTP response entity as JSON Array. Response entity: "+responseString);
        }
        return responseArray;
    }

    public JSONObject getResponseAsJSONObject(HttpResponse response){
        JSONObject jsonObject = null;
        String responseString = getEntityAsString(response);
        try {
            jsonObject = (JSONObject)jsonParser.parse(responseString);
        } catch (ParseException e){
            fail(byException(e)
                    + "Cannot parse HTTP response entity as JSON Object. Response entity: "+responseString);
        }
        return jsonObject;
    }

    public List<Tweet> getTweetsFromResponse(HttpResponse response){
        List<Tweet> tweets = new ArrayList<>();
        JSONArray jsonResponse = getResponseAsJSONArray(response);
        for (Object aJsonResponse : jsonResponse) {
            JSONObject tweetEntry = ((JSONObject) aJsonResponse);
            tweets.add(new Tweet(tweetEntry));
        }
        return tweets;
    }

    public Tweet getSingleTweetFromResponse(HttpResponse response){
        JSONObject jsonObject = getResponseAsJSONObject(response);
        return new Tweet(jsonObject);
    }

    public String getSingleTweetIdFromResponse(HttpResponse response){
        return getSingleTweetFromResponse(response).getId();
    }

    public List<String> getTweetIdsFromResponse(HttpResponse response){
        return getTweetsFromResponse(response).stream()
                .map(Tweet::getId)
                .collect(Collectors.toList());
    }

    private String getEntityAsString(HttpResponse response){
        try {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            fail(byException(e)
                    + "Cannot parse HTTP response. Response object: "+response.toString());
        }
        return null;
    }
}
