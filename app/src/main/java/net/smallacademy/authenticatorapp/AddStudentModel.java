package net.smallacademy.authenticatorapp;

public class AddStudentModel {
    private String name;
    private String Email;
    private String status;
    String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public AddStudentModel(String name, String email, String status) {
        this.name = name;
        Email = email;
        this.status = status;
    }

    public AddStudentModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
