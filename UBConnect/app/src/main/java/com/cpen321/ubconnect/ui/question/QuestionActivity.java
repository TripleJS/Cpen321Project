package com.cpen321.ubconnect.ui.question;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.ui.NavigateToOtherAnswers.NavBetweenAnswersActivity;
import com.cpen321.ubconnect.ui.account.AccountActivity;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.otheranswers.OtherAnswersActivity;
import com.cpen321.ubconnect.ui.postquestion.PostQuestionActivity;
import com.cpen321.ubconnect.ui.search.SearchActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class QuestionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView title;
    private TextView content;
    private TextView dateAuthor;
    private TextView answer;
    private String questionId = "dummyString";


    private QuestionViewModel questionViewModel;
    //joshua
    private Button startAnswer;
    private Button viewMoreAnswers;


    private ErrorHandlingUtils errorHandlingUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        errorHandlingUtils = new ErrorHandlingUtils();

        title = findViewById(R.id.questionATitle);
        content = findViewById(R.id.questionAContent);
        dateAuthor = findViewById(R.id.questionADateAuthor);
        answer = findViewById(R.id.questionAAnswer);
        startAnswer = findViewById(R.id.answerButton);
        viewMoreAnswers = findViewById(R.id.navAnswersButton);

        String token = ((GlobalVariables) this.getApplication()).getJwt();
        String userId = ((GlobalVariables) this.getApplication()).getUserID();

        questionViewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);
//joshua
        observeViewModel();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            questionId = bundle.getString("arg");
        }
        else {
            questionViewModel.getRecentQuestion(userId, token);
        }




    }

    protected void observeViewModel() {
        questionViewModel.getQuestionData().observe(this, this::onChangedQuestion);
        questionViewModel.getError().observe(this, this::onError);
    }

    public void onError(String err){
        findViewById(R.id.questionLayout).setVisibility(View.GONE);
        errorHandlingUtils.showError(QuestionActivity.this,err, retryOnClickListener, "Retry", Snackbar.LENGTH_INDEFINITE);
    }

    private View.OnClickListener retryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            errorHandlingUtils.hideError();
            findViewById(R.id.questionLayout).setVisibility(View.VISIBLE);
        }
    };

    public void onChangedQuestion(Question question){
        questionId = question.getId();
        if (questionId.equals("")) {
            Intent intent = new Intent(QuestionActivity.this, NoQuestionActivity.class);
            startActivity(intent);
            QuestionActivity.this.finish();
        }
        title.setText(question.getQuestionTitle());
        content.setText(question.getQuestion());
        dateAuthor.setText(question.getDate() + " by " + question.getOwner());
        if(question.getAnswer() != null){
                answer.setText(question.getAnswer().get(0));
            }
            else {
                answer.setText("No Answers. Be the first to answer!!");
        }
        View.OnClickListener answerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, OtherAnswersActivity.class);
                intent.putExtra("arg",questionId);
                startActivity(intent);
                QuestionActivity.this.finish();
            }
        };
        View.OnClickListener viewMoreOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, NavBetweenAnswersActivity.class);
                intent.putExtra("arg",questionId);
                startActivity(intent);
                QuestionActivity.this.finish();
            }
        };

        startAnswer.setOnClickListener(answerOnClickListener);
        viewMoreAnswers.setOnClickListener(viewMoreOnClickListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(QuestionActivity.this, HomeActivity.class);
            startActivity(intent);
            QuestionActivity.this.finish();
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
                Intent h= new Intent(QuestionActivity.this, HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(QuestionActivity.this,PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(QuestionActivity.this, SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(QuestionActivity.this, AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(QuestionActivity.this, QuestionActivity.class);
                startActivity(s);
                break;
            case R.id.nav_continue_answering:
                Intent t= new Intent(QuestionActivity.this, OtherAnswersActivity.class);
                startActivity(t);
                break;
            default:
                break;

        }

        QuestionActivity.this.finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
