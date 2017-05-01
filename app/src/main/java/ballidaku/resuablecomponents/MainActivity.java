package ballidaku.resuablecomponents;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{


    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        gpsTracker = new GPSTracker( MainActivity.this);
//        gpsTracker.getLocation();




        // checkCamera();

        checkAudio();

    }

    private void checkCamera()
    {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            manager.registerAvailabilityCallback(new CameraManager.AvailabilityCallback()
            {
                @Override
                public void onCameraAvailable(String cameraId)
                {
                    super.onCameraAvailable(cameraId);
                    //Do your work

                    Toast.makeText(getApplicationContext(), "Camera Available", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCameraUnavailable(String cameraId)
                {
                    super.onCameraUnavailable(cameraId);
                    //Do your work
                    Toast.makeText(getApplicationContext(), "Camera UnAvailable", Toast.LENGTH_SHORT).show();
                }
            }, null);
        }
    }

    private boolean validateMicAvailability()
    {
        Boolean available = true;
        AudioRecord recorder =
                new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_DEFAULT, 44100);

        try
        {
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED)
            {
                available = false;

            }

            recorder.startRecording();
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING)
            {
                recorder.stop();
                available = false;

            }
            recorder.stop();
        } finally
        {
            recorder.release();
            recorder = null;
        }

        return available;
    }


    public void checkAudio()
    {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

// Request audio focus for playback
        int result = am.requestAudioFocus(focusChangeListener,
// Use the music stream.
                AudioManager.STREAM_MUSIC,
// Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        Log.e("checkAudio Result" ,""+result);


        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
        {
// other app had stopped playing song now , so you can do your stuffs now .
        }
    }

    private AudioManager.OnAudioFocusChangeListener focusChangeListener =
            new AudioManager.OnAudioFocusChangeListener()
            {
                public void onAudioFocusChange(int focusChange)
                {
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    switch (focusChange)
                    {

                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                            // Lower the volume while ducking.
                            // mediaPlayer.setVolume(0.2f, 0.2f);
                            Log.e("checkAudio " ,"1");

                            break;
                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                            Log.e("checkAudio " ,"2");
                            Toast.makeText(getApplicationContext(), "Other App using Mic", Toast.LENGTH_SHORT).show();
                            // pause();
                            break;

                        case (AudioManager.AUDIOFOCUS_LOSS):
                            Log.e("checkAudio " ,"3");
                            //stop();
//                            ComponentName component =new ComponentName(AudioPlayerActivity.this,MediaControlReceiver.class);
//                            am.unregisterMediaButtonEventReceiver(component);
                            break;

                        case (AudioManager.AUDIOFOCUS_GAIN):
                            Log.e("checkAudio " ,"4");
                            // Return the volume to normal and resume if paused.
//                            mediaPlayer.setVolume(1f, 1f);
//                            mediaPlayer.start();
                            break;
                        default:
                            break;
                    }
                }
            };






/*
    public void toRetrieveData()
    {
        // Build the query looking at all users:
        RealmQuery<User> query = realm.where(User.class);

// Add query conditions:
        query.equalTo("name", "John");
        query.or().equalTo("name", "Peter");

// Execute the query:
        RealmResults<User> result1 = query.findAll();

// Or alternatively do the same all at once (the "Fluent interface"):
        RealmResults<User> result2 = realm.where(User.class)
                                          .equalTo("name", "John")
                                          .or()
                                          .equalTo("name", "Peter")
                                          .findAll();
    }*/


}
