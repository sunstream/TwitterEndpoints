package utils;

import models.TestUser;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpRequest;

import static org.junit.Assert.fail;

public final class OAuthSigner implements CommonErrorMessages{
    private final OAuthConsumer consumer;
    private final String propertyKey;
    public OAuthSigner(TestUser user){
        this.propertyKey = user.getPropertyKey();

        final String oAuthConsumerKey = readProperty("oAuthConsumerKey");
        final String oAuthConsumerSecret = readProperty("oAuthConsumerSecret");
        final String oAuthAccessToken = readProperty("oAuthAccessToken");
        final String oAuthAccessTokenSecret = readProperty("oAuthAccessTokenSecret");

        consumer = new CommonsHttpOAuthConsumer(oAuthConsumerKey, oAuthConsumerSecret);
        consumer.setTokenWithSecret(oAuthAccessToken, oAuthAccessTokenSecret);
    }

    private String readProperty(String paramName){
        return PropertiesReader.getEncodedProperty(propertyKey + "." + paramName);
    }

    public void signRequest(HttpRequest request){
        try {
            consumer.sign(request);
        } catch (OAuthMessageSignerException
                | OAuthExpectationFailedException
                | OAuthCommunicationException e) {
            fail(byException(e, "Cannot sign HTTP request ["+request.getRequestLine()+"]"));
        }
    }
}
