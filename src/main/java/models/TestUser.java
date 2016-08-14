package models;

import utils.PropertiesReader;

public enum TestUser {
    DEFAULT("default"),
    PROTECTED("protected"),
    APPROVED_FOLLOWER("approvedfollower"),
    FOLLOWER_PENDING("pendingfollower"),
    UNRELATED_USER("unrelated");

    private final String username;
    private final String userId;
    private final String propertyKey;
    TestUser(String key){
        this.username = PropertiesReader.getPlainProperty(key + ".username");
        this.userId = PropertiesReader.getPlainProperty(key + ".userid");
        this.propertyKey = key;
    }
    public String getUsername(){
        return username;
    }
    public String getUserId(){
        return userId;
    }
    public String getPropertyKey(){
        return propertyKey;
    }
}
