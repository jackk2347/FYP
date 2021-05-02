/*Author By Koo Chung Hing */
/*Date: 2-4-2021 */

package com.example.eshop;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private CircleImageView UserProfile;
    private DatabaseReference reference;
    private String userID;
    private TextView UserName;
    private TextView Balance;
    private ImageView profile,purchase,qrcode;
    private Button ChangeProfile;
    private StorageReference storageReference,pic_storge;
    private Intent intent;
    private int PICK_CONTACT = 1;
    private String datalist;
    private Uri uri;
    private GifImageView refersh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("  AutoShop");
        actionBar.setIcon(R.drawable.logo2);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        UserName = (TextView) findViewById(R.id.Name);
        Balance = (TextView) findViewById(R.id.Balance);

        profile = (ImageView) findViewById(R.id.profile);
        purchase = (ImageView) findViewById(R.id.purchase);
        qrcode = (ImageView) findViewById(R.id.qrcode);

        UserProfile = (CircleImageView)findViewById(R.id.UserProfile);
        ChangeProfile = (Button)findViewById(R.id.ChangeProfile);
        refersh = (GifImageView)findViewById(R.id.refersh);


        ChangeProfile.setOnClickListener(this);
        profile.setOnClickListener(this);
        purchase.setOnClickListener(this);
        qrcode.setOnClickListener(this);
        refersh.setOnClickListener(this);

        storageReference = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        if(user.getPhotoUrl()!=null){
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(UserProfile);
        }


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile !=null){
                    String fullName = userProfile.getLastName()+" "+userProfile.getFirstName();
                    int balance = userProfile.getBalance();

                    UserName.setText(fullName);
                    Balance.setText(String.valueOf(balance));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainPageActivity.this,"Database cannot connect !",Toast.LENGTH_LONG).show();
            }
        });





    }


    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.profile:
                    startActivity(new Intent(this,Profile.class));
                    break;

                case R.id.qrcode:
                    startActivity(new Intent(this,QRcodeActivity.class));
                    break;

                case R.id. ChangeProfile:
                    startActivity(new Intent(this,ModifyIcon.class));
                    break;

                case R.id. refersh:
                    startActivity(new Intent(this,MainPageActivity.class));
                   break;
            }
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
                startActivity(new Intent(MainPageActivity.this,MainPageActivity.class));
                break;
            case R.id.profle:
                startActivity(new Intent(MainPageActivity.this,Profile.class));
                break;
                case R.id.qrpayment:
                startActivity(new Intent(MainPageActivity.this,QRcodeActivity.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainPageActivity.this,MainActivity.class));
                Toast.makeText(MainPageActivity.this, "Logout!", Toast.LENGTH_LONG).show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }



}
