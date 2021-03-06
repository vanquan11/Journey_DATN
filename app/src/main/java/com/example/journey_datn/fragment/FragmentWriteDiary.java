package com.example.journey_datn.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.journey_datn.Activity.MainActivity;
import com.example.journey_datn.Adapter.AdapterRcvAllDiary;
import com.example.journey_datn.Model.Diary;
import com.example.journey_datn.R;
import com.example.journey_datn.db.FirebaseDB;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentWriteDiary extends Fragment implements AdapterRcvAllDiary.OnClickItemTab1 {
    private ImageView imgBack, imgCalendar, imgMark, imgDelete;
    private TextView txtDate;
    private EditText edtDiary, edtTitle;
    private int mDay, mMonth, mYear;
    private FirebaseDB firebaseDB = new FirebaseDB(MainActivity.userId);
    private AdapterRcvAllDiary.OnClickItemTab1 onClickItemTab1 = this;
    private String idUpdate = "";
    private  String today;
    clickItemDiary clickItemDiary;

    public AdapterRcvAllDiary.OnClickItemTab1 getOnClickItemTab1() {
        return onClickItemTab1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_diary, container, false);
        init(view);

        getCalendar();
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimePicker();
            }
        });

        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimePicker();
            }
        });

        imgMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strDate = txtDate.getText().toString();
                String strTitle = edtTitle.getText().toString();
                String strContent = edtDiary.getText().toString();
                if (!strContent.equals("") && !strTitle.equals("")){
                    if (!idUpdate.equals("")){
                        Diary diary = new Diary(idUpdate, strDate, strTitle, strContent);
                        firebaseDB.updateDiary(diary.getId(), diary);
                        idUpdate = "";
                    }else {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        String diaryId = mDatabase.child(MainActivity.userId).push().getKey();
                        Diary diary = new Diary(diaryId, strDate, strTitle, strContent);
                        firebaseDB.insertDiary(diary);
                    }
                    edtTitle.setText("");
                    edtDiary.setText("");
                    hideKeyboard(getActivity());
                }
                if (strContent.equals("")) edtDiary.setError(getString(R.string.content_not_empty));
                if (strTitle.equals("")) edtTitle.setError(getString(R.string.title_not_empty));
            }
        });

        return view;
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void dateTimePicker() {
        getCalendar();
        mMonth = mMonth - 1;
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                Date date = c.getTime();
                SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                txtDate.setText(format1.format(date));
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getCalendar() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Date date = c.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        today = format1.format(date);
        mMonth = mMonth + 1;
        txtDate.setText(today);
    }

    private void dialogDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage(getString(R.string.delete_content));
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edtDiary.setText("");
                edtTitle.setText("");
                txtDate.setText(today);
                idUpdate = "";
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void init(View view){
        imgBack = view.findViewById(R.id.img_back_write_diary);
        imgCalendar = view.findViewById(R.id.img_calendar_write_diary);
        imgMark = view.findViewById(R.id.img_mark_write_diary);
        imgDelete = view.findViewById(R.id.img_delete_write_diary);
        txtDate = view.findViewById(R.id.txt_date_write_diary);
        edtDiary = view.findViewById(R.id.edt_write_diary);
        edtTitle = view.findViewById(R.id.edt_title_write_diary);
    }

    @Override
    public void onClickItem(String id, String title, String content, String date) {
        edtDiary.setText(content);
        edtTitle.setText(title);
        txtDate.setText(date);
        idUpdate = id;
        clickItemDiary.onClickDiary(true);
    }

    public void setOnClickDiary(clickItemDiary clickDiary){
        this.clickItemDiary = clickDiary;
    }

    public  interface  clickItemDiary{
        void onClickDiary(boolean isClick);
    }
}
