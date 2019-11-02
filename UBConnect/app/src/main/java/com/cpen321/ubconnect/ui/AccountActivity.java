package com.cpen321.ubconnect.ui;

import android.content.Intent;
import android.os.Bundle;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SearchQuestionAdapter;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.viewModel.AccountViewModel;
import com.cpen321.ubconnect.viewModel.SearchViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

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

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_account);

            //josh
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            //josh

            userImage = findViewById(R.id.userImage);

            userNameET = findViewById(R.id.usernameET);
            userNameTV = findViewById(R.id.usernameTV);
            editUser = findViewById(R.id.edit_username);

            emailNameET = findViewById(R.id.emailET);
            emailNameTV = findViewById(R.id.emailTV);
            editEmail = findViewById(R.id.edit_email);

            coursesNameET = findViewById(R.id.coursesET);
            coursesNameTV = findViewById(R.id.coursesTV);
            editCourses = findViewById(R.id.edit_courses);

            apply = findViewById(R.id.saveAC);

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
            recyclerView = findViewById(R.id.accountRecycle);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            updatejljl();

            adapter = new SearchQuestionAdapter(questions, getApplicationContext());
            recyclerView.setAdapter(adapter);

            //observeViewModelGet();
        }

        public void updatejljl(){
            // begin
            Question q1 = new Question();
            q1.setQuestionTitle("q1");
            Date d1 = new Date();
            q1.setDate(d1);
            q1.setOwner("gg");
            q1.setQuestion("joojpp");
            q1.set_id("qqqqqqqqqqqqqqqq");
            questions.add(q1);
            Question q2 = new Question();
            q2.setQuestionTitle("q2");
            Date d2 = new Date();
            q2.setDate(d2);
            q2.setOwner("gg222222");
            q2.setQuestion("joojpp22222222");
            q2.set_id("wwwwwwwwwwwwwwwwwwww");
            questions.add(q2);
            Question q3 = new Question();
            q3.setQuestionTitle("q3");
            Date d3 = new Date();
            q3.setDate(d3);
            q3.setOwner("gg3333333");
            q3.setQuestion("joojpp33333333");
            q3.set_id("eeeeeeeeeeeeeeeee");
            questions.add(q3);
        }

        protected void observeViewModelSet() {
            accountViewModel.setUserAccount().observe(this, this::onChangedUserSet);
        }

        public void onChangedUserSet(User user){
            //set up user
        }

        protected void observeViewModelGet() {
            accountViewModel.getUserAccount().observe(this, this::onChangedUser);
            searchViewModel.getQuestions().observe(this, this::onChangedQuestions);
        }

        public void onChangedUser(User user){
            //set up user
        }

        public void onChangedQuestions(List<Question> questions){
            this.questions.addAll(questions);
            adapter = new SearchQuestionAdapter(questions, getApplicationContext());
            recyclerView.setAdapter(adapter);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        int id=item.getItemId();
        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(AccountActivity.this,HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(AccountActivity.this,PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(AccountActivity.this,SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(AccountActivity.this,AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(AccountActivity.this,QuestionActivity.class);
                startActivity(s);
            case R.id.nav_continue_answering:
                Intent t= new Intent(AccountActivity.this,ViewOnlyOtherAnswerActivity.class);
                startActivity(t);
                break;

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
