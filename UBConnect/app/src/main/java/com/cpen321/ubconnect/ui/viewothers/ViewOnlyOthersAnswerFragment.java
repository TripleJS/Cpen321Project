package com.cpen321.ubconnect.ui.viewothers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cpen321.ubconnect.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class ViewOnlyOthersAnswerFragment extends Fragment {
    public TextView messagetxt ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_view_only_other_answer, container, false);

        messagetxt = (TextView) root.findViewById(R.id.myTextBox1);
        Socket socket = null;
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

//                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");
                            TextView myTextBox1 = (TextView) root.findViewById(R.id.myTextBox1);
                            myTextBox1.setText(message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        return root;
    }
}
