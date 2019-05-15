package com.varunsaini.android.bestchatapp.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;
import com.onesignal.OneSignal;
import com.rm.rmswitch.RMSwitch;
import com.varunsaini.android.bestchatapp.AppPreferences;
import com.varunsaini.android.bestchatapp.R;
import com.varunsaini.android.bestchatapp.models.NewUserInDbModel;
import com.varunsaini.android.bestchatapp.ui.activities.LoginActivity;
import com.varunsaini.android.bestchatapp.ui.activities.MainBaseActivity;
import com.varunsaini.android.bestchatapp.ui.activities.SplashActivity;
import com.varunsaini.android.bestchatapp.viewmodels.SettingsViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsFragment extends Fragment {
    private static final int PICK_IMAGE = 1;
    File compressedImageFile;
    AppPreferences appPreferences;

    public SettingsFragment() {
        // Required empty public constructor
    }

    View v;
    TextView signOut_tv, username_tv, status_tv, age_tv, nationality_tv;
    CircleImageView profile_image;
    ImageView status_edit_iv, age_edit_iv, nationality_edit_iv;
    String myUid;
    CardView save_btn;
    SettingsViewModel settingsViewModel;
    NumberPicker numberPicker;
    RMSwitch notification_switch, disguised_switch;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_settings, container, false);
        initViews();
        settingsViewModel = ViewModelProviders.of(getActivity()).get(SettingsViewModel.class);
        signOut_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                AppPreferences appPreferences = new AppPreferences(getContext());
                                appPreferences.writeUid(null);
                                appPreferences.writeUName(null);
                                appPreferences.writeProfilePicUri(null);
                                Intent i = new Intent(getContext(), LoginActivity.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                        });
            }
        });
        getMyUid();

        if(appPreferences.readNotificationState().equals("true")){
            notification_switch.setChecked(true);
            disguised_switch.setChecked(Boolean.parseBoolean(appPreferences.readDisguisedState()));
        }else{
            notification_switch.setChecked(false);
            disguised_switch.setEnabled(false);
        }


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("outputX", 256);
                intent.putExtra("outputY", 256);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, PICK_IMAGE);

            }
        });

        settingsViewModel.getAllSettingsOfAUser(myUid).observe(getActivity(), new Observer<NewUserInDbModel>() {
            @Override
            public void onChanged(@Nullable NewUserInDbModel newUserInDbModel) {
                username_tv.setText(newUserInDbModel.displayName);
                status_tv.setText(newUserInDbModel.status);

                if (newUserInDbModel.age != null && !newUserInDbModel.age.isEmpty())
                    age_tv.setText(newUserInDbModel.age);

                if (newUserInDbModel.nationality != null && !newUserInDbModel.nationality.isEmpty())
                    nationality_tv.setText(newUserInDbModel.nationality);

                if (getActivity() != null) {
                    if (newUserInDbModel.profilePicUrl != null && !newUserInDbModel.profilePicUrl.isEmpty()) {
                        Glide.with(getActivity()).load(newUserInDbModel.profilePicUrl).into(profile_image);
                    } else {
                        Glide.with(getActivity()).load(R.drawable.placeholder_person).into(profile_image);
                    }
                }

            }
        });

        status_edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.dialog_status_et, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Write you Status");
                alertDialog.setCancelable(false);
                final EditText etComments = (EditText) view.findViewById(R.id.etComments);
                etComments.setText(status_tv.getText().toString());
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        status_tv.setText(etComments.getText().toString());
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(view);
                alertDialog.show();
            }
        });

        age_edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAgeSelectDialog();
            }
        });

        nationality_edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountryPicker.Builder builder =
                        new CountryPicker.Builder().with(getContext()).listener(new OnCountryPickerListener() {
                            @Override
                            public void onSelectCountry(Country country) {
                                Toast.makeText(getContext(), country.getName(), Toast.LENGTH_SHORT).show();
                                nationality_tv.setText(country.getName());
                            }
                        });
                CountryPicker picker = builder.build();
                picker.showBottomSheet((AppCompatActivity) getActivity());
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewUserInDbModel newUserInDbModel = new NewUserInDbModel(status_tv.getText().toString(), age_tv.getText().toString(),
                        nationality_tv.getText().toString(), "");
                settingsViewModel.setAllSettingsDetails(myUid, newUserInDbModel);
                appPreferences.writeNotificationState(notification_switch.isChecked());
                appPreferences.writeDisguisedState(disguised_switch.isChecked());
                if(notification_switch.isChecked()){
                    OneSignal.setSubscription(true);
                }else{
                    OneSignal.setSubscription(false);
                }
            }
        });

//        notification_switch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(notification_switch.isChecked()){
//                    disguised_switch.setEnabled(true);
//                }else{
//                    age_tv.setEnabled(false);
//                    disguised_switch.setEnabled(false);
//                }
//            }
//        });

        notification_switch.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                if (isChecked) {
                    disguised_switch.setEnabled(true);
                } else {

                    disguised_switch.setEnabled(false);
                }
            }
        });

        return v;
    }

    private void showAgeSelectDialog() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_age_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Select Your Age");
        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(99);
        numberPicker.setMinValue(16);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                settingsViewModel.selectedValueFromPicker = i1;
                Log.d("sa", "onValueChange: " + i1);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                age_tv.setText(String.valueOf(settingsViewModel.selectedValueFromPicker));
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();

    }


    private void getMyUid() {
        appPreferences = new AppPreferences(getContext());
        myUid = appPreferences.readUId();
    }

    private void initViews() {
        profile_image = v.findViewById(R.id.profile_image);
        signOut_tv = v.findViewById(R.id.signOut_tv);
        username_tv = v.findViewById(R.id.username_tv);
        age_tv = v.findViewById(R.id.age_tv);
        status_edit_iv = v.findViewById(R.id.status_edit_iv);
        status_tv = v.findViewById(R.id.status_tv);
        age_edit_iv = v.findViewById(R.id.age_edit_iv);
        nationality_edit_iv = v.findViewById(R.id.nationality_edit_iv);
        nationality_tv = v.findViewById(R.id.nationality_tv);
        save_btn = v.findViewById(R.id.save_btn);
        notification_switch = v.findViewById(R.id.notification_switch);
        disguised_switch = v.findViewById(R.id.disguised_switch);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(), data.getData().toString(), Toast.LENGTH_SHORT).show();
                Uri selectedImageURI = data.getData();
                File imageFile = new File(getPath(selectedImageURI));
                try {
                    compressedImageFile = new Compressor(getContext()).compressToFile(imageFile);
                    Log.d("dsds", "onActivityResult: " + Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath());
                    settingsViewModel.setProfilePicture(Uri.fromFile(compressedImageFile), myUid);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        getActivity().startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
