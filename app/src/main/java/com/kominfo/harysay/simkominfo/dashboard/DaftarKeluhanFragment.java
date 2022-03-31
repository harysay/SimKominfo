package com.kominfo.harysay.simkominfo.dashboard;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.kominfo.harysay.simkominfo.R;
import com.kominfo.harysay.simkominfo.dashboard.KeluhanRecyclerView.Keluhan;
import com.kominfo.harysay.simkominfo.dashboard.KeluhanRecyclerView.KeluhanAdapter;
import com.kominfo.harysay.simkominfo.dashboard.KeluhanRecyclerView.RecyclerTouchListener;
import com.kominfo.harysay.simkominfo.handler.HttpHandler;
import com.kominfo.harysay.simkominfo.handler.Server;
import com.kominfo.harysay.simkominfo.handler.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DaftarKeluhanFragment extends Fragment {
    public static final String TAG = DaftarKeluhanFragment.class.getSimpleName();
    private List<Keluhan> movieList = new ArrayList<>();
    //ArrayList<HashMap<String, String>> daftarPengaduan;
    private RecyclerView recyclerView;
    private KeluhanAdapter mAdapter;
    SessionManager session;
    String sysId, token;
    private ListView lv;
    List<Pengaduan> pengaduanDash;
    View vskeluhan;

    public DaftarKeluhanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vskeluhan = inflater.inflate(R.layout.fragment_daftar_keluhan, container, false);
        session = new SessionManager(getActivity().getApplication());
        HashMap<String, String> userid = session.getUserDetails();
        sysId = userid.get(SessionManager.KEY_ID);
        token = userid.get(SessionManager.KEY_TOKEN);
        recyclerView = (RecyclerView) vskeluhan.findViewById(R.id.recycler_view_keluhan);

        mAdapter = new KeluhanAdapter(movieList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Keluhan keluh = movieList.get(position);
                Toast.makeText(getActivity(), keluh.getOpd() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
//        DashboardFragmentAttendChart getDaftarPengg = new DashboardFragmentAttendChart();
//        DashboardFragmentAttendChart.GetDaftarPengaduan daftarPengaduan = getDaftarPengg.new GetDaftarPengaduan(getActivity(),vskeluhan);
        new GetDaftarPengaduan(getActivity(),vskeluhan).execute();

        return vskeluhan;
    }

    public class GetDaftarPengaduan extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        private View rootView;
        public GetDaftarPengaduan(Context context, View rootView){
            this.mContext = context;
            this.rootView = rootView;
        }
        ProgressDialog pdLoading = new ProgressDialog(getActivity());//karena di fragment pakainya getActivity()
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading Pengaduan Terbaru...");
            if(sysId == ""){
                Toast.makeText(getActivity().getApplicationContext(),
                        "Couldn't get sysId from server. Contact to Administrator for possible errors!",
                        Toast.LENGTH_LONG).show();
            }
            pdLoading.setCancelable(true);
            pdLoading.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = Server.URL+"profil?token="+token;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray dataPengaduanTerbaru = jsonObj.getJSONArray("pengaduan_baru");
                    //pengaduanDash = new ArrayList<>();
                    for (int i = 0; i < dataPengaduanTerbaru.length(); i++) {
                        JSONObject c = dataPengaduanTerbaru.getJSONObject(i);
                        String noUrutPengaduan= c.getString("nomor");
                        String tanggalPengaduan = c.getString("tanggal_pengaduan");
                        String kecamatanPengaduan= c.getString("kecamatan");
                        String opdPengaduan = c.getString("opd");
                        String keteranganPengaduan = c.getString("keterangan");
                        String statusPerrbaikanPengaduan = c.getString("status_perbaikan");
//
                        Keluhan movie = new Keluhan(opdPengaduan, keteranganPengaduan, tanggalPengaduan);
                        movieList.add(movie);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {

                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Couldn't get data. Check your connection or contact to Administrator for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mAdapter.notifyDataSetChanged();
            pdLoading.dismiss();
        }
    }

}
