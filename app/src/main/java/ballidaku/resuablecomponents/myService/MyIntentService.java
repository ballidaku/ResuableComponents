package ballidaku.resuablecomponents.myService;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by brst-pc93 on 6/8/17.
 */

public class MyIntentService extends IntentService
{

    /*Methods called
    * Constructor
    * onCreate
    * onStartCommand
    * onStart
    * onHandleIntent
    * onDestroy*/

    String TAG ="MyIntentService";
    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.vogella.android.service.receiver";

    public MyIntentService()
    {
        super("MyIntentService");

        Log.e(TAG,"Constructor");
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e(TAG,"onCreate");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId)
    {
        super.onStart(intent, startId);
        Log.e(TAG,"onStart");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId)
    {
        Log.e(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG,"onDestroy");
        super.onDestroy();
    }






    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG,"onHandleIntent");

        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        File output = new File(Environment.getExternalStorageDirectory(),fileName);
        //File output = new File(getFilesDir(),fileName);


        Log.e("TAG 1  ",""+output.getPath());

//        File direct = new File(getFilesDir()+"/"+fileName);
        //direct.mkdir();

        if (output.exists()) {
            output.delete();
        }


        InputStream stream = null;
        FileOutputStream fos = null;
        try {

            URL url = new URL(urlPath);
            stream = url.openConnection().getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            fos = new FileOutputStream(output.getPath());
            int next = -1;
            while ((next = reader.read()) != -1) {
                fos.write(next);
            }
            // successfully finished
            result = Activity.RESULT_OK;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        publishResults(output.getAbsolutePath(), result);
    }

    private void publishResults(String outputPath, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}
