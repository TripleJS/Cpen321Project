package com.cpen321.ubconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.OtherAnswersAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import com.cpen321.ubconnect.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

public class OtherAnswersActivity extends AppCompatActivity {
    private Socket socket;
    private String Nickname ;

    public RecyclerView myRecylerView ;
    public List<Message> MessageList ;
    public OtherAnswersAdapter chatBoxAdapter;
    public  EditText messagetxt ;
    public  Button send ;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_answers);

// get the nickame of the user


        messagetxt = (EditText) findViewById(R.id.message) ;
        send = (Button)findViewById(R.id.send);
        Nickname= (String)getIntent().getExtras().getString(SocketStarterActivity.NICKNAME);

//connect you socket client to the server

        try {


//if you are using a phone device you should connect to same local network as your laptop and disable your pubic firewall as well

            socket = IO.socket("https://ubconnect.azurewebsites.net");

            //create connection

            socket.connect();

// emit the event join along side with the nickname

            socket.emit("join", Nickname);


        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection


                if(!messagetxt.getText().toString().isEmpty()){

                    socket.emit("messagedetection",Nickname,messagetxt.getText().toString());

                    messagetxt.setText(" ");
                }




            }
        });

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");

                            // make instance of message

                            Message m = new Message(nickname,message);


                            //add the message to the messageList

                            MessageList.add(m);

                            // add the new updated list to the adapter
                            chatBoxAdapter = new OtherAnswersAdapter(MessageList);

                            // notify the adapter to update the recycler view

                            chatBoxAdapter.notifyDataSetChanged();

                            //set the adapter for the recycler view

                            myRecylerView.setAdapter(chatBoxAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });

        socket.on("userdisconnect", new Emitter.Listener() {
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
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    }
