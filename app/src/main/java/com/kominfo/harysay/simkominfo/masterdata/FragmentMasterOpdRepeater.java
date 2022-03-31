package com.kominfo.harysay.simkominfo.masterdata;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kominfo.harysay.simkominfo.R;
import com.kominfo.harysay.simkominfo.handler.Server;
import com.kominfo.harysay.simkominfo.handler.SessionManager;
import com.kominfo.harysay.simkominfo.masterdata.adapter.AdapterMasterKendala;
import com.kominfo.harysay.simkominfo.masterdata.adapter.AdapterMasterOpdRepeater;
import com.kominfo.harysay.simkominfo.masterdata.spinner.DaftarKecamatan;
import com.kominfo.harysay.simkominfo.masterdata.spinner.DaftarKecamatanAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.diskominfo.harysay.e_kinerjadraw.Handler.ConfigApp;

/**
 * Created by harysay on 10/02/2018.
 */

public class FragmentMasterOpdRepeater extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SessionManager session;
    String empId,tokenku;
    ArrayList<HashMap<String, String>> payslipPeriod;
    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<MasterDataOpdRepeater> itemList_mstopd = new ArrayList<MasterDataOpdRepeater>();
    AdapterMasterOpdRepeater adapter;
    String success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View v;
    View dialogView;
    Spinner spin_mstkecamatan;
    EditText txt_namaopd;
    TextView tv_idmasteropd,tv_kodekecamat;
    String idMasterOpdRepeater111, ambil_kodekecamatan11, namakecamata, namaopd11;
    private ProgressDialog pDialog;

    private static final String TAG = FragmentMasterOpdRepeater.class.getSimpleName();

    private static String url_read      = Server.URL + "data_opd?token="; //Read MTk3MDA4MjgxOTk3MDMxMDEy
    private static String url_create    = Server.URL + "data_opd/insert?token="; //Create
    private static String url_edit      = Server.URL + "data_opd/edit_detail?token="; //Edit
    private static String url_update    = Server.URL + "data_opd/update?token="; //Update
    private static String url_delete    = Server.URL + "data_opd/delete?token="; //Delete
    private static String url_spinner   = Server.URL + "data_opd/kecamatan?token="; //Spinner

    public static final String TAG_KODEOPD       = "kode_opd";
    public static final String TAG_NOMORURUT = "no";
    public static final String TAG_NAMAKEC   = "nama_kecamatan";
    public static final String TAG_NAMAOPD    = "nama_opd";
    public static final String TAG_KODEKECAMATAN    = "kode_kecamatan";
    private static final String TAG_SUCCESS = "status";
    private static final String TAG_MESSAGE = "message";

    String json_obj_simkominfo = "json_obj_simkominfo_req";

    public FragmentMasterOpdRepeater() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_master_opdrepeater,container,false);
       session = new SessionManager(getActivity().getApplication());
        payslipPeriod = new ArrayList<>();
        HashMap<String, String> userid = session.getUserDetails();
        empId = userid.get(SessionManager.KEY_ID);
        tokenku = userid.get(SessionManager.KEY_TOKEN);
        //getActivity().setTitle("Master Kendala");
//        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
//        getActivity().setSupportActionBar(toolbar);

        fab     = (FloatingActionButton) v.findViewById(R.id.fab_add_masteropd);
        swipe   = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_masteropd);
        list    = (ListView) v.findViewById(R.id.listmasteropd);

        adapter = new AdapterMasterOpdRepeater(getActivity(), itemList_mstopd);
        list.setAdapter(adapter);


        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
