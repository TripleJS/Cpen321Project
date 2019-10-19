package com.cpen321.ubconnect.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SuggestedQuestionAdapter;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.viewModel.AccountViewModel;
import com.cpen321.ubconnect.viewModel.SuggestionViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    SuggestionViewModel suggestionViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        suggestionViewModel = new SuggestionViewModel();
        suggestionViewModel.getSuggestion();

        questions = new ArrayList<>();
        recyclerView = findViewById(R.id.suggestedRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        observeViewModel();
        suggestionViewModel.getSuggestion();
    }

    protected void observeViewModel() {
        suggestionViewModel.getQuestions().observe(this, this::onChangedSuggestions);
    }

    public void onChangedSuggestions(List<Question> questions){
        this.questions.addAll(questions);
        adapter = new SuggestedQuestionAdapter(questions, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
