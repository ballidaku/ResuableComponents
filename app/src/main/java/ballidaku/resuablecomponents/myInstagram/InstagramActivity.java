package ballidaku.resuablecomponents.myInstagram;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import ballidaku.resuablecomponents.R;
import ballidaku.resuablecomponents.myInstagram.util.Constants;
import ballidaku.resuablecomponents.myInstagram.util.InstagramApp;
import ballidaku.resuablecomponents.myTwitter.TwitterActivity;

/**
 * Created by brst-pc93 on 5/2/17.
 */

public class InstagramActivity extends AppCompatActivity implements View.OnClickListener
{
    String TAG = TwitterActivity.class.getSimpleName();

    Context context;


    InstagramApp mApp;

    private Button btnConnect,
              btnViewInfo,
              btnGetAllImages,
              btnFollowers,
              btnFollwing;
    private LinearLayout llAfterLoginView;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();


    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            if (msg.what == InstagramApp.WHAT_FINALIZE)
            {
                userInfoHashmap = mApp.getUserInfo();
            }
            else if (msg.what == InstagramApp.WHAT_FINALIZE)
            {
                Toast.makeText(InstagramActivity.this, "Check your network.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_instagram);


        context = this;


        setWidgetReference();
        bindEventHandlers();

        mApp = new InstagramApp(this, Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener()
        {
            @Override
            public void onSuccess()
            {
// tvSummary.setText("Connected as " + mApp.getUserName());
                btnConnect.setText("Disconnect");
                llAfterLoginView.setVisibility(View.VISIBLE);
// userInfoHashmap = mApp.
                mApp.fetchUserName(handler);
            }

            @Override
            public void onFail(String error)
            {
                Toast.makeText(InstagramActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });


        if (mApp.hasAccessToken())
        {
            // tvSummary.setText("Connected as " + mApp.getUserName());
            btnConnect.setText("Disconnect");
            llAfterLoginView.setVisibility(View.VISIBLE);
            mApp.fetchUserName(handler);

        }


    }


    //Options Setting
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.dash_board)
        {
            Intent intent = new Intent(this, DashBoardActivty.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void bindEventHandlers()
    {
        btnConnect.setOnClickListener(this);
        btnViewInfo.setOnClickListener(this);
        btnGetAllImages.setOnClickListener(this);
        btnFollwing.setOnClickListener(this);
        btnFollowers.setOnClickListener(this);
    }

    private void setWidgetReference()
    {
        llAfterLoginView = (LinearLayout) findViewById(R.id.llAfterLoginView);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnViewInfo = (Button) findViewById(R.id.btnViewInfo);
        btnGetAllImages = (Button) findViewById(R.id.btnGetAllImages);
        btnFollowers = (Button) findViewById(R.id.btnFollows);
        btnFollwing = (Button) findViewById(R.id.btnFollowing);
    }

    // OAuthAuthenticationListener listener ;

    @Override
    public void onClick(View v)
    {
        if (v == btnConnect)
        {
            connectOrDisconnectUser();
        }
        else if (v == btnViewInfo)
        {
            displayInfoDialogView();
        }
        else if (v == btnGetAllImages)
        {
            startActivity(new Intent(InstagramActivity.this, AllMediaFiles.class)
                      .putExtra("userInfo", userInfoHashmap));
        }
        else
        {
            String url = "";
            if (v == btnFollowers)
            {
                url = "https://api.instagram.com/v1/users/"
                          + userInfoHashmap.get(InstagramApp.TAG_ID)
                          + "/follows?access_token=" + mApp.getTOken();



            }
            else if (v == btnFollwing)
            {
                url = "https://api.instagram.com/v1/users/"
                          + userInfoHashmap.get(InstagramApp.TAG_ID)
                          + "/followed-by?access_token=" + mApp.getTOken();
            }
            Log.e("ABC",url);
            startActivity(new Intent(InstagramActivity.this, Relationship.class) .putExtra("userInfo", url));
        }
    }

    private void connectOrDisconnectUser()
    {
        if (mApp.hasAccessToken())
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                      InstagramActivity.this);
            builder.setMessage("Disconnect from Instagram?")
                   .setCancelable(false)
                   .setPositiveButton("Yes",
                             new DialogInterface.OnClickListener()
                             {
                                 public void onClick(DialogInterface dialog,
                                                     int id)
                                 {
                                     mApp.resetAccessToken();
                                     // btnConnect.setVisibility(View.VISIBLE);
                                     llAfterLoginView.setVisibility(View.GONE);
                                     btnConnect.setText("Connect");
                                     // tvSummary.setText("Not connected");
                                 }
                             })
                   .setNegativeButton("No",
                             new DialogInterface.OnClickListener()
                             {
                                 public void onClick(DialogInterface dialog,
                                                     int id)
                                 {
                                     dialog.cancel();
                                 }
                             });
            final AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
            mApp.authorize();
        }
    }


    private void displayInfoDialogView()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InstagramActivity.this);
        alertDialog.setTitle("Profile Info");

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.profile_view, null);
        alertDialog.setView(view);
        ImageView ivProfile = (ImageView) view .findViewById(R.id.ivProfileImage);
        TextView tvName = (TextView) view.findViewById(R.id.tvUserName);
        TextView tvNoOfFollwers = (TextView) view.findViewById(R.id.tvNoOfFollowers);
        TextView tvNoOfFollowing = (TextView) view.findViewById(R.id.tvNoOfFollowing);
        TextView textViewMedia = (TextView) view.findViewById(R.id.textViewMedia);

       /* new ImageLoader(InstagramActivity.this).DisplayImage(
                  userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE),
                  ivProfile);*/

        setImage(ivProfile, userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE));


        tvName.setText(userInfoHashmap.get(InstagramApp.TAG_USERNAME));
        tvNoOfFollowing.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWS));
        tvNoOfFollwers.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWED_BY));
        textViewMedia.setText(userInfoHashmap.get(InstagramApp.TAG_MEDIA));
        alertDialog.create().show();
    }


    public void setImage(ImageView imageView, String url)
    {
        Picasso.with(context)
               .load(url)
               // .resize(50, 50)
               //.centerCrop()
               .into(imageView);
    }


}
