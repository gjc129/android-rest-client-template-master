package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet
{
    public String body;
    public long uid;
    public String createdAt;
    public User user;

    //emppty constructor needed by the parceler library
    public Tweet()
    {

    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {

        Tweet tweet = new Tweet();
        String timeStamp;

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        timeStamp = jsonObject.getString("created_at");
        tweet.createdAt = TimeFormatter.getTimeDifference(timeStamp);
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        return tweet;

    }


}
