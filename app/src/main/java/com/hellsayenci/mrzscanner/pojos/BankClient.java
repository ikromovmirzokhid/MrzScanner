package com.hellsayenci.mrzscanner.pojos;

import android.database.Cursor;

import com.hellsayenci.mrzscanner.Utilities.GetInfoFromCursor;

public class BankClient {
    private int id, dualCitizenship;
    private String bankAccountType, firstName, lastName, passportId,
            expiryDate, gender, dateOfBirth, nationality, email, streetAddress, city, province, mailingAddress, cardName,
            phoneNum, openedDate, accountNum;
    private byte[] personPhoto, passportPhoto;


    public BankClient(int id, String firstName, String lastName, String passportId, String expiryDate, String gender,
                      String dateOfBirth, String nationality, String email, String streetAddress, String city, String province,
                      String phoneNum, String openedDate, String accountNum, String cardName, byte[] personPhoto, byte[] passportPhoto) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
        this.expiryDate = expiryDate;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.email = email;
        this.streetAddress = streetAddress;
        this.city = city;
        this.province = province;
        this.phoneNum = phoneNum;
        this.openedDate = openedDate;
        this.personPhoto = personPhoto;
        this.cardName = cardName;
        this.passportPhoto = passportPhoto;
        this.accountNum = accountNum;
    }


    public BankClient(int id, int dualCitizenship, String firstName, String lastName, String passportId, String expiryDate,
                      String gender, String dateOfBirth, String nationality, String email, String streetAddress, String city,
                      String province, String mailingAddress, String phoneNum, String openedDate,
                      byte[] personPhoto, byte[] passportPhoto) {
        this.id = id;
        this.dualCitizenship = dualCitizenship;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
        this.expiryDate = expiryDate;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.email = email;
        this.streetAddress = streetAddress;
        this.city = city;
        this.province = province;
        this.mailingAddress = mailingAddress;
        this.phoneNum = phoneNum;
        this.openedDate = openedDate;
        this.personPhoto = personPhoto;
        this.passportPhoto = passportPhoto;
    }

    public BankClient(int id, String bankAccountType, String firstName, String lastName, String passportId, String expiryDate,
                      String gender, String dateOfBirth, String nationality, String email, String streetAddress, String city,
                      String province, String phoneNum, String openedDate, byte[] personPhoto, byte[] passportPhoto) {
        this.id = id;
        this.bankAccountType = bankAccountType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
        this.expiryDate = expiryDate;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.email = email;
        this.streetAddress = streetAddress;
        this.city = city;
        this.province = province;
        this.phoneNum = phoneNum;
        this.openedDate = openedDate;
        this.personPhoto = personPhoto;
        this.passportPhoto = passportPhoto;
    }

    public BankClient(int id, String firstName, String lastName, String passportId, String expiryDate, String gender,
                      String dateOfBirth, String nationality, String email, String streetAddress, String city, String province,
                      String phoneNum, String openedDate, byte[] personPhoto, byte[] passportPhoto) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
        this.expiryDate = expiryDate;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.email = email;
        this.streetAddress = streetAddress;
        this.city = city;
        this.province = province;
        this.phoneNum = phoneNum;
        this.openedDate = openedDate;
        this.personPhoto = personPhoto;
        this.passportPhoto = passportPhoto;
    }


    public static BankClient getBankClient(Cursor cursor, String blankType) {
        if (blankType.equals("AccountOpeningForm")) {
            GetInfoFromCursor c = new GetInfoFromCursor(cursor);
            return new BankClient(c.getInt("id"),
                    c.getString("bankAccountType"), c.getString("firstname"), c.getString("lastname"),
                    c.getString("passportId"), c.getString("expiryDate"), c.getString("gender"), c.getString("dateOfBirth"),
                    c.getString("nationality"), c.getString("email"), c.getString("streetAdress"),
                    c.getString("city"), c.getString("province"), c.getString("phoneNum"), c.getString("openedDate"),
                    c.getPhoto("personPhoto"), c.getPhoto("passportPhoto"));
        } else if (blankType.equals("DebitCard")) {
            GetInfoFromCursor c = new GetInfoFromCursor(cursor);
            return new BankClient(c.getInt("id"),
                    c.getString("firstname"), c.getString("lastname"),
                    c.getString("passportId"), c.getString("expiryDate"), c.getString("gender"), c.getString("dateOfBirth"),
                    c.getString("nationality"), c.getString("email"), c.getString("streetAdress"),
                    c.getString("city"), c.getString("province"), c.getString("phoneNum"), c.getString("openedDate"),
                    c.getPhoto("personPhoto"), c.getPhoto("passportPhoto"));
        } else if (blankType.equals("VisaCardForm")) {
            GetInfoFromCursor c = new GetInfoFromCursor(cursor);
            return new BankClient(c.getInt("id"), c.getInt("dualCitizenship"),
                    c.getString("firstname"), c.getString("lastname"),
                    c.getString("passportId"), c.getString("expiryDate"), c.getString("gender"), c.getString("dateOfBirth"),
                    c.getString("nationality"), c.getString("email"), c.getString("streetAdress"),
                    c.getString("city"), c.getString("province"), c.getString("mailingAddress"),
                    c.getString("phoneNum"), c.getString("openedDate"),
                    c.getPhoto("personPhoto"), c.getPhoto("passportPhoto"));
        } else if (blankType.equals("ClosingForm")) {
            GetInfoFromCursor c = new GetInfoFromCursor(cursor);
            return new BankClient(c.getInt("id"),
                    c.getString("firstname"), c.getString("lastname"),
                    c.getString("passportId"), c.getString("expiryDate"), c.getString("gender"), c.getString("dateOfBirth"),
                    c.getString("nationality"), c.getString("email"), c.getString("streetAdress"),
                    c.getString("city"), c.getString("province"), c.getString("phoneNum"), c.getString("openedDate"),
                    c.getString("accountNum"), c.getString("cardName"),
                    c.getPhoto("personPhoto"), c.getPhoto("passportPhoto"));
        }

        return null;

    }

    public int getId() {
        return id;
    }

    public int getDualCitizenship() {
        return dualCitizenship;
    }

    public String getBankAccountType() {
        return bankAccountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassportId() {
        return passportId;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getEmail() {
        return email;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getOpenedDate() {
        return openedDate;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public String getCardName() {
        return cardName;
    }

    public byte[] getPersonPhoto() {
        return personPhoto;
    }

    public byte[] getPassportPhoto() {
        return passportPhoto;
    }
}
