package com.cpen321.ubconnect.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

public class HomeFragment extends Fragment {

    private SuggestionViewModel suggestionViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Question> questions;
    private String userId;

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
                        suggestionViewModel.getSuggestion(userId);
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


        observeViewModel();

        User user = new User();
        user.setUserId(userId);
        suggestionViewModel.getSuggestion(userId);

        return root;

    }


    protected void observeViewModel() {
        suggestionViewModel.getQuestions().observe(this, this::onChangedSuggestions);
    }

    public void onChangedSuggestions(List<Question> questions){
        this.questions.addAll(questions);
        adapter = new SuggestedQuestionAdapter(this.questions);
        recyclerView.setAdapter(adapter);

    }

}