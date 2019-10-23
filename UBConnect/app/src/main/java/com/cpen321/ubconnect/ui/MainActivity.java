package com.cpen321.ubconnect.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.AccessTokens;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.viewModel.MainViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {


    private final String TAG = "MainActivity";

    MainViewModel mainViewModel;

    private CallbackManager callbackManager;

    private LoginButton loginButton;

    private Button appLoginButton;
    private Button signupButton;
    private EditText email;
    private EditText password;

    private String fcmtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, SocketStarterActivity.class);
        startActivity(intent);
        MainActivity.this.finish();

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        getLifecycle().addObserver(mainViewModel);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        appLoginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signUpButton);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);



        View.OnClickListener loginOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! checkEmailFormat(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"wrong email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                observeViewModelGet();

            }
        };

        View.OnClickListener signupOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! checkEmailFormat(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"wrong email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                observeViewModelSet(new User());

            }
        };

        appLoginButton.setOnClickListener(loginOnClickListener);
        signupButton.setOnClickListener(signupOnClickListener);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "User has successfully logged in");
                AccessTokens accessTokenFB = new AccessTokens();
                accessTokenFB.setAccess_token(loginResult.getAccessToken().getToken());
                accessTokenFB.setFcmAccessToken(fcmtoken);

                mainViewModel.getAppUserByFB(accessTokenFB);
//                mainViewModel.getAppUserByFB(accessTokenFB);
//                Intent intent = new Intent(MainActivity.this, PostQuestionActivity.class);
//                startActivity(intent);
//                MainActivity.this.finish();

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "User has cancelled the login process");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "Oh no. You have no network or some other problem");
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        fcmtoken = task.getResult().getToken();

                    }
                });

        observeViewModelGetByFB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onStart() {

        super.onStart();

        /*if (AccessToken.getCurrentAccessToken() != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }*/
    }

    protected void observeViewModelGetByFB() {
        Log.d("Fuck", "onChangedUserIdByFB: fuckfuck");
        mainViewModel.getCurrentUserByFB().observe(this,this::onChangedUserIdByFB);
    }

    protected void observeViewModelGet() {
        mainViewModel.getCurrentUser().observe(this, this::onChangedUserIdGet);
    }

    protected void observeViewModelSet(User user) {
        mainViewModel.setCurrentUser(user).observe(this, this::onChangedUserIdSet);
    }

    public void onChangedUserIdByFB(User user){
        Log.d("Fuck", "onChangedUserIdByFB:  " + user.getUserId());
        ((GlobalVariables) this.getApplication()).setUserID(user.getUserId());
        Log.d("Fuck", "onChangedUserIdByFB9999999999999:  " + ((GlobalVariables) this.getApplication()).getUserID());
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public void onChangedUserIdGet(User user){
        // check if the login was successful
        ((GlobalVariables) this.getApplication()).setUserID("dummy");
        Intent intent = new Intent(MainActivity.this, OtherAnswersActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public void onChangedUserIdSet(User user){
        // check if the signup was successful
        ((GlobalVariables) this.getApplication()).setUserID("dummy");
        Intent intent = new Intent(MainActivity.this, OtherAnswersActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public boolean checkEmailFormat(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
