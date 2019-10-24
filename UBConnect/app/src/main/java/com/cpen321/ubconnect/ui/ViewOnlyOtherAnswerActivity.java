package com.cpen321.ubconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.OtherAnswersAdapter;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import com.cpen321.ubconnect.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ViewOnlyOtherAnswerActivity extends AppCompatActivity {

    private Socket socket;
    private String message;
    private String nickname;


    public  TextView messagetxt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_only_other_answer);


        messagetxt = (TextView) findViewById(R.id.myTextBox1);
        try {


//if you are using a phone device you should connect to same local network as your laptop and disable your pubic firewall as well

            socket = IO.socket("https://ubconnect.azurewebsites.net");

            //create connection

            socket.connect();


        } catch (URISyntaxException e) {
            e.printStackTrace();

        }

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

//                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");
                            TextView myTextBox1 = (TextView) findViewById(R.id.myTextBox1);
                            myTextBox1.setText(message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
