package com.cpen321.ubconnect.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.viewModel.AccountViewModel;
import com.cpen321.ubconnect.viewModel.SuggestionViewModel;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    SuggestionViewModel suggestionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        suggestionViewModel = new SuggestionViewModel();
        suggestionViewModel.getSuggestion();
    }

    protected void observeViewModel() {
        suggestionViewModel.getQuestions().observe(this, this::onChangedSuggestions);
    }

    public void onChangedSuggestions(List<Question> questions){

    }
}
