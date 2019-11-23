package com.cpen321.ubconnect.ui.viewothers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.fcmservice.MessagingService;
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
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class ViewOnlyOthersAnswerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public TextView messagetxt ;
//josh how to make global variable, get recent question, make sure that buttons get deleted /created by id by checking socket
    private String questionId;
//    private String roomId;
    private String userId;
    private String userAnsweringId;
    private String answerId;
    private TextView question;
    private TextView userAnswering;
    private OtherAnswersViewModel otherAnswersViewModel;
    private QuestionViewModel questionViewModel;
    private String token;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private JSONObject joinViewAnswerJSONObject = new JSONObject();

    //josh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_only_other_answer);
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



//        questionId = getIntent().getExtras().getString("arg");
//        userAnsweringId = getIntent().getExtras().getString("userAnsweringId");
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            questionId = bundle.getString("questionId");
            userAnsweringId = bundle.getString("userId");
        }
        else {
//            MessagingService messagingService = new MessagingService();
//            questionId = messagingService.getFCMQuestionId();
//            userAnsweringId = messagingService.getFCMUserAnsweringId();
        }


//        questionId = getIntent().getExtras().getString("arg");
//        roomId = getIntent().getExtras().getString("roomId");
//        userAnsweringId = getIntent().getExtras().getString("userAnsweringId");
//        answer = getIntent().getExtras().getString("answer");
        userId = ((GlobalVariables) this.getApplication()).getUserID();

        otherAnswersViewModel = ViewModelProviders.of(this).get(OtherAnswersViewModel.class);

        token = ((GlobalVariables) this.getApplication()).getJwt();
        //connect you socket client to the server
        question = findViewById(R.id.questionView);

        userAnswering = findViewById(R.id.textView1);


        //josh

        messagetxt = (TextView) findViewById(R.id.myTextBox1);


        try {
            joinViewAnswerJSONObject.put("userId", userId);
            joinViewAnswerJSONObject.put("userAnsweringId", userAnsweringId);
            joinViewAnswerJSONObject.put("questionId", questionId);
            //jon should make room name questionid +useranwerinID

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

            socket.emit("joinViewAnswer", joinViewAnswerJSONObject);


        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Could not connect to server",
                    Toast.LENGTH_LONG)
                    .show();

        }
//josh
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

//                            String user = data.getString("senderNickname");
                            String message = data.getString("message");
                            TextView myTextBox1 = (TextView) findViewById(R.id.myTextBox1);
                            myTextBox1.setText(message);
                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();
                            Log.d("received", "time received" + ts);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Could not connect to server",
                                    Toast.LENGTH_LONG)
                                    .show();

                        }
                    }
                });
            }
        });
        observeViewModel();
        otherAnswersViewModel.getQuestionById(questionId, token);
        userAnswering.setText("Answer From" );
        //+ otherAnswersViewModel.getUserNameById(userAnsweringId, token)
    }
    private void observeViewModel(){
        otherAnswersViewModel.getQuestionData().observe(this, this::onChangedQuestion);
    }
    private void onChangedQuestion(Question question){
        this.question.setText(question.getQuestion());
    }

    //josh


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
                Intent h= new Intent(ViewOnlyOthersAnswerActivity.this, HomeActivity.class);
                startActivity(h);
                break;
            case R.id.nav_post_question:
                Intent i= new Intent(ViewOnlyOthersAnswerActivity.this, PostQuestionActivity.class);
                startActivity(i);
                break;
            case R.id.nav_search:
                Intent g= new Intent(ViewOnlyOthersAnswerActivity.this, SearchActivity.class);
                startActivity(g);
                break;
            case R.id.nav_profile:
                Intent k= new Intent(ViewOnlyOthersAnswerActivity.this, AccountActivity.class);
                startActivity(k);
                break;
            case R.id.nav_current_question:
                Intent s= new Intent(ViewOnlyOthersAnswerActivity.this, QuestionActivity.class);
                startActivity(s);
                break;
            case R.id.nav_continue_answering:
                Intent t= new Intent(ViewOnlyOthersAnswerActivity.this, OtherAnswersActivity.class);
                startActivity(t);
                break;

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
