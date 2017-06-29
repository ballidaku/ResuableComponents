package ballidaku.resuablecomponents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ballidaku.resuablecomponents.myFacebook.FacebookActivity;
import ballidaku.resuablecomponents.myInstagram.InstagramActivity;
import ballidaku.resuablecomponents.myLocation.GetLocation;
import ballidaku.resuablecomponents.myRealm.RealmActivity;
import ballidaku.resuablecomponents.myService.MyServiceDemo;
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

        context = this;

        //MyUtil.getKeyHash(context);


/*
        Thread thread = new Thread() {
            @Override
            public void run() {

                    hello();
            }
        };

        thread.start();*/


    }

 /*   public void hello()
    {
        try {
            String token = GoogleAuthUtil.getToken(this, "sharanpalsinghsraa@gmail.com", "https://www.googleapis.com/auth/devstorage.read_only");
            System.out.println(token);
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (UserRecoverableAuthException e) {
            System.out.println("UserRecoverableAuthException");
        } catch (GoogleAuthException e) {
            System.out.println("GoogleAuthException");
        }
    }*/


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


    public void service(View v)
    {
        startActivity(new Intent(context, MyServiceDemo.class));
    }

    public void location(View v)
    {
        startActivity(new Intent(context, GetLocation.class));
    }

    public void maps(View v)
    {

        // startActivity(new Intent(context, MapActivity.class));

//        TheDemo theDemo = new TheDemo();
        // new TestThread("THREAD 1", theDemo);
//        new TestThread("THREAD 2", theDemo);
//        new TestThread("THREAD 3", theDemo);


//        theDemo.test("THREAD 1","THREAD 1");
//        theDemo.test("THREAD 2","THREAD 2");
//        theDemo.test("THREAD 3","THREAD 3");
    }

   /* public class SOP
    {
        public void print(String s)
        {
            System.out.println(s + "\n");
        }
    }

    public class TestThread extends Thread
    {
        String name;
        TheDemo theDemo;

        public TestThread(String name, TheDemo theDemo)
        {
            this.theDemo = theDemo;
            this.name = name;
        }

        @Override
        public void run()
        {
            theDemo.test(name, name);
        }
    }

    public class TheDemo
    {
        public synchronized void test(String thread, String name)
        {
            for (int i = 0; i < 10; i++)
            {
                Log.e(thread, name + " :: " + i);
                try
                {
                    Thread.sleep(500);
                }
                catch (Exception e)
                {
                    Log.e("TAG", e.getMessage());
                }
            }
        }
    }*/
}