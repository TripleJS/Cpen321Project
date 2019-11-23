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
import com.cpen321.ubconnect.ui.question.QuestionActivity;
import com.cpen321.ubconnect.ui.question.QuestionFragment;

import java.util.List;

public class SearchQuestionAdapter extends RecyclerView.Adapter<SearchQuestionAdapter.ViewHolder> {

    private List<Question> searchQuestionItems;

    public SearchQuestionAdapter(List<Question> searchQuestionItems) {
        this.searchQuestionItems = searchQuestionItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.question_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Question searchQuestionItem = searchQuestionItems.get(i);
        viewHolder.questionTitle.setText(searchQuestionItem.getQuestionTitle());
        viewHolder.questionContent.setText(searchQuestionItem.getQuestion());
        viewHolder.questionDateAuthor.setText(searchQuestionItem.getDate().toString() + " by " + searchQuestionItem.getOwner());

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("arg", searchQuestionItems.get(i).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchQuestionItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView questionTitle;
        public TextView questionContent;
        public TextView questionDateAuthor;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTitle = (TextView) itemView.findViewById(R.id.questionTitle);
            questionContent = (TextView) itemView.findViewById(R.id.questionContent);
            questionDateAuthor = (TextView) itemView.findViewById(R.id.questionDateAuthor);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.questionItemLinearLayout);

        }
    }
}

