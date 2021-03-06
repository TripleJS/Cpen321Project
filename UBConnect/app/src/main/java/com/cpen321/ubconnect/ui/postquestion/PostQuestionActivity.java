package com.cpen321.ubconnect.ui.postquestion;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.ErrorHandlingUtils;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.HelperUtils;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.ui.account.AccountActivity;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.otheranswers.OtherAnswersActivity;
import com.cpen321.ubconnect.ui.question.QuestionActivity;
import com.cpen321.ubconnect.ui.search.SearchActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

public class PostQuestionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText title;
    private EditText question;
    private EditText course;
    private EditText topic;
    private String userId;
    private String token;



    private PostQuestionVewModel postQuestionVewModel;


    private ErrorHandlingUtils errorHandlingUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postquestion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                // nothing to do
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                HelperUtils.hideKeyboard(PostQuestionActivity.this);
                title.setCursorVisible(false);
                question.setCursorVisible(false);
                course.setCursorVisible(false);
                topic.setCursorVisible(false);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                title.setCursorVisible(true);
                question.setCursorVisible(true);
                course.setCursorVisible(true);
                topic.setCursorVisible(true);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // nothing to do
            }
        });

        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        errorHandlingUtils = new ErrorHandlingUtils();

        title = findViewById(R.id.titlePQ);
        question = findViewById(R.id.contentPQ);
        course = findViewById(R.id.coursePQ2);
        topic = findViewById(R.id.topicPQ2);
        Button submit = findViewById(R.id.submitButton);

        token = ((GlobalVariables) this.getApplication()).getJwt();
        title.setText("");
        question.setText("");
        course.setText("");
        topic.setText("");

        userId = ((GlobalVariables) this.getApplication()).getUserID();

        postQuestionVewModel = ViewModelProviders.of(this).get(PostQuestionVewModel.class);

        View.OnClickListener submitOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(notValid()){
                    Toast.makeText(getApplicationContext(),"Please complete all the entries", Toast.LENGTH_SHORT).show();
                    return;
                }

                Question questionToSubmit = new Question();
                questionToSubmit.setQuestion(question.getText().toString());
                questionToSubmit.setOwner(userId);
                questionToSubmit.setCourse(course.getText().toString());
                questionToSubmit.setTopic(topic.getText().toString());
                questionToSubmit.setTitle(title.getText().toString());
                postQuestionVewModel.submitQuestion(questionToSubmit,token);
            }
        };

        submit.setOnClickListener(submitOnClickListener);

        observeViewModel();

    }

    private boolean notValid() {
        return (question.getText().toString().length() == 0) || (course.getText().toString().length() == 0) || (topic.getText().toString().length() == 0) || (title.getText().toString().length() == 0);
    }

    protected void observeViewModel() {
        postQuestionVewModel.getQuestionData().observe(this, this::onSubmit);
        postQuestionVewModel.getError().observe(this, this::onError);
    }

    public void onError(String err){
        findViewById(R.id.postquestionLayout).setVisibility(View.GONE);
        errorHandlingUtils.showError(PostQuestionActivity.this,err, retryOnClickListener, "Retry", Snackbar.LENGTH_INDEFINITE);
    }

    private View.OnClickListener retryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            errorHandlingUtils.hideError();
            findViewById(R.id.postquestionLayout).setVisibility(View.VISIBLE);
        }
    };
    public void onSubmit(Question question){
        Toast.makeText(getApplicationContext(),"submitted", Toast.LENGTH_SHORT).show();
        FirebaseMessaging.getInstance().subscribeToTopic(question.getId());
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
            Intent intent = new Intent(PostQuestionActivity.this, HomeActivity.class);
            startActivity(intent);
            PostQuestionActivity.this.finish();
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
                Intent h= new Intent(PostQuestionActivity.this, HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(PostQuestionActivity.this,PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(PostQuestionActivity.this, SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(PostQuestionActivity.this, AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(PostQuestionActivity.this, QuestionActivity.class);
                startActivity(s);
                break;
            case R.id.nav_continue_answering:
                Intent t= new Intent(PostQuestionActivity.this, OtherAnswersActivity.class);
                startActivity(t);
                break;
            default:
                break;

        }

        PostQuestionActivity.this.finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
