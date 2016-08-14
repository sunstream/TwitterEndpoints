package asserts;

import api.response.ResponseParser;
import org.apache.http.HttpResponse;
import org.json.simple.JSONObject;
import org.junit.Assert;
import utils.CommonErrorMessages;

public class ResponseAsserts implements CommonErrorMessages {
    private final ResponseParser responseParser = new ResponseParser();
    public void verifyRequestSuccessful(HttpResponse response, int expectedCode){
        int actualCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals("Invalid response code in status line ["
                        +response.getStatusLine().toString()+"]",
                expectedCode, actualCode);
    }

    public void verifyRequestFailed(HttpResponse response, int expectedCode) {
        int actualCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals("Unexpected success response code in status line ["
                        +response.getStatusLine().toString()+"]",
                expectedCode, actualCode);
    }

    public void verifyResponseContainsField(HttpResponse response, String key, String expectedValue){
        JSONObject jsonObject = responseParser.getResponseAsJSONObject(response);
        Object fieldValue = jsonObject.get(key);
        Assert.assertNotNull(byNotFound(key, "response"), fieldValue);

        String actualValue = fieldValue.toString();
        Assert.assertEquals(byMismatch("Field "+key+" in response"), expectedValue, actualValue);
    }

}
