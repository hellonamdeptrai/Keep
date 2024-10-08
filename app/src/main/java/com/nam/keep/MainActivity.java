package com.nam.keep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
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
import com.nam.keep.ui.note.EditNoteActivity;
import com.nam.keep.ui.search.SearchActivity;
import com.nam.keep.ui.setting.SettingActivity;
import com.nam.keep.ui.user.ProfileActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomAppBar = findViewById(R.id.bottom_app_bar);

        setSupportActionBar(binding.appBarMain.toolbar);

        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        if (shortcutManager.isRequestPinShortcutSupported()) {
            // Tạo đối tượng ShortcutInfo cho từng tùy chọn nút bấm nhanh
            ShortcutInfo shortcut1 = new ShortcutInfo.Builder(this, "shortcut1")
                    .setShortLabel("Văn bản ghi chú mới")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_baseline_lightbulb_circle_24))
                    .setIntent(new Intent(this, AddNoteActivity.class).setAction("shortcut1_action"))
                    .build();
            ShortcutInfo shortcut2 = new ShortcutInfo.Builder(this, "shortcut2")
                    .setShortLabel("Danh sách mới")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_outline_check_box_keep_24))
                    .setIntent(new Intent(this, AddNoteActivity.class).setAction("shortcut2_action").putExtra("check_box_shortcut", true))
                    .build();
            ShortcutInfo shortcut3 = new ShortcutInfo.Builder(this, "shortcut3")
                    .setShortLabel("Văn bản giọng nói mới")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_baseline_mic_none_keep_24))
                    .setIntent(new Intent(this, AddNoteActivity.class).setAction("shortcut3_action").putExtra("mic_shortcut", true))
                    .build();
            ShortcutInfo shortcut4 = new ShortcutInfo.Builder(this, "shortcut4")
                    .setShortLabel("Ghi chứ ảnh mới")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_outline_image_keep_24))
                    .setIntent(new Intent(this, AddNoteActivity.class).setAction("shortcut4_action").putExtra("image_shortcut", true))
                    .build();

            // Đăng ký các tùy chọn nút bấm nhanh với ShortcutManager
            shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut1, shortcut2, shortcut3, shortcut4));
        }

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
                R.id.nav_home, R.id.nav_reminder, R.id.nav_label, R.id.nav_archive)
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
                } else if (itemId == R.id.nav_archive) {
                    navController.navigate(R.id.nav_archive);
                }
                // Đóng drawer sau khi chọn một mục
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intentNote = new Intent(MainActivity.this, AddNoteActivity.class);
                switch (item.getItemId()) {
                    case R.id.home_add_check_box:
                        AddNoteActivity.isOpenCheckBox = true;
                        startActivity(intentNote);
                        return true;
                    case R.id.home_add_brush:
                        AddNoteActivity.isOpenAddBrush = true;
                        startActivity(intentNote);
                        return true;
                    case R.id.home_add_mic:
                        AddNoteActivity.isOpenAddMic = true;
                        startActivity(intentNote);
                        return true;
                    case R.id.home_add_image:
                        AddNoteActivity.isOpenAddImage = true;
                        startActivity(intentNote);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.login_menu_item);
        MenuItem itemSearch = menu.findItem(R.id.search_menu_item);
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
        itemSearch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intentSearch= new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intentSearch);
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