package asserts;

import models.Tweet;
import org.junit.Assert;
import utils.CommonErrorMessages;

import java.util.List;

public class TimelineAsserts implements CommonErrorMessages{
    public void verifyTweetsCount(List<Tweet> tweets, int expectedCount){
        int actualCount= tweets.size();
        Assert.assertEquals(byInvalidCount("tweets in user timeline"), expectedCount, actualCount);
    }

    public void verifyMaxAllowedHistorySize(List<String> tweetIds, int expectedHistorySize){
        int actualHistorySize = tweetIds.size();
        Assert.assertEquals(byMismatch("History size"), expectedHistorySize, actualHistorySize);
    }
}

