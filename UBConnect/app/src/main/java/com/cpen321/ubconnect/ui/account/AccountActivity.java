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
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SearchQuestionAdapter;
import com.cpen321.ubconnect.model.ErrorHandlingUtils;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.HelperUtils;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.main.MainActivity;
import com.cpen321.ubconnect.ui.otheranswers.OtherAnswersActivity;
import com.cpen321.ubconnect.ui.postquestion.PostQuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionActivity;
import com.cpen321.ubconnect.ui.search.SearchActivity;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText emailNameET;
    private EditText coursesNameET;
    private EditText userNameET;

    private String token;
    private String userId;

    private AccountViewModel accountViewModel;

    private RecyclerView recyclerView;
    private List<Question> questions;

    private String noEmail;
    private String noUsername;
    private String noCourses;

    private boolean isKeyboardShowing = false;
    private View contentView;

    private boolean applied = false;

    private ErrorHandlingUtils errorHandlingUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //josh
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        drawer.addDrawerListener(drawerListener);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        //josh

        errorHandlingUtils = new ErrorHandlingUtils();

        userNameET = findViewById(R.id.usernameET);
        emailNameET = findViewById(R.id.emailET);
        coursesNameET = findViewById(R.id.coursesET);

        noCourses = "Update your courses";
        noUsername = "Update your username";
        noEmail = "Update your email";

        userNameET.setText(noUsername);
        emailNameET.setText(noEmail);
        coursesNameET.setText(noCourses);

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

        // ContentView is the root view of the layout of this activity/fragment
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);

        editUser.setOnClickListener(usernameOnClickListener);
        editEmail.setOnClickListener(emailOnClickListener);
        editCourses.setOnClickListener(coursesOnClickListener);
        apply.setOnClickListener(applyOnClickListener);



        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);

        questions = new ArrayList<>();
        recyclerView = findViewById(R.id.accountRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        observeViewModel();

        accountViewModel.getUserInfo(token, userId);
    }

    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

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
    };

    private View.OnClickListener applyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(notValid()){
                Toast.makeText(getApplicationContext(),"Email and Username cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (! checkEmailFormat(emailNameET.getText().toString())) {
                Toast.makeText(getApplicationContext(),"wrong email format", Toast.LENGTH_SHORT).show();
                return;
            }

            userNameET.setFocusableInTouchMode(false);
            emailNameET.setFocusableInTouchMode(false);
            coursesNameET.setFocusableInTouchMode(false);
            applied = true;
            User user = new User();
            user.setEmail(emailNameET.getText().toString());
            user.setUserName(userNameET.getText().toString());
            user.setCourses(Arrays.asList(coursesNameET.getText().toString().split("\\s*,\\s*")));
            accountViewModel.setUserInfo(token, userId,user);
        }
    };

    private View.OnClickListener usernameOnClickListener = new View.OnClickListener() {
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

    private View.OnClickListener emailOnClickListener = new View.OnClickListener() {
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

    private View.OnClickListener coursesOnClickListener = new View.OnClickListener() {
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

            if (!isKeyboardShowing) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }

        }
    };

    private DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            // nothing to do
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            HelperUtils.hideKeyboard(AccountActivity.this);
            emailNameET.setCursorVisible(false);
            coursesNameET.setCursorVisible(false);
            userNameET.setCursorVisible(false);
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            emailNameET.setCursorVisible(true);
            coursesNameET.setCursorVisible(true);
            userNameET.setCursorVisible(true);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            // nothing to do
        }
    };


    protected void observeViewModel() {
        accountViewModel.getSetUserAccount().observe(this, this::onChangedUser);
        accountViewModel.getError().observe(this, this::onError);
    }

    public void onError(String err){
        findViewById(R.id.accountLayout).setVisibility(View.GONE);
        errorHandlingUtils.showError(AccountActivity.this,err, retryOnClickListener, "Retry", Snackbar.LENGTH_INDEFINITE);
    }

    private View.OnClickListener retryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            errorHandlingUtils.hideError();
            findViewById(R.id.accountLayout).setVisibility(View.VISIBLE);
            accountViewModel.getUserInfo(token,userId);
        }
    };

    public void onChangedUser(User user){
        if(applied){
            Toast.makeText(getApplicationContext(),"Applied", Toast.LENGTH_SHORT).show();
            applied = false;
        }

        if(!user.getUserName().equals("")){
            userNameET.setText(user.getUserName());
        }
        else {
            userNameET.setText(noUsername);
        }

        if(!user.getEmail().equals("")){
            emailNameET.setText(user.getEmail());
        }
        else {
            emailNameET.setText(noEmail);
        }

        if(!updateCourses(user.getCourses()).equals("")){
            coursesNameET.setText(updateCourses(user.getCourses()));
        }
        else {
            coursesNameET.setText(noCourses);
        }

        this.questions.clear();
        if(user.getQuestions() != null){
            this.questions.addAll(user.getQuestions());
        }

        RecyclerView.Adapter adapter = new SearchQuestionAdapter(questions);
        recyclerView.setAdapter(adapter);
        noEmail = user.getEmail();
        noUsername = user.getUserName();
        noCourses = updateCourses(user.getCourses());
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


    private String updateCourses(List<String> courses){
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

    private void checkEditTexts(){
        if(coursesNameET.getText().toString().equals("")){
            coursesNameET.setText(noCourses);
        }
        if(emailNameET.getText().toString().equals("")){
            emailNameET.setText(noEmail);
        }
        if(userNameET.getText().toString().equals("")){
            userNameET.setText(noUsername);
        }
    }

    private boolean notValid() {
        return ((userNameET.getText().toString().length() == 0) || (emailNameET.getText().toString().length() == 0));
    }

    public boolean checkEmailFormat(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
                Intent t= new Intent(AccountActivity.this, OtherAnswersActivity.class);
                startActivity(t);
                break;
            default:
                break;

        }

        AccountActivity.this.finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
