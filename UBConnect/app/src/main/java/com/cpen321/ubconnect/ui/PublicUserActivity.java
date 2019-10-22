package com.cpen321.ubconnect.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.viewModel.MainViewModel;
import com.cpen321.ubconnect.viewModel.PublicUserViewModel;

public class PublicUserActivity extends AppCompatActivity {

    private TextView username;
    private TextView info;
    private ImageView userImage;
    private Button rate;
    private Button report;
    private RatingBar stars;

    private PublicUserViewModel publicUserViewModel;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicuser);

        username = findViewById(R.id.publicUserName);
        info = findViewById(R.id.publicUserInfo);
        userImage = findViewById(R.id.publicUserImage);
        rate = findViewById(R.id.publicRate);
        report = findViewById(R.id.publicReport);
        stars = findViewById(R.id.publicStar);

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

    }

    protected void observeViewModelRate(String userId) {
        publicUserViewModel.userRate(userId).observe(this, this::onChangedRate);
    }

    public void onChangedRate(User user){

    }

    protected void observeViewModelReport(String userId) {
        publicUserViewModel.userRate(userId).observe(this, this::onChangedReport);
    }

    public void onChangedReport(User user){

    }

}
