package com.hellsayenci.mrzscanner.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.hellsayenci.mrzscanner.Activities.MainActivity;
import com.hellsayenci.mrzscanner.R;
import com.hellsayenci.mrzscanner.pojos.BankClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PersonInfosFragment extends Fragment {

    private NavController navController;
    private BankClient client;
    private String blankType, blankTypeText;
    private Bitmap bitmap;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private int permission;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.surname)
    TextView surname;
    @BindView(R.id.bankType)
    TextView bankType;
    @BindView(R.id.passportID)
    TextView passportID;
    @BindView(R.id.expiryDate)
    TextView expiryDate;
    @BindView(R.id.gender)
    TextView gender;
    @BindView(R.id.dateOfBirth)
    TextView dateOfBirth;
    @BindView(R.id.nationality)
    TextView nationality;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.streetText)
    TextView street;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.province)
    TextView province;
    @BindView(R.id.phoneNum)
    TextView phoneNum;
    @BindView(R.id.dualCitizenshipCheck)
    TextView dualCitizenship;
    @BindView(R.id.accountName)
    TextView accountName;
    @BindView(R.id.accountSurname)
    TextView accountSurname;
    @BindView(R.id.accountNum)
    TextView accountNum;
    @BindView(R.id.openedDate)
    TextView openedDate;
    @BindView(R.id.convertBtn)
    Button convertBtn;


    @BindView(R.id.bankTypeText)
    TextView bankTypeText;
    @BindView(R.id.dualCitizenshipText)
    TextView dualCitizenshipText;
    @BindView(R.id.nameOnAccountText)
    TextView nameOnAccountText;
    @BindView(R.id.accountNumText)
    TextView accountNumText;
    @BindView(R.id.bankTypeLayout)
    ConstraintLayout bankTypeLayout;
    @BindView(R.id.view)
    ConstraintLayout cView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String finalResult = getArguments().getString("finalResult");
        blankType = getArguments().getString("blankType");
        blankTypeText = getArguments().getString("blankTypeText");
        if (!finalResult.equals("")) {
            client = new Gson().fromJson(finalResult, BankClient.class);
        }

        return inflater.inflate(R.layout.person_infos_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        navController = Navigation.findNavController(view);

        ((MainActivity) getActivity()).setToolbar(toolbar);

        toolbar.setTitle(blankTypeText);

        setMrzResult();

        if (blankType.equals("AccountOpeningForm")) {
            bankType.setText(client.getBankAccountType());

        } else if (blankType.equals("DebitCard")) {
            bankType.setVisibility(View.GONE);
            bankTypeText.setVisibility(View.GONE);
            bankTypeLayout.setVisibility(View.GONE);

        } else if (blankType.equals("VisaCardForm")) {
            bankType.setVisibility(View.GONE);
            bankTypeText.setVisibility(View.GONE);
            bankTypeLayout.setVisibility(View.GONE);
            dualCitizenshipText.setVisibility(View.VISIBLE);
            dualCitizenship.setVisibility(View.VISIBLE);
            String dual;

            if (client.getDualCitizenship() == 1)
                dual = "Yes";
            else
                dual = "No";

            dualCitizenship.setText(dual);


        } else if (blankType.equals("ClosingForm")) {
            bankType.setVisibility(View.GONE);
            bankTypeText.setVisibility(View.GONE);
            bankTypeLayout.setVisibility(View.GONE);
            nameOnAccountText.setVisibility(View.VISIBLE);
            accountName.setVisibility(View.VISIBLE);
            accountSurname.setVisibility(View.VISIBLE);
            accountNumText.setVisibility(View.VISIBLE);
            accountNum.setVisibility(View.VISIBLE);

            accountName.setText(client.getFirstName());
            accountSurname.setText(client.getLastName());
        }

        convertBtn.setOnClickListener(v -> {
            verifyStoragePermissions(getActivity());
            Log.d("size", " " + cView.getWidth() + "  " + cView.getWidth());
            bitmap = loadBitmapFromView(cView, cView.getWidth(), cView.getHeight());
            if (permission == PackageManager.PERMISSION_GRANTED)
                createPdf();
            else
                Snackbar.make(getView(), "Please grant permission!", Snackbar.LENGTH_SHORT).show();

        });

    }

    private void createPdf() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        }
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write the document content
        String targetPdf = "/sdcard/" + blankType + "_" + client.getLastName() + ".pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Snackbar.make(getView(), "PDF successfully created", Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), "Pdf written", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAGS", e.getMessage());
            Toast.makeText(getActivity(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        //Toast.makeText(getActivity(), "PDF is created!!!", Toast.LENGTH_SHORT).show();

        //openGeneratedPDF(targetPdf);
    }

    private void openGeneratedPDF(String targetPdf) {
        File file = new File(targetPdf);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplication().getPackageName() + ".provider", file);

            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        } else
            Toast.makeText(getActivity(), "not found", Toast.LENGTH_SHORT).show();
    }

    private Bitmap loadBitmapFromView(ConstraintLayout cView, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        cView.draw(c);

        return b;
    }

    private void setMrzResult() {
        name.setText(client.getFirstName());
        surname.setText(client.getLastName());
        passportID.setText(client.getPassportId());
        expiryDate.setText(client.getExpiryDate());
        gender.setText("" + client.getGender());
        dateOfBirth.setText(client.getDateOfBirth());
        nationality.setText(client.getNationality());
        openedDate.setText(client.getOpenedDate());
        email.setText(client.getEmail());
        street.setText(client.getStreetAddress());
        city.setText(client.getCity());
        province.setText(client.getProvince());
        phoneNum.setText(client.getPhoneNum());

        if (client.getGender().equals("F"))
            Glide.with(getView()).load(R.drawable.women).into(photo);
        else
            Glide.with(getView()).load(R.drawable.men).into(photo);

    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
