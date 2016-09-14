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
import shareapp.vsshv.com.shareapp.datasets.UberDataSet;
import shareapp.vsshv.com.shareapp.utils.Utility;

/**
 * Created by PC414506 on 12/09/16.
 */

public class UberAdapter extends RecyclerView.Adapter<UberAdapter.ViewHolder> {
    private List<UberDataSet> mDataset;
    String user = "";
    DataBaseDao dao = null;
    Context ctx;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextPView;
        private TextView mTextDView;
        private TextView mDate;
        private Button edit;
        private Button delete;
        private LinearLayout topbar;
        private ImageView image;
        private TextView userName;
        private ViewHolder(View v) {
            super(v);
            mTextPView = (TextView) v.findViewById(R.id.pName);
            mTextDView = (TextView) v.findViewById(R.id.dName);
            edit = (Button)v.findViewById(R.id.edit);
            delete = (Button)v.findViewById(R.id.delete);
            mDate = (TextView) v.findViewById(R.id.txtDate);
            topbar = (LinearLayout) v.findViewById(R.id.topbar);
            image = (ImageView) v.findViewById(R.id.image);
            userName = (TextView) v.findViewById(R.id.userName);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UberAdapter(List<UberDataSet> myDataset, Context ctx) {
        mDataset = myDataset;
        SharedPreferences settings = ctx.getSharedPreferences("ShareApp",Context.MODE_PRIVATE);
        user = settings.getString("ub_account", "");
        dao = new DataBaseDao(ctx);
        this.ctx = ctx;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.uber_card_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new UberAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(UberAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final UberDataSet set = mDataset.get(position);
       // Log.d("","======"+set.getMessage());
        Log.d("","======"+set.getScheduled());
        holder.userName.setText(user);
        holder.mTextDView.setText(set.getDropOffNick());
        holder.mTextPView.setText(set.getPickUpNick());
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
                long row = dao.removeUMessage(set.get_id());
                if(row > 0){
                    Snackbar.make(view, "Uber Ride deleted successfully", Snackbar.LENGTH_SHORT).show();
                    mDataset.remove(position);
                    notifyDataSetChanged();
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
