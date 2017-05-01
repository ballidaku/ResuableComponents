package ballidaku.resuablecomponents.myRealm;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ballidaku.resuablecomponents.R;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmActivity extends AppCompatActivity implements View.OnClickListener
{
    String TAG = RealmActivity.class.getSimpleName();

    Realm realm;
    EditText editText;
    Button buttonSave;

    RecyclerView recyclerView;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);


        context=this;

        editText = (EditText) findViewById(R.id.editText);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);


        buttonSave.setOnClickListener(this);

        realm = Realm.getDefaultInstance();


        setUpRecyclerView();


        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        Toast.makeText(getApplicationContext(), "Touched", Toast.LENGTH_SHORT).show();


                    }
                })
        );*/



        /*Alternative*/

        //First create simple object
        //User realmUser = realm.copyToRealm(user);
        //realm.commitTransaction();


        /*realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                User user = realm.createObject(User.class);
                user.setName("John");
                user.setAge(55);
            }

        });


        realm.executeTransactionAsync(new Realm.Transaction()
        {
            @Override
            public void execute(Realm bgRealm)
            {
                User user = bgRealm.createObject(User.class);
                user.setName("John");
            }
        }, new Realm.Transaction.OnSuccess()
        {
            @Override
            public void onSuccess()
            {
                // Transaction was a success.
            }
        }, new Realm.Transaction.OnError()
        {
            @Override
            public void onError(Throwable error)
            {
                // Transaction failed and was automatically canceled.
            }
        });


        RealmAsyncTask transaction = realm.executeTransactionAsync(new Realm.Transaction()
        {
            @Override
            public void execute(Realm bgRealm)
            {
                User user = bgRealm.createObject(User.class);
                user.setName("John");
            }
        }, null, null);


        if (transaction != null && !transaction.isCancelled())
        {
            transaction.cancel();
        }*/


    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.buttonSave:
                addData();
                break;
        }
    }


    public void addData()
    {
        final String value = editText.getText().toString().trim();

        if (!value.isEmpty())
        {
          //  realm.beginTransaction();

           /* User user = realm.createObject(User.class);
            user.setName(value);
            user.setId(System.currentTimeMillis());

            realm.commitTransaction();*/


            realm.executeTransaction(new Realm.Transaction()
            { // must be in transaction for this to work
                @Override
                public void execute(Realm realm)
                {
                    // increment index
                    Number currentIdNum = realm.where(User.class).max("id");
                    int nextId;
                    if (currentIdNum == null)
                    {
                        nextId = 1;
                    }
                    else
                    {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    User user = new User(); // unmanaged
                    user.setId(nextId);
                    user.setName(value);

                    realm.insertOrUpdate(user); // using insert API
                }
            });


            getAllData();

            //setUpRecyclerView();

            adapter.refresh();

            editText.getText().clear();
        }
    }


    public void getAllData()
    {
        RealmResults<User> result = realm.where(User.class).findAll().sort("id", Sort.DESCENDING);

        for (int i = 0; i < result.size(); i++)
        {
            Log.e(TAG, "User  " + result.get(i).getName());
        }
    }

    MyRecyclerViewAdapter adapter;

    private void setUpRecyclerView()
    {
        adapter = new MyRecyclerViewAdapter(context, realm, realm.where(User.class).findAll().sort("id", Sort.DESCENDING));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        /*
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerView);*/

    }


}
