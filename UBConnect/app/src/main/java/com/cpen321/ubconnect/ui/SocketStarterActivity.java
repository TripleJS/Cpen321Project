package com.cpen321.ubconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.ui.otheranswers.OtherAnswersFragment;

public class SocketStarterActivity extends AppCompatActivity {


    private EditText nickname;
    public static final String NICKNAME = "usernickname";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_starter2);

        //call UI components  by id
        Button btn = (Button)findViewById(R.id.enterchat) ;
        nickname = (EditText) findViewById(R.id.nickname);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the nickname is not empty go to chatbox activity and add the nickname to the intent extra


                if(!nickname.getText().toString().isEmpty()){

                    Intent i  = new Intent(SocketStarterActivity.this, OtherAnswersFragment.class);

                    //retreive nickname from EditText and add it to intent extra
                    i.putExtra(NICKNAME,nickname.getText().toString());

                    startActivity(i);
                }
            }
        });

    }
}
