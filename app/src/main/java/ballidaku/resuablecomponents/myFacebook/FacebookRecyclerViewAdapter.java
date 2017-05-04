package ballidaku.resuablecomponents.myFacebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import ballidaku.resuablecomponents.MyConstants;
import ballidaku.resuablecomponents.MySharedPreference;
import ballidaku.resuablecomponents.MyUtil;
import ballidaku.resuablecomponents.R;
import io.realm.Realm;

/**
 * Created by brst-pc93 on 4/29/17.
 */

public class FacebookRecyclerViewAdapter  extends RecyclerView.Adapter<FacebookRecyclerViewAdapter.MyViewHolder>
{

    Context context;
    Realm realm;
    ArrayList<HashMap<String,String>> mainArrayList;
    MyUtil myUtil =new MyUtil();

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewName;
        public TextView textCreatedTime;
        public TextView textMessage;
        public ImageView imageViewUser;
        public ImageView imageViewPost;
        //public WebView webView;

        public MyViewHolder(View view)
        {
            super(view);
            textViewName = (TextView) view.findViewById(R.id.textViewName);
            textCreatedTime = (TextView) view.findViewById(R.id.textCreatedTime);
            textMessage = (TextView) view.findViewById(R.id.textMessage);
            imageViewUser = (ImageView) view.findViewById(R.id.imageViewUser);
            imageViewPost = (ImageView) view.findViewById(R.id.imageViewPost);
            //webView = (WebView) view.findViewById(R.id.webView);

        }
    }


    public FacebookRecyclerViewAdapter(Context applicationContext,ArrayList<HashMap<String,String>> mainArrayList)
    {
        this.context=applicationContext;
        this.mainArrayList=mainArrayList;
    }

    @Override
    public FacebookRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.custom_facebook_item, parent, false);

        return new FacebookRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FacebookRecyclerViewAdapter.MyViewHolder holder, int position)
    {

        setImage(holder.imageViewUser, MySharedPreference.getInstance().getUserImage(context));
        holder.textViewName.setText(MySharedPreference.getInstance().getUserName(context));


        String story= mainArrayList.get(position).get(MyConstants.STORY);
        String name= mainArrayList.get(position).get(MyConstants.NAME);


        if((!story.isEmpty() || !name.isEmpty()) && !mainArrayList.get(position).get(MyConstants.TYPE).equals(MyConstants.LINK))
        {
            holder.textViewName.setText(story.isEmpty() ? name : story);
        }





        holder.textCreatedTime.setText(""+myUtil.ConvertFacebookTimeToDate(mainArrayList.get(position).get(MyConstants.CREATED_TIME)));
        holder.textMessage.setText(""+mainArrayList.get(position).get(MyConstants.MESSAGE));

//        if(mainArrayList.get(position).get(MyConstants.TYPE).equals(MyConstants.LINK))
//        {
//            holder.webView.loadUrl(mainArrayList.get(position).get(MyConstants.LINK));
//        }


        if(!mainArrayList.get(position).get(MyConstants.PICTURE).isEmpty() && (mainArrayList.get(position).get(MyConstants.TYPE).equals(MyConstants.LINK)
                || mainArrayList.get(position).get(MyConstants.TYPE).equals(MyConstants.PHOTO)
                || mainArrayList.get(position).get(MyConstants.TYPE).equals(MyConstants.VIDEO)))
        {

            holder.imageViewPost.setVisibility(View.VISIBLE);

            setImage(holder.imageViewPost,mainArrayList.get(position).get(MyConstants.FULL_PICTURE));
        }
        else
        {
            holder.imageViewPost.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount()
    {
        return mainArrayList.size();
    }



    public void refresh(ArrayList<HashMap<String, String>> mainArrayList)
    {
        this.mainArrayList=mainArrayList;

        notifyDataSetChanged();

    }


    public void setImage(ImageView imageView,String url)
    {
        Picasso.with(context)
               .load(url)
               // .resize(50, 50)
               //.centerCrop()
               .into(imageView);
    }


}