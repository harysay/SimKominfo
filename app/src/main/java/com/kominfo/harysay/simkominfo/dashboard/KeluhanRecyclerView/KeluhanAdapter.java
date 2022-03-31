package com.kominfo.harysay.simkominfo.dashboard.KeluhanRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kominfo.harysay.simkominfo.R;

import java.util.List;

public class KeluhanAdapter extends RecyclerView.Adapter<KeluhanAdapter.MyViewHolder> {

    private List<Keluhan> keluhanList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView opd, tanggal, keterangan;

        public MyViewHolder(View view) {
            super(view);
            opd = (TextView) view.findViewById(R.id.opd);
            keterangan = (TextView) view.findViewById(R.id.keterangan);
            tanggal = (TextView) view.findViewById(R.id.tanggal_daftaraduan);
        }
    }


    public KeluhanAdapter(List<Keluhan> keluhansList) {
        this.keluhanList = keluhansList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_daftar_keluhan_listrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Keluhan keluh = keluhanList.get(position);
        holder.opd.setText(keluh.getOpd());
        holder.keterangan.setText(keluh.getKeterangan());
        holder.tanggal.setText(keluh.getTanggal());
    }

    @Override
    public int getItemCount() {
        return keluhanList.size();
    }
}
