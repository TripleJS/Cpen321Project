package com.cpen321.ubconnect.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.viewModel.MainViewModel;
import com.cpen321.ubconnect.viewModel.PostQuestionVewModel;
import com.facebook.AccessToken;

import java.util.Date;

public class PostQuestionActivity extends AppCompatActivity {

    private EditText title;
    private EditText question;
    private EditText course;
    private EditText topic;
    private Button submit;
    private String userId;

    private PostQuestionVewModel postQuestionVewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postquestion);

        title = findViewById(R.id.titlePQ);
        question = findViewById(R.id.contentPQ);
        course = findViewById(R.id.coursePQ2);
        topic = findViewById(R.id.topicPQ2);
        submit = findViewById(R.id.submitButton);

        title.setText("");
        question.setText("");
        course.setText("");
        topic.setText("");

        userId = ((GlobalVariables) this.getApplication()).getUserID();

        postQuestionVewModel = ViewModelProviders.of(this).get(PostQuestionVewModel.class);

        View.OnClickListener submitOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isValid()){
                    Toast.makeText(getApplicationContext(),"Please complete all the entries", Toast.LENGTH_SHORT).show();
                    return;
                }

                Question questionToSubmit = new Question();
                questionToSubmit.setQuestion(question.getText().toString());
                questionToSubmit.setOwner(userId);
                questionToSubmit.setCourse(course.getText().toString());
                questionToSubmit.setTopic(topic.getText().toString());
                questionToSubmit.setTitle(title.getText().toString());
                postQuestionVewModel.submitQuestion(questionToSubmit);
            }
        };

        submit.setOnClickListener(submitOnClickListener);

        observeViewModel();

    }

    private boolean isValid() {
        if ((question.getText().toString().length() == 0) || (course.getText().toString().length() == 0) || (topic.getText().toString().length() == 0) || (title.getText().toString().length() == 0)) {
            return false;
        }
        return true;
    }

    protected void observeViewModel() {
        postQuestionVewModel.getQuestionData().observe(this, this::onSubmit);
    }

    public void onSubmit(Question question){
        Log.d("Fuck", "call back works ");
        Toast.makeText(getApplicationContext(),"submitted", Toast.LENGTH_SHORT).show();
        title.setText("");
        this.question.setText("");
        course.setText("");
        topic.setText("");
    }

}
