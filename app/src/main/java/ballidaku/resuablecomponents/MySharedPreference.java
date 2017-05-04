package ballidaku.resuablecomponents;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by brst-pc93 on 5/1/17.
 */

public class MySharedPreference
{

    public final String PreferenceName = "MyPreference";

    public MySharedPreference()
    {
    }

    public static MySharedPreference instance = null;

    public static MySharedPreference getInstance()
    {
        if (instance == null)
        {
            instance = new MySharedPreference();
        }
        return instance;
    }

    public SharedPreferences getPreference(Context context)
    {
        return context.getSharedPreferences(PreferenceName, Activity.MODE_PRIVATE);
    }

    public void saveUserName(Context context, String userName)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstants.NAME, userName);
        editor.apply();
    }

    public String getUserName(Context context)
    {
        return getPreference(context).getString(MyConstants.NAME, "");
    }


    public void saveUserImage(Context context, String userImage)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstants.PICTURE, userImage);
        editor.apply();
    }

    public String getUserImage(Context context)
    {
        return getPreference(context).getString(MyConstants.PICTURE, "");
    }



    //TWITTER

    public void saveTwitterUserName(Context context, String userName)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstants.Twitter_User_NAME, userName);
        editor.apply();
    }

    public String getTwitterUserName(Context context)
    {
        return getPreference(context).getString(MyConstants.Twitter_User_NAME, "");
    }


    public void saveTwitterUserID(Context context, long userID)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putLong(MyConstants.Twitter_User_ID, userID);
        editor.apply();
    }

    public long getTwitterUserID(Context context)
    {
        return getPreference(context).getLong(MyConstants.Twitter_User_ID, 0);
    }


}
