package ballidaku.resuablecomponents.myInstagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import ballidaku.resuablecomponents.MyConstants;
import ballidaku.resuablecomponents.R;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.media_list_inflater, null);
        Holder holder = new Holder();
        holder.ivPhoto = (ImageView) view.findViewById(R.id.ivImage);
        holder.textViewLike = (TextView) view.findViewById(R.id.textViewLike);
        holder.textViewComment = (TextView) view.findViewById(R.id.textViewComment);
       // imageLoader.DisplayImage(imageThumbList.get(position), holder.ivPhoto);

        holder.textViewLike.setText("L : "+mainList.get(position).get(MyConstants.LIKE_COUNT));
        holder.textViewComment.setText("  C : "+mainList.get(position).get(MyConstants.COMMENT_COUNT));

        setImage(holder.ivPhoto,mainList.get(position).get(MyConstants.IMAGE_URL));


        return view;
    }

    private class Holder {
        private ImageView ivPhoto;
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


}
