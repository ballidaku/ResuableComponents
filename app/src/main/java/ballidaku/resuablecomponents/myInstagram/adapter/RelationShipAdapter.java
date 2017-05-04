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

import ballidaku.resuablecomponents.R;
import ballidaku.resuablecomponents.myInstagram.Relationship;

/**
 * Created by ashok.kumar on 04/02/16.
 */
public class RelationShipAdapter extends BaseAdapter
{

    private ArrayList<HashMap<String, String>> usersInfo;
    private LayoutInflater inflater;
//    private ImageLoader imageLoader;
    Context context;

    public RelationShipAdapter(Context context,
                               ArrayList<HashMap<String, String>> usersInfo) {
        this.context=context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.usersInfo = usersInfo;
//        this.imageLoader = new ImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.relationship_inflater, null);
        Holder holder = new Holder();
        holder.ivPhoto = (ImageView) view.findViewById(R.id.ivImage);
        holder.tvFullName = (TextView) view.findViewById(R.id.tvFullName);
        holder.tvFullName.setText(usersInfo.get(position).get(
                Relationship.TAG_USERNAME));
       /* imageLoader.DisplayImage(
                usersInfo.get(position).get(Relationship.TAG_PROFILE_PICTURE),
                holder.ivPhoto);*/

        setImage(holder.ivPhoto,usersInfo.get(position).get(Relationship.TAG_PROFILE_PICTURE));


        return view;
    }

    private class Holder {
        private ImageView ivPhoto;
        private TextView tvFullName;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return usersInfo.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
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
