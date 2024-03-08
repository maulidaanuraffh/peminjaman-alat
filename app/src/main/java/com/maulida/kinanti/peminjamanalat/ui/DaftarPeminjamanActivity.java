package com.maulida.kinanti.peminjamanalat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maulida.kinanti.peminjamanalat.R;
import com.maulida.kinanti.peminjamanalat.data.DataAdapter;
import com.maulida.kinanti.peminjamanalat.data.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DaftarPeminjamanActivity extends AppCompatActivity {
    private RequestQueue vol = null;
    private final String url = "http://172.16.202.209/pinjambarang/view.php";
    private ArrayList<Database> arr = new ArrayList<>();
    private DataAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_peminjaman);
        vol = Volley.newRequestQueue(this);
        adapter = new DataAdapter(arr, DaftarPeminjamanActivity.this);  // Initialize the adapter
        listView = findViewById(R.id.list_peminjaman);
        listView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.btdata);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bthome) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.btdata) {
                return true;
            }
            return false;
        });

        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            arr.clear(); // Clear the array before adding new data
                            JSONArray j = response.getJSONArray("alldata");
                            for (int i = 0; i < j.length(); i++) {
                                JSONObject jes = j.getJSONObject(i);
                                Log.d("Data Debug", "id_pinjam: " + jes.getString("id_pinjam"));
                                String id_pinjam = jes.getString("id_pinjam");
                                String nama_pinjam = jes.getString("nama_pinjam");
                                String no_hp = jes.getString("no_hp");
                                String ktp = jes.getString("ktp");
                                String tgl_pinjam = jes.getString("tgl_pinjam");
                                String kode_barang = jes.getString("kode_barang");
                                String status = jes.getString("status");
                                arr.add(new Database(id_pinjam, nama_pinjam, no_hp, ktp, tgl_pinjam, kode_barang, status));
                            }
                            adapter.notifyDataSetChanged();  // Notify adapter that data has changed
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "error", error); // Menambahkan error ke log
                Toast.makeText(DaftarPeminjamanActivity.this, "Sambungan Gagal", Toast.LENGTH_SHORT).show();
            }
        });

        vol.add(json);
    }

}