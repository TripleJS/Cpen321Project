package com.cpen321.ubconnect.ui.publicaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.cpen321.ubconnect.model.data.PublicUser;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.model.data.UserReportRate;
import com.cpen321.ubconnect.ui.account.AccountActivity;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.postquestion.PostQuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionActivity;
import com.cpen321.ubconnect.ui.search.SearchActivity;
import com.cpen321.ubconnect.ui.viewothers.ViewOnlyOthersAnswerActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class PublicUserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private PublicUserViewModel publicUserViewModel;
    private String token;
    private String userId;
    private String publicUserId;

    private String state;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private RatingBar stars;
    private TextView publicUN;
    private TextView publicUInf;

    private Button report;



    private ErrorHandlingUtils errorHandlingUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicuser);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        errorHandlingUtils = new ErrorHandlingUtils();

        stars = findViewById(R.id.publicStar);
        publicUN = findViewById(R.id.publicUserName);
        publicUInf = findViewById(R.id.publicUserInfo);
        state = "NA";



        Button rate = findViewById(R.id.publicRate);
        report = findViewById(R.id.publicReport);

        token = ((GlobalVariables) this.getApplication()).getJwt();
        userId = ((GlobalVariables) this.getApplication()).getUserID();
        publicUserId = getIntent().getExtras().getString("publicUser");

        publicUserViewModel = ViewModelProviders.of(this).get(PublicUserViewModel.class);

        View.OnClickListener rateOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars.getRating();
                state = "rated";
                User user = new User();
                user.setUserId(publicUserId);
                user.setRating(stars.getRating());
                publicUserViewModel.rate(userId,token,user);
            }
        };

        View.OnClickListener reportOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "reported";
                User user = new User();
                user.setUserId(publicUserId);
                publicUserViewModel.report(userId,token,user);
//                User user = new User();
//                user.setUserId(userId);
//                publicUserViewModel.report(publicUserId,token,user);
            }
        };

        rate.setOnClickListener(rateOnClickListener);
        report.setOnClickListener(reportOnClickListener);
        observeViewModel();

        publicUserViewModel.getPublicUser(publicUserId,token);

    }

    protected void observeViewModel() {
        publicUserViewModel.getPublicUserObserve().observe(this, this::onChangeUser);
        publicUserViewModel.getError().observe(this,this::onError);
    }

    public void onError(String err){
        findViewById(R.id.publicuserLayout).setVisibility(View.GONE);
        errorHandlingUtils.showError(PublicUserActivity.this,err, retryOnClickListener, "Retry", Snackbar.LENGTH_INDEFINITE);
    }

    private View.OnClickListener retryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            errorHandlingUtils.hideError();
            findViewById(R.id.publicuserLayout).setVisibility(View.VISIBLE);
            publicUserViewModel.getPublicUser(publicUserId,token);

        }
    };

    private void onChangeUser(User user){
        publicUN.setText(user.getUserName());
        publicUInf.setText("Courses: " + user.getCourses().toString() + "\n" + "Rating: " + user.getRating());

        for(UserReportRate ruser : user.getUsersWhoRated()){
            if(ruser.getId().equals(userId)){
                stars.setRating(ruser.getRating());
            }
        }

        if(report.getText().toString().equals("Reported")){
            report.setEnabled(false);
        }

        for(String puserId : user.getUsersWhoReported()){
            if(puserId.equals(userId)){
                report.setText("Reported");
                report.setEnabled(false);
            }
        }

        if(state.equals("rated")){
            Toast.makeText(getApplicationContext(),"rated", Toast.LENGTH_SHORT).show();
        }
        else if (state.equals("reported")){
            Toast.makeText(getApplicationContext(),"reported", Toast.LENGTH_SHORT).show();
        }
        state = "NA";

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
                Intent h= new Intent(PublicUserActivity.this, HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(PublicUserActivity.this,PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(PublicUserActivity.this, SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(PublicUserActivity.this, AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(PublicUserActivity.this, QuestionActivity.class);
                startActivity(s);
                break;
            case R.id.nav_continue_answering:
                Intent t= new Intent(PublicUserActivity.this, ViewOnlyOthersAnswerActivity.class);
                startActivity(t);
                break;

        }

        PublicUserActivity.this.finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
