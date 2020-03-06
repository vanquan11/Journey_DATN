package com.example.journey_datn.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.MyOptionsPickerView;
import com.bumptech.glide.Glide;
import com.example.journey_datn.Adapter.AdapterRcvAdd;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener, AdapterRcvAdd.OnItemClickListener {

    private ImageView img_mark, img_calendar_add, img_tag_add, img_three_dots_add;
    private TextView txt_day_add, txt_month_add, txt_year_add, txt_hour_add, txt_minute_add;
    private EditText edt_contain_add;
    private RecyclerView rcv_add;
    private AdapterRcvAdd adapterRcvAdd;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    private int mYear, mMonth, mDay, mHour, mMinute;

    private String th;
    private String position = "",srcImage = "";
    private int temperature = 0, action = R.drawable.ic_action_black_24dp , mood = R.drawable.ic_mood_black_24dp;
    private  String contain;
    private int day, month, year, hour, minute;
    private static final int CAMERA_CODE = 0, GALLERY_CODE = 1;
    private int mposition;
    private String urri_random;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        init();
        adapterRcvAdd = new AdapterRcvAdd(this);
        adapterRcvAdd.setListener(this);
        rcv_add.setLayoutManager(linearLayoutManager);
        rcv_add.setAdapter(adapterRcvAdd);
        img_mark.setOnClickListener(this);
        img_three_dots_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        img_calendar_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimePicker();
            }
        });

        getCalendar();
        txt_day_add.setText( mDay + "");
        txt_month_add.setText( mMonth + "");
        txt_year_add.setText( mYear + "");
        txt_hour_add.setText( mHour + "");
        txt_minute_add.setText( mMinute + "");

        urri_random = UUID.randomUUID() +".jpg";
    }

    private void getCalendar(){
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        getDayofMonth(mDay, mMonth, mYear);
        mMonth = mMonth + 1;
    }

    private void getDayofMonth(int day, int month, int year){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        day = day - 1;
        Date date = new Date(year, month, day);
        th = sdf.format(date);
    }

    private void dateTimePicker(){
        getCalendar();
        mMonth = mMonth - 1;
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txt_hour_add.setText(hourOfDay + "");
                txt_minute_add.setText(minute + "");
            }
        }, mHour, mMinute, false);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txt_day_add.setText(dayOfMonth + "");
                        int month = monthOfYear + 1;
                        txt_month_add.setText(month + "");
                        txt_year_add.setText(year + "");

                        getDayofMonth(dayOfMonth, monthOfYear, year);
                        timePickerDialog.show();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discard");
        builder.setMessage("Do you want to discard the changes?");
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void init(){
        img_mark = findViewById(R.id.img_mark);
        img_calendar_add = findViewById(R.id.img_calendar_add);
        img_tag_add = findViewById(R.id.img_tag_add);
        img_three_dots_add = findViewById(R.id.img_three_dots_add);
        edt_contain_add = findViewById(R.id.edt_contain_add);
        txt_day_add = findViewById(R.id.txt_day_add);
        txt_month_add = findViewById(R.id.txt_month_add);
        txt_year_add = findViewById(R.id.txt_year_add);
        txt_hour_add = findViewById(R.id.txt_hour_add);
        txt_minute_add = findViewById(R.id.txt_minute_add);
        rcv_add = findViewById(R.id.rcv_add);
    }

    @Override
    public void onClick(View v) {
      contain = edt_contain_add.getText().toString();
      day = Integer.parseInt(txt_day_add.getText().toString());
      month = Integer.parseInt(txt_month_add.getText().toString());
      year = Integer.parseInt(txt_year_add.getText().toString());
      hour = Integer.parseInt(txt_hour_add.getText().toString());
      minute = Integer.parseInt(txt_minute_add.getText().toString());

        if (TextUtils.isEmpty(contain)) {
            Toast.makeText(this, "No contain", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = getIntent();
            Entity entity = new Entity(contain, action, position, temperature, year, month, day, th, hour, minute, mood, srcImage);
            intent.putExtra("entity", entity);
            setResult(113, intent);
            finish();
        }
    }

    @Override
    public void OnItemClick(int position) {
        mposition = position;
        switch (position){
            case 0:
                mediaClick();
                break;
            case 1:
                placeClick();
                break;
            case 2:
                temperatureClick();
                break;
            case 3:
                pickerView();
                break;
            case 4:
                faceClick();
                break;
            case 5:
                boldClick();
                break;
            case 6:
                italicClick();
                break;
            case 7:
                underlineClick();
                break;
            case 8:
                backClick();
                break;
            case 9:
                forwardClick();
                break;
        }
    }

    private void mediaClick(){
        final View dialogView = View.inflate(this,R.layout.dialog_media,null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);

        ImageView imgGallery = dialog.findViewById(R.id.img_dl_gallery);
        ImageView imgFile = dialog.findViewById(R.id.img_dl_file);
        ImageView imgPhoto = dialog.findViewById(R.id.img_dl_photo);
        ImageView imgVideo = dialog.findViewById(R.id.img_dl_video);
        ImageView imgMicrophone = dialog.findViewById(R.id.img_dl_microphone);

        dialog.show();
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , GALLERY_CODE);
                dialog.dismiss();
            }
        });

        imgFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose file", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(),  urri_random);
                Uri uri = FileProvider.getUriForFile(AddDataActivity.this, AddDataActivity.this.getApplicationContext().getPackageName() + ".provider", file);
                m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(m_intent, CAMERA_CODE);
                dialog.dismiss();
            }
        });
        imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose video", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        imgMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose microphone", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CAMERA_CODE:
                File file = new File(Environment.getExternalStorageDirectory(), urri_random);
                Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                Glide.with(this).load(uri).into(img_tag_add);
                srcImage = uri.toString();
                break;
            case GALLERY_CODE:
                Uri selectedImage = data.getData();
                Glide.with(this).load(selectedImage).into(img_tag_add);
                srcImage = String.valueOf(selectedImage);
                break;
        }
    }

    private void placeClick(){
        final View dialogView = View.inflate(this,R.layout.dialog_pick_place,null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);

        ImageView imgPick = dialog.findViewById(R.id.img_dl_pick_place);
        ImageView imgRename = dialog.findViewById(R.id.img_dl_rename_place_pick_place);
        ImageView imgRemove = dialog.findViewById(R.id.img_dl_remove_place);
        ImageView imgSetup = dialog.findViewById(R.id.img_dl_setup_gps);

        imgPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose pick", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        imgRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose rename", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose remove", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        imgSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose setup", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void pickerView(){
        MyOptionsPickerView singlePicker = new MyOptionsPickerView(AddDataActivity.this);
        final ArrayList<String> items = new ArrayList<>();
        items.add("None");
        items.add("Stationary");
        items.add("Eating");
        items.add("Walking");
        items.add("Running");
        items.add("Biking");
        items.add("Automotive");
        items.add("Flying");
        singlePicker.setPicker(items);
        singlePicker.setTitle("Activity");
        singlePicker.setCyclic(false);
        singlePicker.setSelectOptions(0);
        singlePicker.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if (items.get(options1) == items.get(0)){
                    action = R.drawable.ic_action_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(1)){
                    action = R.drawable.ic_accessibility_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(2)){
                    action = R.drawable.ic_restaurant_menu_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(3)){
                    action = R.drawable.ic_directions_walk_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(4)){
                    action = R.drawable.ic_directions_run_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(5)){
                    action = R.drawable.ic_directions_bike_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(6)){
                    action = R.drawable.ic_directions_car_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(7)){
                    action = R.drawable.ic_airplanemode_active_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
            }
        });

        singlePicker.show();
    }

    private void temperatureClick(){
        final View dialogView = View.inflate(this,R.layout.dialog_temperature,null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);

        dialog.show();
    }

    private void faceClick(){
        final View dialogView = View.inflate(this,R.layout.dialog_face,null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);

        ImageView imgHeart = dialog.findViewById(R.id.img_dl_heart);
        ImageView imgHappy = dialog.findViewById(R.id.img_dl_happy);
        ImageView imgGrinning = dialog.findViewById(R.id.img_dl_grinning);
        ImageView imgSad = dialog.findViewById(R.id.img_dl_sad);
        ImageView imgNeutral = dialog.findViewById(R.id.img_dl_neutral);

        imgHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.ic_favorite_red_24dp;
                adapterRcvAdd.updateItem(mposition, mood);
                dialog.dismiss();
            }
        });

        imgHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.ic_happy_red_24dp;
                adapterRcvAdd.updateItem(mposition, mood);
                dialog.dismiss();
            }
        });

        imgGrinning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.ic_mood_emoticon_red_24dp;
                adapterRcvAdd.updateItem(mposition, mood);
                dialog.dismiss();
            }
        });

        imgSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.ic_sad_red_24dp;
                adapterRcvAdd.updateItem(mposition, mood);
                dialog.dismiss();
            }
        });

        imgNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.ic_neutral_red_24dp;
                adapterRcvAdd.updateItem(mposition, mood);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void boldClick(){
        Toast.makeText(AddDataActivity.this, "click bold", Toast.LENGTH_SHORT).show();
    }

    private void italicClick(){
        Toast.makeText(AddDataActivity.this, "click italick", Toast.LENGTH_SHORT).show();
    }

    private void underlineClick(){
        Toast.makeText(AddDataActivity.this, "click underline", Toast.LENGTH_SHORT).show();
    }

    private void backClick(){
        Toast.makeText(AddDataActivity.this, "click back", Toast.LENGTH_SHORT).show();
    }

    private void forwardClick(){
        Toast.makeText(AddDataActivity.this, "click forward", Toast.LENGTH_SHORT).show();
    }
}