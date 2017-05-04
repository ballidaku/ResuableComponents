package ballidaku.resuablecomponents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ballidaku.resuablecomponents.myFacebook.FacebookActivity;
import ballidaku.resuablecomponents.myInstagram.InstagramActivity;
import ballidaku.resuablecomponents.myRealm.RealmActivity;
import ballidaku.resuablecomponents.myTwitter.TwitterActivity;

/**
 * Created by brst-pc93 on 4/27/17.
 */

public class MainScreen extends AppCompatActivity
{

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        context=this;

        //MyUtil.getKeyHash(context);


    }



    public void relam(View v)
    {
        startActivity(new Intent(context, RealmActivity.class));
    }

    public void facebook(View v)
    {
        startActivity(new Intent(context, FacebookActivity.class));
    }

    public void twitter(View v)
    {
        startActivity(new Intent(context, TwitterActivity.class));
    }

    public void instagram(View v)
    {
        startActivity(new Intent(context, InstagramActivity.class));
    }
}