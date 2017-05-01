package ballidaku.resuablecomponents.myRealm;

/**
 * Created by brst-pc93 on 4/26/17.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ballidaku.resuablecomponents.R;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>
{

    Context context;
    Realm realm;
    private RealmResults<User> userlist;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewName;
        public TextView textViewDelete;
        public TextView textViewUpdate;

        public MyViewHolder(View view)
        {
            super(view);
            textViewName = (TextView) view.findViewById(R.id.textViewName);
            textViewDelete = (TextView) view.findViewById(R.id.textViewDelete);
            textViewUpdate = (TextView) view.findViewById(R.id.textViewUpdate);

            textViewDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    deleteItem(getAdapterPosition());
                }
            });


            textViewUpdate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    showShareWinCheckInDialog(getAdapterPosition());
                }
            });
        }
    }


    public MyRecyclerViewAdapter(Context applicationContext, Realm realm, RealmResults<User> userlist)
    {
        this.context=applicationContext;
        this.realm=realm;
        this.userlist = userlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.custom_user_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        User user = userlist.get(position);
        holder.textViewName.setText(user.getName());


    }

    @Override
    public int getItemCount()
    {
        return userlist.size();
    }



    public void refresh()
    {
        userlist =realm.where(User.class).findAll().sort("id", Sort.DESCENDING);

        notifyDataSetChanged();

    }


    public void deleteItem(final int position)
    {
        final RealmResults<User> results = realm.where(User.class).findAll().sort("id", Sort.DESCENDING);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // remove single match
               // results.deleteFirstFromRealm();
               // results.deleteLastFromRealm();

                // remove a single object
                //User user = results.get(position);
                results.deleteFromRealm(position);

                notifyItemRemoved(position);


                // Delete all matches
               // results.deleteAllFromRealm();
            }
        });
    }


    public void updateItem(int position,final String value)
    {
       final int id=userlist.get(position).getId();

        realm.executeTransaction(new Realm.Transaction()
        { // must be in transaction for this to work
            @Override
            public void execute(Realm realm)
            {

                User user = new User(); // unmanaged
                user.setId(id);
                user.setName(value);

                realm.insertOrUpdate(user); // using insert API

                refresh();
            }
        });
    }

    public void showShareWinCheckInDialog(final int position)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView txtvCancel = (TextView) dialog.findViewById(R.id.txtvCancel);
        TextView txtvUpdate = (TextView) dialog.findViewById(R.id.txtvUpdate);

        final EditText edtv_name = (EditText) dialog.findViewById(R.id.edtv_name);

        edtv_name.setText(userlist.get(position).getName());


        txtvCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });


        txtvUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               String value= edtv_name.getText().toString().trim();


                if(!value.isEmpty())
                {

                    updateItem(position, value);

                    dialog.dismiss();
                }
            }
        });

    }
}