package models;

import org.json.simple.JSONObject;

public class Tweet {
    private String text;
    private String id;

    public Tweet(JSONObject object){
        text = object.get("text").toString();
        id = object.get("id_str").toString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
