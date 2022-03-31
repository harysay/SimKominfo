package com.kominfo.harysay.simkominfo.dashboard;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kominfo.harysay.simkominfo.handler.Server;
import com.kominfo.harysay.simkominfo.R;
import com.kominfo.harysay.simkominfo.handler.SessionManager;
import com.kominfo.harysay.simkominfo.handler.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Created by harysay on 06/06/2017.
 */
public class DashboardFragmentAttendChart extends Fragment {
    public static final String TAG = DashboardFragmentAttendChart.class.getSimpleName();
    SessionManager session;
    String sysId, token;
//    private ListView lv;
    List<Pengaduan> pengaduanDash;
    String jumBelumDikerjakan, jumSudahDanBlmSelesai,jumSudahDanSelesai;
    List<PieEntry> pieEntries;
//    ArrayList<HashMap<String, String>> daftarPengaduan;
    String jumlah[]={"6","3","1"};//= new String[3];//;
    String jenis[] = {"Sudah","Proses", "Belum"};
    View v;
    Calendar c = Calendar.getInstance();
    String perBulan = new SimpleDateFormat("MMM yyyy").format(c.getTime());
    public DashboardFragmentAttendChart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        session = new SessionManager(getActivity().getApplication());
        v =inflater.inflate(R.layout.dashboard_piechartlistview, container, false);

//        daftarPengaduan = new ArrayList<>();
//        lv = (ListView)v.findViewById(R.id.dashpengaduanlistview);
        HashMap<String, String> userid = session.getUserDetails();
        sysId = userid.get(SessionManager.KEY_ID);
        token = userid.get(SessionManager.KEY_TOKEN);
        new GetDaftarPengaduan(getActivity(),v).execute();
        // Inflate header view
//        ViewGroup headerView = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.dashboard_aduan_listheader, lv,false);
        // Add header view to the ListView
//        lv.addHeaderView(headerView);
        return v;
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
                    JSONObject dataPelaksana = jsonObj.getJSONObject("data");
                    String dataPelaksanaUsername = dataPelaksana.getString("username");
                    String dataPelaksanaAkses = dataPelaksana.getString("akses");
                    String dataPelaksanaNama = dataPelaksana.getString("nama");
                    String dataPelaksanaJumlahAduan = dataPelaksana.getString("jumlah_aduan");
                    jumBelumDikerjakan = dataPelaksana.getString("belum_dikerjakan");
                    jumSudahDanBlmSelesai = dataPelaksana.getString("sudah_dikerjakan_dan_belum_selesai");
                    jumSudahDanSelesai = dataPelaksana.getString("sudah_dikerjakan_dan_selesai");

//                    JSONArray dataPengaduanTerbaru = jsonObj.getJSONArray("pengaduan_baru");
//                    pengaduanDash = new ArrayList<>();
//                    for (int i = 0; i < dataPengaduanTerbaru.length(); i++) {
//                        JSONObject c = dataPengaduanTerbaru.getJSONObject(i);
//                        String noUrutPengaduan= c.getString("nomor");
//                        String tanggalPengaduan = c.getString("tanggal_pengaduan");
//                        String kecamatanPengaduan= c.getString("kecamatan");
//                        String opdPengaduan = c.getString("opd");
//                        String keteranganPengaduan = c.getString("keterangan");
//                        String statusPerrbaikanPengaduan = c.getString("status_perbaikan");
////
//                        HashMap<String, String> aduanArray = new HashMap<>();
//
//                        aduanArray.put("norut", noUrutPengaduan);
//                        aduanArray.put("tglpengaduan", tanggalPengaduan);
//                        aduanArray.put("kecamatan", kecamatanPengaduan);
//                        aduanArray.put("opd", opdPengaduan);
//                        aduanArray.put("keterangan", keteranganPengaduan);
//                        aduanArray.put("statusperbaikan", statusPerrbaikanPengaduan);
//                        daftarPengaduan.add(aduanArray);
//                    }
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

            //new getDataAttendance(getActivity(),v).execute();
            setupPieChart();
//            ListAdapter adapter = new SimpleAdapter(getActivity(), daftarPengaduan,
//                    R.layout.dashboard_daftaraduan_row, new String[]{ "norut","tglpengaduan","opd","keterangan"},
//                    new int[]{R.id.dashtahunsaldo,R.id.dashplafondleave,R.id.dashusedleave,R.id.dashunusedleave});
//            lv.setAdapter(adapter);
            pdLoading.dismiss();
        }
    }

