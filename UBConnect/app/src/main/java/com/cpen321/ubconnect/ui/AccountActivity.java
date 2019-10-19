package com.cpen321.ubconnect.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.viewModel.AccountViewModel;

import java.util.Observable;
import java.util.Observer;

public class AccountActivity extends AppCompatActivity {

    AccountViewModel accountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountViewModel = new AccountViewModel();
        accountViewModel.getUser();
    }

    protected void observeViewModel() {
        accountViewModel.getUserAccount().observe(this, this::onChangedUser);
    }

    public void onChangedUser(User user){

    }

}
