package com.cpen321.ubconnect.ui.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SearchQuestionAdapter;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.ui.main.MainActivity;
import com.cpen321.ubconnect.ui.search.SearchViewModel;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountFragment extends Fragment {

    private EditText emailNameET;
    private EditText coursesNameET;
    private EditText userNameET;

    private String token;
    private String userId;

    private AccountViewModel accountViewModel;
    private SearchViewModel searchViewModel;

    private RecyclerView recyclerView;
    private List<Question> questions;

    private String oldEmail;
    private String oldUsername;
    private String oldCourses;

    private AlertDialog dialog;
    private boolean isKeyboardShowing = false;
    private View contentView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.content_account, container, false);

        userNameET = root.findViewById(R.id.usernameET);
        emailNameET = root.findViewById(R.id.emailET);
        coursesNameET = root.findViewById(R.id.coursesET);

        oldCourses = "Update your courses";
        oldUsername = "Update your username";
        oldEmail = "Update your email";

        userNameET.setText(oldUsername);
        emailNameET.setText(oldEmail);
        coursesNameET.setText(oldCourses);



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.create();
        contentView = root;


        userId = ((GlobalVariables) getActivity().getApplication()).getUserID();
        token = ((GlobalVariables) getActivity().getApplication()).getJwt();

        Button editUser = root.findViewById(R.id.edit_username);
        Button editEmail = root.findViewById(R.id.edit_email);
        Button editCourses = root.findViewById(R.id.edit_courses);

        Button logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(logoutOnClickListener);

        Button apply = root.findViewById(R.id.saveAC);

        userNameET.setFocusable(false);
        emailNameET.setFocusable(false);
        coursesNameET.setFocusable(false);

        View.OnClickListener usernameOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEditTexts();
                if(userNameET.getText().toString().equals("Update your username")){
                    userNameET.setText("");
                }
                emailNameET.setFocusableInTouchMode(false);
                coursesNameET.setFocusableInTouchMode(false);
                userNameET.setFocusableInTouchMode(true);
                userNameET.requestFocus();
                userNameET.setSelection(userNameET.getText().length());

                if (!isKeyboardShowing) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                }

            }
        };

        View.OnClickListener emailOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEditTexts();
                if(emailNameET.getText().toString().equals("Update your email")){
                    emailNameET.setText("");
                }
                userNameET.setFocusableInTouchMode(false);
                coursesNameET.setFocusableInTouchMode(false);
                emailNameET.setFocusableInTouchMode(true);
                emailNameET.requestFocus();
                emailNameET.setSelection(emailNameET.getText().length());

                if (!isKeyboardShowing) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                }

            }
        };

        View.OnClickListener coursesOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEditTexts();
                if(coursesNameET.getText().toString().equals("Update your courses")){
                    coursesNameET.setText("");
                }
                userNameET.setFocusableInTouchMode(false);
                emailNameET.setFocusableInTouchMode(false);
                coursesNameET.setFocusableInTouchMode(true);
                coursesNameET.requestFocus();
                coursesNameET.setSelection(coursesNameET.getText().length());

//                int screenHeight = getView().getHeight();
//                Rect r = new Rect();
//                getView().getWindowVisibleDisplayFrame(r);
//                int keypadHeight = screenHeight - r.bottom;
//                Log.d("hi", "onClick: "+ screenHeight + "????????? "+ keypadHeight);
                if (!isKeyboardShowing) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                }

            }
        };

        // ContentView is the root view of the layout of this activity/fragment
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        contentView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = contentView.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        Log.d("Fuck2", "keypadHeight = " + keypadHeight);

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                            }
                        }
                        else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                            }
                        }
                    }
                });

        View.OnClickListener applyOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameET.setFocusableInTouchMode(false);
                emailNameET.setFocusableInTouchMode(false);
                coursesNameET.setFocusableInTouchMode(false);
                User user = new User();
                user.setEmail(emailNameET.getText().toString());
                user.setUserName(userNameET.getText().toString());
                user.setCourses(Arrays.asList(coursesNameET.getText().toString().split("\\s*,\\s*")));
                accountViewModel.setUserInfo(token, userId,user);
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
        observeViewModelSet();

        accountViewModel.getUserInfo(token, userId);

        return root;
    }


    protected void observeViewModelSet() {
        //accountViewModel.setUserAccount().observe(this, this::onChangedUserSet);
    }

    public void onChangedUserSet(User user){
        userNameET.setText(user.getUserName());
        emailNameET.setText(user.getEmail());
        coursesNameET.setText(updateCourses(user.getCourses()));
        this.questions.clear();
        this.questions.addAll(user.getQuestions());
        RecyclerView.Adapter adapter = new SearchQuestionAdapter(questions);
        recyclerView.setAdapter(adapter);
        oldEmail = user.getEmail();
        oldUsername = user.getUserName();
        oldCourses = updateCourses(user.getCourses());
    }

    protected void observeViewModelGet() {
        //accountViewModel.getUserAccount().observe(this, this::onChangedUser);
//        searchViewModel.getQuestions(token).observe(this, this::onChangedQuestions);
    }

    public void onChangedUser(User user){
        userNameET.setText(user.getUserName());
        emailNameET.setText(user.getEmail());
        coursesNameET.setText(updateCourses(user.getCourses()));
        this.questions.clear();
        this.questions.addAll(user.getQuestions());
        RecyclerView.Adapter adapter = new SearchQuestionAdapter(questions);
        recyclerView.setAdapter(adapter);
        oldEmail = user.getEmail();
        oldUsername = user.getUserName();
        oldCourses = updateCourses(user.getCourses());
    }

    public void onChangedQuestions(List<Question> questions){
        this.questions.clear();
        this.questions.addAll(questions);
        RecyclerView.Adapter adapter = new SearchQuestionAdapter(questions);
        recyclerView.setAdapter(adapter);
    }

    private View.OnClickListener logoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    };


    String updateCourses(List<String> courses){
        String result = "";
        for(int i = 0; i < courses.size(); i++){
            if(i == courses.size()-1){
                result += courses.get(i);
            }
            else {
                result += courses.get(i) + ", ";
            }
        }
        return  result;
    }

    void checkEditTexts(){
        if(coursesNameET.getText().toString().equals("")){
            coursesNameET.setText(oldCourses);
        }
        if(emailNameET.getText().toString().equals("")){
            emailNameET.setText(oldEmail);
        }
        if(userNameET.getText().toString().equals("")){
            userNameET.setText(oldUsername);
        }
    }



}
