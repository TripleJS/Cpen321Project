package com.cpen321.ubconnect.ui.otheranswers;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.HelperUtils;

import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.ui.navigatetootheranswers.NavBetweenAnswersActivity;
import com.cpen321.ubconnect.ui.account.AccountActivity;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.postquestion.PostQuestionActivity;
import com.cpen321.ubconnect.ui.question.NoQuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionActivity;

import com.cpen321.ubconnect.ui.search.SearchActivity;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class OtherAnswersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Socket socket;
    private String questionId;
    private String userId;



    public EditText messagetxt ;

    private TextView question;
    private OtherAnswersViewModel otherAnswersViewModel;




    private JSONObject textWatcherJSONObject = new JSONObject();
    private JSONObject joinQuestionJSONObject = new JSONObject();


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_answers);

        questionId = "dummyString";

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



        messagetxt = (EditText) findViewById(R.id.myTextBox);
        messagetxt.addTextChangedListener(textWatcher);


        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(floatingActionButtonOnClickListener);

        otherAnswersViewModel = ViewModelProviders.of(this).get(OtherAnswersViewModel.class);

        String token = ((GlobalVariables) this.getApplication()).getJwt();
        userId = ((GlobalVariables) this.getApplication()).getUserID();
        //connect you socket client to the server
        question = findViewById(R.id.QuestioToAnswer);


        //josh

        observeViewModel();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            questionId = bundle.getString("arg");
            otherAnswersViewModel.getQuestionById(questionId, token);
        }
        else {
            otherAnswersViewModel.getRecentQuestionToAnswerId(token, userId);
        }

}

    private void observeViewModel(){
        otherAnswersViewModel.getQuestionData().observe(this, this::onChangedQuestion);
    }
    private void onChangedQuestion(Question question){
        this.question.setText(question.getQuestion());
        if ("dummyString".equals(questionId)) {
            questionId = question.getId();
        }
        if ("".equals(questionId)) {
            Intent intent = new Intent(OtherAnswersActivity.this, NoQuestionActivity.class);
            startActivity(intent);
            OtherAnswersActivity.this.finish();
        }
        mainSocketMethod();
    }
//joshua
    private void mainSocketMethod(){

        try {
            joinQuestionJSONObject.put("userId", userId);
            joinQuestionJSONObject.put("questionId", questionId);

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),
                    "Unexpected Error. Please try again later.",
                    Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }
        try {
            //if you are using a phone device you should connect to same local
            //network as your laptop and disable your pubic firewall as well
            socket = IO.socket("http://168.62.166.42:3000/");
//            "https://ubconnect.azurewebsites.net"

            //create connection
            socket.connect();

            // emit the event join along side with the nickname
            // josh

            socket.emit("joinQuestion", joinQuestionJSONObject);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Could not connect to server",
                    Toast.LENGTH_LONG)
                    .show();
        }


        //josh
        socket.on("create", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

//
                            String answer = data.getString("answer");
                            messagetxt.setText(answer);
//

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



    }

    private View.OnClickListener floatingActionButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(OtherAnswersActivity.this, NavBetweenAnswersActivity.class);
            intent.putExtra("arg",questionId);
            intent.putExtra("userAnsweringId", userId);
            startActivity(intent);
            OtherAnswersActivity.this.finish();
        }
    };


    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            // nothing to do
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // nothing to do
        }
        //josh
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

//josh
            try {
                textWatcherJSONObject.put("userId", userId);
                textWatcherJSONObject.put("currentSequence", s);
                textWatcherJSONObject.put("questionId", questionId);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),
                        "Unexpected Error. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();
                e.printStackTrace();
            }
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            Log.d("emitted", "time started " + ts);
            socket.emit("messagedetection", textWatcherJSONObject);
        }
    };

    private DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            // nothing to do
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            HelperUtils.hideKeyboard(OtherAnswersActivity.this);
            messagetxt.setCursorVisible(false);
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            messagetxt.setCursorVisible(true);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            // nothing to do
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(OtherAnswersActivity.this, HomeActivity.class);
            startActivity(intent);
            OtherAnswersActivity.this.finish();
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
                Intent h= new Intent(OtherAnswersActivity.this, HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(OtherAnswersActivity.this, PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(OtherAnswersActivity.this, SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(OtherAnswersActivity.this, AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(OtherAnswersActivity.this, QuestionActivity.class);
                startActivity(s);
                break;
            case R.id.nav_continue_answering:
                Intent t= new Intent(OtherAnswersActivity.this, OtherAnswersActivity.class);
                startActivity(t);
                break;
            default:
                break;

        }

        OtherAnswersActivity.this.finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.close();
        messagetxt.removeTextChangedListener(textWatcher);
    }
}
