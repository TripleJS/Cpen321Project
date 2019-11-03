package com.cpen321.ubconnect.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cpen321.ubconnect.Main2Activity;
import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.AESCrypt;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.AccessTokens;
import com.cpen321.ubconnect.model.data.User;
import com.facebook.AccessToken;
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get("qid")!= null) {
            //here can get notification message
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            intent.putExtra("qid", bundle.get("qid").toString());
            startActivity(intent);
            MainActivity.this.finish();
        }

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

                try {
                    String encrypted = AESCrypt.encrypt(password.getText().toString());
                    User user = new User();
                    user.setEmail(email.getText().toString());
                    user.setEncryptedPassword(encrypted);
                    mainViewModel.getAppUser(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        View.OnClickListener signupOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! checkEmailFormat(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"wrong email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String encrypted = AESCrypt.encrypt(password.getText().toString());
                    User user = new User();
                    user.setEmail(email.getText().toString());
                    user.setEncryptedPassword(encrypted);
                    mainViewModel.setAppUser(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        appLoginButton.setOnClickListener(loginOnClickListener);
        signupButton.setOnClickListener(signupOnClickListener);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessTokens accessTokenFB = new AccessTokens();
                accessTokenFB.setAccess_token(loginResult.getAccessToken().getToken());
                accessTokenFB.setFcmAccessToken(fcmtoken);

                mainViewModel.getAppUserByFB(accessTokenFB);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException exception) {}
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

        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            AccessTokens accessTokenFB = new AccessTokens();
            accessTokenFB.setAccess_token(token.getToken());
            accessTokenFB.setFcmAccessToken(fcmtoken);
            mainViewModel.getAppUserByFB(accessTokenFB);
        }
    }

    protected void observeViewModelGetByFB() {
        mainViewModel.getCurrentUserByFB().observe(this,this::onChangedUserIdByFB);
    }

    public void onChangedUserIdByFB(User user){
        ((GlobalVariables) this.getApplication()).setUserID(user.getUserId());

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.authorization), user.getJwt());
        editor.commit();

        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
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
