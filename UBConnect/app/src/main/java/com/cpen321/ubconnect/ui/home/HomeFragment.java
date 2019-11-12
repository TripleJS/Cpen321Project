package com.cpen321.ubconnect.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.cpen321.ubconnect.ui.question.QuestionFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private SuggestionViewModel suggestionViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Question> questions;
    private String userId;
    private String token;

    private Button retry;
    private TextView error;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_home, container, false);

        suggestionViewModel = ViewModelProviders.of(this).get(SuggestionViewModel.class);
        userId = ((GlobalVariables) getActivity().getApplication()).getUserID();

        questions = new ArrayList<>();
        recyclerView = root.findViewById(R.id.suggestedRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        token = ((GlobalVariables) getActivity().getApplication()).getJwt();

        error = root.findViewById(R.id.errMessage);
        error.setVisibility(View.GONE);
        retry = root.findViewById(R.id.retry);
        retry.setVisibility(View.GONE);
        retry.setOnClickListener(retryOnClickListener);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                Question questionTemp = questions.get(pos);
                String qid = questionTemp.getId();
                if(direction == ItemTouchHelper.LEFT ) {
                    questions.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    if(questions.size()==1){
                        suggestionViewModel.getSuggestion(userId, token, getActivity().getCurrentFocus());
                    }
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

                    Bundle bundle=new Bundle();
                    bundle.putString("arg", questions.get(pos).getId());
                    QuestionFragment questionFragment = new QuestionFragment();
                    questionFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .add(((ViewGroup)getView().getParent()).getId(), questionFragment).commit();
                }
            }
        }).attachToRecyclerView(recyclerView);

//        updatejljl();
//
//        adapter = new SuggestedQuestionAdapter(this.questions);
//        recyclerView.setAdapter(adapter);

        observeViewModel();

        User user = new User();
        user.setUserId(userId);
        suggestionViewModel.getSuggestion(userId, token, getView());

        return root;

    }

    public void updatejljl(){
        // begin
        Question q1 = new Question();
        q1.setQuestionTitle("q1");
        Date d1 = new Date();
        q1.setDate(d1);
        q1.setOwner("gg");
        q1.setQuestion("joojpp");
        q1.setId("qqqqqqqqqqqqqqqq");
        questions.add(q1);
        Question q2 = new Question();
        q2.setQuestionTitle("q2");
        Date d2 = new Date();
        q2.setDate(d2);
        q2.setOwner("gg222222");
        q2.setQuestion("joojpp22222222");
        q2.setId("wwwwwwwwwwwwwwwwwwww");
        questions.add(q2);
        Question q3 = new Question();
        q3.setQuestionTitle("q3");
        Date d3 = new Date();
        q3.setDate(d3);
        q3.setOwner("gg3333333");
        q3.setQuestion("joojpp33333333");
        q3.setId("eeeeeeeeeeeeeeeee");
        questions.add(q3);
    }

    protected void observeViewModel() {
        suggestionViewModel.getQuestions().observe(this, this::onChangedSuggestions);
        suggestionViewModel.getError().observe(this, this::onError);
    }

    public void onChangedSuggestions(List<Question> questions){
        this.questions.addAll(questions);
        adapter = new SuggestedQuestionAdapter(this.questions);
        recyclerView.setAdapter(adapter);

    }

    public void onError(String err){
        recyclerView.setVisibility(View.GONE);
        retry.setVisibility(View.VISIBLE);
        error.setVisibility(View.VISIBLE);

        error.setText(err);
    }

    private View.OnClickListener retryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            recyclerView.setVisibility(View.VISIBLE);
            retry.setVisibility(View.GONE);
            error.setVisibility(View.GONE);
            User user = new User();
            user.setUserId(userId);
            suggestionViewModel.getSuggestion(userId, token, getView());
        }
    };

}