package com.nam.keep.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nam.keep.MainActivity;
import com.nam.keep.R;
import com.nam.keep.api.ApiClient;
import com.nam.keep.database.DatabaseHelper;
import com.nam.keep.model.User;
import com.nam.keep.utils.UtilsFunction;

public class SignUpActivity extends AppCompatActivity {

    // view
    TextView mBackLogin;
    Button mBtnSignUp;
    EditText mFullName, mEmail, mPassword, mPasswordConfirmation;

    // data
    private DatabaseHelper dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mBackLogin = findViewById(R.id.txtBackLogin);
        mBtnSignUp = findViewById(R.id.btnSignUp);
        mFullName = findViewById(R.id.edit_text_full_name);
        mEmail = findViewById(R.id.edit_text_email_signup);
        mPassword = findViewById(R.id.edit_text_password_signup);
        mPasswordConfirmation = findViewById(R.id.edit_text_password_confirmation_signup);

        dataSource = new DatabaseHelper(SignUpActivity.this);

        mBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mFullName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String passwordConfirm = mPasswordConfirmation.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    mFullName.setError("Họ và tên không được để trống!");
                    return;
                }
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
                if (TextUtils.isEmpty(passwordConfirm)) {
                    mPasswordConfirmation.setError("Xác nhận mật khẩu không được để trống!");
                    return;
                }
                if (!password.equals(passwordConfirm)) {
                    mPasswordConfirmation.setError("Mật khẩu không khớp!");
                    return;
                }
                ApiClient apiClient = new ApiClient(SignUpActivity.this);
                User user = new User();
                user.setName(mFullName.getText().toString());
                user.setEmail(mEmail.getText().toString());
                user.setPassword(mPassword.getText().toString());
                user.setPassword_confirmation(mPasswordConfirmation.getText().toString());
                apiClient.registerUser(SignUpActivity.this, user);

                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}