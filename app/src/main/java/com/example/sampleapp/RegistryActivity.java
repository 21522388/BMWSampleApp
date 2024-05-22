package com.example.sampleapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sampleapp.model.User;

import java.util.regex.Pattern;

public class RegistryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SQLiteConnector connector = new SQLiteConnector(this);

        TextView fEmail = (TextView) findViewById(R.id.formRegisterEmail);
        TextView fUsername = (TextView) findViewById(R.id.formRegisterUsername);
        TextView fPassword = (TextView) findViewById(R.id.formRegisterPassword);
        Button bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = fEmail.getText().toString();
                String username = fUsername.getText().toString();
                String password = fPassword.getText().toString();
                if (email.isEmpty() || username.isEmpty() || password.isEmpty())
                    Toast.makeText(RegistryActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else {
                    if (!isValidEmail(email)) {
                        Toast.makeText(RegistryActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!connector.checkUser(email) && !connector.checkUser(username, password)) {
                        User u = new User();
                        u.setEmail(email);
                        u.setName(username);
                        u.setPassword(password);
                        connector.addUser(u);

                        Toast.makeText(RegistryActivity.this, "Your account has been registered!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                        Toast.makeText(RegistryActivity.this, "This user has already existed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}