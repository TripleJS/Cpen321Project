package com.cpen321.ubconnect.ui.publicaccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.User;

public class PublicUserFragment extends Fragment {

    private PublicUserViewModel publicUserViewModel;
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.content_publicuser, container, false);


        Button rate = root.findViewById(R.id.publicRate);
        Button report = root.findViewById(R.id.publicReport);

        token = ((GlobalVariables) getActivity().getApplication()).getJwt();

        publicUserViewModel = ViewModelProviders.of(this).get(PublicUserViewModel.class);

        View.OnClickListener rateOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //observeViewModelRate();
            }
        };

        View.OnClickListener reportOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // observeViewModelReport();
            }
        };

        rate.setOnClickListener(rateOnClickListener);
        report.setOnClickListener(reportOnClickListener);

        return root;
    }

    protected void observeViewModelRate(String userId) {
        publicUserViewModel.userRate(userId, token).observe(this, this::onChangedRate);
    }

    public void onChangedRate(User user){
        // to do
    }

    protected void observeViewModelReport(String userId) {
        publicUserViewModel.userRate(userId, token).observe(this, this::onChangedReport);
    }

    public void onChangedReport(User user){
        // to do
    }
}
