package io.agora.tutorials1v1vcall;

public class Data {
    public int userID;
    public String subject;
    public String grade;
    public String topic;
    public String desc;
    public int status;
    public int teacherID;
    public int questionID;

    public Data(int userID, int questionID, String subject, String grade, String topic, String desc, int status, int teacherID) {
        this.userID = userID;
        this.subject = subject;
        this.grade = grade;
        this.topic = topic;
        this.desc = desc;
        this.status = status;
        this.teacherID = teacherID;
        this.questionID = questionID;
    }
}