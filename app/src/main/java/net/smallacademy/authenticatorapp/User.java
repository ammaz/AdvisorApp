package net.smallacademy.authenticatorapp;

public class User {
    private String id;
    private String name;
    private String image;
    private String stuts;
    private String Email;


    public User(String id, String name, String image,String stuts,String Email) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.stuts = stuts;
        this.Email = Email;

    }

    public User() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getStuts() {
        return stuts;
    }

    public void setStuts(String stuts) {
        this.stuts = stuts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
