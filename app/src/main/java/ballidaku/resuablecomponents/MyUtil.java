package ballidaku.resuablecomponents;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 4/28/17.
 */

public class MyUtil
{


    public static void getKeyHash(Context context)
    {


        try

        {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                      "ballidaku.resuablecomponents", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e)

        {

        } catch (NoSuchAlgorithmException e)
        {

        }
    }


    public String ConvertFacebookTimeToDate(String time)
    {
        // String dateString = "2013-05-30T19:30:00+0300";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = null;
        try
        {
            //dateFormat.setTimeZone( TimeZone.getTimeZone("GMT"));
            date = dateFormat.parse(time);

        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        //dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM 'at' HH:mm");
        return dateFormat1.format(date);
    }


    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size = 1024;
        try
        {
            byte[] bytes = new byte[buffer_size];
            for (; ; )
            {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex)
        {
        }
    }

    public static String streamToString(InputStream is) throws IOException
    {
        String str = "";

        if (is != null)
        {
            StringBuilder sb = new StringBuilder();
            String line;

            try
            {
                BufferedReader reader = new BufferedReader(
                          new InputStreamReader(is));

                while ((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }

                reader.close();
            } finally
            {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    public static String convertHashMapToString(HashMap<String, String> map)
    {
        JSONObject obj = null;
        try
        {
            obj = new JSONObject(map);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return obj.toString();
    }


}
