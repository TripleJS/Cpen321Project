package com.cpen321.ubconnect.ui.question;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.ui.otheranswers.OtherAnswersFragment;

public class QuestionFragment extends Fragment {

    private TextView title;
    private TextView content;
    private TextView dateAuthor;
    private TextView answer;
    private String questionId;

    private QuestionViewModel questionViewModel;

    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.content_question, container, false);

        title = root.findViewById(R.id.questionATitle);
        content = root.findViewById(R.id.questionAContent);
        dateAuthor = root.findViewById(R.id.questionADateAuthor);
        answer = root.findViewById(R.id.questionAAnswer);
        Button startAnswer = root.findViewById(R.id.answerButton);

        token = ((GlobalVariables) getActivity().getApplication()).getJwt();

        questionViewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);

        questionId = getArguments().getString("arg");

        View.OnClickListener answerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("arg", questionId);
                OtherAnswersFragment otherAnswersFragment=new OtherAnswersFragment();
                otherAnswersFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .add(((ViewGroup)getView().getParent()).getId(), otherAnswersFragment).commit();
            }
        };

        startAnswer.setOnClickListener(answerOnClickListener);

        observeViewModel(questionId);

        return root;
    }

    protected void observeViewModel(String questionId) {
        questionViewModel.getQuestion(questionId, token).observe(this, this::onChangedQuestion);
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
