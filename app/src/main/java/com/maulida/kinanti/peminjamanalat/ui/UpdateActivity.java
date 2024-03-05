package com.maulida.kinanti.peminjamanalat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.maulida.kinanti.peminjamanalat.R;
import com.maulida.kinanti.peminjamanalat.data.Send;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateActivity extends AppCompatActivity {
    private static final int VIEW = 101;
    private String code;

    private TextView tvKode, tvNama, tvNomor, tvTanggal, tvBarang, tvStatus, btUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            code = b.getString("id_pinjam");
        }

        tvKode = findViewById(R.id.tvkode);
        tvNama = findViewById(R.id.tvnama);
        tvNomor = findViewById(R.id.tvnomor);
        tvTanggal = findViewById(R.id.tvtanggal);
        tvBarang = findViewById(R.id.tvbarang);
        tvStatus = findViewById(R.id.tvstatus);

        tvKode.setText(code);
        tvNama.setText(b.getString("nama_pinjam"));
        tvNomor.setText(b.getString("no_hp"));
        tvTanggal.setText(b.getString("tgl_pinjam"));
        tvBarang.setText(b.getString("kode_barang"));
        tvStatus.setText(b.getString("status"));

        btUpdate = findViewById(R.id.btupdate);
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(view);
            }
        });
    }

    public void update(View view) {
        Response.Listener<String> lis = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean success = json.getBoolean("success");
                    if (success) {
                        Intent intent = new Intent(UpdateActivity.this, DaftarPeminjamanActivity.class);
                        startActivityForResult(intent, VIEW);
                        finish();
                    } else {
                        Toast.makeText(UpdateActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(UpdateActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        Send send = new Send(code, lis);
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(send);
    }

}