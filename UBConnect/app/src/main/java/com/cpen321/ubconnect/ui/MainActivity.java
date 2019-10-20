package com.cpen321.ubconnect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.SearchQuestionAdapter;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.viewModel.MainViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.List;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        MainActivity.this.finish();

        mainViewModel = new MainViewModel();

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

            }
        };

        View.OnClickListener signupOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! checkEmailFormat(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"wrong email format", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        };

        appLoginButton.setOnClickListener(loginOnClickListener);
        signupButton.setOnClickListener(signupOnClickListener);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "User has successfully logged in");
                observeViewModel(loginResult.getAccessToken());
                Intent intent = new Intent(MainActivity.this, OtherAnswersActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
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

    protected void observeViewModel(AccessToken accessToken) {
        mainViewModel.getCurrentUser(accessToken).observe(this, this::onChangedUserId);
    }

    public void onChangedUserId(User user){
        ((GlobalVariables) this.getApplication()).setUserID("dummy");
    }

    public boolean checkEmailFormat(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
