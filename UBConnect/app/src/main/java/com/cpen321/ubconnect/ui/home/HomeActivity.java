package com.cpen321.ubconnect.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SuggestedQuestionAdapter;
import com.cpen321.ubconnect.model.ErrorHandlingUtils;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.Swiped;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.ui.account.AccountActivity;
import com.cpen321.ubconnect.ui.otheranswers.OtherAnswersActivity;
import com.cpen321.ubconnect.ui.postquestion.PostQuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionActivity;
import com.cpen321.ubconnect.ui.search.SearchActivity;
import com.cpen321.ubconnect.ui.viewothers.ViewOnlyOthersAnswerActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SuggestionViewModel suggestionViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Question> questions;
    private String userId;
    private String token;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    private ErrorHandlingUtils errorHandlingUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //josh
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        toolbar.bringToFront();
        drawer.bringToFront();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        //josh

        suggestionViewModel = ViewModelProviders.of(this).get(SuggestionViewModel.class);
        userId = ((GlobalVariables) this.getApplication()).getUserID();

        questions = new ArrayList<>();
        recyclerView = findViewById(R.id.suggestedRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        token = ((GlobalVariables) this.getApplication()).getJwt();

        errorHandlingUtils = new ErrorHandlingUtils();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                Question questionTemp = questions.get(pos);
                String qid = questionTemp.getId();
                if(direction == ItemTouchHelper.LEFT ) {
                    questions.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    if(questions.size()==1){
                        suggestionViewModel.getSuggestion(userId, token);
                    }
                    Swiped swiped = new Swiped();
                    swiped.setDirection("left");
                    swiped.setQuestionId(qid);

                    swiped.setUserId(userId);
                    suggestionViewModel.sendSwipe(swiped);
                }
                else if (direction == ItemTouchHelper.RIGHT) {
                    Swiped swiped = new Swiped();
                    swiped.setDirection("right");
                    swiped.setQuestionId(qid);
                    swiped.setUserId(userId);
                    suggestionViewModel.sendSwipe(swiped);

                    Intent intent = new Intent(HomeActivity.this, OtherAnswersActivity.class);
                    intent.putExtra("arg", questions.get(pos).getId());
                    startActivity(intent);
                    HomeActivity.this.finish();
                }
            }
        }).attachToRecyclerView(recyclerView);

        observeViewModel();

        User user = new User();
        user.setUserId(userId);
        suggestionViewModel.getSuggestion(userId, token);

        toolbar.bringToFront();
        drawer.bringToFront();
        navigationView.bringToFront();

    }

    protected void observeViewModel() {
        suggestionViewModel.getQuestions().observe(this, this::onChangedSuggestions);
        suggestionViewModel.getError().observe(this, this::onError);
    }

    public void onChangedSuggestions(List<Question> questions){
        this.questions.addAll(questions);
        adapter = new SuggestedQuestionAdapter(this.questions);
        recyclerView.setAdapter(adapter);
        toolbar.bringToFront();
        drawer.bringToFront();
        navigationView.bringToFront();
    }

    public void onError(String err){
        findViewById(R.id.homeLayout).setVisibility(View.GONE);
        errorHandlingUtils.showError(HomeActivity.this,err, retryOnClickListener, "Retry",Snackbar.LENGTH_INDEFINITE);
    }

    private View.OnClickListener retryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            errorHandlingUtils.hideError();
            findViewById(R.id.homeLayout).setVisibility(View.VISIBLE);
            User user = new User();
            user.setUserId(userId);
            suggestionViewModel.getSuggestion(userId, token);
        }
    };

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
        mDrawerToggle.syncState();
        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(HomeActivity.this,HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(HomeActivity.this, PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(HomeActivity.this, AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(HomeActivity.this, QuestionActivity.class);
                startActivity(s);
                break;
            case R.id.nav_continue_answering:
                Intent t= new Intent(HomeActivity.this, OtherAnswersActivity.class);
                startActivity(t);
                break;

        }

        HomeActivity.this.finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
