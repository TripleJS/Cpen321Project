package com.cpen321.ubconnect.ui.NavigateToOtherAnswers;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.ui.account.AccountActivity;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.otheranswers.OtherAnswersActivity;
import com.cpen321.ubconnect.ui.otheranswers.OtherAnswersViewModel;
import com.cpen321.ubconnect.ui.postquestion.PostQuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionViewModel;
import com.cpen321.ubconnect.ui.search.SearchActivity;
import com.cpen321.ubconnect.ui.viewothers.ViewOnlyOthersAnswerActivity;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class NavBetweenAnswersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String questionId;
    //josh
//    private JSONArray answersId;
    private JSONArray userAnsweringId;
    private String userId;
    private LinearLayout linearLayout;


    private TextView question;
    private TextView userAnswering;
    private OtherAnswersViewModel otherAnswersViewModel;
    private String token;
    private QuestionViewModel questionViewModel;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private JSONObject joinNavAnswerJSONObject = new JSONObject();

    //josh how am i gunna get question id from drawer????

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_between_answers);
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

        questionId = getIntent().getExtras().getString("arg");
        userId = ((GlobalVariables) this.getApplication()).getUserID();


        otherAnswersViewModel = ViewModelProviders.of(this).get(OtherAnswersViewModel.class);

        token = ((GlobalVariables) this.getApplication()).getJwt();
        //connect you socket client to the server
        question = findViewById(R.id.question);
        observeViewModel();




        //josh
        try {
            joinNavAnswerJSONObject.put("userId", userId);
            joinNavAnswerJSONObject.put("questionId", questionId);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),
                    "Unexpected Error. Please try again later.",
                    Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }

        Socket socket = null;
        try {


//if you are using a phone device you should connect to same local network as your laptop and disable your pubic firewall as well

            socket = IO.socket("http://168.62.166.42:3000/");
            //"https://ubconnect.azurewebsites.net"

            //create connection

            socket.connect();
            socket.emit("joinNavAnswers", joinNavAnswerJSONObject);
            // make this room id the userId cuz only want user to see this


        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Could not connect to server",
                    Toast.LENGTH_LONG)
                    .show();

        }
//josh
        socket.on("getUserAnswering", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

//                            String user = data.getString("senderNickname");
//                            answersId = data.getJSONArray("answersId");
                            userAnsweringId = data.getJSONArray("userAnswering");
                            linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

                            Button[] btnArray = new Button[userAnsweringId.length()];
                            for (int i = 0; i < userAnsweringId.length(); i++) {
                                btnArray[i] = new Button(getApplicationContext());
                                //otherAnswersViewModel.getUserNameById(userAnsweringId.get(i).toString(), token).getValue().getUserId()
                                btnArray[i].setText((userAnsweringId.get(i).toString()));
                                btnArray[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                linearLayout.addView(btnArray[i]);
                                btnArray[i].setOnClickListener(handleOnClick(btnArray[i]));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Could not connect to server",
                                    Toast.LENGTH_LONG)
                                    .show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        otherAnswersViewModel.getQuestionById(questionId, token);


    }
    private void observeViewModel(){
        otherAnswersViewModel.getQuestionData().observe(this, this::onChangedQuestion);

    }

    private void onChangedQuestion(Question question){
        this.question.setText(question.getQuestion());
    }

    View.OnClickListener handleOnClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NavBetweenAnswersActivity.this, ViewOnlyOthersAnswerActivity.class);
                intent.putExtra("arg",questionId);
                intent.putExtra("userAnsweringId",button.getText().toString());
                startActivity(intent);
                NavBetweenAnswersActivity.this.finish();
            }
        };
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(NavBetweenAnswersActivity.this, HomeActivity.class);
            startActivity(intent);
            NavBetweenAnswersActivity.this.finish();
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
                Intent h= new Intent(NavBetweenAnswersActivity.this, HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(NavBetweenAnswersActivity.this, PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(NavBetweenAnswersActivity.this, SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(NavBetweenAnswersActivity.this, AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(NavBetweenAnswersActivity.this, QuestionActivity.class);
                startActivity(s);
                break;
            case R.id.nav_continue_answering:
                Intent t= new Intent(NavBetweenAnswersActivity.this, OtherAnswersActivity.class);
                startActivity(t);
                break;

        }

        NavBetweenAnswersActivity.this.finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

