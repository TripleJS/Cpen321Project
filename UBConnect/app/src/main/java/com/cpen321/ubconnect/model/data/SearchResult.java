package com.cpen321.ubconnect.model.data;

import java.util.List;

public class SearchResult {
    private List<User> users;
    private List<Question> questions;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
