package com.kominfo.harysay.simkominfo.tindakanteknis;

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
import com.kominfo.harysay.simkominfo.masterdata.AppController;
import com.kominfo.harysay.simkominfo.masterdata.MasterDataKendala;
import com.kominfo.harysay.simkominfo.masterdata.adapter.AdapterMasterKendala;

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

public class FragmentTindakanTeknis extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SessionManager session;
    String empId,tokenku;
    ArrayList<HashMap<String, String>> payslipPeriod;
    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<MasterDataKendala> itemList = new ArrayList<MasterDataKendala>();
    AdapterMasterKendala adapter;
    String success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View v;
    View dialogView;
    EditText txt_jamselesai, txt_uraianpekerjaan;
    TextView txt_idmasterkendala;
    String idMasterKendala, judul, uraianpek;
    private ProgressDialog pDialog;

    private static final String TAG = FragmentTindakanTeknis.class.getSimpleName();

    private static String url_read      = Server.URL + "kendala_jaringan?token="; //Read MTk3MDA4MjgxOTk3MDMxMDEy
    private static String url_create    = Server.URL + "kendala_jaringan/insert?token="; //Create
    private static String url_edit      = Server.URL + "kendala_jaringan/editdetail?token="; //Edit
    private static String url_update    = Server.URL + "kendala_jaringan/update?token="; //Update
    private static String url_delete    = Server.URL + "kendala_jaringan/delete?token="; //Delete

    public static final String TAG_ID       = "id";
    public static final String TAG_NOMORURUT = "no";
    public static final String TAG_JUDUL   = "judul";
    public static final String TAG_KETERANGAN    = "keterangan";
    private static final String TAG_SUCCESS = "status";
    private static final String TAG_MESSAGE = "message";

    String json_obj_simkominfo = "json_obj_simkominfo_req";

    public FragmentTindakanTeknis() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_master_kendala,container,false);
       session = new SessionManager(getActivity().getApplication());
        payslipPeriod = new ArrayList<>();
        HashMap<String, String> userid = session.getUserDetails();
        empId = userid.get(SessionManager.KEY_ID);
        tokenku = userid.get(SessionManager.KEY_TOKEN);
        //getActivity().setTitle("Master Kendala");
//        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
//        getActivity().setSupportActionBar(toolbar);

        fab     = (FloatingActionButton) v.findViewById(R.id.fab_add_masterkendala);
        swipe   = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_masterkendala);
        list    = (ListView) v.findViewById(R.id.listmasterkendala);

        adapter = new AdapterMasterKendala(getActivity(), itemList);
        list.setAdapter(adapter);


        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
//                           swipe.setRefreshing(true);
//                           itemList.clear();
//                           adapter.notifyDataSetChanged();
                           koneksi();
                       }
                   }
        );

        // manggil dialog catatan
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //displayLoader();
                //postData(url_edit+tokenku);
                DialogForm("", "","", "SIMPAN");
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Aufruf", "onItemClick");
                final String idx = itemList.get(position).getIdKendala();

                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(idx);
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
        itemList.clear();
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
                                MasterDataKendala item = new MasterDataKendala();

                                item.setNoUrut(jsonOb.getString(TAG_NOMORURUT));
                                item.setIdKendala(jsonOb.getString(TAG_ID));
                                item.setJudulKendala(jsonOb.getString(TAG_JUDUL));
                                item.setKeterangan(jsonOb.getString(TAG_KETERANGAN));
                                // menambah item ke array
                                itemList.add(item);
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
    private void DialogForm(String idmasterkendala, String judulkendala, String keterangankendala, String button) {
        dialog = new AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialogform_data_kendala, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_tambahmaster);
        if (button.equals("UBAH")){
            dialog.setTitle("Ubah Master Kendala");
        }else{
            dialog.setTitle("Master Kendala");
        }
        txt_idmasterkendala = (TextView) dialogView.findViewById(R.id.txt_idmasterkendala);
        txt_jamselesai    = (EditText) dialogView.findViewById(R.id.txt_jamselesai);
        txt_uraianpekerjaan  = (EditText) dialogView.findViewById(R.id.txt_uraianpekerjaan);

        if (!idmasterkendala.isEmpty()){ // mengisi datanya yang diubah
            txt_idmasterkendala.setText(idmasterkendala);
            txt_jamselesai.setText(judulkendala);
            txt_uraianpekerjaan.setText(keterangankendala);
            //displayLoader();
        } else {
            kosong();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                idMasterKendala      = txt_idmasterkendala.getText().toString();
                judul    = txt_jamselesai.getText().toString();
                uraianpek  = txt_uraianpekerjaan.getText().toString();

                simpan_update();
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
    private void simpan_update() {
        String url;
        itemList.clear();
        // jika id kosong maka simpan, jika id ada nilainya maka ubah
        if (idMasterKendala.isEmpty()){
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
                if (idMasterKendala.isEmpty()){
                    params.put("judul", judul);
                    params.put("keterangan", uraianpek);
                } else {
                    //ubah
                    params.put("id",idMasterKendala);
                    params.put("judul", judul);
                    params.put("keterangan", uraianpek);

                }

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReqRead, json_obj_simkominfo);
        swipe.setRefreshing(true);
        itemList.clear();
        adapter.notifyDataSetChanged();
//        koneksi();
    }

    // fungsi untuk get edit data
    private void edit(final String idx){
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
                        String idx      = jObj.getString(TAG_ID);
                        String judulkendala  = jObj.getString(TAG_JUDUL);
                        String urpek    = jObj.getString(TAG_KETERANGAN);

                        DialogForm(idx, judulkendala, urpek,"UBAH");

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
                params.put("id", idx);

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
                params.put("id", idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReqDel, json_obj_simkominfo);
    }

    // untuk clear edittext pada form
    private void kosong(){
        txt_idmasterkendala.setText(null);
        txt_jamselesai.setText(null);
        txt_uraianpekerjaan.setText(null);
    }


//    private void displayLoader() {
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading Dropdown Pekerjaan.. Mohon Tunggu...");
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(true);
//        pDialog.show();
//
//    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        koneksi();
    }
}
