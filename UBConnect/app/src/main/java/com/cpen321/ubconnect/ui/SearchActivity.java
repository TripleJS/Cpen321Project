package com.cpen321.ubconnect.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.SearchQuestionAdapter;
import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.viewModel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    SearchViewModel searchViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchViewModel = new SearchViewModel();

        questions = new ArrayList<>();
        recyclerView = findViewById(R.id.suggestedRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        observeViewModel();
        searchViewModel.getResults();
        Log.d(TAG, "onCreate: out");
    }

    protected void observeViewModel() {
        searchViewModel.getQuestions().observe(this, this::onChangedResult);
    }

    public void onChangedResult(List<Question> questions){
        this.questions.addAll(questions);
        Log.d(TAG, "onCreate: outdjfkdsahfdsjjsfs");
        adapter = new SearchQuestionAdapter(questions, getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

    private static final String TAG = "MainActivity";
    private final String TEXT_CONTENTS = "TextContents";


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: in");
        super.onDestroy();
        Log.d(TAG, "onDestroy: out");
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: in");
        super.onStart();
        Log.d(TAG, "onStart: out");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: in");
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: out");
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: in");
        super.onRestart();
        Log.d(TAG, "onRestart: out");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: in");
        super.onStop();
        Log.d(TAG, "onStop: out");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: in");
        super.onPause();
        Log.d(TAG, "onPause: out");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: in");
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: out");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: in");
        super.onResume();
        Log.d(TAG, "onResume: out");
    }
}
