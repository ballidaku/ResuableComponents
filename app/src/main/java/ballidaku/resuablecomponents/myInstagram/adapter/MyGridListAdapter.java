package ballidaku.resuablecomponents.myInstagram.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ballidaku.resuablecomponents.MyConstants;
import ballidaku.resuablecomponents.R;
import ballidaku.resuablecomponents.myInstagram.AllMediaFiles;
import ballidaku.resuablecomponents.myInstagram.CommentsActivity;
import ballidaku.resuablecomponents.myInstagram.util.InstagramSession;

/**
 * Created by ashok.kumar on 04/02/16.
 */
public class MyGridListAdapter extends BaseAdapter
{
     private Context context;
    private ArrayList<HashMap<String, String>> mainList;
    private LayoutInflater inflater;
    //private ImageLoader imageLoader;

    public MyGridListAdapter(Context context, ArrayList<HashMap<String, String>> mainList) {

        this.context=context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mainList = mainList;
        //this.imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return mainList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.media_list_inflater, null);
        Holder holder = new Holder();
        holder.ivPhoto = (ImageView) view.findViewById(R.id.ivImage);
        holder.imageViewLike = (ImageView) view.findViewById(R.id.imageViewLike);
        holder.imageComment = (ImageView) view.findViewById(R.id.imageComment);
        holder.textViewLike = (TextView) view.findViewById(R.id.textViewLike);
        holder.textViewComment = (TextView) view.findViewById(R.id.textViewComment);
       // imageLoader.DisplayImage(imageThumbList.get(position), holder.ivPhoto);

        holder.textViewLike.setText("L : "+mainList.get(position).get(MyConstants.LIKE_COUNT));
        holder.textViewComment.setText("  C : "+mainList.get(position).get(MyConstants.COMMENT_COUNT));

        setImage(holder.ivPhoto,mainList.get(position).get(MyConstants.IMAGE_URL));


        holder.imageViewLike.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String likeOrDelete=mainList.get(position).get(MyConstants.USER_HAS_LIKED).equals("true") ? MyConstants.DELETE : MyConstants.LIKE;

                addLike(mainList.get(position).get(MyConstants.MEDIA_ID),likeOrDelete);
            }
        });

        holder.imageComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
              Intent intent= new Intent(context,CommentsActivity.class);
                intent.putExtra(MyConstants.MEDIA_ID,mainList.get(position).get(MyConstants.MEDIA_ID));
                context.startActivity(intent);
            }
        });

        return view;
    }

    private class Holder {
        private ImageView ivPhoto;
        private ImageView imageComment;
        private ImageView imageViewLike;
        private TextView textViewLike;
        private TextView textViewComment;
    }

    public void setImage(ImageView imageView,String url)
    {
        Picasso.with(context)
               .load(url)
               // .resize(50, 50)
               //.centerCrop()
               .into(imageView);
    }


    private int WHAT_FINALIZE = 0;
    private static int WHAT_ERROR = 1;

    private Handler handler = new Handler(new Handler.Callback()
    {

        @Override
        public boolean handleMessage(Message msg)
        {
            if (pd != null && pd.isShowing())
                pd.dismiss();
            if (msg.what == WHAT_FINALIZE)
            {
                ((AllMediaFiles)context).getAllMediaImages();
                //setImageGridAdapter();
                Toast.makeText(context, "Done",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "Check your network.",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    ProgressDialog pd;

    private void addLike(final String mediaId ,final String likeOrDelete )
    {
        Log.e("MediaId", mediaId);
        pd = ProgressDialog.show(context, "", "Loading images...");
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                int what = WHAT_FINALIZE;
                try
                {
                    OkHttpClient client = new OkHttpClient();

                    RequestBody reqbody = RequestBody.create(null, new byte[0]);
                    Request request = null;

                    if(likeOrDelete.equals(MyConstants.LIKE))
                    {

                        request = new Request.Builder()
                                  .url("https://api.instagram.com/v1/media/" + mediaId + "/likes?access_token=" + new InstagramSession(context).getAccessToken())
                                  .post(reqbody)
                                  .addHeader("cache-control", "no-cache")
                                  .build();

                    }
                    else if(likeOrDelete.equals(MyConstants.DELETE))
                    {

                        request = new Request.Builder()
                                  .url("https://api.instagram.com/v1/media/" + mediaId + "/likes?access_token=" + new InstagramSession(context).getAccessToken())
                                  .delete(reqbody)
                                  .addHeader("cache-control", "no-cache")
                                  .build();

                    }

                    try
                    {
                         Response response = client.newCall(request).execute();

                           Log.e("Response",""+response.body().string());
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                } catch (Exception exception)
                {
                    exception.printStackTrace();
                    what = WHAT_ERROR;
                }
                // pd.dismiss();
                handler.sendEmptyMessage(what);
            }
        }).start();
    }






}
