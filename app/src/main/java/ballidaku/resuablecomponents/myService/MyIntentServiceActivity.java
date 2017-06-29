package ballidaku.resuablecomponents.myService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ballidaku.resuablecomponents.R;

public class MyIntentServiceActivity extends AppCompatActivity
{

    Context context;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_intent_service);
        context = this;

        textView = (TextView) findViewById(R.id.status);

    }




    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(MyIntentService.NOTIFICATION));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                String string = bundle.getString(MyIntentService.FILEPATH);
                int resultCode = bundle.getInt(MyIntentService.RESULT);
                if (resultCode == RESULT_OK)
                {
                    Toast.makeText(context,
                              "Download complete. Download URI: " + string,
                              Toast.LENGTH_LONG).show();
                    textView.setText("Download done");
                }
                else
                {
                    Toast.makeText(context, "Download failed",
                              Toast.LENGTH_LONG).show();
                    textView.setText("Download failed");
                }
            }
        }
    };


    public void onClick(View view)
    {

        Intent intent = new Intent(this, MyIntentService.class);
        // add infos for the service which file to download and where to store
//        intent.putExtra(MyIntentService.FILENAME, "index.html");
//        intent.putExtra(MyIntentService.URL,"http://www.vogella.com/index.html");
        intent.putExtra(MyIntentService.FILENAME, "jatin.zip");
        intent.putExtra(MyIntentService.URL,"\n" +
                  "http://content.oddcast.com/ccs2/voki/mobile/android-data-v2.zip");
                 // "http://content.oddcast.com/ccs2/voki/mobile/android-data-v1.zip");
        startService(intent);
        textView.setText("Service started");
    }
}
