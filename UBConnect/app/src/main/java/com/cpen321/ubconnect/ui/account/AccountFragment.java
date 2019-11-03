package com.cpen321.ubconnect.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SearchQuestionAdapter;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.ui.search.SearchViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountFragment extends Fragment {


    private ImageView userImage;

    private TextView userNameTV;
    private EditText userNameET;
    private Button editUser;

    private TextView emailNameTV;
    private EditText emailNameET;
    private Button editEmail;

    private TextView coursesNameTV;
    private EditText coursesNameET;
    private Button editCourses;

    private Button apply;



    private AccountViewModel accountViewModel;
    private SearchViewModel searchViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Question> questions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_account, container, false);

        userImage = root.findViewById(R.id.userImage);

        userNameET = root.findViewById(R.id.usernameET);
        userNameTV = root.findViewById(R.id.usernameTV);
        editUser = root.findViewById(R.id.edit_username);

        emailNameET = root.findViewById(R.id.emailET);
        emailNameTV = root.findViewById(R.id.emailTV);
        editEmail = root.findViewById(R.id.edit_email);

        coursesNameET = root.findViewById(R.id.coursesET);
        coursesNameTV = root.findViewById(R.id.coursesTV);
        editCourses = root.findViewById(R.id.edit_courses);

        apply = root.findViewById(R.id.saveAC);

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
        adapter = new SearchQuestionAdapter(questions, getActivity());
        recyclerView.setAdapter(adapter);
    }

}
