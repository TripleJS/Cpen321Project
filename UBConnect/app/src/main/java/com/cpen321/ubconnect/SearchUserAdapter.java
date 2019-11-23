package com.cpen321.ubconnect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.ui.publicaccount.PublicUserActivity;
import com.cpen321.ubconnect.ui.question.QuestionFragment;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private List<User> searchUserItems;

    public SearchUserAdapter(List<User> searchUserItems) {
        this.searchUserItems = searchUserItems;
    }

    @NonNull
    @Override
    public SearchUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_item, viewGroup, false);

        return new SearchUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserAdapter.ViewHolder viewHolder, int i) {

        User user = searchUserItems.get(i);
        viewHolder.userName.setText(user.getUserName());
        viewHolder.userInfo.setText(user.getCourses().toString());

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PublicUserActivity.class);
                intent.putExtra("publicUser", searchUserItems.get(i).getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchUserItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public TextView userInfo;
        public LinearLayout linearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.usercardUN);
            userInfo = (TextView) itemView.findViewById(R.id.usercardUInf);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.userItemLin);

        }
    }
}
