package com.cpen321.ubconnect.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SearchQuestionAdapter;
import com.cpen321.ubconnect.model.data.Question;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SearchFragment extends Fragment {
    private SearchViewModel searchViewModel;

    private RecyclerView recyclerView;
    private List<Question> questions;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_home, container, false);

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        questions = new ArrayList<>();
        recyclerView = root.findViewById(R.id.suggestedRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        observeViewModel();
        searchViewModel.getResults();
        return root;
    }

    protected void observeViewModel() {
        searchViewModel.getQuestions().observe(this, this::onChangedResult);
    }

    public void onChangedResult(List<Question> questions){
        this.questions.addAll(questions);
        RecyclerView.Adapter adapter = new SearchQuestionAdapter(questions, getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

}
