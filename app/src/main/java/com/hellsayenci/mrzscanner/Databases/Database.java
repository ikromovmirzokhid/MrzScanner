package com.hellsayenci.mrzscanner.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.hellsayenci.mrzscanner.pojos.BankClient;
import com.hellsayenci.mrzscanner.pojos.BlankForm;

import java.util.ArrayList;
import java.util.List;

public class Database extends DBHelper {

    private static Database db;

    public static void init(Context context) {
        if (db == null)
            db = new Database(context, "BankForms.db", 1);
    }

    public static Database getDB() {
        return db;
    }


    private Database(Context context, String mDataBaseName, int version) {
        super(context, mDataBaseName, version);
    }

    public List<BlankForm> getBankForms() {
        List<BlankForm> bankForms = new ArrayList<>();
        mDatabase = this.getReadableDatabase();
        Cursor cursor = mDatabase.rawQuery("Select * from Blanks", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                bankForms.add(BlankForm.getBankForm(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return bankForms;
    }

    public List<BankClient> getBankClients(String blankType) {
        List<BankClient> bankClients = new ArrayList<>();
        mDatabase = this.getReadableDatabase();
        Cursor cursor = mDatabase.rawQuery("Select * from " + blankType, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                bankClients.add(BankClient.getBankClient(cursor, blankType));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return bankClients;
    }

    public BankClient getClient(int id, String blankType) {
        BankClient client = null;
        mDatabase = this.getReadableDatabase();
        Cursor cursor = mDatabase.rawQuery("Select * from " + blankType + " where id=" + id, null);
        cursor.moveToFirst();
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            client = BankClient.getBankClient(cursor, blankType);
        }
        return client;
    }

    public boolean insertNewClient(String blankType, BankClient client) {
        boolean isClientInserted = false;

        mDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        switch (blankType) {
            case "AccountOpeningForm":
                values.put("bankAccountType", client.getBankAccountType());
                values.put("firstname", client.getFirstName());
                values.put("lastname", client.getLastName());
                values.put("passportId", client.getPassportId());
                values.put("expiryDate", client.getExpiryDate());
                values.put("gender", client.getGender());
                values.put("dateOfBirth", client.getDateOfBirth());
                values.put("nationality", client.getNationality());
                values.put("email", client.getEmail());
                values.put("phoneNum", client.getPhoneNum());
                values.put("streetAdress", client.getStreetAddress());
                values.put("city", client.getCity());
                values.put("province", client.getProvince());
                values.put("openedDate", client.getOpenedDate());
                values.put("personPhoto", client.getPersonPhoto());
                values.put("passportPhoto", client.getPassportPhoto());

                isClientInserted = mDatabase.insert(blankType, null, values) > 0;

                mDatabase.close();

                break;
            case "DebitCard":

                values.put("firstname", client.getFirstName());
                values.put("lastname", client.getLastName());
                values.put("passportId", client.getPassportId());
                values.put("expiryDate", client.getExpiryDate());
                values.put("gender", client.getGender());
                values.put("dateOfBirth", client.getDateOfBirth());
                values.put("nationality", client.getNationality());
                values.put("email", client.getEmail());
                values.put("phoneNum", client.getPhoneNum());
                values.put("streetAdress", client.getStreetAddress());
                values.put("city", client.getCity());
                values.put("province", client.getProvince());
                values.put("openedDate", client.getOpenedDate());
                values.put("personPhoto", client.getPersonPhoto());
                values.put("passportPhoto", client.getPassportPhoto());

                isClientInserted = mDatabase.insert(blankType, null, values) > 0;

                mDatabase.close();
                break;
            case "VisaCardForm":

                values.put("dualCitizenship", client.getDualCitizenship());
                values.put("firstname", client.getFirstName());
                values.put("lastname", client.getLastName());
                values.put("passportId", client.getPassportId());
                values.put("expiryDate", client.getExpiryDate());
                values.put("gender", client.getGender());
                values.put("dateOfBirth", client.getDateOfBirth());
                values.put("nationality", client.getNationality());
                values.put("email", client.getEmail());
                values.put("phoneNum", client.getPhoneNum());
                values.put("streetAdress", client.getStreetAddress());
                values.put("city", client.getCity());
                values.put("province", client.getProvince());
                values.put("openedDate", client.getOpenedDate());
                values.put("personPhoto", client.getPersonPhoto());
                values.put("passportPhoto", client.getPassportPhoto());
                values.put("mailingAddress", client.getMailingAddress());

                isClientInserted = mDatabase.insert(blankType, null, values) > 0;
                break;
            case "ClosingForm":
                values.put("firstname", client.getFirstName());
                values.put("lastname", client.getLastName());
                values.put("passportId", client.getPassportId());
                values.put("expiryDate", client.getExpiryDate());
                values.put("gender", client.getGender());
                values.put("dateOfBirth", client.getDateOfBirth());
                values.put("nationality", client.getNationality());
                values.put("email", client.getEmail());
                values.put("phoneNum", client.getPhoneNum());
                values.put("streetAdress", client.getStreetAddress());
                values.put("city", client.getCity());
                values.put("province", client.getProvince());
                values.put("openedDate", client.getOpenedDate());
                values.put("personPhoto", client.getPersonPhoto());
                values.put("passportPhoto", client.getPassportPhoto());
                values.put("accountNum", client.getAccountNum());
                values.put("cardName", client.getCardName());

                isClientInserted = mDatabase.insert(blankType, null, values) > 0;

                mDatabase.close();
                break;
        }


        return isClientInserted;
    }
}
