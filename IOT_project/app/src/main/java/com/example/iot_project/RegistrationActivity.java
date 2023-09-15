package com.example.iot_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    private TextView loginNowTextView;
    private ProgressBar progressBar;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new DatabaseHelper(this);

        usernameEditText = findViewById(R.id.EEusername);
        emailEditText = findViewById(R.id.EEmail);
        passwordEditText = findViewById(R.id.EPassword);
        confirmPasswordEditText = findViewById(R.id.Ecpassword);
        registerButton = findViewById(R.id.btnRegister);
        loginNowTextView = findViewById(R.id.loginNow);
        progressBar = findViewById(R.id.LProgressBar);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (validateInputs(username, email, password, confirmPassword)) {
                    // Registration is valid, proceed to save data
                    saveUserData(username, email, password);
                    navigateToUserHome();
                }
            }
        });

        loginNowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLoginActivity();
            }
        });
    }

    private boolean validateInputs(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveUserData(String username, String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
    }

    private void navigateToUserHome() {
        startActivity(new Intent(RegistrationActivity.this, home.class));
        finish(); // Close this activity so the user can't go back to the registration form
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(RegistrationActivity.this, loginactivity.class);
        startActivity(intent);
    }
}
