package com.cpen321.ubconnect.ui.otheranswers;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.OtherAnswersAdapter;
import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Message;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.ui.NavigateToOtherAnswers.NavBetweenAnswersActivity;
import com.cpen321.ubconnect.ui.account.AccountActivity;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.postquestion.PostQuestionActivity;
import com.cpen321.ubconnect.ui.question.NoQuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionViewModel;
import com.cpen321.ubconnect.ui.search.SearchActivity;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class OtherAnswersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Socket socket;
    private String questionId = "dummyString";
    private String userId;
    private Question questionClass;
    //josh
//    private String roomId;
    private String answer;
//    private String answerId = null;
//josh

    public RecyclerView myRecyclerView;
    public List<Message> MessageList = new ArrayList<Message>();
    public OtherAnswersAdapter otherAnswersAdapter;
    public EditText messagetxt ;
    //    public  Button send ;
    private TextView question;
    private OtherAnswersViewModel otherAnswersViewModel;
    private QuestionViewModel questionViewModel;
    private String token;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private JSONObject textWatcherJSONObject = new JSONObject();
    private JSONObject joinQuestionJSONObject = new JSONObject();


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_answers);

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



        messagetxt = (EditText) findViewById(R.id.myTextBox) ;
        messagetxt.addTextChangedListener(textWatcher);

//        Button saveAnswer = findViewById(R.id.saveChanges);
//        saveAnswer.setOnClickListener(saveAnswerOnClickListener);
        Button submitAnswer = findViewById(R.id.submitAnswer);
        //joshua
//        submitAnswer.setOnClickListener(submitAnswerOnClickListener);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(floatingActionButtonOnClickListener);

        otherAnswersViewModel = ViewModelProviders.of(this).get(OtherAnswersViewModel.class);

        token = ((GlobalVariables) this.getApplication()).getJwt();
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

        //josh


//        socket.on("typing", onTyping);
//
//        socket.on("message", onMessage);
//
//        socket.on("userdisconnect", onUserDisconnect);

}

    private void observeViewModel(){
        otherAnswersViewModel.getQuestionData().observe(this, this::onChangedQuestion);
    }
    private void onChangedQuestion(Question question){
        this.question.setText(question.getQuestion());
        if (questionId.equals("dummyString")) {
            questionId = question.getId();
        }
        if (questionId.equals("")) {
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

//                            String nickname = data.getString("senderNickname");
                            answer = data.getString("answer");
                            messagetxt.setText(answer);
                            messagetxt.addTextChangedListener(textWatcher);
//                            answerId = data.getString("answerId");
                            // HAVE JON SET THIS ANSWEERID AS NEW CURRENT ANSWER
//                            roomId = data.getString("roomId");

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
//josh
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
//joshua
//    private View.OnClickListener submitAnswerOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            try {
//                submitAnswerJSONObject.put("userId", userId);
//                submitAnswerJSONObject.put("messagetxt", messagetxt);
//                submitAnswerJSONObject.put("questionId", questionId);
//            } catch (JSONException e) {
//                Toast.makeText(getApplicationContext(),
//                        "Unexpected Error. Please try again later.",
//                        Toast.LENGTH_LONG)
//                        .show();
//                e.printStackTrace();
//            }
//            socket.emit("submitAnswer", submitAnswerJSONObject);
//            Intent intent = new Intent(OtherAnswersActivity.this, HomeActivity.class);
//            startActivity(intent);
//            OtherAnswersActivity.this.finish();
//            //have jon take this answer out of recent and store in data base
//        }
//    };
//    private View.OnClickListener saveAnswerOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            try {
//                saveAnswerJSONObject.put("userId", userId);
//                saveAnswerJSONObject.put("messagetxt", messagetxt);
//            } catch (JSONException e) {
//                Toast.makeText(getApplicationContext(),
//                        "Unexpected Error. Please try again later.",
//                        Toast.LENGTH_LONG)
//                        .show();
//                e.printStackTrace();
//            }
//            socket.emit("saveAnswer", saveAnswerJSONObject);
//            //store in data base
//
//        }
//    };
    //josh

    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            // to do
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // to do
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

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // to do
                }
            });
        }
    };

    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // to do


                }
            });
        }
    };

    private Emitter.Listener onUserDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];

                    Toast.makeText(OtherAnswersActivity.this,data,Toast.LENGTH_SHORT).show();

                }
            });
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

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
