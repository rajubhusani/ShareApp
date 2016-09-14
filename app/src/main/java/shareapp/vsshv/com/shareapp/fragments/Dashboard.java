package shareapp.vsshv.com.shareapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import shareapp.vsshv.com.shareapp.R;
import shareapp.vsshv.com.shareapp.TwitterOAuthAct;
import shareapp.vsshv.com.shareapp.gmail.GmailActivity;
import uber.UberActivity;

/**
 * Created by PC414506 on 31/08/16.
 */

public class Dashboard extends Fragment implements View.OnClickListener{

    ImageButton mTwitterBtn = null;
    ImageButton mUberBtn = null;
    ImageButton mGmailBtn = null;


    public static Dashboard newInstance() {
        Dashboard f = new Dashboard();

        return (f);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, null);


        mTwitterBtn = (ImageButton) view.findViewById(R.id.twitterBtn);
        mUberBtn = (ImageButton) view.findViewById(R.id.uberBtn);
        mGmailBtn = (ImageButton) view.findViewById(R.id.gmailBtn);

        mTwitterBtn.setOnClickListener(this);
        mUberBtn.setOnClickListener(this);
        mGmailBtn.setOnClickListener(this);

        return view;
    }



    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){
            case R.id.twitterBtn:
                Intent intent = new Intent(getActivity(), TwitterOAuthAct.class);
                startActivity(intent);
                break;
            case R.id.uberBtn:
                Intent uberIntent = new Intent(getActivity(), UberActivity.class);
                startActivity(uberIntent);
                break;
            case R.id.gmailBtn:
                Intent gmailIntent = new Intent(getActivity(), GmailActivity.class);
                startActivity(gmailIntent);
                break;

        }
    }
}
