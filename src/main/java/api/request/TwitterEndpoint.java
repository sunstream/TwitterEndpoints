package api.request;

import models.TestUser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.CommonErrorMessages;
import utils.OAuthSigner;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

abstract class TwitterEndpoint implements CommonErrorMessages {
    static final String GET_REQUEST = "GET";
    static final String POST_REQUEST = "POST";

    private String methodName;
    private String url;
    private Map<String, String> urlParams;
    private TestUser testUser;
    private final Logger logger = LogManager.getLogger(this.getClass());

    HttpResponse sendRequest(){
        HttpUriRequest request = getRequestByMethodType();
        OAuthSigner oAuth = new OAuthSigner(testUser);
        oAuth.signRequest(request);

        logger.debug("Prepared request: "+request.getRequestLine());

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            fail(byException(e) + "Failed to execute request ["+request.getRequestLine()+"]");
        }
        return response;
    }

    private HttpUriRequest getRequestByMethodType(){
        String urlWithParams = getUrlWithParams();

        HttpUriRequest request = null;
        switch (methodName){
            case GET_REQUEST: {
                request = new HttpGet(urlWithParams);
                break;
            }
            case POST_REQUEST: {
                request = new HttpPost(urlWithParams);
                break;
            }
            default:
                fail("Invalid HTTP method name: "+methodName);
        }
        return request;
    }

    private String getUrlWithParams(){
        String urlParamsStr = urlParams
                .entrySet()
                .stream()
                .map(entry -> entry.getKey()+"="+entry.getValue())
                .collect(Collectors.joining("&"));
        return url + "?" + urlParamsStr;
    }

    void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    void setUrl(String url) {
        this.url = url;
    }

    void setUrlParams(Map<String, String> urlParams) {
        this.urlParams = urlParams;
    }

    void setTestUser(TestUser testUser) {
        this.testUser = testUser;
    }
}