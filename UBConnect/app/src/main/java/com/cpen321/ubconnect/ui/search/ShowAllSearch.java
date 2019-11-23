package com.cpen321.ubconnect.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import com.cpen321.ubconnect.SearchUserAdapter;
import com.cpen321.ubconnect.model.ErrorHandlingUtils;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.SearchResult;
import com.cpen321.ubconnect.ui.NavigateToOtherAnswers.NavBetweenAnswersActivity;
import com.cpen321.ubconnect.ui.account.AccountActivity;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.postquestion.PostQuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionActivity;
import com.cpen321.ubconnect.ui.viewothers.ViewOnlyOthersAnswerActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ShowAllSearch extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SearchViewModel searchViewModel;

    private RecyclerView recyclerView;

    private ErrorHandlingUtils errorHandlingUtils;

    private String searchItemType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showall);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        searchItemType = getIntent().getExtras().getString("searchItem");
        String question = getIntent().getExtras().getString("question");

        errorHandlingUtils = new ErrorHandlingUtils();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);



        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        String token = ((GlobalVariables) this.getApplication()).getJwt();


        recyclerView = findViewById(R.id.showallRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        observeViewModel();
        searchViewModel.getResults(token,question);
//        updatejljl();
//        RecyclerView.Adapter adapter = new SearchAdapter(searchItems);
//        recyclerView.setAdapter(adapter);
    }

    protected void observeViewModel() {
        searchViewModel.getQuestions().observe(this, this::onChangedResult);
        searchViewModel.getError().observe(this,this::onError);
    }

    public void onError(String err){
        findViewById(R.id.searchLayout).setVisibility(View.GONE);
        errorHandlingUtils.showError(ShowAllSearch.this,err, retryOnClickListener, "Retry", Snackbar.LENGTH_INDEFINITE);
    }

    private View.OnClickListener retryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            errorHandlingUtils.hideError();
            findViewById(R.id.searchLayout).setVisibility(View.VISIBLE);
        }
    };

    public void onChangedResult(SearchResult searchResult){
        // this.questions.addAll(questions);
        if("Question".equals(searchItemType)){
            RecyclerView.Adapter adapter = new SearchQuestionAdapter(searchResult.getQuestions());
            recyclerView.setAdapter(adapter);
        }
        else if("User".equals(searchItemType)){
            RecyclerView.Adapter adapter = new SearchUserAdapter(searchResult.getUsers());
            recyclerView.setAdapter(adapter);
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(ShowAllSearch.this, HomeActivity.class);
            startActivity(intent);
            ShowAllSearch.this.finish();
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
                Intent h= new Intent(ShowAllSearch.this, HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(ShowAllSearch.this, PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(ShowAllSearch.this, SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(ShowAllSearch.this, AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(ShowAllSearch.this, QuestionActivity.class);
                startActivity(s);
                break;
            case R.id.nav_continue_answering:
                Intent t= new Intent(ShowAllSearch.this, ViewOnlyOthersAnswerActivity.class);
                startActivity(t);
                break;
            default:
                break;
        }

        ShowAllSearch.this.finish();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
