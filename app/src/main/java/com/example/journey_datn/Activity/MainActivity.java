package com.example.journey_datn.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.journey_datn.Model.Diary;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.Model.User;
import com.example.journey_datn.R;
import com.example.journey_datn.db.FirebaseDB;
import com.example.journey_datn.fragment.FragmentAtlas;
import com.example.journey_datn.fragment.FragmentCalendar;
import com.example.journey_datn.fragment.FragmentJourney;
import com.example.journey_datn.fragment.FragmentMedia;
import com.example.journey_datn.fragment.Weather.FragmentWeather;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import static com.example.journey_datn.Activity.SearchActivity.RESULT_CODE;

public class MainActivity extends AppCompatActivity implements FragmentCalendar.onDataChangeListener, FragmentMedia.onDataChangeListenerM{

    private BottomNavigationView btnNavigationView;
    private ImageView img_search, img_cloud;
    private int WRITE_EXTERNAL_STORAGE_CODE = 1, MY_CAMERA_PERMISSION_CODE = 2, READ_EXTERNAL_STORAGE_CODE = 3, idClick, idFragment;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    public static String userId, firstName, lastName;
    private NavigationView navigationView;
    public static ArrayList<Entity> entityList = new ArrayList<>();
    public static ArrayList<Diary> diaryList = new ArrayList<>();
    private FirebaseDB firebaseDB;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissions();
        init();
        firebaseDB = new FirebaseDB(MainActivity.userId);
        entityList = (ArrayList<Entity>) firebaseDB.getAllEntity();
        diaryList = (ArrayList<Diary>) firebaseDB.getAllDiary();

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        loadFragment(new FragmentJourney());
        idFragment = 1;
        btnNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_journey:
                        loadFragment(new FragmentJourney());
                        idFragment = 1;
                        return true;
                    case R.id.menu_calendar:
                        FragmentCalendar fc = new FragmentCalendar();
                        loadFragment(fc);
                        fc.setOnDataChangeListener(MainActivity.this);
                        idFragment = 2;
                        return true;
                    case R.id.menu_media:
                        FragmentMedia fm = new FragmentMedia();
                        loadFragment(fm);
                        fm.setOnDataChangeListener(MainActivity.this);
                        idFragment = 3;
                        return true;
                    case R.id.menu_atlas:
                        loadFragment(new FragmentAtlas());
                        idFragment = 4;
                        return true;
                    case R.id.menu_weather:
                        loadFragment(new FragmentWeather());
                        idFragment = 5;
                        return true;
                }
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_diary:
                        Intent intent = new Intent(MainActivity.this, DairyActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idClick = idFragment;
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_CODE){
            boolean check = data.getBooleanExtra("checkUpdate", false);
            if (check){
                switch (idClick){
                    case 1:
                        loadFragment(new FragmentJourney());
                        return;
                    case 2:
                        loadFragment(new FragmentCalendar());
                        return;
                    case 3:
                        loadFragment(new FragmentMedia());
                        return;
                    case 4:
                        loadFragment(new FragmentAtlas());
                        return;
                    case 5:
                       loadFragment(new FragmentWeather());
                       return;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        loadFragment(new FragmentJourney());
        btnNavigationView.getMenu().getItem(0).setChecked(true);
        toolbar.setVisibility(View.VISIBLE);
        btnNavigationView.setVisibility(View.VISIBLE);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_contain, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permissions() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "external permission granted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "external permission denied", Toast.LENGTH_LONG).show();
        }
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
        }
        if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "external permission granted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "external permission denied", Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        btnNavigationView = findViewById(R.id.navigation);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        img_search = findViewById(R.id.img_search);
        img_cloud = findViewById(R.id.img_cloud);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");
        userId = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        if (firstName.equals("null")) firstName = "";
        if (lastName.equals("null")) lastName = "";
        navigationView.getMenu().getItem(0).setTitle(lastName + " " + firstName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChange(boolean change) {
        if (change){
            loadFragment(new FragmentJourney());
            btnNavigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public void onDataChangeM(boolean change) {
        if (change){
            loadFragment(new FragmentJourney());
            btnNavigationView.getMenu().getItem(0).setChecked(true);
        }
    }
}

