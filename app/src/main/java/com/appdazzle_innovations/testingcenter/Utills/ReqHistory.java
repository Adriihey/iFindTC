package com.appdazzle_innovations.testingcenter.Utills;

public class ReqHistory {
    private String body, centerEmail, centerName, subject;

    public ReqHistory(){
    }

    public ReqHistory(String body, String centerEmail, String centerName, String subject) {
        this.body = body;
        this.centerEmail = centerEmail;
        this.centerName = centerName;
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCenterEmail() {
        return centerEmail;
    }

    public void setCenterEmail(String centerEmail) {
        this.centerEmail = centerEmail;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
