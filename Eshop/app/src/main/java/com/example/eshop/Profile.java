/*Author By Koo Chung Hing */
/*Date: 2-4-2021 */

package com.example.eshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile  extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        final TextView Emails = (TextView) findViewById(R.id.Email);
        final TextView Fullname = (TextView) findViewById(R.id.Fullname);
        final TextView Birthday = (TextView) findViewById(R.id.Birthday);
        final TextView Sex = (TextView) findViewById(R.id.Sex);
        final TextView acbalance = (TextView) findViewById(R.id.acbalance);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("  Profile");
        actionBar.setIcon(R.drawable.logo2);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


    user = FirebaseAuth.getInstance().getCurrentUser();
    reference = FirebaseDatabase.getInstance().getReference("Users");
    userID = user.getUid();

    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            User userProfile = snapshot.getValue(User.class);

            if(userProfile !=null){
                String fullName = userProfile.getLastName()+" "+userProfile.getFirstName();
                String Email = userProfile.getEmail();
                String sex = userProfile.getSex();
                String birthday = userProfile.getBirthday();
                int balance = userProfile.getBalance();

                Emails.setText(Email);
                Fullname.setText(fullName);
                Birthday.setText(birthday);
                Sex.setText(sex);
                acbalance.setText(String.valueOf(balance));
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(Profile.this,"Profile connect is failed",Toast.LENGTH_LONG).show();
        }
    });


    }







    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.mainpage:
                startActivity(new Intent(Profile.this,MainPageActivity.class));
                break;
            case R.id.profle:
                startActivity(new Intent(Profile.this,Profile.class));
                break;
            case R.id.qrpayment:
                startActivity(new Intent(Profile.this,QRcodeActivity.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this,MainActivity.class));
                Toast.makeText(Profile.this, "Logout!", Toast.LENGTH_LONG).show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }





}
