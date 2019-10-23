package com.cpen321.ubconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SuggestedQuestionAdapter;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.Swiped;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.viewModel.SuggestionViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    SuggestionViewModel suggestionViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Question> questions;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        suggestionViewModel = ViewModelProviders.of(this).get(SuggestionViewModel.class);
        userId = ((GlobalVariables) this.getApplication()).getUserID();

        questions = new ArrayList<>();
        recyclerView = findViewById(R.id.suggestedRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);


//        suggestionViewModel.getSuggestion();
//        updatejljl();
//
//        adapter = new SuggestedQuestionAdapter(questions, getApplicationContext());
//        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                Log.d(TAG, "onSwiped: " + pos );
                Question questionTemp = questions.get(pos);
                String qid = questionTemp.get_id();
                Log.d("FUCK", "onSwiped: " + questionTemp.get_id());
                if(direction == ItemTouchHelper.LEFT ) {
                    questions.remove(pos);
                    if(questions.size()==1){
                        suggestionViewModel.getSuggestion(userId);
                    }
                    adapter.notifyItemRemoved(pos);
                    Swiped swiped = new Swiped();
                    swiped.setDirection("left");
                    swiped.setQuestionId(qid);

                    swiped.setUserId(userId);
                    suggestionViewModel.sendSwipe(swiped);
                }
                else if (direction == ItemTouchHelper.RIGHT) {
                    Swiped swiped = new Swiped();
                    swiped.setDirection("right");
                    swiped.setQuestionId(qid);
                    swiped.setUserId(userId);
                    suggestionViewModel.sendSwipe(swiped);
                    Intent intent = new Intent(HomeActivity.this, QuestionActivity.class);
                    intent.putExtra("arg", questions.get(pos).get_id());
                    startActivity(intent);
                    HomeActivity.this.finish();
                }
            }
        }).attachToRecyclerView(recyclerView);
        // end

        observeViewModel();

        User user = new User();
        user.setUserId(userId);
        suggestionViewModel.getSuggestion(userId);



        Log.d(TAG, "onCreate: out");
    }

//    public void updatejljl(){
//        // begin
//        Question q1 = new Question();
//        q1.setQuestionTitle("q1");
//        Date d1 = new Date();
//        q1.setDate(d1);
//        q1.setOwner("gg");
//        q1.setQuestion("joojpp");
//        q1.set_id("qqqqqqqqqqqqqqqq");
//        questions.add(q1);
//        Question q2 = new Question();
//        q2.setQuestionTitle("q2");
//        Date d2 = new Date();
//        q2.setDate(d2);
//        q2.setOwner("gg222222");
//        q2.setQuestion("joojpp22222222");
//        q2.set_id("wwwwwwwwwwwwwwwwwwww");
//        questions.add(q2);
//        Question q3 = new Question();
//        q3.setQuestionTitle("q3");
//        Date d3 = new Date();
//        q3.setDate(d3);
//        q3.setOwner("gg3333333");
//        q3.setQuestion("joojpp33333333");
//        q3.set_id("eeeeeeeeeeeeeeeee");
//        questions.add(q3);
//    }

    protected void observeViewModel() {
        suggestionViewModel.getQuestions().observe(this, this::onChangedSuggestions);
    }

    public void onChangedSuggestions(List<Question> questions){
        this.questions.addAll(questions);
        Log.d("FUCK", "onSwiped2: " + questions.get(0).get_id());
        Log.d("FUCK", "onSwiped2: " + questions.get(0).get_id());
        Log.d(TAG, "onCreate: outdjfkdsahfdsjjsfs");
        adapter = new SuggestedQuestionAdapter(questions, getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

//    public class CustomLayoutManager extends LinearLayoutManager {
//        private boolean isScrollEnabled = true;
//
//        public CustomLayoutManager(Context context) {
//            super(context);
//        }
//
//        public void setScrollEnabled(boolean flag) {
//            this.isScrollEnabled = flag;
//        }
//
//        @Override
//        public boolean canScrollVertically() {
//            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
//            return isScrollEnabled && super.canScrollVertically();
//        }
//    }

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
