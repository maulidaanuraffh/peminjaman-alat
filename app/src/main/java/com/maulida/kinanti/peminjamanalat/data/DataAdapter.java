package com.maulida.kinanti.peminjamanalat.data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maulida.kinanti.peminjamanalat.R;
import com.maulida.kinanti.peminjamanalat.ui.UpdateActivity;

import java.util.ArrayList;

public class DataAdapter extends BaseAdapter {

    private ArrayList<Database> adapter;
    private Context context;

    public DataAdapter(ArrayList<Database> arr, Context con) {
        adapter = arr;
        context = con;
    }

    @Override
    public int getCount() {
        return adapter.size();
    }

    @Override
    public Object getItem(int position) {
        return adapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_peminjaman, null);
        }

        Database data = adapter.get(position);

        TextView tvKode = view.findViewById(R.id.kode);
        TextView tvNama = view.findViewById(R.id.nama);
        TextView tvNoHP = view.findViewById(R.id.nohp);
        TextView tvTgl = view.findViewById(R.id.tgl);
        TextView tvBarang = view.findViewById(R.id.barang);
        TextView tvStatus = view.findViewById(R.id.status);

        tvNama.setText(data.getNamaPinjam());
        tvNoHP.setText(data.getNoHp());
        tvTgl.setText(data.getTglPinjam());
        tvBarang.setText(data.getKodeBarang());
        tvStatus.setText(data.getStatus());

        // Set data to your views
        tvKode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id_pinjam", data.getIdPinjam());
                intent.putExtra("nama_pinjam", data.getNamaPinjam());
                intent.putExtra("no_hp", data.getNoHp());
                intent.putExtra("tgl_pinjam", data.getTglPinjam());
                intent.putExtra("kode_barang", data.getKodeBarang());
                intent.putExtra("status", data.getStatus());
                context.startActivity(intent);
            }
        });

        return view;
    }
}

