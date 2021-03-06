package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 140;
    public static String RED_HEX_COLOR = "#FF0000";

    private EditText etCompose;
    private Button btnTweet;
    private TwitterClient client;
    private TextView tvCharacters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tvCharacters = findViewById(R.id.tvCharacters);


        //set type listener on the multiline textview for character count
        etCompose.addTextChangedListener(new TextWatcher()
        {
            String charLength;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int counterValue = s.length();

                charLength = String.valueOf(s.length());
                tvCharacters.setText(charLength);

                if(counterValue >= MAX_TWEET_LENGTH)
                {
                    tvCharacters.setTextColor(Color.RED);
                    btnTweet.setEnabled(false);
                }
                else
                {
                    tvCharacters.setTextColor(Color.BLACK);
                    btnTweet.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        //set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                // error-handling
                if(tweetContent.isEmpty())
                {
                    Toast.makeText(ComposeActivity.this, "Your Tweet is empty!", Toast.LENGTH_LONG).show();
                    return;

                }
                if(tweetContent.length() > MAX_TWEET_LENGTH)
                {
                    Toast.makeText(ComposeActivity.this, "Your tweet is too long!", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this, "Tweet successful!", Toast.LENGTH_LONG).show();
                // make api call to twitter to publish the content in edit text
                client.composeTweet(tweetContent, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                    {
                        Log.d("TwitterClient", "Successfully Posted Tweet!" + response.toString());
                        try {
                            Tweet tweet = Tweet.fromJson(response);
                            Intent data = new Intent();
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, data);
                            finish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                    {
                        Log.e("TwitterClient", "Failed to publish the post!" + responseString);
                    }
                });
            }
        });

    }
}

