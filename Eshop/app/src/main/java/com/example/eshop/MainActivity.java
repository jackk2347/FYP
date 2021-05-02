/*Author By Koo Chung Hing */
/*Date: 2-4-2021 */

package com.example.eshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText Email,password;
    private Button Login;
    private TextView Register,Forget;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Email = (EditText)findViewById(R.id.Email);
        password = (EditText)findViewById(R.id.password);
        Login = (Button)findViewById(R.id.Login);
        Register = (TextView)findViewById(R.id.Register);
        Forget = (TextView)findViewById(R.id.Forget);

        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
        Forget.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();




    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.Register:
                startActivity(new Intent(this,Register.class));
                break;

            case R.id.Login:
                Login();
                break;

            case R.id.Forget:
                startActivity(new Intent(this,ResetPassword.class));
                break;
        }
    }

    private void Login() {
        String email = Email.getText().toString().trim();
        String Password = password.getText().toString().trim();

        if (email.isEmpty()) {
            Email.setError("Email is required");
            Email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Please enter a vaild email");
            Email.requestFocus();
            return;
        }

        if (Password.isEmpty()) {
            password.setError("Email is required");
            password.requestFocus();
            return;
        }
        if (Password.length() < 6) {
            password.setError("Min password length is 6 characters!");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                 if(user.isEmailVerified()){
                     startActivity(new Intent(MainActivity.this,MainPageActivity.class));
                 }else{
                     user.sendEmailVerification();
                     Toast.makeText(MainActivity.this,"Check your email to verify your account!",Toast.LENGTH_LONG).show();
                 }


                }else {
                    Toast.makeText(MainActivity.this,"Failed to Login! Please check your credentials",Toast.LENGTH_LONG).show();
                }
            }
        });




    }
}