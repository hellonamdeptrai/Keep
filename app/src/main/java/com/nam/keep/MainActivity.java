package com.nam.keep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.nam.keep.databinding.ActivityMainBinding;
import com.nam.keep.notification.NotificationHelper;
import com.nam.keep.ui.login.LoginActivity;
import com.nam.keep.ui.note.AddNoteActivity;
import com.nam.keep.ui.setting.SettingActivity;
import com.nam.keep.ui.user.ProfileActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddNote = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intentAddNote,0);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_reminder, R.id.nav_label)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_setting) {
                    // Mở SettingActivity
                    Intent intentSetting = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intentSetting);
                } else if (itemId == R.id.nav_home) {
                    // Mở HomeFragment
                    navController.navigate(R.id.nav_home);
                } else if (itemId == R.id.nav_reminder) {
                    // Mở ReminderFragment
                    navController.navigate(R.id.nav_reminder);
                } else if (itemId == R.id.nav_label) {
                    // Mở LabelFragment
                    navController.navigate(R.id.nav_label);
                }
                // Đóng drawer sau khi chọn một mục
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.login_menu_item);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                if (token.isEmpty()) {
                    Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                } else {
                    Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intentProfile);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}