package com.shray.wisemonkeystutor.model;

/**
 * Created by Shray on 5/14/2017.
 */

public class Request {


    String studentName,studentUid,tutorUid,Status,key,studentToken,tutorName;
    String languageLevel;

    public Request(String studentName, String studentUid, String tutorUid, String key,
                   String Status,String studentToken,String tutorName,String languageLevel) {
        this.studentName=studentName;
        this.studentUid=studentUid;
        this.tutorUid=tutorUid;
        this.Status=Status;
        this.key=key;
        this.studentToken=studentToken;
        this.tutorName=tutorName;
        this.languageLevel=languageLevel;
    }

    public String getLanguageLevel() {
        return languageLevel;
    }

    public void setLanguageLevel(String languageLevel) {
        this.languageLevel = languageLevel;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getStudentToken() {
        return studentToken;
    }

    public void setStudentToken(String studentToken) {
        this.studentToken = studentToken;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentUid() {
        return studentUid;
    }

    public void setStudentUid(String studentUid) {
        this.studentUid = studentUid;
    }

    public String getTutorUid() {
        return tutorUid;
    }

    public void setTutorUid(String tutorUid) {
        this.tutorUid = tutorUid;
    }
}
