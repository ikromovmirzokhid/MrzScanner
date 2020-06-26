package com.hellsayenci.mrzscanner.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.hellsayenci.mrzscanner.Activities.MainActivity;
import com.hellsayenci.mrzscanner.Databases.Database;
import com.hellsayenci.mrzscanner.R;
import com.hellsayenci.mrzscanner.pojos.BankClient;
import com.hellsayenci.mrzscanner.pojos.MrzResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CheckScannedInfoFragment extends Fragment {

    private NavController navController;
    private Database db;
    private BankClient client;
    private MrzResult mrzResult;
    private String blankType, blankTypeText;
    private PopupMenu bankTypeMenu, regionsMenu, menu;
    private MenuInflater inflater;
    private String openedDateText;
    private Bundle bundle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.surname)
    EditText surname;
    @BindView(R.id.bankType)
    TextView bankType;
    @BindView(R.id.passportID)
    EditText passportID;
    @BindView(R.id.expiryDate)
    EditText expiryDate;
    @BindView(R.id.gender)
    EditText gender;
    @BindView(R.id.dateOfBirth)
    EditText dateOfBirth;
    @BindView(R.id.nationality)
    EditText nationality;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.streetInput)
    TextInputEditText streetInput;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.province)
    TextView province;
    @BindView(R.id.phoneNum)
    EditText phoneNum;
    @BindView(R.id.dualCitizenshipCheck)
    CheckBox dualCitizenshipCheck;
    @BindView(R.id.accountName)
    EditText accountName;
    @BindView(R.id.accountSurname)
    EditText accountSurname;
    @BindView(R.id.accountNum)
    EditText accountNum;
    @BindView(R.id.openedDate)
    EditText openedDate;
    @BindView(R.id.editBtn)
    Button editBtn;
    @BindView(R.id.saveBtn)
    Button saveBtn;

    @BindView(R.id.bankTypeText)
    TextView bankTypeText;
    @BindView(R.id.dualCitizenshipText)
    TextView dualCitizenshipText;
    @BindView(R.id.nameOnAccountText)
    TextView nameOnAccountText;
    @BindView(R.id.accountNumText)
    TextView accountNumText;
    @BindView(R.id.dropdown_icon)
    ImageButton dropdownIcon;
    @BindView(R.id.bankTypeLayout)
    ConstraintLayout bankTypeLayout;
    @BindView(R.id.cityText)
    ConstraintLayout cityText;
    @BindView(R.id.provinceText)
    ConstraintLayout provinceText;
    @BindView(R.id.backArrow)
    ImageView backArrow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = Database.getDB();

        bundle = new Bundle();

        String mrzJson = getArguments().getString("mrzResult");
        if (!mrzJson.equals("")) {
            mrzResult = new Gson().fromJson(mrzJson, MrzResult.class);
        }

        blankType = mrzResult.getRelationName();
        blankTypeText = mrzResult.getBlankName();


        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        openedDateText = sdf.format(date);


        return inflater.inflate(R.layout.check_scanned_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        navController = Navigation.findNavController(view);

        //((MainActivity) getActivity()).setToolbar(toolbar);

        setMrzResult();

        setPopUpMenus();


        if (blankType.equals("AccountOpeningForm")) {


        } else if (blankType.equals("DebitCard")) {
            bankType.setVisibility(View.GONE);
            bankTypeText.setVisibility(View.GONE);
            dropdownIcon.setVisibility(View.GONE);
            bankTypeLayout.setVisibility(View.GONE);

        } else if (blankType.equals("VisaCardForm")) {
            bankType.setVisibility(View.GONE);
            bankTypeText.setVisibility(View.GONE);
            dropdownIcon.setVisibility(View.GONE);
            bankTypeLayout.setVisibility(View.GONE);
            dualCitizenshipText.setVisibility(View.VISIBLE);
            dualCitizenshipCheck.setVisibility(View.VISIBLE);

        } else if (blankType.equals("ClosingForm")) {
            bankType.setVisibility(View.GONE);
            bankTypeText.setVisibility(View.GONE);
            dropdownIcon.setVisibility(View.GONE);
            bankTypeLayout.setVisibility(View.GONE);
            nameOnAccountText.setVisibility(View.VISIBLE);
            accountName.setVisibility(View.VISIBLE);
            accountSurname.setVisibility(View.VISIBLE);
            accountNumText.setVisibility(View.VISIBLE);
            accountNum.setVisibility(View.VISIBLE);

            accountName.setText(mrzResult.getFirstName());
            accountSurname.setText(mrzResult.getSurname());
        }

        bankTypeLayout.setOnClickListener(v -> bankTypeMenu.show());

        cityText.setOnClickListener(v -> regionsMenu.show());

        provinceText.setOnClickListener(v -> {
            if (city.getText().equals("Select Region")) {
                Snackbar.make(view, "First select city please", Snackbar.LENGTH_SHORT).show();
            } else {
                menu.show();
            }
        });

        editBtn.setOnClickListener(v -> {
            setEditable();
        });

        saveBtn.setOnClickListener(v -> {
            if (blankType.equals("AccountOpeningForm")) {
                if (name.getText().toString().equals("") || surname.getText().toString().equals("") || passportID.getText().toString().equals("") ||
                        expiryDate.getText().toString().equals("") || gender.getText().toString().equals("") || dateOfBirth.getText().toString().equals("") ||
                        nationality.getText().toString().equals("") || bankType.getText().toString().equals("Select Bank Type") || email.getText().toString().equals("") ||
                        streetInput.getText().toString().equals("") || city.getText().toString().equals("Select City") || province.getText().toString().equals("Select Province") ||
                        phoneNum.getText().toString().equals("") || openedDate.getText().toString().equals("")) {
                    Snackbar.make(view, "Please fill all required fields!", Snackbar.LENGTH_SHORT).show();
                } else {
                    client = new BankClient(0, bankType.getText().toString(), name.getText().toString(), surname.getText().toString(), passportID.getText().toString(),
                            expiryDate.getText().toString(), gender.getText().toString(), dateOfBirth.getText().toString(), nationality.getText().toString(),
                            email.getText().toString(), streetInput.getText().toString(), city.getText().toString(), province.getText().toString(), phoneNum.getText().toString(),
                            openedDate.getText().toString(), null, null);
                    String finalResult = new Gson().toJson(client);
                    bundle.clear();
                    bundle.putString("finalResult", finalResult);
                    bundle.putString("blankType", blankType);
                    bundle.putString("blankTypeText", blankTypeText);
                    db.insertNewClient(blankType, client);
                    Snackbar.make(view, "Application successfully created!", Snackbar.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_checkScannedInfoFragment_to_personInfosFragment, bundle);

                }

            } else if (blankType.equals("DebitCard")) {
                if (name.getText().toString().equals("") || surname.getText().toString().equals("") || passportID.getText().toString().equals("") ||
                        expiryDate.getText().toString().equals("") || gender.getText().toString().equals("") || dateOfBirth.getText().toString().equals("") ||
                        nationality.getText().toString().equals("") || email.getText().toString().equals("") ||
                        streetInput.getText().toString().equals("") || city.getText().toString().equals("Select City") || province.getText().toString().equals("Select Province") ||
                        phoneNum.getText().toString().equals("") || openedDate.getText().toString().equals("")) {
                    Snackbar.make(view, "Please fill all required fields!", Snackbar.LENGTH_SHORT).show();
                } else {
                    client = new BankClient(0, name.getText().toString(), surname.getText().toString(), passportID.getText().toString(),
                            expiryDate.getText().toString(), gender.getText().toString(), dateOfBirth.getText().toString(), nationality.getText().toString(),
                            email.getText().toString(), streetInput.getText().toString(), city.getText().toString(), province.getText().toString(), phoneNum.getText().toString(),
                            openedDate.getText().toString(), null, null);
                    String finalResult = new Gson().toJson(client);
                    bundle.clear();
                    bundle.putString("finalResult", finalResult);
                    bundle.putString("blankType", blankType);
                    bundle.putString("blankTypeText", blankTypeText);
                    db.insertNewClient(blankType, client);
                    Snackbar.make(view, "Application successfully created!", Snackbar.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_checkScannedInfoFragment_to_personInfosFragment, bundle);
                }

            } else if (blankType.equals("VisaCardForm")) {
                if (name.getText().toString().equals("") || surname.getText().toString().equals("") || passportID.getText().toString().equals("") ||
                        expiryDate.getText().toString().equals("") || gender.getText().toString().equals("") || dateOfBirth.getText().toString().equals("") ||
                        nationality.getText().toString().equals("") || email.getText().toString().equals("") ||
                        streetInput.getText().toString().equals("") || city.getText().toString().equals("Select City") || province.getText().toString().equals("Select Province") ||
                        phoneNum.getText().toString().equals("") || openedDate.getText().toString().equals("")) {
                    Snackbar.make(view, "Please fill all required fields!", Snackbar.LENGTH_SHORT).show();
                } else {
                    int dual;
                    if (dualCitizenshipCheck.isChecked())
                        dual = 1;
                    else
                        dual = 0;
                    String mailingAddress = streetInput.getText().toString() + "/" + province.getText().toString() + "/" + city.getText().toString();
                    client = new BankClient(0, dual, name.getText().toString(), surname.getText().toString(), passportID.getText().toString(),
                            expiryDate.getText().toString(), gender.getText().toString(), dateOfBirth.getText().toString(), nationality.getText().toString(),
                            email.getText().toString(), streetInput.getText().toString(), city.getText().toString(), province.getText().toString(), mailingAddress, phoneNum.getText().toString(),
                            openedDate.getText().toString(), null, null);
                    String finalResult = new Gson().toJson(client);
                    bundle.clear();
                    bundle.putString("finalResult", finalResult);
                    bundle.putString("blankType", blankType);
                    bundle.putString("blankTypeText", blankTypeText);
                    db.insertNewClient(blankType, client);
                    Snackbar.make(view, "Application successfully created!", Snackbar.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_checkScannedInfoFragment_to_personInfosFragment, bundle);
                }

            } else if (blankType.equals("ClosingForm")) {
                String cardName = accountName + " " + accountSurname;
                if (name.getText().toString().equals("") || surname.getText().toString().equals("") || passportID.getText().toString().equals("") ||
                        expiryDate.getText().toString().equals("") || gender.getText().toString().equals("") || dateOfBirth.getText().toString().equals("") ||
                        nationality.getText().toString().equals("") || email.getText().toString().equals("") ||
                        streetInput.getText().toString().equals("") || city.getText().toString().equals("Select City") || province.getText().toString().equals("Select Province") ||
                        phoneNum.getText().toString().equals("") || openedDate.getText().toString().equals("") || accountName.getText().toString().equals("")
                        || accountSurname.getText().toString().equals("") || accountNum.getText().toString().equals("")) {
                    Snackbar.make(view, "Please fill all required fields!", Snackbar.LENGTH_SHORT).show();
                } else {
                    client = new BankClient(0, name.getText().toString(), surname.getText().toString(), passportID.getText().toString(),
                            expiryDate.getText().toString(), gender.getText().toString(), dateOfBirth.getText().toString(), nationality.getText().toString(),
                            email.getText().toString(), streetInput.getText().toString(), city.getText().toString(), province.getText().toString(), phoneNum.getText().toString(),
                            openedDate.getText().toString(), accountNum.getText().toString(), cardName, null, null);
                    String finalResult = new Gson().toJson(client);
                    bundle.clear();
                    bundle.putString("finalResult", finalResult);
                    bundle.putString("blankType", blankType);
                    bundle.putString("blankTypeText", blankTypeText);
                    db.insertNewClient(blankType, client);
                    Snackbar.make(view, "Application successfully created!", Snackbar.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_checkScannedInfoFragment_to_personInfosFragment, bundle);
                }
            }
        });

        backArrow.setOnClickListener(v -> {
            navController.popBackStack();
        });

    }

    private void setEditable() {
        name.setFocusable(true);
        name.setFocusableInTouchMode(true);
        surname.setFocusable(true);
        surname.setFocusableInTouchMode(true);
        passportID.setFocusable(true);
        passportID.setFocusableInTouchMode(true);
        expiryDate.setFocusable(true);
        expiryDate.setFocusableInTouchMode(true);
        gender.setFocusable(true);
        gender.setFocusableInTouchMode(true);
        dateOfBirth.setFocusable(true);
        dateOfBirth.setFocusableInTouchMode(true);
        nationality.setFocusable(true);
        nationality.setFocusableInTouchMode(true);
        accountName.setFocusable(true);
        accountName.setFocusableInTouchMode(true);
        accountSurname.setFocusable(true);
        accountSurname.setFocusableInTouchMode(true);
        openedDate.setFocusable(true);
        openedDate.setFocusableInTouchMode(true);
        editBtn.setVisibility(View.GONE);
    }

    private void setMrzResult() {
        name.setText(mrzResult.getFirstName());
        surname.setText(mrzResult.getSurname());
        passportID.setText(mrzResult.getDocumentNumber());
        expiryDate.setText("" + mrzResult.getExpiryDate());
        gender.setText("" + mrzResult.getGender());
        dateOfBirth.setText(mrzResult.getDateOfBirth());
        nationality.setText(mrzResult.getNationality());
        openedDate.setText(openedDateText);
        if (mrzResult.getGender() == 'F')
            Glide.with(getView()).load(R.drawable.women).into(photo);
        else
            Glide.with(getView()).load(R.drawable.men).into(photo);

    }


    private void setPopUpMenus() {
        bankTypeMenu = new PopupMenu(getActivity(), bankType);
        inflater = bankTypeMenu.getMenuInflater();
        inflater.inflate(R.menu.bank_types_menu, bankTypeMenu.getMenu());
        bankTypeMenu.setOnMenuItemClickListener(item -> {
            bankType.setText(item.getTitle());

            return true;
        });

        regionsMenu = new PopupMenu(getActivity(), cityText);
        inflater.inflate(R.menu.regions_menu, regionsMenu.getMenu());
        regionsMenu.setOnMenuItemClickListener(item -> {
            city.setText(item.getTitle());
            setUpProvincesMenu();
            return true;
        });


    }

    private void setUpProvincesMenu() {
        if (city.getText().equals("Andijon viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.andijon_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Buxoro viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.buxoro_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Fargʻona viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.fargona_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Jizzax viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.jizzax_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Xorazm viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.xorazm_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Namangan viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.namangan_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Navoiy viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.navoiy_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Qashqadaryo viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.qashqadaryo_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Qoraqalpogʻiston Respublikasi")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.qoraqalpog_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Samarqand viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.samarqand_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Sirdaryo viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.sirdaryo_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("surxandaryo viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.surxandaryo_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Toshkent viloyati")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.toshkentv_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        } else if (city.getText().equals("Toshkent shahri")) {
            menu = new PopupMenu(getActivity(), provinceText);
            inflater.inflate(R.menu.toshkentsh_provinces, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                province.setText(item.getTitle());
                return true;
            });
        }
    }

}
