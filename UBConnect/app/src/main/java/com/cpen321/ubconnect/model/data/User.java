package com.cpen321.ubconnect.model.data;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class User {
    private String _id;
    private String jwt;
    private String email;
    private String password;
    private List<String> courses;
    private String userName;
    private List<Question> questions;
    private float rating;
    private List<UserReportRate> usersWhoRated;
    private List<String> usersWhoReported;

    public String getUserId() {
        return _id;
    }

    public void setUserId(String userId) {
        this._id = userId;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<UserReportRate> getUsersWhoRated() {
        return usersWhoRated;
    }

    public void setUsersWhoRated(List<UserReportRate> usersWhoRated) {
        this.usersWhoRated = usersWhoRated;
    }

    public List<String> getUsersWhoReported() {
        return usersWhoReported;
    }

    public void setUsersWhoReported(List<String> usersWhoReported) {
        this.usersWhoReported = usersWhoReported;
    }
}
