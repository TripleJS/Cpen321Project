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
import com.cpen321.ubconnect.ui.AnswerActivity;
import com.cpen321.ubconnect.ui.HomeActivity;
import com.cpen321.ubconnect.ui.MainActivity;

import java.util.List;

public class SuggestedQuestionAdapter extends RecyclerView.Adapter<SuggestedQuestionAdapter.ViewHolder> {

    private List<Question> suggestedQuestionItems;
    private Context context;

    public SuggestedQuestionAdapter(List<Question> suggestedQuestionItems, Context context) {
        this.suggestedQuestionItems = suggestedQuestionItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.suggested_question, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Question suggestedQuestionItem = suggestedQuestionItems.get(i);
        viewHolder.suggestedTitle.setText(suggestedQuestionItem.getQuestionTitle());
        viewHolder.suggestedContent.setText(suggestedQuestionItem.getQuestion());
        viewHolder.suggestedTitle.setText(suggestedQuestionItem.getDate().toString() + " by " + suggestedQuestionItem.getOwner());

//        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return suggestedQuestionItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView suggestedTitle;
        public TextView suggestedContent;
        public TextView suggestedDateAuthor;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            suggestedTitle = (TextView) itemView.findViewById(R.id.suggestedTitle);
            suggestedContent = (TextView) itemView.findViewById(R.id.suggestedContent);
            suggestedDateAuthor = (TextView) itemView.findViewById(R.id.suggestedDateAuthor);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.suggestedItemLinearLayout);

        }
    }
}

