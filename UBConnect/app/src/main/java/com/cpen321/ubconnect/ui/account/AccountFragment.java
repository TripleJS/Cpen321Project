package com.cpen321.ubconnect.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SearchQuestionAdapter;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.ui.search.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {


    private EditText emailNameET;

    private EditText coursesNameET;

    private AccountViewModel accountViewModel;
    private SearchViewModel searchViewModel;

    private RecyclerView recyclerView;
    private List<Question> questions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_account, container, false);

        EditText userNameET = root.findViewById(R.id.usernameET);

        Button editUser = root.findViewById(R.id.edit_username);

        emailNameET = root.findViewById(R.id.emailET);

        Button editEmail = root.findViewById(R.id.edit_email);

        coursesNameET = root.findViewById(R.id.coursesET);
        Button editCourses = root.findViewById(R.id.edit_courses);

        Button apply = root.findViewById(R.id.saveAC);

        userNameET.setFocusable(false);
        emailNameET.setFocusable(false);
        coursesNameET.setFocusable(false);

        View.OnClickListener usernameOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameET.setFocusableInTouchMode(true);
            }
        };

        View.OnClickListener emailOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailNameET.setFocusableInTouchMode(true);
            }
        };

        View.OnClickListener coursesOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coursesNameET.setFocusableInTouchMode(true);
            }
        };

        View.OnClickListener applyOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observeViewModelSet();
            }
        };

        editUser.setOnClickListener(usernameOnClickListener);
        editEmail.setOnClickListener(emailOnClickListener);
        editCourses.setOnClickListener(coursesOnClickListener);
        apply.setOnClickListener(applyOnClickListener);



        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        questions = new ArrayList<>();
        recyclerView = root.findViewById(R.id.accountRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        observeViewModelGet();
        return root;
    }


    protected void observeViewModelSet() {
        accountViewModel.setUserAccount().observe(this, this::onChangedUserSet);
    }

    public void onChangedUserSet(User user){
        //set up user
    }

    protected void observeViewModelGet() {
        accountViewModel.getUserAccount("fuck").observe(this, this::onChangedUser);
        searchViewModel.getQuestions().observe(this, this::onChangedQuestions);
    }

    public void onChangedUser(User user){
        //set up user
    }

    public void onChangedQuestions(List<Question> questions){
        this.questions.addAll(questions);
        RecyclerView.Adapter adapter = new SearchQuestionAdapter(questions);
        recyclerView.setAdapter(adapter);
    }

}