/*Author By Koo Chung Hing */
/*Date: 2-4-2021 */

package com.example.eshop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class WeclomeActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState)  {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.loadingpage);

        }
    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WeclomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }, 2000);
    }
    }
