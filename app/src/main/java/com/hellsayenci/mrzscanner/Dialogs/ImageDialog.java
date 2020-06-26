package com.hellsayenci.mrzscanner.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hellsayenci.mrzscanner.R;

public class ImageDialog extends AlertDialog {

    private Bitmap bitmap;
    private View v;
    private ImageView mImage;


    public ImageDialog(Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
        v = LayoutInflater.from(context).inflate(R.layout.image_view_dialog, null, false);
        setView(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImage = v.findViewById(R.id.mImage);

        Glide.with(v).load(bitmap).into(mImage);
    }
}
