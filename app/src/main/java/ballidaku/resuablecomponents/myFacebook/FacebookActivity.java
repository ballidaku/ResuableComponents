package ballidaku.resuablecomponents.myFacebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import ballidaku.resuablecomponents.MyConstants;
import ballidaku.resuablecomponents.R;

/**
 * Created by brst-pc93 on 4/27/17.
 */

public class FacebookActivity extends AppCompatActivity
{


    String TAG = FacebookActivity.class.getSimpleName();

    Context context;

    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;


    RecyclerView recyclerView;
    FacebookRecyclerViewAdapter facebookRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //FacebookSdk.sdkInitialize(this.getApplicationContext());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_facebook);


        context = this;

       /* LoginManager.getInstance().logInWithPublishPermissions(
                FacebookActivity.this,
                Arrays.asList("publish_actions"));*/

        setUpIds();
    }

    private void setUpIds()
    {
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_posts"));

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        // If using in a fragment
        // loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                // App code
                Log.e(TAG, "Success");
            }

            @Override
            public void onCancel()
            {
                // App code
                Log.e(TAG, "Cancel");
            }

            @Override
            public void onError(FacebookException exception)
            {
                // App code
                Log.e(TAG, "Error");
            }
        });


        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        Log.e(TAG, "accessToken   " + accessToken);


        accessToken();

        profileTrack();


    }

    public void accessToken()
    {
        accessTokenTracker = new AccessTokenTracker()
        {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken)
            {
                // Set the access token using
                // currentAccessToken when it's loaded or set.

                Log.e(TAG, "oldAccessToken   " + oldAccessToken + "   currentAccessToken  " + currentAccessToken);
            }
        };

    }


    public void profileTrack()
    {
        profileTracker = new ProfileTracker()
        {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile)
            {
                // App code

                Log.e(TAG, "oldProfile   " + oldProfile + "   currentProfile  " + currentProfile);

                if (currentProfile != null)
                {
                    Log.e(TAG, "getName   " + currentProfile.getName());
                    Log.e(TAG, "getId   " + currentProfile.getId());
                    Log.e(TAG, "getFirstName   " + currentProfile.getFirstName());
                    Log.e(TAG, "getMiddleName   " + currentProfile.getMiddleName());
                    Log.e(TAG, "getLastName   " + currentProfile.getLastName());
                    Log.e(TAG, "getLinkUri   " + currentProfile.getLinkUri());
                    Log.e(TAG, "getProfilePictureUri   " + currentProfile.getProfilePictureUri(500, 500));
                }
            }
        };
    }


    public void getFeeds(View view)
    {
        GraphRequest request =  new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback()
                {
                    public void onCompleted(GraphResponse response)
                    {
                         //handle the result
                        Log.e(TAG, "Feeds  " + response.getRawResponse());


                        try
                        {
                            JSONObject jsonObject=new JSONObject(response.getRawResponse().toString());
                            JSONArray jsonArray=jsonObject.getJSONArray(MyConstants.DATA);

                            ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++)
                            {

                                JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                HashMap<String,String> map=new HashMap<>();
                                map.put(MyConstants.ID,jsonObject1.getString(MyConstants.ID));
                                map.put(MyConstants.CREATED_TIME,jsonObject1.getString(MyConstants.CREATED_TIME));
                                map.put(MyConstants.NAME,jsonObject1.optString(MyConstants.NAME));
                                map.put(MyConstants.TYPE,jsonObject1.getString(MyConstants.TYPE));
                                map.put(MyConstants.PICTURE,jsonObject1.optString(MyConstants.PICTURE));
                                map.put(MyConstants.STORY,jsonObject1.optString(MyConstants.STORY));
                                map.put(MyConstants.MESSAGE,jsonObject1.optString(MyConstants.MESSAGE));
                                map.put(MyConstants.LINK,jsonObject1.optString(MyConstants.LINK));


                                arrayList.add(map);
                            }

                            if(!arrayList.isEmpty())
                            {
                                setData(arrayList);
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
        );


        Bundle bundle=new Bundle();
        bundle.putString("fields","message,id,created_time,from,name,picture,object_id,type,link,story");
        request.setParameters(bundle);
        request.executeAsync();

    }

    ArrayList<HashMap<String,String>> mainArrayList=new ArrayList<>();
    private void setData(ArrayList<HashMap<String, String>> arrayList)
    {
        if(mainArrayList.isEmpty())
        {
            mainArrayList.addAll(arrayList);
            setUpRecyclerView(mainArrayList);
        }
        else
        {
            mainArrayList.addAll(arrayList);
            facebookRecyclerViewAdapter.refresh(mainArrayList);
        }
    }


    private void setUpRecyclerView(ArrayList<HashMap<String, String>> mainArrayList)
    {
        facebookRecyclerViewAdapter = new FacebookRecyclerViewAdapter(context,mainArrayList );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(facebookRecyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        /*
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerView);*/

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        accessTokenTracker.stopTracking();

        profileTracker.stopTracking();
    }


}