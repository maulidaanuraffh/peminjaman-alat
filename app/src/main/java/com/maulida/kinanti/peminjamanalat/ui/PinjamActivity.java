package com.maulida.kinanti.peminjamanalat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.maulida.kinanti.peminjamanalat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class PinjamActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA = 1;
    private static final int GALERY = 2;
    private static final int GALLERY_REQUEST_CODE = 3;
    private static final int CAMERA_REQUEST_CODE = 123;

    private int tahun = 0;
    private int bulan = 0;
    private int hari = 0;

    private IntentIntegrator intentIntegrator;
    private ImageView imvkp, back;
    private TextView id_pinjam, txtglpinjam, txkodebarang, txnamabarang, btnpinjam;
    private EditText txnamapinjam, txnohp;
    private Button btngalery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinjam);

        random(6);
        permissionCamera();

        Calendar cal = Calendar.getInstance();
        bulan = cal.get(Calendar.MONTH);
        hari = cal.get(Calendar.DAY_OF_MONTH);
        tahun = cal.get(Calendar.YEAR);

        imvkp = findViewById(R.id.imvkp);
        id_pinjam = findViewById(R.id.id_pinjam);
        txtglpinjam = findViewById(R.id.txtglpinjam);
        txkodebarang = findViewById(R.id.txkodebarang);
        txnamabarang = findViewById(R.id.txnamabarang);
        txnamapinjam = findViewById(R.id.txnamapinjam);
        txnohp = findViewById(R.id.txnohp);
        btngalery = findViewById(R.id.btngalery);
        btnpinjam = findViewById(R.id.btn_pinjam);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        findViewById(R.id.btntglmasuk).setOnClickListener(this);
        intentIntegrator = new IntentIntegrator(this);
        findViewById(R.id.btnscan).setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bthome) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
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

    private void permissionCamera() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
    }

    public void insert(View view) {
        Response.Listener<String> lis = response -> {
            try {
                Log.d("Insert", "Server response: " + response);

                // Setelah data berhasil tersimpan, kosongkan isian data
                txtglpinjam.setText("DD/MM/YYYY");
                txnamapinjam.setText("");
                txnohp.setText("");
                imvkp.setImageDrawable(null);
                txkodebarang.setText("");
                txnamabarang.setText("");
                // Set id_pinjam baru
                random(6);
                // Tampilkan toast
                Toast.makeText(PinjamActivity.this, "Data berhasil tersimpan!", Toast.LENGTH_SHORT).show();
                // ...
            } catch (Exception e) {
                Toast.makeText(PinjamActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        Bitmap bit = ((BitmapDrawable) imvkp.getDrawable()).getBitmap();
        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, arr);

        String img = Base64.encodeToString(arr.toByteArray(), Base64.DEFAULT);

        Send send = new Send(id_pinjam.getText().toString(), txnamapinjam.getText().toString(),
                txnohp.getText().toString(), img, txtglpinjam.getText().toString(),
                txkodebarang.getText().toString(), lis);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(send);
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera"};

        pictureDialog.setItems(pictureDialogItems, (dialog, which) -> {
            switch (which) {
                case 0:
                    choosePhotoFromGallery();
                    break;
                case 1:
                    takePhotoFromCamera();
                    break;
            }
        });

        pictureDialog.show();
    }

    private String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/pinjambarang");
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), myBitmap, "Title", null);
        Uri uri = Uri.parse(path);

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + " " + txnamapinjam.toString() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return "";
    }

    private void random(int length) {
        StringBuilder sb = new StringBuilder(length);
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random r = new Random();

        // Inisialisasi id_pinjam dengan referensi objek View yang benar
        id_pinjam = findViewById(R.id.id_pinjam);

        for (int i = 0; i < length; i++) {
            int index = r.nextInt(alphabet.length());
            sb.append(alphabet.charAt(index));
        }

        id_pinjam.setText(sb.toString());
    }


    private DatePickerDialog.OnDateSetListener dateChangeDialog = (view, year, month, dayOfMonth) -> {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(cal.getTime());

        txtglpinjam.setText(formattedDate);
        tahun = year;
        bulan = month;
        hari = dayOfMonth;
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 20:
                return new DatePickerDialog(this, dateChangeDialog, tahun, bulan, hari);
            default:
                return super.onCreateDialog(id);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnscan) {
            intentIntegrator.setBeepEnabled(true).initiateScan();
        } else if (v.getId() == R.id.btntglmasuk) {
            showDialog(20);
        } else if (v.getId() == R.id.btngalery) {
            image(v);
        } else if (v.getId() == R.id.btn_pinjam) {
            insert(v);
        } else if(v.getId() == R.id.back) {
            onBackPressed();
        }
    }

    public void image(View view) {
        // Implementasi untuk btngalery di sini
        showPictureDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Camera capture is successful
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            handleCameraCaptureSuccess(thumbnail);
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(PinjamActivity.this, "Gambar Tersimpan!", Toast.LENGTH_SHORT).show();
                    imvkp.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(PinjamActivity.this, "Gagal!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Pemindaian dibatalkan", Toast.LENGTH_SHORT).show();
                } else {
                    // QR code scanning is successful, process the result
                    String qrResult = result.getContents();
                    processQRResult(qrResult);
                }
            }
        }
    }

    private void processQRResult(String qrResult) {
        try {
            // Menganalisis JSON dari hasil pemindaian QR code
            JSONObject jsonObject = new JSONObject(qrResult);

            // Mendapatkan nilai dari objek JSON
            String kodeBarang = jsonObject.optString("kode_barang", "");
            String namaBarang = jsonObject.optString("nama_barang", "");

            // Menetapkan nilai ke TextViews
            txkodebarang.setText(kodeBarang);
            txnamabarang.setText(namaBarang);
        } catch (JSONException e) {
            e.printStackTrace();
            // Tidak dapat mengurai JSON, menampilkan pesan kesalahan
            Toast.makeText(this, "Format JSON QR code tidak sesuai", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCameraCaptureSuccess(Bitmap bitmap) {
        Toast.makeText(this, "Camera capture successful!", Toast.LENGTH_SHORT).show();
        imvkp.setImageBitmap(bitmap);
        saveImage(bitmap);
    }

    private class Send extends StringRequest {
        private static final String URL = "http://192.168.0.105/pinjambarang/insert.php";
        private final Map<String, String> map = new HashMap<>();

        public Send(String id_pinjam, String nama_pinjam, String no_hp, String ktp, String tgl_pinjam, String kode_barang, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null); // Change Method.POST
            map.put("id_pinjam", id_pinjam);
            map.put("nama_pinjam", nama_pinjam);
            map.put("no_hp", no_hp);
            map.put("ktp", ktp);
            map.put("tgl_pinjam", tgl_pinjam);
            map.put("kode_barang", kode_barang);
        }

        @Override
        protected Map<String, String> getParams() {
            return map;
        }
    }
}