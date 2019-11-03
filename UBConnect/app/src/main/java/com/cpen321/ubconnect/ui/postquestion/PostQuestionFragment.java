package com.cpen321.ubconnect.ui.postquestion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PostQuestionFragment extends Fragment {

    private EditText title;
    private EditText question;
    private EditText course;
    private EditText topic;
    private Button submit;
    private String userId;

    private PostQuestionVewModel postQuestionVewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_postquestion, container, false);

        title = root.findViewById(R.id.titlePQ);
        question = root.findViewById(R.id.contentPQ);
        course = root.findViewById(R.id.coursePQ2);
        topic = root.findViewById(R.id.topicPQ2);
        submit = root.findViewById(R.id.submitButton);

        title.setText("");
        question.setText("");
        course.setText("");
        topic.setText("");

        userId = ((GlobalVariables) getActivity().getApplication()).getUserID();

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

                FirebaseMessaging.getInstance().subscribeToTopic(questionToSubmit.get_id());
            }
        };

        submit.setOnClickListener(submitOnClickListener);

        observeViewModel();

        return root;
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
        Toast.makeText(getApplicationContext(),"submitted", Toast.LENGTH_SHORT).show();
        title.setText("");
        this.question.setText("");
        course.setText("");
        topic.setText("");
    }

}
