package com.kominfo.harysay.simkominfo.masterdata.adapter;

/**
 * Created by DISHUBKOMINFO on 23/01/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kominfo.harysay.simkominfo.R;
import com.kominfo.harysay.simkominfo.masterdata.MasterDataKendala;

import java.util.List;

public class AdapterMasterKendala extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<MasterDataKendala> items;

    public AdapterMasterKendala(Activity activity, List<MasterDataKendala> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.fragment_master_kendala_listitem, null);

        TextView norut = (TextView) convertView.findViewById(R.id.nourut_masterkendala);
        TextView iddatapekerjaan = (TextView) convertView.findViewById(R.id.id_data_masterkendala);
        TextView judulKendala = (TextView) convertView.findViewById(R.id.namamasterkendala);
        TextView keteranganKenda = (TextView) convertView.findViewById(R.id.keterangankendala);

        MasterDataKendala data = items.get(position);

        norut.setText(data.getNoUrut());
        iddatapekerjaan.setText(data.getIdKendala());
        judulKendala.setText(data.getJudulKendala());
        keteranganKenda.setText(data.getKeterangan());

        return convertView;
    }

}

