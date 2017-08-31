package ballidaku.resuablecomponents;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


/**
 * Created by brst-pc93 on 8/3/17.
 */

public class MyFirebase
{
    public static MyFirebase instance = new MyFirebase();

    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();


    public static MyFirebase getInstance()
    {
        return instance;
    }

    public void saveUser(String username,String userType)
    {

        HashMap<String, Object> result = new HashMap<>();
        result.put(MyConstants.USER_NAME, username);
        result.put(MyConstants.USER_TYPE,userType );

        root.child(MyConstants.USERS).push().updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

            }
        });
    }


}
