package com.cpen321.ubconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.viewModel.QuestionViewModel;
import com.facebook.AccessToken;

public class QuestionActivity extends AppCompatActivity {

    private TextView title;
    private TextView content;
    private TextView dateAuthor;
    private TextView answer;
    private Button startAnswer;
    String questionId;

    private QuestionViewModel questionViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        title = findViewById(R.id.questionATitle);
        content = findViewById(R.id.questionAContent);
        dateAuthor = findViewById(R.id.questionADateAuthor);
        answer = findViewById(R.id.questionAAnswer);
        startAnswer = findViewById(R.id.answerButton);

        questionViewModel = new QuestionViewModel();

        questionId = getIntent().getExtras().getString("arg");

        View.OnClickListener answerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, AnswerActivity.class);
                startActivity(intent);
                QuestionActivity.this.finish();
            }
        };

        startAnswer.setOnClickListener(answerOnClickListener);

        observeViewModel(questionId);

    }

    protected void observeViewModel(String questionId) {
        questionViewModel.getQuestion(questionId).observe(this, this::onChangedQuestion);
    }

    public void onChangedQuestion(Question question){
        title.setText(question.getQuestionTitle());
        content.setText(question.getQuestion());
        dateAuthor.setText(question.getDate() + " by " + question.getOwner());
        if(question.getAnswer() != null){
            answer.setText(question.getAnswer().get(0));
        }
        else {
            answer.setText("No Answers. Be the first to answer!!");
        }
    }

}
