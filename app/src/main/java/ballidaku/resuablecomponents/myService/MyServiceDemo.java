package ballidaku.resuablecomponents.myService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ballidaku.resuablecomponents.R;

/**
 * Created by brst-pc93 on 6/7/17.
 */

public class MyServiceDemo extends AppCompatActivity
{
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_demo);

        context=this;
    }


    public void onClickIntentService(View v)
    {
        startActivity(new Intent(context,MyIntentServiceActivity.class));
    }

    public void onClickService(View v)
    {
        startActivity(new Intent(context,MyServiceActivity.class));
    }


}
