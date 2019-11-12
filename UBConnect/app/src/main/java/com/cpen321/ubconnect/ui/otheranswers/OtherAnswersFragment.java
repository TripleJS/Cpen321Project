package com.cpen321.ubconnect.ui.otheranswers;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.OtherAnswersAdapter;
import com.cpen321.ubconnect.R;
import com.cpen321.ubconnect.model.GlobalVariables;
import com.cpen321.ubconnect.model.data.Message;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.ui.viewothers.ViewOnlyOthersAnswerFragment;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class OtherAnswersFragment extends Fragment {

    private Socket socket;
    private String questionId;
    private String userId;

    public RecyclerView myRecyclerView;
    public List<Message> MessageList = new ArrayList<Message>();
    public OtherAnswersAdapter otherAnswersAdapter;
    public EditText messagetxt ;
    //    public  Button send ;
    private TextView question;
    private OtherAnswersViewModel otherAnswersViewModel;
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_other_answers, container, false);

        messagetxt = (EditText) root.findViewById(R.id.myTextBox) ;

        questionId = getArguments().getString("arg");

        FloatingActionButton floatingActionButton = root.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(floatingActionButtonOnClickListener);

        otherAnswersViewModel = ViewModelProviders.of(this).get(OtherAnswersViewModel.class);

        token = ((GlobalVariables) getActivity().getApplication()).getJwt();
        //connect you socket client to the server
        question = root.findViewById(R.id.QuestioToAnswer);
        userId = ((GlobalVariables) getActivity().getApplication()).getUserID();

        try {
            //if you are using a phone device you should connect to same local
            //network as your laptop and disable your pubic firewall as well
            socket = IO.socket("https://ubconnect.azurewebsites.net");

            //create connection
            socket.connect();

            // emit the event join along side with the nickname
            socket.emit("join", userId, questionId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        messagetxt.addTextChangedListener(textWatcher);


        socket.on("typing", onTyping);

        socket.on("message", onMessage);

        socket.on("userdisconnect", onUserDisconnect);

        observeViewModel();
        otherAnswersViewModel.getQuestionById(questionId, token);

        return root;
    }


    private void observeViewModel(){
        otherAnswersViewModel.getQuestionData().observe(this, this::onChangedQuestion);
    }

    private void onChangedQuestion(Question question){
        this.question.setText(question.getQuestion());
    }

    private View.OnClickListener floatingActionButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle=new Bundle();
            bundle.putString("arg", questionId);
            ViewOnlyOthersAnswerFragment viewOnlyOthersAnswerFragment = new ViewOnlyOthersAnswerFragment();
            viewOnlyOthersAnswerFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(((ViewGroup)getView().getParent()).getId(), viewOnlyOthersAnswerFragment).commit();
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            // to do
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // to do
        }
        public void onTextChanged(CharSequence s, int start,
        int before, int count) {

            socket.emit("messagedetection", userId, s);
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
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
            getActivity().runOnUiThread(new Runnable() {
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];

                    Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();

                }
            });
        }
    };

}
