package com.cpen321.ubconnect.ui.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SearchQuestionAdapter;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.main.MainActivity;
import com.cpen321.ubconnect.ui.postquestion.PostQuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionActivity;
import com.cpen321.ubconnect.ui.search.SearchActivity;
import com.cpen321.ubconnect.ui.search.SearchViewModel;
import com.cpen321.ubconnect.ui.viewothers.ViewOnlyOthersAnswerActivity;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //josh
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        //josh

        userNameET = findViewById(R.id.usernameET);
        emailNameET = findViewById(R.id.emailET);
        coursesNameET = findViewById(R.id.coursesET);

        oldCourses = "Update your courses";
        oldUsername = "Update your username";
        oldEmail = "Update your email";

        userNameET.setText(oldUsername);
        emailNameET.setText(oldEmail);
        coursesNameET.setText(oldCourses);

        contentView = this.findViewById(android.R.id.content).getRootView();



        userId = ((GlobalVariables) this.getApplication()).getUserID();
        token = ((GlobalVariables) this.getApplication()).getJwt();

        Button editUser = findViewById(R.id.edit_username);
        Button editEmail = findViewById(R.id.edit_email);
        Button editCourses = findViewById(R.id.edit_courses);

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(logoutOnClickListener);

        Button apply = findViewById(R.id.saveAC);

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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
        recyclerView = findViewById(R.id.accountRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        observeViewModelGet();
        observeViewModelSet();

        accountViewModel.getUserInfo(token, userId);
    }


    protected void observeViewModelSet() {
        accountViewModel.setUserAccount().observe(this, this::onChangedUserSet);
    }

    public void onChangedUserSet(User user){
        Toast.makeText(getApplicationContext(),"Applied", Toast.LENGTH_SHORT).show();
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
        accountViewModel.getUserAccount().observe(this, this::onChangedUser);
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
            Intent intent = new Intent(AccountActivity.this, MainActivity.class);
            startActivity(intent);
            AccountActivity.this.finish();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        int id=item.getItemId();
//        mDrawerToggle.syncState();
        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(AccountActivity.this, HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(AccountActivity.this, PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(AccountActivity.this, SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(AccountActivity.this,AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(AccountActivity.this, QuestionActivity.class);
                startActivity(s);
                break;
            case R.id.nav_continue_answering:
                Intent t= new Intent(AccountActivity.this, ViewOnlyOthersAnswerActivity.class);
                startActivity(t);
                break;

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}