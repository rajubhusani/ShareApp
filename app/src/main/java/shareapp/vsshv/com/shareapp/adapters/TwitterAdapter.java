package shareapp.vsshv.com.shareapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import shareapp.vsshv.com.shareapp.R;
import shareapp.vsshv.com.shareapp.database.DataBaseDao;
import shareapp.vsshv.com.shareapp.datasets.TwitterSet;
import shareapp.vsshv.com.shareapp.utils.Utility;

/**
 * Created by PC414506 on 31/08/16.
 */

public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.ViewHolder> {
    private List<TwitterSet> mDataset;

    private String user = "";
    DataBaseDao dao = null;
    Context ctx;

    class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextView;
        private TextView mDate;
        private LinearLayout topbar;
        private ImageView image;
        private TextView userName;
        private Button edit;
        private Button delete;
        private ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.txtName);
            mDate = (TextView) v.findViewById(R.id.txtDate);
            topbar = (LinearLayout) v.findViewById(R.id.topbar);
            image = (ImageView) v.findViewById(R.id.image);
            userName = (TextView) v.findViewById(R.id.userName);
            edit = (Button)v.findViewById(R.id.edit);
            delete = (Button)v.findViewById(R.id.delete);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TwitterAdapter(List<TwitterSet> myDataset, Context ctx) {
        mDataset = myDataset;
        SharedPreferences settings = ctx.getSharedPreferences("ShareApp",Context.MODE_PRIVATE);
        user = settings.getString("twitter_user_name", "");
        dao = new DataBaseDao(ctx);
        this.ctx = ctx;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TwitterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new TwitterAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final TwitterSet set = mDataset.get(position);
        if(set.getStatus() == 1){
            holder.edit.setVisibility(View.GONE);
        }
        Log.d("","======"+set.getMessage());
        Log.d("","======"+set.getScheduled());
        holder.userName.setText(user);
        holder.mTextView.setText(set.getMessage());
        holder.mDate.setText(set.getScheduled());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Edit Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(set.getStatus() == 0){
                    Utility.getInstance(ctx).cancelScheduledAlarm(ctx, set.get_id());
                }
                long row = dao.removeTMessage(set.get_id());
                if(row > 0){
                    mDataset.remove(position);
                    notifyDataSetChanged();
                    Snackbar.make(view, "Message deleted successfully", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
