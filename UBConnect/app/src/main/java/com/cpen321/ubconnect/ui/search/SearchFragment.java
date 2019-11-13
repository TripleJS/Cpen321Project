package com.cpen321.ubconnect.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SearchQuestionAdapter;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Question;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private SearchViewModel searchViewModel;

    private RecyclerView recyclerView;
    private List<Question> questions;
    private String token;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.content_search, container, false);


        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        token = ((GlobalVariables) getActivity().getApplication()).getJwt();
        questions = new ArrayList<>();
//        recyclerView = root.findViewById(R.id.suggestedRecyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        observeViewModel();
//        searchViewModel.getResults();
        return root;
    }

    protected void observeViewModel() {
        searchViewModel.getQuestions(token).observe(this, this::onChangedResult);
    }

    public void onChangedResult(List<Question> questions){
        this.questions.addAll(questions);
        RecyclerView.Adapter adapter = new SearchQuestionAdapter(questions);
        recyclerView.setAdapter(adapter);

    }


}
