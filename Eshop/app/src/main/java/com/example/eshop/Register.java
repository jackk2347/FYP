/*Author By Koo Chung Hing */
/*Date: 2-4-2021 */

package com.example.eshop;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private EditText Email,password,confirmpassword,FirstName,LastName,Brithday;

    private RadioGroup SexGroup;
    private Button Register;
    private FirebaseAuth mAuth;
    private DatePickerDialog.OnDateSetListener setListener;
    private RadioButton sex;

    public Register() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        mAuth = FirebaseAuth.getInstance();
        Email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        FirstName = (EditText) findViewById(R.id.FirstName);
        LastName = (EditText) findViewById(R.id.LastName);
        Brithday = (EditText) findViewById(R.id.Brithday);
        Register = (Button) findViewById(R.id. Register);
        SexGroup = (RadioGroup) findViewById(R.id.SexGroup);

        Register.setOnClickListener(this);

        Brithday.setOnClickListener(this);

        int selected = SexGroup.getCheckedRadioButtonId();
        sex = (RadioButton) findViewById(selected);

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.Register:
                checkregisterform();
                break;
            case R.id.Brithday:
                BirthdayOption();
                break;
        }
    }

    private void BirthdayOption(){
        Calendar calendar = getInstance();
        final int year = calendar.get(YEAR);
        final int month = calendar.get(MONTH);
        final int day = calendar.get(DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                Brithday.setText(date);
            }
        },year,month,day);
        datePickerDialog.show();
    }




    private void checkregisterform(){
        String Emails = Email.getText().toString().trim();
        String passwords =  password.getText().toString().trim();
        String confirmpasswords = confirmpassword.getText().toString().trim();
        String  FirstNames  = FirstName.getText().toString().trim();
        String LastNames = LastName.getText().toString().trim();
        String Brithdays = Brithday.getText().toString().trim();
        String Sex = sex.getText().toString().trim();


        if(Emails.isEmpty()){
            Email.setError("Email is required !");
            Email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Emails).matches()){
            Email.setError("Please input valid email !");
            Email.requestFocus();
            return;
        }

        if(passwords.isEmpty()){
            password.setError("Password is required !");
            password.requestFocus();
            return;
        }

        if(passwords.length()<6){
            password.setError("Min password length should be 6 characters");
            password.requestFocus();
            return;
        }



        if(confirmpasswords.isEmpty()){
            confirmpassword.setError("Confirm Password is required !");
            confirmpassword.requestFocus();
            return;
        }else if(!confirmpasswords.equals(passwords)){
            confirmpassword.setError("Confirm Password is not same with password !");
            confirmpassword.requestFocus();
            return;
        }

        if(FirstNames.isEmpty()){
            FirstName.setError("First Name is required !");
            FirstName.requestFocus();
            return;
        }

        if(LastNames.isEmpty()){
            LastName.setError("Last Name is required !");
            LastName.requestFocus();
            return;
        }

        if(Brithdays.isEmpty()){
            Brithday.setError("Birthday is required !");
            Brithday.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(Emails,passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(Emails,confirmpasswords,FirstNames,LastNames,Brithdays,Sex,0);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setClass(Register.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Register.this, "Failed to Register!Try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(Register.this, "Failed to Register!Try again", Toast.LENGTH_LONG).show();
                }
            }
        });



    }




}

