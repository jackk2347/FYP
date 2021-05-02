/*Author By Koo Chung Hing */
/*Date: 2-4-2021 */

package com.example.eshop;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ModifyIcon extends AppCompatActivity implements View.OnClickListener {
    private StorageReference storageReference,pic_storge;
    private FirebaseUser user;
    private String userID;
    private Intent intent;
    private int PICK_CONTACT = 1;
    private String datalist;
    private Uri uri;
    private CircleImageView UserProfile;
    private Button ChangeProfile,Exit;
    private DatabaseReference reference;
    private String TAG;
    private UserProfileChangeRequest request;
    private int TAKE_IMAGE_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyicon);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("  Edit Icon");
        actionBar.setIcon(R.drawable.logo2);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        ChangeProfile = (Button) findViewById (R.id.ChangeProfile);
        Exit = (Button) findViewById (R.id.Exit);
        ChangeProfile.setOnClickListener(this);

        Exit.setOnClickListener(this);

        UserProfile = (CircleImageView) findViewById (R.id.UserProfile);
        storageReference = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        if(user.getPhotoUrl()!=null){
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(UserProfile);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ChangeProfile:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent,TAKE_IMAGE_CODE);
                }
                break;



            case R.id.Exit:
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
                startActivity(new Intent(this,MainPageActivity.class));
                break;
            case R.id.profle:
                startActivity(new Intent(this,Profile.class));
                break;
            case R.id.qrpayment:
                startActivity(new Intent(this,QRcodeActivity.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,MainActivity.class));
                Toast.makeText(this, "Logout!", Toast.LENGTH_LONG).show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }


   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
   super.onActivityResult(requestCode, resultCode, data);
   if(requestCode == TAKE_IMAGE_CODE){
       switch(resultCode){
           case RESULT_OK:
               Bitmap bitmap = (Bitmap)data.getExtras().get("data");
               UserProfile.setImageBitmap(bitmap);
               handleUpload(bitmap);
       }
   }
    }

    private void handleUpload(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        pic_storge = FirebaseStorage.getInstance().getReference().child("profile_image").child(userID+ ".jpeg");

        pic_storge.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDowloadUrl(pic_storge);
            }
        })
          .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Log.e(TAG,"onFailure: ",e.getCause());
              }
          });
    }

        private void getDowloadUrl (StorageReference pic_storge){
        pic_storge.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG,"onSuccess: "+uri);
                setUserProfileUrI(uri);
            }
        });
    }

    private void setUserProfileUrI(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ModifyIcon.this, "Profile image succesfully  !",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ModifyIcon.this, "Profile image failed!",Toast.LENGTH_LONG).show();
            }
        });
    }
}



