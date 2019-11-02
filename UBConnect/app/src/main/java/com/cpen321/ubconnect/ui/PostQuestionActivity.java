package com.cpen321.ubconnect.ui;

import android.content.Intent;
import android.os.Bundle;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.viewModel.PostQuestionVewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PostQuestionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;

    private EditText title;
    private EditText question;
    private EditText course;
    private EditText topic;
    private Button submit;
    private String userId;

    private PostQuestionVewModel postQuestionVewModel;

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postquestion);
//josh
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        title = findViewById(R.id.titlePQ);
        question = findViewById(R.id.contentPQ);
        course = findViewById(R.id.coursePQ2);
        topic = findViewById(R.id.topicPQ2);
        submit = findViewById(R.id.submitButton);

        title.setText("");
        question.setText("");
        course.setText("");
        topic.setText("");

        userId = ((GlobalVariables) this.getApplication()).getUserID();

        postQuestionVewModel = ViewModelProviders.of(this).get(PostQuestionVewModel.class);

        View.OnClickListener submitOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isValid()){
                    Toast.makeText(getApplicationContext(),"Please complete all the entries", Toast.LENGTH_SHORT).show();
                    return;
                }

                Question questionToSubmit = new Question();
                questionToSubmit.setQuestion(question.getText().toString());
                questionToSubmit.setOwner(userId);
                questionToSubmit.setCourse(course.getText().toString());
                questionToSubmit.setTopic(topic.getText().toString());
                questionToSubmit.setTitle(title.getText().toString());
                postQuestionVewModel.submitQuestion(questionToSubmit);

                FirebaseMessaging.getInstance().subscribeToTopic("weather")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "uuuuuuuuuuuuuuuuuu", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        };

        submit.setOnClickListener(submitOnClickListener);

        observeViewModel();

    }

    private boolean isValid() {
        if ((question.getText().toString().length() == 0) || (course.getText().toString().length() == 0) || (topic.getText().toString().length() == 0) || (title.getText().toString().length() == 0)) {
            return false;
        }
        return true;
    }

    protected void observeViewModel() {
        postQuestionVewModel.getQuestionData().observe(this, this::onSubmit);
    }

    public void onSubmit(Question question){
        Log.d("Fuck", "call back works ");
        Toast.makeText(getApplicationContext(),"submitted", Toast.LENGTH_SHORT).show();
        title.setText("");
        this.question.setText("");
        course.setText("");
        topic.setText("");
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
                Intent h= new Intent(PostQuestionActivity.this,HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(PostQuestionActivity.this,PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(PostQuestionActivity.this,SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(PostQuestionActivity.this,AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(PostQuestionActivity.this,QuestionActivity.class);
                startActivity(s);
            case R.id.nav_continue_answering:
                Intent t= new Intent(PostQuestionActivity.this,ViewOnlyOtherAnswerActivity.class);
                startActivity(t);
                break;

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
