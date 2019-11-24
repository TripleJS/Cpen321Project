package com.cpen321.ubconnect.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.ErrorHandlingUtils;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.AccessTokens;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.cpen321.ubconnect.ui.viewothers.ViewOnlyOthersAnswerActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {


    private final String TAG = "MainActivity";

    private MainViewModel mainViewModel;

    private CallbackManager callbackManager;

    private EditText email;
    private EditText password;
    private EditText username;

    private TextView usernameTV;


    private String fcmtoken;

    private ErrorHandlingUtils errorHandlingUtils;

    private Button loginSignup;
    private Button appLoginButton;
    private Button signupButton;

    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        bundleHandling(getIntent().getExtras());

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        getLifecycle().addObserver(mainViewModel);

        callbackManager = CallbackManager.Factory.create();

        errorHandlingUtils = new ErrorHandlingUtils();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        appLoginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signUpButton);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        username = findViewById(R.id.username);
        loginSignup = findViewById(R.id.Login2);

        usernameTV = findViewById(R.id.usernameTV);

        appLoginButton.setOnClickListener(pageOnClickListener);
        signupButton.setOnClickListener(pageOnClickListener);
        loginSignup.setOnClickListener(loginSignupOnClickListener);

        appLoginButton.setBackground(appLoginButton.getContext().getDrawable(R.drawable.border));
        signupButton.setBackground(signupButton.getContext().getDrawable(R.drawable.border2));

        username.setVisibility(View.GONE);
        usernameTV.setVisibility(View.GONE);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessTokens accessTokenFB = new AccessTokens();
                accessTokenFB.setAccessToken(loginResult.getAccessToken().getToken());
                accessTokenFB.setFcmAccessToken(fcmtoken);

                mainViewModel.getAppUserByFB(accessTokenFB);
            }

            @Override
            public void onCancel() {
                // to do
            }

            @Override
            public void onError(FacebookException exception) {
                // to do
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
                        AccessToken token = AccessToken.getCurrentAccessToken();
                        if (token != null) {
                            AccessTokens accessTokenFB = new AccessTokens();
                            accessTokenFB.setAccessToken(token.getToken());
                            accessTokenFB.setFcmAccessToken(fcmtoken);
                            mainViewModel.getAppUserByFB(accessTokenFB);
                        }
                    }
                });

        observeViewModelGetByFB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void observeViewModelGetByFB() {
        mainViewModel.getCurrentUserByFB().observe(this,this::onChangedUserIdByFB);
        mainViewModel.getError().observe(this,this::onError);
    }

    public void onError(Pair<Integer,String> err){
        if(!(err.first == 403 || err.first == 404)){
            findViewById(R.id.mainLayoutLayout).setVisibility(View.GONE);
            errorHandlingUtils.showError(MainActivity.this,err.second, retryOnClickListener, "Retry", Snackbar.LENGTH_INDEFINITE);
        }

        errorHandlingUtils.showError(MainActivity.this,err.second, null, "Retry", Snackbar.LENGTH_LONG);

    }

    private View.OnClickListener retryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            errorHandlingUtils.hideError();
            LoginManager.getInstance().logOut();
            findViewById(R.id.mainLayoutLayout).setVisibility(View.VISIBLE);
            email.setText("");
            password.setText("");
        }
    };

    public void onChangedUserIdByFB(User user){
        ((GlobalVariables) this.getApplication()).setUserID(user.getUserId());
        ((GlobalVariables) this.getApplication()).setJwt(user.getJwt());

        bundleHandling(getIntent().getExtras());
        if(!loggedIn){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }

    public boolean checkEmailFormat(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean checkPassword(String password) {
        String expression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void bundleHandling(Bundle bundle) {
        if (bundle != null && bundle.get("questionId") != null && bundle.get("userId") != null) {
            //here can get notification message
            loggedIn = true;
            Log.d("not", "bundleHandling: " + bundle.get("questionId").toString());
            Log.d("not", "bundleHandling: " + bundle.get("userId"));
            Intent intent = new Intent(MainActivity.this, ViewOnlyOthersAnswerActivity.class);
            intent.putExtra("questionId", bundle.get("questionId").toString());
            intent.putExtra("userId", bundle.get("userId").toString());
            startActivity(intent);
            MainActivity.this.finish();

        }
    }


    private View.OnClickListener pageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            if(button.getText().toString().equals("Login")){
                appLoginButton.setBackground(appLoginButton.getContext().getDrawable(R.drawable.border));
                signupButton.setBackground(signupButton.getContext().getDrawable(R.drawable.border2));
                loginSignup.setText("Login");
                username.setVisibility(View.GONE);
                usernameTV.setVisibility(View.GONE);
            }
            else {
                loginSignup.setText("Sign Up");
                appLoginButton.setBackground(appLoginButton.getContext().getDrawable(R.drawable.border2));
                signupButton.setBackground(signupButton.getContext().getDrawable(R.drawable.border));
                username.setVisibility(View.VISIBLE);
                usernameTV.setVisibility(View.VISIBLE);
            }
        }
    };


    private View.OnClickListener loginSignupOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(notValid() && !loginSignup.getText().equals("Login")){
                Toast.makeText(getApplicationContext(),"Please Complete All The Fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (! checkEmailFormat(email.getText().toString())) {
                Toast.makeText(getApplicationContext(),"wrong email format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (! checkPassword(password.getText().toString())) {
                Toast.makeText(getApplicationContext(),"Password must contain at least:\n8 characters, a digit, no spaces and both lower and upper case letters", Toast.LENGTH_LONG).show();
                return;
            }


            try {
                //String encrypted = AESCrypt.encrypt(password.getText().toString());
                User user = new User();
                user.setEmail(email.getText().toString());
                user.setUserName(username.getText().toString());
                user.setPassword(password.getText().toString());
                if(loginSignup.getText().toString().equals("Login")){
                    mainViewModel.getAppUser(user);
                }
                else {
                    mainViewModel.setAppUser(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private boolean notValid(){
        return username.getText().toString().equals("") || password.getText().toString().equals("") || email.getText().toString().equals("");
    }

}
