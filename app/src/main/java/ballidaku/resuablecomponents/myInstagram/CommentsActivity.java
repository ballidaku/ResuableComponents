package ballidaku.resuablecomponents.myInstagram;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ballidaku.resuablecomponents.MyConstants;
import ballidaku.resuablecomponents.R;
import ballidaku.resuablecomponents.myInstagram.util.InstagramSession;

/**
 * Created by brst-pc93 on 5/4/17.
 */

public class CommentsActivity extends AppCompatActivity
{

    Context context;


    ListView listViewChat;
    EditText editTextComment;


    private int WHAT_FINALIZE = 0;
    private static int WHAT_ERROR = 1;
    private ProgressDialog pd;


    ArrayList<HashMap<String, String>> mainList = new ArrayList<>();

    String mediaID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        context = this;

        setUpIds();

        mediaID = getIntent().getStringExtra(MyConstants.MEDIA_ID);

        getAllComments();

        Log.e("MediaId", mediaID);


    }

    private void setUpIds()
    {
        listViewChat = (ListView) findViewById(R.id.listViewChat);
        editTextComment = (EditText) findViewById(R.id.editTextComment);
    }


    public void refresh()
    {

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                listViewChat.setAdapter(new Adapter());
            }
        });
    }

    public void send(View v)
    {

        final String comment = editTextComment.getText().toString().trim();


        if (!comment.isEmpty())
        {
           /* new Thread(new Runnable()
            {

                @Override
                public void run()
                {*/
            sendComments(comment);
            editTextComment.getText().clear();
                /*}
            }).start();*/
        }
    }

    public void back(View v)
    {

        finish();
    }


    class Adapter extends BaseAdapter
    {
        LayoutInflater inflater;

        Adapter()
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return mainList.size();
        }

        @Override
        public Object getItem(int i)
        {
            return null;
        }

        @Override
        public long getItemId(int i)
        {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup)
        {
            view = inflater.inflate(R.layout.custom_comment, null);


            Holder holder = new Holder();

            holder.imageViewUser = (ImageView) view.findViewById(R.id.imageViewUser);
            holder.textViewComment = (TextView) view.findViewById(R.id.textViewComment);
            holder.textViewName = (TextView) view.findViewById(R.id.textViewName);


            setImage(holder.imageViewUser, mainList.get(position).get(MyConstants.PROFILE_PICTURE));
            holder.textViewComment.setText(mainList.get(position).get(MyConstants.TEXT));
            holder.textViewName.setText(mainList.get(position).get(MyConstants.FULL_NAME));

            return view;
        }
    }


    public void setImage(ImageView imageView, String url)
    {
        Picasso.with(context)
               .load(url)
               // .resize(50, 50)
               //.centerCrop()
               .into(imageView);
    }


    public class Holder
    {
        ImageView imageViewUser;
        TextView textViewComment;
        TextView textViewName;
    }


    public void getAllComments()
    {
        mainList.clear();
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                pd = ProgressDialog.show(context, "", "Loading images...");
      /*  new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                int what = WHAT_FINALIZE;*/
                Response response = null;

                /*try
                {*/
                OkHttpClient client = new OkHttpClient();

                RequestBody reqbody = RequestBody.create(null, new byte[0]);
                Request request = null;


                request = new Request.Builder()
                          .url("https://api.instagram.com/v1/media/" + mediaID + "/comments?access_token=" + new InstagramSession(context).getAccessToken())
                          .get()
                          .addHeader("cache-control", "no-cache")
                          .build();

                //  response = client.newCall(request).execute();


                client.newCall(request).enqueue(new Callback()
                {
                    @Override
                    public void onFailure(Request request, IOException e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException
                    {
                        if (!response.isSuccessful())
                        {
                            throw new IOException("Unexpected code " + response);
                        }
                        else
                        {

                            if (pd != null && pd.isShowing())
                                pd.dismiss();


                            String res = response.body().string();


                            try
                            {
                                JSONObject jsonObject = new JSONObject(res);
                                JSONArray data = jsonObject.getJSONArray(MyConstants.DATA);

                                for (int data_i = 0; data_i < data.length(); data_i++)
                                {
                                    JSONObject data_obj = data.getJSONObject(data_i);

                                    String picture = data_obj.getJSONObject(MyConstants.FROM).getString(MyConstants.PROFILE_PICTURE);
                                    String name = data_obj.getJSONObject(MyConstants.FROM).getString(MyConstants.FULL_NAME);
                                    String text = data_obj.getString(MyConstants.TEXT);


                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(MyConstants.PROFILE_PICTURE, picture);
                                    map.put(MyConstants.FULL_NAME, name);
                                    map.put(MyConstants.TEXT, text);

                                    mainList.add(map);
                                }

                                refresh();
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                });


            }
        });






           /*     } catch (Exception exception)
                {
                    exception.printStackTrace();
                    what = WHAT_ERROR;
                }

                // pd.dismiss();
                handler.sendEmptyMessage(what);

            }*/
//        }).start();
    }


    public void sendComments(String text)
    {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        HashMap<String, String> map = new HashMap<>();
        map.put(MyConstants.TEXT, text);
        map.put(MyConstants.ACCESS_TOKEN, new InstagramSession(context).getAccessToken());

        String b = "access_token=" + new InstagramSession(context).getAccessToken() + "&text=" + text;

        RequestBody body = RequestBody.create(mediaType, b);

        Request request = new Request.Builder()
                  .url("https://api.instagram.com/v1/media/" + mediaID + "/comments")
                  .post(body)
                  .addHeader("content-type", "application/x-www-form-urlencoded")
                  .addHeader("cache-control", "no-cache")
                  .build();


        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Request request, IOException e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException
            {
                if (!response.isSuccessful())
                {
                    throw new IOException("Unexpected code " + response);
                }
                else
                {
                    getAllComments();

                    // Log.e("ResponseBALLI", response.body().string());
                }
            }


        });
    }

}