//                           swipe.setRefreshing(true);
//                           itemList_mstopd.clear();
//                           adapter.notifyDataSetChanged();
                           koneksi();
                       }
                   }
        );

        // manggil dialog catatan
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayLoader();
                getDataKecamatan(url_spinner+tokenku, "");
                DialogForm("", "","", "","SIMPAN");
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Aufruf", "onItemClick");
                final String idx = itemList_mstopd.get(position).getIdMstOpd();
                final String kodekecamata = itemList_mstopd.get(position).getKodeKecamatan();
                final String namaKecam = itemList_mstopd.get(position).getMstNamaKecamatan();
                final String kodeopd = itemList_mstopd.get(position).getIdMstOpd();
                final String namaopd = itemList_mstopd.get(position).getMstNamaOpd();

                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                //saat klik edit di pop up menu
                                edit(idx,kodekecamata,namaKecam);
                                break;
                            case 1:
                                delete(idx);
                                break;
                        }
                    }
                }).show();
            }
        });
        return v;
    }

    // untuk menampilkan seluruh data pada listview
    private void koneksi(){
        itemList_mstopd.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat req array dengan JSON
        JsonObjectRequest jsonObjSimkom = new JsonObjectRequest(Request.Method.GET,url_read+tokenku, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        JSONArray jsonAr = null;
                        try {
                            jsonAr = response.getJSONArray("data");
                            for(int i=0; i<jsonAr.length();i++){
                                JSONObject jsonOb = (JSONObject) jsonAr.get(i);
                                MasterDataOpdRepeater item = new MasterDataOpdRepeater();

                                item.setNoUrutOpd(jsonOb.getString(TAG_NOMORURUT));
                                item.setIdMstOpd(jsonOb.getString(TAG_KODEOPD));
                                item.setMstNamaOpd(jsonOb.getString(TAG_NAMAOPD));
                                item.setMstNamaKecamatan(jsonOb.getString(TAG_NAMAKEC));
                                item.setMstKodeKec(jsonOb.getString(TAG_KODEKECAMATAN));
                                // menambah item ke array
                                itemList_mstopd.add(item);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        // notifikasi perubahan data adapter
                        adapter.notifyDataSetChanged();

                        swipe.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.e("ERROR", "Error occurred ", error);
                swipe.setRefreshing(false);
            }
        });
        // menambah request ke antrian request
        AppController.getInstance().addToRequestQueue(jsonObjSimkom);
    }

    // untuk menampilkan dialog aktivitas
    private void DialogForm(String kodemstopd, String kodekecamatparam, String keterangankendala, String namakecamat, String button) {
        dialog = new AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialogform_data_opdrepeater, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_tambahmaster);
        if (button.equals("UBAH")){
            dialog.setTitle("Ubah Master OPD/Repeater");
        }else{
            dialog.setTitle("Master OPD/Repeater");
        }
        tv_idmasteropd = (TextView) dialogView.findViewById(R.id.txt_idmasteropdrepeater);
        spin_mstkecamatan    = (Spinner) dialogView.findViewById(R.id.spin_mst_pilihkecamatan);
        txt_namaopd  = (EditText) dialogView.findViewById(R.id.txt_mst_namaopdrepeater);
        tv_kodekecamat = (TextView) dialogView.findViewById(R.id.txt_kodekecparam);

        if (!kodemstopd.isEmpty()){ // mengisi datanya yang diubah
            tv_idmasteropd.setText(kodemstopd);
            //spin_mstkecamatan.setText(judulkendala);
            txt_namaopd.setText(keterangankendala);
            tv_kodekecamat.setText(kodekecamatparam);
            displayLoader();
            getDataKecamatan(url_spinner+tokenku, namakecamat);

        } else {
            kosong();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ambil_kodeopd = tv_idmasteropd.getText().toString();
                DaftarKecamatan state = (DaftarKecamatan) spin_mstkecamatan.getSelectedItem();
                String namakecamatan_dapat = state.getNamaKecamatan();
                String namopd = txt_namaopd.getText().toString();
                String ambil_kodekecamatan = tv_kodekecamat.getText().toString();
                simpan_update(ambil_kodeopd, ambil_kodekecamatan, namakecamatan_dapat, namopd);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
            }
        });

        dialog.show();
    }

    // fungsi untuk simpan atau ubah
    private void simpan_update(String kodeopdget, String kodekecamatanget, String kecamatanget, String namaopdget) {
        itemList_mstopd.clear();
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka ubah
        if (kodeopdget.isEmpty()){
            url = url_create+tokenku;
        } else {
            url = url_update+tokenku;
        }

        StringRequest strReqRead = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getString(TAG_SUCCESS);

                    // Cek error pada json
                    if (success.equals("true")) {
                        Log.d("Add/update", jObj.toString());

                        koneksi();
                        kosong();

                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameter ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka ubah
                if (kodeopdget.isEmpty()){
                    params.put("nama_kecamatan", kecamatanget);
                    params.put("nama_opd", namaopdget);
                } else {
                    //ubah
                    params.put("kode_opd",kodeopdget);
                    params.put("kode_kecamatan", kodekecamatanget);
                    params.put("nama_opd", namaopdget);
                    params.put("nama_kecamatan", kecamatanget);
                }

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReqRead, json_obj_simkominfo);
        swipe.setRefreshing(false);
        itemList_mstopd.clear();
        adapter.notifyDataSetChanged();
//        koneksi();
    }

    // fungsi untuk get edit data
    private void edit(final String idx, final String kodekec_ambil, final String nama_kecamat){
        StringRequest strReqEdit = new StringRequest(Request.Method.POST, url_edit+tokenku, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getString(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success.equals("true")) {
                        Log.d("get edit data", jObj.toString());
                        String kodeopd      = jObj.getString(TAG_KODEOPD);
                        //String judulkendala  = jObj.getString(TAG_NAMAKEC);
                        String namaopd    = jObj.getString(TAG_NAMAOPD);

                        DialogForm(kodeopd, kodekec_ambil, namaopd, nama_kecamat, "UBAH");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post webservice
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode_opd", idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReqEdit, json_obj_simkominfo);
    }

    // fungsi untuk hapus
    private void delete(final String idx){
        StringRequest strReqDel = new StringRequest(Request.Method.POST, url_delete+tokenku, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getString(TAG_SUCCESS);
                    //JSONObject datObj = jObj.getJSONObject("data");

                    // Cek error node pada json
                    if (success.equals("true")) {
                        Log.d("delete", jObj.toString());

                        koneksi();

                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity().getApplicationContext(),"Terjadi kesalahan pada url json!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode_opd", idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReqDel, json_obj_simkominfo);
    }

    // untuk clear edittext pada form
    private void kosong(){
        getDataKecamatan(url_spinner+tokenku,"");
        //spin_mstkecamatan.getSelectedItem();
        //txt_jamselesai.setText(null);
        txt_namaopd.setText(null);
    }

    private void getDataKecamatan(String url, String namakecamatan){

        //Log.d(Tag.getTesting_volley()+" URL :", url);
        JsonObjectRequest dataReq = new JsonObjectRequest(Request.Method.GET, url, null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d(Tag.getTesting_volley(), response.toString());
                try {

                    String status = response.getString("status");

                    if(status.equalsIgnoreCase("true")){
                        ArrayList<DaftarKecamatan> dataMasterList = new ArrayList<>();
                        JSONArray kecamatanArray = response.getJSONArray("data");

                        for (int i=0; i<  kecamatanArray.length(); i++)
                        {
                            JSONObject obj 		    = kecamatanArray.getJSONObject(i);
                            //ambil_kodekecamatan	    = obj.getString("kode_kecamatan");
                            String namaKecamat	= obj.getString ("nama_kecamatan");

                            DaftarKecamatan dataMaster 	= new DaftarKecamatan();
                            dataMaster.setNamaKecamatan(namaKecamat);
                            //dataMaster.setKodeKecamatan(kodeKecamat);

                            dataMasterList.add(dataMaster);
                        }
                        final DaftarKecamatanAdapter pekerjaanAdapter = new DaftarKecamatanAdapter(getActivity(),
                                R.layout.daftar_kecamatan_list, R.id.namakecamatanSpinnerText, dataMasterList);
                        spin_mstkecamatan.setAdapter(pekerjaanAdapter);

                        int position = 0;
                        for(int i=0; i< dataMasterList.size();i++){

                            String jenengKecam = dataMasterList.get(i).getNamaKecamatan().replaceAll("\\\\", "").replaceAll("\\s+","");

                            if(namakecamatan.equalsIgnoreCase(jenengKecam)){
                                position = i;
                            }
                        }
                        spin_mstkecamatan.setSelection(position);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        });

        AppController.getInstance().addToRequestQueue(dataReq);
        pDialog.cancel();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Dropdown Kecamatan.. Mohon Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

    }

    @Override
    public void onRefresh() {
        itemList_mstopd.clear();
        adapter.notifyDataSetChanged();
        koneksi();
    }
}
