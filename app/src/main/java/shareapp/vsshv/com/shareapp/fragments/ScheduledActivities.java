package shareapp.vsshv.com.shareapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import shareapp.vsshv.com.shareapp.R;
import shareapp.vsshv.com.shareapp.adapters.TwitterAdapter;
import shareapp.vsshv.com.shareapp.database.DataBaseDao;
import shareapp.vsshv.com.shareapp.datasets.TwitterSet;

/**
 * Created by PC414506 on 31/08/16.
 */

public class ScheduledActivities extends Fragment {

    private TwitterAdapter mList = null;
    private RecyclerView recList = null;
    private List<TwitterSet> arrayList = null;
    private TextView noContent = null;

    public static ScheduledActivities newInstance() {
        ScheduledActivities f = new ScheduledActivities();

        return (f);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduled_activities, null);

        recList = (RecyclerView) view.findViewById(R.id.cardList);
        noContent = (TextView)view.findViewById(R.id.noContent);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        getActivities();

        return view;
    }

    private void getActivities(){
        DataBaseDao dao = new DataBaseDao(getActivity());
        arrayList = dao.getAllTMessages();
        Log.d("","=====getActivities=");
        if(arrayList != null && arrayList.size() > 0){
            mList = new TwitterAdapter(arrayList, getActivity());
            Log.d("","======"+arrayList.get(0).getMessage());
            Log.d("","======"+arrayList.get(0).getScheduled());
            recList.setAdapter(mList);
            noContent.setVisibility(View.GONE);
            recList.setVisibility(View.VISIBLE);
        }else{
            noContent.setVisibility(View.VISIBLE);
            recList.setVisibility(View.GONE);
        }

    }
}
