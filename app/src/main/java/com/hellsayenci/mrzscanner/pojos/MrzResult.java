package com.hellsayenci.mrzscanner.pojos;

import android.graphics.Bitmap;

public class MrzResult {
    private String dateOfBirth, documentNumber, expiryDate, nationality, firstName, issuingCountry, surname, blankName, relationName;
    private char gender;
    private Bitmap photo;

    public MrzResult(String dateOfBirth, String documentNumber, String expiryDate, String nationality,
                     char gender, String firstName, String issuingCountry, String surname, Bitmap photo, String blankName, String relationName) {
        this.dateOfBirth = dateOfBirth;
        this.documentNumber = documentNumber;
        this.expiryDate = expiryDate;
        this.nationality = nationality;
        this.gender = gender;
        this.firstName = firstName;
        this.issuingCountry = issuingCountry;
        this.surname = surname;
        this.photo = photo;
        this.blankName = blankName;
        this.relationName = relationName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getNationality() {
        return nationality;
    }

    public char getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public String getSurname() {
        return surname;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public String getBlankName() {
        return blankName;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setBlankName(String blankName) {
        this.blankName = blankName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }
}
