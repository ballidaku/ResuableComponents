package ballidaku.resuablecomponents.myTwitter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;
import com.twitter.sdk.android.tweetui.UserTimeline;

import ballidaku.resuablecomponents.MySharedPreference;
import ballidaku.resuablecomponents.R;
import retrofit2.Call;

/**
 * Created by brst-pc93 on 5/2/17.
 */

public class TwitterActivity extends AppCompatActivity
{
    String TAG = TwitterActivity.class.getSimpleName();

    Context context;

    private TwitterLoginButton loginButton;

    ListView listViewTwitter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_twitter);


        context = this;

        listViewTwitter = (ListView) findViewById(R.id.listViewTwitter);


        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>()
        {
            @Override
            public void success(Result<TwitterSession> result)
            {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";

                MySharedPreference.getInstance().saveTwitterUserName(context, session.getUserName());
                MySharedPreference.getInstance().saveTwitterUserID(context, session.getUserId());



                getTweets(session.getUserName());


                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


                refresh();
            }

            @Override
            public void failure(TwitterException exception)
            {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        refresh();

    }


    public void refresh()
    {

        Call<User> user = Twitter.getApiClient(Twitter.getSessionManager().getActiveSession()).getAccountService().verifyCredentials(true, false);
        user.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                String name = userResult.data.name;
                String email = userResult.data.email;

                // _normal (48x48px) | _bigger (73x73px) | _mini (24x24px)
                String photoUrlNormalSize   = userResult.data.profileImageUrl;
                String photoUrlBiggerSize   = userResult.data.profileImageUrl.replace("_normal", "_bigger");
                String photoUrlMiniSize     = userResult.data.profileImageUrl.replace("_normal", "_mini");
                String photoUrlOriginalSize = userResult.data.profileImageUrl.replace("_normal", "");

                Log.e(TAG,photoUrlNormalSize);
                Log.e(TAG,photoUrlBiggerSize);
                Log.e(TAG,photoUrlMiniSize);
                Log.e(TAG,photoUrlOriginalSize);


                Log.e(TAG,name+"  "+email);
            }

            @Override
            public void failure(TwitterException exc) {
                Log.d("TwitterKit", "Verify Credentials Failure", exc);
            }
        });
    }





    public void getTweets(String key)
    {
        final UserTimeline userTimeline = new UserTimeline.Builder()
                  .screenName(key)
                  .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                  .setTimeline(userTimeline)
                  .build();

        listViewTwitter.setAdapter(adapter);
    }


    public void postStatus(View v)
    {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                  .text("just setting up my Fabric.");
        //.image(myImageUri);
        builder.show();
    }


    public void showParticularTweet()
    {
        // TODO: Use a more specific parent
        final ViewGroup parentView = (ViewGroup) getWindow().getDecorView().getRootView();
        // TODO: Base this Tweet ID on some data from elsewhere in your app
        long tweetId = 631879971628183552L;
        TweetUtils.loadTweet(tweetId, new Callback<Tweet>()
        {
            @Override
            public void success(Result<Tweet> result)
            {
                TweetView tweetView = new TweetView(TwitterActivity.this, result.data);
                parentView.addView(tweetView);
            }

            @Override
            public void failure(TwitterException exception)
            {
                Log.d("TwitterKit", "Load Tweet failure", exception);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
