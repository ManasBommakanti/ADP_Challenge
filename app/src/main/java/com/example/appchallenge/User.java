package com.example.appchallenge;

public class User {

    private String name, hs, city, grade, telephone, email, status, hours;

    public User(String name, String hs, String city, String grade, String email)
    {
        this.name = name;
        this.hs = hs;
        this.city = city;
        this.grade = grade;
        this.email = email;
        this.status = "Active";
        this.telephone = "";
        this.hours = "0.0";
    }

    public User(String name, String hs, String city, String grade, String telephone, String email)
    {
        this.name = name;
        this.hs = hs;
        this.city = city;
        this.grade = grade;
        this.email = email;
        this.status = "Active";
        this.telephone = telephone;
        this.hours ="0.0";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHs() {
        return hs;
    }

    public void setHs(String hs) {
        this.hs = hs;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHours(String hours){
        this.hours = hours;
    }

    public String getHours(){
        return hours;
    }
}
