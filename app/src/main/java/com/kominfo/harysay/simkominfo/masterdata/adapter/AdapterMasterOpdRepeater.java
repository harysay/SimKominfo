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
import com.kominfo.harysay.simkominfo.masterdata.MasterDataOpdRepeater;

import java.util.List;

public class AdapterMasterOpdRepeater extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<MasterDataOpdRepeater> items;

    public AdapterMasterOpdRepeater(Activity activity, List<MasterDataOpdRepeater> items) {
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
            convertView = inflater.inflate(R.layout.fragment_master_opd_listitem, null);

        TextView norut = (TextView) convertView.findViewById(R.id.nourut_mstopd);
        TextView idmstopd = (TextView) convertView.findViewById(R.id.id_kode_mstopd);
        TextView mstnamakecamatan = (TextView) convertView.findViewById(R.id.mstopd_kecamatan);
        TextView mstnamaopd = (TextView) convertView.findViewById(R.id.mstopd_nama);
        TextView kodekecamat = (TextView) convertView.findViewById(R.id.txt_kodekecamat);

        MasterDataOpdRepeater data = items.get(position);

        norut.setText(data.getNoUrutOpd());
        idmstopd.setText(data.getIdMstOpd());
        mstnamakecamatan.setText(data.getMstNamaKecamatan());
        mstnamaopd.setText(data.getMstNamaOpd());
        kodekecamat.setText(data.getKodeKecamatan());

        return convertView;
    }

}

