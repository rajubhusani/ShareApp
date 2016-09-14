package shareapp.vsshv.com.shareapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
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
import shareapp.vsshv.com.shareapp.datasets.GmailSet;
import shareapp.vsshv.com.shareapp.utils.Utility;

/**
 * Created by PC414506 on 13/09/16.
 */

public class GmailAdapter extends RecyclerView.Adapter<GmailAdapter.ViewHolder> {
    private List<GmailSet> mDataset;
    Context context;
    SharedPreferences settings = null;
    String user = "";
    DataBaseDao dao = null;
    Context ctx;
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextView;
        private TextView mDate;
        private LinearLayout topbar;
        private ImageView image;
        private TextView userName;
        private TextView txtToName;
        private Button edit;
        private Button delete;
        private ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.txtName);
            mDate = (TextView) v.findViewById(R.id.txtDate);
            topbar = (LinearLayout) v.findViewById(R.id.topbar);
            image = (ImageView) v.findViewById(R.id.image);
            userName = (TextView) v.findViewById(R.id.userName);
            txtToName = (TextView) v.findViewById(R.id.txtToName);
            edit = (Button)v.findViewById(R.id.edit);
            delete = (Button)v.findViewById(R.id.delete);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GmailAdapter(List<GmailSet> myDataset, Context ctx) {
        mDataset = myDataset;
        settings = ctx.getSharedPreferences("ShareApp",Context.MODE_PRIVATE);
        user = settings.getString("accountName", "");
        dao = new DataBaseDao(ctx);
        this.ctx = ctx;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GmailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_gmail_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new GmailAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(GmailAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final GmailSet set = mDataset.get(position);
        //Log.d("","======"+set.getMessage());
        //Log.d("","======"+set.getScheduled());
        if(set.getStatus() == 1){
            holder.edit.setVisibility(View.GONE);
        }
        holder.userName.setText(user);
        holder.mTextView.setText(set.getBody());
        holder.mDate.setText(set.getScheduled());
        holder.txtToName.setText(set.getToMail());

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
                long row = dao.removeGMessage(set.get_id());
                if(row > 0){
                    mDataset.remove(position);
                    notifyDataSetChanged();
                    Snackbar.make(view, "Mail deleted successfully", Snackbar.LENGTH_SHORT).show();
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
