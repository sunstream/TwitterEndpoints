package asserts;

import models.Tweet;
import org.junit.Assert;
import utils.CommonErrorMessages;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TweetAsserts implements CommonErrorMessages {
    private static final String NEW_LINE = System.lineSeparator();

    public void verifyTweetCharCount(Tweet tweet, int expectedCharCount){
        int actualCharCount = removeExtraSpacesFromTweet(tweet.getText()).length();
        Assert.assertEquals(byMismatch("Tweet char count"), expectedCharCount, actualCharCount);
    }

    public void verifyTweetId(Tweet tweet, String expectedId){
        String actualId = removeExtraSpacesFromTweet(tweet.getId());
        Assert.assertEquals(byMismatch("Latest status update content"), expectedId, actualId );
    }

    public void verifyTweetText(Tweet tweet, String expectedText){
        expectedText = removeExtraSpacesFromTweet(expectedText);
        String actualText = removeExtraSpacesFromTweet(tweet.getText());
        Assert.assertEquals(byMismatch("Latest status update content"), expectedText.trim(), actualText.trim() );
    }

    private String removeExtraSpacesFromTweet(String text){
        return Arrays.asList(text.split("\\r?\\n"))
                .stream().map(String::trim).collect(Collectors.joining(NEW_LINE));
    }
}
