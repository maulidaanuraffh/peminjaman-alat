package com.maulida.kinanti.peminjamanalat.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maulida.kinanti.peminjamanalat.R;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PINJAM_REQUEST_CODE = 101;
    private static final int PROFILE = 102;
    private static final int WRITE_REQUEST_CODE = 103;
    TextView btPinjam;
    ImageView profile;
    ConstraintLayout syarat, cara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btPinjam = findViewById(R.id.btpinjam);
        profile = findViewById(R.id.profile);
        syarat = findViewById(R.id.syarat);
        cara = findViewById(R.id.cara);

        btPinjam.setOnClickListener(this);
        syarat.setOnClickListener(this);
        cara.setOnClickListener(this);

        setupPermissions();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bthome);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bthome) {
                return true;
            } else if (item.getItemId() == R.id.btdata) {
                startActivity(new Intent(getApplicationContext(), DaftarPeminjamanActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btpinjam) {
            Intent pinjamIntent = new Intent(this, PinjamActivity.class);
            startActivityForResult(pinjamIntent, PINJAM_REQUEST_CODE);
        } else if(view.getId() == R.id.profile) {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivityForResult(profileIntent, PROFILE);
        } else if(view.getId() == R.id.syarat) {
            showSyaratDialog();
        } else if(view.getId() == R.id.cara) {
            showCaraDialog();
        }
    }

    private void showSyaratDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_syarat);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    private void showCaraDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_cara);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    private void setupPermissions() {
        int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
    }

    // Fungsi untuk mendapatkan direktori penyimpanan eksternal
    private File getExternalStorageDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    }
}