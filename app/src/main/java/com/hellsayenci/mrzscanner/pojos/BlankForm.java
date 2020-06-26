package com.hellsayenci.mrzscanner.pojos;

import android.database.Cursor;

import com.hellsayenci.mrzscanner.Utilities.GetInfoFromCursor;

public class BlankForm {
    public String formName, relationName;
    public int id;
    public byte[] photoOfBlank;

    private BlankForm(String formName, String relationName, int id, byte[] photoOfBlank) {
        this.formName = formName;
        this.relationName = relationName;
        this.id = id;
        this.photoOfBlank = photoOfBlank;
    }

    public static BlankForm getBankForm(Cursor cursor) {
        GetInfoFromCursor c = new GetInfoFromCursor(cursor);
        return new BlankForm(c.getString("formName"),
                c.getString("relationName"),
                c.getInt("id"),
                c.getPhoto("photo"));
    }

    public String getFormName() {
        return formName;
    }

    public String getRelationName() {
        return relationName;
    }

    public int getId() {
        return id;
    }

    public byte[] getPhotoOfBlank() {
        return photoOfBlank;
    }
}
