package shareapp.vsshv.com.shareapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import shareapp.vsshv.com.shareapp.R;
import shareapp.vsshv.com.shareapp.adapters.GmailAdapter;
import shareapp.vsshv.com.shareapp.database.DataBaseDao;
import shareapp.vsshv.com.shareapp.datasets.GmailSet;

/**
 * Created by PC414506 on 13/09/16.
 */

public class GmailFragment extends Fragment {

    private RecyclerView recList = null;
    private TextView noContent = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_inner_layout, null);


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
        List<GmailSet> arrayList = dao.getAllGMessages();
        Log.d("","=====getActivities=");
        if(arrayList != null && arrayList.size() > 0){
            GmailAdapter mList = new GmailAdapter(arrayList, getActivity());
            // Log.d("","======"+arrayList.get(0).getMessage());
            // Log.d("","======"+arrayList.get(0).getScheduled());
            recList.setAdapter(mList);
            noContent.setVisibility(View.GONE);
            recList.setVisibility(View.VISIBLE);
        }else{
            noContent.setVisibility(View.VISIBLE);
            recList.setVisibility(View.GONE);
        }

    }
}
