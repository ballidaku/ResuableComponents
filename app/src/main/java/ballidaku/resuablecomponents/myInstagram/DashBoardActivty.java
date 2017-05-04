package ballidaku.resuablecomponents.myInstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ballidaku.resuablecomponents.R;


public class DashBoardActivty extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_activty);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new GraphFragment()).commit();
        }
    }

}
