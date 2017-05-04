package ballidaku.resuablecomponents.myFacebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import ballidaku.resuablecomponents.MyConstants;
import ballidaku.resuablecomponents.MySharedPreference;
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
    Bitmap image;

    ShareDialog shareDialog;


    private Uri fileUri;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;


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

        shareDialog = new ShareDialog(FacebookActivity.this);

        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);


        setUpIds();

     /*   Uri imageUri =UrifromFile(new File( "/storage/emulated/0/DCIM/Camera/IMG_20161128_020425.jpg";
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e)
        {
            e.printStackTrace();
        }*/

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

                    MySharedPreference.getInstance().saveUserName(context, currentProfile.getName());
                    MySharedPreference.getInstance().saveUserImage(context, currentProfile.getProfilePictureUri(500, 500).toString());
                }
            }
        };
    }


    public void getFeeds(View view)
    {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback()
                {
                    public void onCompleted(GraphResponse response)
                    {

                        Log.e(TAG, "Feeds  " + response.getRawResponse());


                        try
                        {
                            JSONObject jsonObject = new JSONObject(response.getRawResponse().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(MyConstants.DATA);

                            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++)
                            {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                HashMap<String, String> map = new HashMap<>();
                                map.put(MyConstants.ID, jsonObject1.getString(MyConstants.ID));
                                map.put(MyConstants.CREATED_TIME, jsonObject1.getString(MyConstants.CREATED_TIME));
                                map.put(MyConstants.NAME, jsonObject1.optString(MyConstants.NAME));
                                map.put(MyConstants.TYPE, jsonObject1.getString(MyConstants.TYPE));
                                map.put(MyConstants.PICTURE, jsonObject1.optString(MyConstants.PICTURE));
                                map.put(MyConstants.FULL_PICTURE, jsonObject1.optString(MyConstants.FULL_PICTURE));
                                map.put(MyConstants.STORY, jsonObject1.optString(MyConstants.STORY));
                                map.put(MyConstants.MESSAGE, jsonObject1.optString(MyConstants.MESSAGE));
                                map.put(MyConstants.LINK, jsonObject1.optString(MyConstants.LINK));


//                                Log.e(TAG, map + "");

                                arrayList.add(map);
                            }

                            if (!arrayList.isEmpty())
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


        Bundle bundle = new Bundle();
        bundle.putString("fields", "message,id,created_time,from,name,picture,object_id,type,link,story,full_picture,icon");
        request.setParameters(bundle);
        request.executeAsync();

    }

    ArrayList<HashMap<String, String>> mainArrayList = new ArrayList<>();

    private void setData(ArrayList<HashMap<String, String>> arrayList)
    {
        if (mainArrayList.isEmpty())
        {
            mainArrayList.addAll(arrayList);
            setUpRecyclerView(mainArrayList);
        }
        else
        {
            mainArrayList=arrayList;
            facebookRecyclerViewAdapter.refresh(mainArrayList);
        }
    }


    private void setUpRecyclerView(ArrayList<HashMap<String, String>> mainArrayList)
    {
        facebookRecyclerViewAdapter = new FacebookRecyclerViewAdapter(context, mainArrayList);
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


    public void postImage(View v)
    {


        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);


    }

    public void postStatus(View v)
    {
        if (ShareDialog.canShow(ShareLinkContent.class))
        {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    //.setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .build();


            shareDialog.show(linkContent);
        }
    }

    public void postVideo(View v)
    {
        // create new Intentwith with Standard Intent action that can be
        // sent to have the camera application capture an video and return it.
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        // create a file to save the video
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set the image file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // set the video image quality to high
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        // start the Video Capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }


    /**
     * Create a file Uri for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type)
    {

        return Uri.fromFile(getOutputMediaFile(type));
    }


    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(int type)
    {

        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraVideo");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists())
        {

            if (!mediaStorageDir.mkdirs())
            {

                Log.e(TAG, "Failed to create directory MyCameraVideo.");

                Toast.makeText(context, "Failed to create directory MyCameraVideo.", Toast.LENGTH_LONG).show();

                Log.e("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }


        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO)
        {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");

        }
        else
        {
            return null;
        }

        return mediaFile;
    }



    public void videoToFacebook(Uri data)
    {
        ShareVideo shareVideo = new ShareVideo.Builder()
                  .setLocalUrl(data)
                  .build();
        ShareVideoContent content = new ShareVideoContent.Builder()
                  .setVideo(shareVideo)
                  .build();

        shareDialog.show(content);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        // After camera screen this code will excuted

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE)
        {

            if (resultCode == RESULT_OK)
            {

                Log.e(TAG, "Video File : " + data.getData());

                videoToFacebook(data.getData());

                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video saved to: " + data.getData(), Toast.LENGTH_LONG).show();

            }
            else if (resultCode == RESULT_CANCELED)
            {

                Log.e(TAG, "User cancelled the video capture.");

                // User cancelled the video capture
                Toast.makeText(this, "User cancelled the video capture.", Toast.LENGTH_LONG).show();

            }
            else
            {

                Log.e(TAG, "Video capture failed.");

                // Video capture failed, advise user
                Toast.makeText(this, "Video capture failed.", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        accessTokenTracker.stopTracking();

        profileTracker.stopTracking();
    }


}