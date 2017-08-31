package ballidaku.resuablecomponents.locateOtherOnMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ballidaku.resuablecomponents.MyFirebase;
import ballidaku.resuablecomponents.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    String TAG=LoginActivity.class.getSimpleName();


    Context context;
    EditText editTextUserName;
    Spinner spinnerUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context=this;

        setUpViews();
    }

    private void setUpViews()
    {

        editTextUserName=(EditText) findViewById(R.id.editTextUserName);
        spinnerUserType=(Spinner) findViewById(R.id.spinnerUserType);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.buttonLogin:

                String userName=editTextUserName.getText().toString().trim();
                String userType=spinnerUserType.getSelectedItem().toString();

                Log.e(TAG,"userName "+userName+" userType "+userType);

                if(!userName.isEmpty())
                {
                    MyFirebase.getInstance().saveUser(userName,userType);
                }
                else
                {
                    Toast.makeText(context,"Please enter username",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