//    public class getDataAttendance extends AsyncTask<Void, Void, Void> {
//        private Context mContext;
//        private View rootView;
//
//        public getDataAttendance(Context context, View rootView){
//            this.mContext = context;
//            this.rootView = rootView;
//        }
//
//        ProgressDialog pdAttendanceLoading = new ProgressDialog(getActivity());
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pdAttendanceLoading.setMessage("\tLoading Chart...");
//            pdAttendanceLoading.setCancelable(false);
//            pdAttendanceLoading.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            HttpHandler sh = new HttpHandler();
//
//
//            // Making a request to url and getting response
//            String urlAttend = Server.URL+"/bukihcisapp/web/mobile/attendancedataallpaging?go=1&fr=31&in=&by=&creatorId="+sysId+"&adminId="+sysId+"&startDate=01/"+startDate+"&endDate="+intervalDate+"&period="+periodDate;//+periodDate;
//            String jsonStrAttend = sh.makeServiceCall(urlAttend);
//
//            Log.e(TAG, "Response from url: " + jsonStrAttend);
//            if (jsonStrAttend != null) {
//                try {
//                    JSONObject jsonObjAttend = new JSONObject(jsonStrAttend);
//
//                    // Getting JSON Array node
//                    JSONArray employeeListAttend = jsonObjAttend.getJSONArray("resultDataList");
//                    // looping through All Contacts
//
//                    for (int i = 0; i < employeeListAttend.length(); i++) {
//                        JSONObject c = employeeListAttend.getJSONObject(i);
//                        String attendanceDate = c.getString("Calender_date");
//                        String attendanceStatus = c.getString("Attendance_std_code");
//                        String no_requestReference = c.getString("Norequest_reference");
//                        String reference_value = c.getString("Reference_value");
//
//                        try {
//                            Date dateAttd = new SimpleDateFormat("dd/MM/yyyy").parse(attendanceDate);
//                            Date dateNow = new SimpleDateFormat("dd/MM/yyyy").parse(DateSkrg);
//                            if(dateAttd.before(dateNow)||dateAttd.equals(dateNow)){
//                                if (!no_requestReference.equals("null") && !reference_value.equals("null")){
//                                    jumCuti++;
//                                }else if(no_requestReference.equals("null")&&attendanceStatus.equals("2")){
//                                    jumAlpha++;
//                                }else{
//                                    jumMasuk++;
//                                }
//
//                                if(attendanceStatus.equals("10")|| attendanceStatus.equals("11") ||attendanceStatus.equals("12")||attendanceStatus.equals("13")){
//                                    jumMasuk--;
//                                }
//                            }
//
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                    //if (jumMasuk != 0){}
//                    //pengurKrnLibur = jumMasuk-jumLibur;
//
//
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getActivity().getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//
//            } else {
//                Log.e(TAG, "Couldn't get json from server.");
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity().getApplicationContext(),
//                                "Couldn't get data. Check your connection or contact to Administrator for possible errors!",
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            setupPieChart();
//            pdAttendanceLoading.dismiss();
//        }
//
//
//    }

    public void setupPieChart(){
        pieEntries = new ArrayList<>();

        jumlah[0] = jumSudahDanSelesai; //
        jumlah[1] = jumSudahDanBlmSelesai;//
        jumlah[2] = jumBelumDikerjakan;//
        for(int i = 0; i<jumlah.length;i++){
            pieEntries.add(new PieEntry(Float.parseFloat(jumlah[i]),jenis[i%jenis.length]+"\n"+jumlah[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries,"");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(10f);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        //data.setValueTypeface(tf);
        //Get the chart
        PieChart chart = (PieChart) v.findViewById(R.id.leaveChart);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setCenterText(generateCenterSpannableText());

        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        chart.setEntryLabelColor(ColorTemplate.rgb("#000000"));
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        //chart.setOnChartValueSelectedListener(this);
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();
//        chart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
//
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                Toast.makeText(getActivity().getApplicationContext(),"Clicked"+jumlah,
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Data per "+perBulan);
//        s.setSpan(new RelativeSizeSpan(1.5f), 0, 14, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 14, s.length(), 0);

        return s;
    }

}
