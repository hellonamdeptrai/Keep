package com.nam.keep.ui.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.nam.keep.R;
import com.nam.keep.api.ApiClient;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.User;
import com.nam.keep.ui.note.EditNoteActivity;

public class SettingActivity extends AppCompatActivity {

    static LottieAnimationView lottieAnimationView;
    static FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar mToolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cài đặt");

        lottieAnimationView = findViewById(R.id.animation_view);
        frameLayout = findViewById(R.id.fragment_container);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private ListPreference themePreference;
        private Preference syncPreference;
        private ApiClient apiClient;
        DatabaseHelper myDatabase;
        SharedPreferences sharedPreferences;
        String token;
        Long idUser;


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            sharedPreferences = getContext().getSharedPreferences("MyDataLogin", Context.MODE_PRIVATE);
            token = sharedPreferences.getString("token", "");
            idUser = sharedPreferences.getLong("tokenable_id", 0);;

            themePreference = findPreference("pref_key_theme_mode");
            assert themePreference != null;
            themePreference.setSummary(getSelectedTheme());

            themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    String selectedTheme = (String) newValue;
                    setTheme(selectedTheme);
                    themePreference.setSummary(selectedTheme);
                    saveTheme(selectedTheme);
                    return true;
                }
            });

            syncPreference = findPreference("pref_key_sync");
            syncPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Đồng bộ dữ liệu?");
                    builder.setMessage(token.isEmpty() ? "Vui lòng đăng nhập để sử dụng chức năng này" : "Bạn có chắc chắn muốn đồng bộ dữ liệu không?");
                    if (!token.isEmpty()) {
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                apiClient = new ApiClient(getContext());
                                apiClient.getAll(getContext(), lottieAnimationView, frameLayout, idUser);
                            }
                        });
                    }
                    builder.setNegativeButton(token.isEmpty() ?"Đóng" : "Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.nam_keep));
                    alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.nam_keep));

                    return true;
                }
            });
        }

        private String getSelectedTheme() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            return sharedPreferences.getString("pref_key_theme_mode", "auto");
        }

        private void setTheme(String selectedTheme) {
            if (selectedTheme.equals("auto")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            } else if (selectedTheme.equals("dark")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (selectedTheme.equals("light")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }

        private void saveTheme(String selectedTheme) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            sharedPreferences.edit().putString("pref_key_theme_mode", selectedTheme).apply();
        }
    }
}