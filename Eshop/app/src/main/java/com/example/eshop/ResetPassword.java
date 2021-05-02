/*Author By Koo Chung Hing */
/*Date: 2-4-2021 */

package com.example.eshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener{
    private EditText Email;
    private Button resetpw;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpw_page);

        Email = (EditText) findViewById(R.id.Email);
        resetpw = (Button) findViewById(R.id.resetpw);

        resetpw.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.resetpw:
                resetPassword();
                break;
        }
    }

    private void resetPassword(){
        String email = Email.getText().toString().trim();

        if(email.isEmpty()){
            Email.setError("Email is required !");
            Email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Please input valid email !");
            Email.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPassword.this,"Check your email to reset your password !",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ResetPassword.this,MainActivity.class));
                }else{
                    Toast.makeText(ResetPassword.this,"Try again!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}
