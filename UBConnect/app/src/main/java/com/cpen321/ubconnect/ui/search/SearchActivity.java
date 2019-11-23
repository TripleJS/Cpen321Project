package com.cpen321.ubconnect.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

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
import com.cpen321.ubconnect.SearchAdapter;
import com.cpen321.ubconnect.model.ErrorHandlingUtils;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.HelperUtils;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.SearchItem;
import com.cpen321.ubconnect.model.data.SearchResult;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.ui.account.AccountActivity;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.postquestion.PostQuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionActivity;
import com.cpen321.ubconnect.ui.viewothers.ViewOnlyOthersAnswerActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SearchViewModel searchViewModel;

    private RecyclerView recyclerView;
    private List<SearchItem> searchItems;
    private String token;
    private EditText search;
    private Button searchButton;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private ErrorHandlingUtils errorHandlingUtils;
    private String question;

    private RecyclerView.Adapter adapter;

    private long start;
    private long end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                HelperUtils.hideKeyboard(SearchActivity.this);
                search.setCursorVisible(false);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                search.setCursorVisible(true);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mDrawerToggle.syncState();

        errorHandlingUtils = new ErrorHandlingUtils();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        search = findViewById(R.id.searchViewView);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(searchOnClickListener);
        search.setText("");

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        token = ((GlobalVariables) this.getApplication()).getJwt();
        searchItems = new ArrayList<>();
        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        observeViewModel();
//        updatejljl();
//        RecyclerView.Adapter adapter = new SearchAdapter(searchItems);
//        recyclerView.setAdapter(adapter);
    }

    public void updatejljl(){
        // begin
        SearchItem<Question> questionSearchItem = new SearchItem<>();
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question();
        q1.setQuestionTitle("q1");
        Date d1 = new Date();
        q1.setDate(d1);
        q1.setOwner("gg");
        q1.setQuestion("joojpp");
        q1.setId("qqqqqqqqqqqqqqqq");
        questions.add(q1);
        Question q2 = new Question();
        q2.setQuestionTitle("q2");
        Date d2 = new Date();
        q2.setDate(d2);
        q2.setOwner("gg222222");
        q2.setQuestion("joojpp22222222");
        q2.setId("wwwwwwwwwwwwwwwwwwww");
        questions.add(q2);
        Question q3 = new Question();
        q3.setQuestionTitle("q3");
        Date d3 = new Date();
        q3.setDate(d3);
        q3.setOwner("gg3333333");
        q3.setQuestion("joojpp33333333");
        q3.setId("eeeeeeeeeeeeeeeee");
        questions.add(q3);
        questionSearchItem.setSearchItem(questions);
        questionSearchItem.setType("Question");
        searchItems.add(questionSearchItem);

        SearchItem<User> userSearchItem = new SearchItem<>();
        List<User> users = new ArrayList<>();
        User u1 = new User();
        List<String> c1 = new ArrayList<>();
        c1.add("cpen221");
        c1.add("cpen211");
        u1.setCourses(c1);
        u1.setUserName("fuck");
        users.add(u1);
        User u2 = new User();
        List<String> c2 = new ArrayList<>();
        c2.add("cpen221");
        c2.add("cpen211");
        u2.setCourses(c2);
        u2.setUserName("fuck2");
        users.add(u2);
        User u3 = new User();
        List<String> c3 = new ArrayList<>();
        c3.add("cpen221");
        c3.add("cpen211");
        u3.setCourses(c3);
        u3.setUserName("fuck");
        users.add(u3);
        userSearchItem.setSearchItem(users);
        userSearchItem.setType("User");
        searchItems.add(userSearchItem);
    }

    protected void observeViewModel() {
        searchViewModel.getQuestions().observe(this, this::onChangedResult);
        searchViewModel.getError().observe(this,this::onError);
    }

    public void onError(String err){
        findViewById(R.id.searchLayout).setVisibility(View.GONE);
        errorHandlingUtils.showError(SearchActivity.this,err, retryOnClickListener, "Retry", Snackbar.LENGTH_INDEFINITE);
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
        SearchItem<Question> questionSearchItem = new SearchItem<>();
        questionSearchItem.setSearchItem(searchResult.getQuestions());
        questionSearchItem.setType("Question");
        searchItems.add(questionSearchItem);
        setupListener("Question");

        SearchItem<User> userSearchItem = new SearchItem<>();
        userSearchItem.setSearchItem(searchResult.getUsers());
        userSearchItem.setType("User");
        searchItems.add(userSearchItem);
        setupListener("User");

        adapter = new SearchAdapter(searchItems, question);
        recyclerView.setAdapter(adapter);

        end = System.currentTimeMillis()/1000;
        Log.d("Search", "onSearchResult: " + end);
        Log.d("Search", "time took: " + (end - start));
    }

    private void setupListener(String searchItem){
        Button showAll = findViewById(R.id.searchitemShowAll);
        if(searchItem.equals("Question")){

        }
        else if(searchItem.equals("User")){

        }
    }

    private View.OnClickListener searchItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener showAllOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener searchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            start = System.currentTimeMillis()/1000;
            Log.d("Search", "onClick: " + start);
            int size = searchItems.size();
            for(int i = 0; i < size; i++){
                searchItems.remove(0);
                adapter.notifyItemRemoved(0);
            }
            question = search.getText().toString();
            searchViewModel.getResults(token,question);
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
                Intent h= new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(SearchActivity.this,PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(SearchActivity.this, SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(SearchActivity.this, AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(SearchActivity.this, QuestionActivity.class);
                startActivity(s);
                break;
            case R.id.nav_continue_answering:
                Intent t= new Intent(SearchActivity.this, ViewOnlyOthersAnswerActivity.class);
                startActivity(t);
                break;

        }

        SearchActivity.this.finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
