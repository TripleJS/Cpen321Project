package com.cpen321.ubconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.model.data.Question;

import java.util.List;

public class SuggestedQuestionAdapter extends RecyclerView.Adapter<SuggestedQuestionAdapter.ViewHolder> {

    private List<Question> suggestedQuestionItems;
    private Context context;

//    // begin
//    public void removebyindex(int i){
//        suggestedQuestionItems.remove(i);
//    }
//    // end

    public SuggestedQuestionAdapter(List<Question> suggestedQuestionItems, Context context) {
        this.suggestedQuestionItems = suggestedQuestionItems;
        this.context = context;
    }

    @NonNull
    @Override
    public SuggestedQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.question_card, viewGroup, false);

        return new SuggestedQuestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedQuestionAdapter.ViewHolder viewHolder, int i) {
        Question suggestedQuestionItem = suggestedQuestionItems.get(i);
        viewHolder.suggestedTitle.setText(suggestedQuestionItem.getQuestionTitle());
        viewHolder.suggestedContent.setText(suggestedQuestionItem.getQuestion());
        viewHolder.suggestedDateAuthor.setText(suggestedQuestionItem.getDate().toString() + " by " + suggestedQuestionItem.getOwner());
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

            suggestedTitle = (TextView) itemView.findViewById(R.id.suggestionTitle);
            suggestedContent = (TextView) itemView.findViewById(R.id.suggestionContent);
            suggestedDateAuthor = (TextView) itemView.findViewById(R.id.suggestionDateAuthor);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.suggestionItemLinearLayout);

        }
    }
}
