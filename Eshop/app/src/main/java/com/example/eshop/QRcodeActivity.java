/*Author By Koo Chung Hing */
/*Date: 2-4-2021 */

package com.example.eshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRcodeActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ImageView qrcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("  Qrcode Payment");
        actionBar.setIcon(R.drawable.logo2);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        qrcode = (ImageView) findViewById (R.id.qrcode);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile !=null){
                    String Email = userProfile.getEmail().toString();
                    QRGEncoder qrgEncoder = new QRGEncoder(Email,null, QRGContents.Type.TEXT,10);
                    Bitmap qrBits = qrgEncoder.getBitmap();
                    qrcode.setImageBitmap(qrBits);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QRcodeActivity.this,"Qrcode is failed !",Toast.LENGTH_LONG).show();
            }
        });





    }

    @Override
    public void onClick(View v) {

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.mainpage:
                startActivity(new Intent(QRcodeActivity.this,MainPageActivity.class));
                break;
            case R.id.profle:
                startActivity(new Intent(QRcodeActivity.this,Profile.class));
                break;
            case R.id.qrpayment:
                startActivity(new Intent(QRcodeActivity.this,QRcodeActivity.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(QRcodeActivity.this,MainActivity.class));
                Toast.makeText(QRcodeActivity.this, "Logout!", Toast.LENGTH_LONG).show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
