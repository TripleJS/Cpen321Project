package com.cpen321.ubconnect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.SearchItem;
import com.cpen321.ubconnect.model.data.User;
import com.cpen321.ubconnect.ui.search.ShowAllSearch;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<SearchItem> searchItems;
    private String question;

    public SearchAdapter(List<SearchItem> searchItems, String question) {
        this.searchItems = searchItems;
        this.question = question;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_item, viewGroup, false);

        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder viewHolder, int i) {
        SearchItem searchItem = searchItems.get(i);
        viewHolder.type.setText(searchItem.getType());

        viewHolder.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(viewHolder.itemView.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        viewHolder.recyclerView.setLayoutManager(linearLayoutManager);

        if(searchItem.getType().equals("Question")){
            List<Question> questions = new ArrayList<>();
            for(int index = 0; index < 3 && index < searchItem.getSearchItem().size(); index++) {
                questions.add((Question) searchItem.getSearchItem().get(index));
            }
            RecyclerView.Adapter questionAdapter = new SearchQuestionAdapter(questions);
            viewHolder.recyclerView.setAdapter(questionAdapter);
            viewHolder.showAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ShowAllSearch.class);
                    intent.putExtra("searchItem", "Question");
                    intent.putExtra("question", question);
                    context.startActivity(intent);
                }
            });
        }
        else if(searchItem.getType().equals("User")){
            List<User> users = new ArrayList<>();
            for(int index = 0; index < 3 && index < searchItem.getSearchItem().size(); index++) {
                users.add((User) searchItem.getSearchItem().get(index));
            }
            RecyclerView.Adapter userAdapter = new SearchUserAdapter(users);
            viewHolder.recyclerView.setAdapter(userAdapter);

            viewHolder.showAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ShowAllSearch.class);
                    intent.putExtra("searchItem", "User");
                    intent.putExtra("question", question);
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView type;
        public RecyclerView recyclerView;
        public RecyclerView userRV;
        private RecyclerView.Adapter userAdapter;
        public LinearLayout linearLayout;
        public Button showAll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.searchitemType);

            recyclerView = itemView.findViewById(R.id.searchitemRV);
            showAll = itemView.findViewById(R.id.searchitemShowAll);
            linearLayout = itemView.findViewById(R.id.searchItemLin);


        }
    }

}
