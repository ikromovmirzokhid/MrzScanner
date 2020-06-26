package com.hellsayenci.mrzscanner.pojos;

public class User {

    private String name, surname, jobPlace, jobPosition, phoneNum, photo;

    public User(String name, String surname, String jobPlace, String jobPosition, String phoneNum, String photo) {
        this.name = name;
        this.surname = surname;
        this.jobPlace = jobPlace;
        this.jobPosition = jobPosition;
        this.phoneNum = phoneNum;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getJobPlace() {
        return jobPlace;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPhoto() {
        return photo;
    }
}
