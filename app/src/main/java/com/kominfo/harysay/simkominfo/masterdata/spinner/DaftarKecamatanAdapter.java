package com.kominfo.harysay.simkominfo.masterdata.spinner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kominfo.harysay.simkominfo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harysay on 04 Jan 2018 004.
 */

public class DaftarKecamatanAdapter extends ArrayAdapter<DaftarKecamatan> {
    private List<DaftarKecamatan> stateList = new ArrayList<>();

    public DaftarKecamatanAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<DaftarKecamatan> stateList) {
        super(context, resource, spinnerText, stateList);
        this.stateList = stateList;
    }

    @Override
    public DaftarKecamatan getItem(int position) {
        return stateList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    /**
     * Gets the state object by calling getItem and
     * Sets the state name to the drop-down TextView.
     *
     * @param position the position of the item selected
     * @return returns the updated View
     */
    private View initView(int position) {
        DaftarKecamatan namkec = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.daftar_kecamatan_list, null);
        TextView textView =  v.findViewById(R.id.namakecamatanSpinnerText);
        textView.setText(namkec.getNamaKecamatan());
        return v;

    }
}
