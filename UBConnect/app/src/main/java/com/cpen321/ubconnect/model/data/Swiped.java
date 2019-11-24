package com.cpen321.ubconnect.model.data;

public class Swiped {
    private String _Id;
    private String questionId;
    private String direction;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getUserId() {
        return _Id;
    }

    public void setUserId(String userId) {
        this._Id = userId;
    }
}
