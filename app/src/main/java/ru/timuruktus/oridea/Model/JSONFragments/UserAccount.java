package ru.timuruktus.oridea.Model.JSONFragments;



public class UserAccount {


    String username;
    int rating = 0;



    String email;

    public UserAccount(){
    }

    public UserAccount(String email){
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
