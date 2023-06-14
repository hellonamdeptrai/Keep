package com.nam.keep.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nam.keep.MainActivity;
import com.nam.keep.R;
import com.nam.keep.api.ApiClient;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.User;
import com.nam.keep.ui.signup.SignUpActivity;
import com.nam.keep.utils.UtilsFunction;

public class LoginActivity extends AppCompatActivity {
    // view
    Button mBtnLogin;
    EditText mEmail, mPassword;

    // data
    private DatabaseHelper dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView mSignUp= findViewById(R.id.txtSignUp);
        mBtnLogin = findViewById(R.id.btnLogin);
        mEmail = findViewById(R.id.edit_text_email_login);
        mPassword = findViewById(R.id.edit_text_password_login);

        dataSource = new DatabaseHelper(LoginActivity.this);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLogin = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intentLogin);
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email không được để trống!");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Email không đúng định dạng!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Mật khẩu không được để trống!");
                    return;
                }
                ApiClient apiClient = new ApiClient(LoginActivity.this);
                User user = new User();
                user.setEmail(mEmail.getText().toString());
                user.setPassword(mPassword.getText().toString());
                user.setDevice_name("mobile");
                apiClient.loginUser(LoginActivity.this, user);
//                if (dataSource.login(new User(
//                        email, UtilsFunction.hashPassword(password)
//                ))) {
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
//                }

            }
        });
    }
}