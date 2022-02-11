package com.example.maintainmore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    Toolbar toolbar;
    Button buttonSignup, buttonLogin;

    EditText email, password;


    @Override
    protected void onStart() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user!=null) {
            startActivity(new Intent(this, MainActivity.class));
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

        email = findViewById(R.id.editText_email);
        password = findViewById(R.id.editText_password);


        buttonLogin.setOnClickListener(view -> Login());
        buttonSignup.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SignupActivity.class)));

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void Login() {

        String emailId = email.getText().toString().trim();
        String Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(emailId)){
            email.setError("Please Enter EmailId");
            return;
        }
        if (TextUtils.isEmpty(Password)){
            password.setError("Please Enter Password");

            return;
        }


//        progressBar.setVisibility(View.VISIBLE);
        final SweetAlertDialog progressDialog= new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setTitleText("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(emailId, Password)
                .addOnCompleteListener(LoginActivity.this, task -> {
//                        progressBar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {

//                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        SweetAlertDialog sweetAlertDialog= new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText("Login Successful");
                        sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            sweetAlertDialog1.dismissWithAnimation();
                        }).setCanceledOnTouchOutside(false);
                        sweetAlertDialog.show();


                    } else {

//                            Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.ERROR_TYPE).
                                setTitleText(Objects.requireNonNull(task.getException()).getMessage()).show();
                    }
                });

    }

}