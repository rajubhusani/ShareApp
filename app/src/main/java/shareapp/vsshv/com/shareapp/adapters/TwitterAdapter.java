package shareapp.vsshv.com.shareapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import shareapp.vsshv.com.shareapp.R;
import shareapp.vsshv.com.shareapp.datasets.TwitterSet;

/**
 * Created by PC414506 on 31/08/16.
 */

public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.ViewHolder> {
    private List<TwitterSet> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextView;
        private TextView mDate;
        private ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.txtName);
            mDate = (TextView) v.findViewById(R.id.txtDate);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TwitterAdapter(List<TwitterSet> myDataset, Context ctx) {
        mDataset = myDataset;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TwitterSet set = mDataset.get(position);
        Log.d("","======"+set.getMessage());
        Log.d("","======"+set.getScheduled());
        holder.mTextView.setText(set.getMessage());
        holder.mDate.setText(set.getScheduled());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
