package com.kominfo.harysay.simkominfo.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kominfo.harysay.simkominfo.R;
import com.kominfo.harysay.simkominfo.TabDashboardActivity;
import com.kominfo.harysay.simkominfo.handler.HttpHandler;
import com.kominfo.harysay.simkominfo.handler.Server;
import com.kominfo.harysay.simkominfo.handler.SessionManager;
import com.transitionseverywhere.*;


import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class AuthFragment extends Fragment {

    protected Callback callback;
    TextInputEditText loginIdPegawai, loginPass, lupapassId;
    SessionManager session;
    String inputanEmpNIK, inputanPassword, namaPegawai, hakAkses, statusPegawai,nilaiKembalian;
    String returnTokenNik,returnTokenStatus,returnTokenCode;
    String ed_loginIdPegawai,ed_loginPassi;
    public static final String TAG = LoginActivity.class.getSimpleName();
    private UserLoginTask mAuthTask;

    @BindView(com.kominfo.harysay.simkominfo.R.id.caption)
    protected VerticalTextView caption;

    @BindView(R.id.root)
    protected ViewGroup parent;

    protected boolean lock;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(authLayout(),container,false);
        ButterKnife.bind(this,root);
        // Session Manager
        session = new SessionManager(getActivity().getApplicationContext());

        loginIdPegawai = (TextInputEditText) root.findViewById(R.id.email_input_edit);
        loginPass = (TextInputEditText) root.findViewById(R.id.password_input_edit);
        lupapassId = (TextInputEditText) root.findViewById(R.id.lupapassword_id);
        KeyboardVisibilityEvent.setEventListener(getActivity(), isOpen -> {
            callback.scale(isOpen);
            if(!isOpen){
                clearFocus();
            }
        });

        caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmbol_loginUbah = caption.getText().toString().trim();
                if (tmbol_loginUbah.equals("Log In")) {
                    ed_loginIdPegawai = loginIdPegawai.getText().toString().trim();
                    ed_loginPassi = loginPass.getText().toString().trim();
                    if (ed_loginIdPegawai.equals("") || ed_loginPassi.equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ID Pegawai atau Password di halaman Log In tidak boleh kosong!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        //Klik tombol login
                        attemptLogin();

//                        Intent in = new Intent(getActivity(), UtamaActivity.class);
//                        startActivity(in);
//                        getActivity().finish();
                    }
                }else if(tmbol_loginUbah.equals("Lupa Password")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Anda mengklik tombol lupa password!",
                            Toast.LENGTH_LONG).show();
                }
//                String coba = "masuk";
//                String masuk = " ke halaman login";
//                String akhir = coba+masuk;
            }
        });
//        caption.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (lock==true){
//                    //maka login
//                    String coba = "masuk";
//                    String masuk = " ke halaman login";
//                    String akhir = coba+masuk;
//                }else if(lock==false){
//                    //maka lupa password
//                    String coba = "masuk";
//                    String masuk = " ke halaman lupa password";
//                    String akhir = coba+masuk;
//                }
//            }
//        });
        return root;
    }

    public void setCallback(@NonNull Callback callback) {
        this.callback = callback;
    }

    @LayoutRes
    public abstract int authLayout();
    public abstract void fold();
    public abstract void clearFocus();

    @OnClick(R.id.root)
    public void unfold(){
        if(!lock) {
            caption.setVerticalText(false);
            caption.requestLayout();
            Rotate transition = new Rotate();
            transition.setStartAngle(-90f);
            transition.setEndAngle(0f);
            transition.addTarget(caption);
            TransitionSet set=new TransitionSet();
            set.setDuration(getResources().getInteger(R.integer.duration));
            ChangeBounds changeBounds=new ChangeBounds();
            set.addTransition(changeBounds);
            set.addTransition(transition);
            TextSizeTransition sizeTransition=new TextSizeTransition();
            sizeTransition.addTarget(caption);
            set.addTransition(sizeTransition);
            set.setOrdering(TransitionSet.ORDERING_TOGETHER);
            caption.post(()->{
                TransitionManager.beginDelayedTransition(parent, set);
                caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.unfolded_size));
                caption.setTextColor(ContextCompat.getColor(getContext(),R.color.color_label));
                caption.setTranslationX(0);
                ConstraintLayout.LayoutParams params = getParams();
                params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                params.verticalBias = 0.78f;
                caption.setLayoutParams(params);

//                caption.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String ed_loginIdPegawai = loginIdPegawai.getText().toString().trim();
//                        String ed_loginPassi = loginPass.getText().toString().trim();
//                        if (ed_loginIdPegawai.equals("")||ed_loginPassi.equals("")){
//                            Toast.makeText(getActivity().getApplicationContext(),
//                                    "ID Pegawai dan Password tidak boleh kosong!",
//                                    Toast.LENGTH_LONG).show();
//                        }else {
//                            Intent in = new Intent(getActivity(), UtamaActivity.class);
//                            loginIdPegawai.setText("");
//                            loginPass.setText("");
//                            startActivity(in);
//                        }
////                String coba = "masuk";
////                String masuk = " ke halaman login";
////                String akhir = coba+masuk;
//                    }
//                });
            });
            callback.show(this);
            lock=true;
        }
    }

    protected ConstraintLayout.LayoutParams getParams(){
        return ConstraintLayout.LayoutParams.class.cast(caption.getLayoutParams());
    }

    interface Callback {
        void show(AuthFragment fragment);
        void scale(boolean hasFocus);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    public class UserLoginTask extends AsyncTask<String, String, String> {
        ProgressDialog progDialogAuthLog;
        private final String mNIK;
        private final String mPassword;


        UserLoginTask(String nik, String password) {
            mNIK = nik;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            progDialogAuthLog = ProgressDialog.show(getActivity(),
                    "Proses",
                    "Verifikasi user,..");
            progDialogAuthLog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... arg0) {

            try{

                URL url = new URL(Server.URL+"login");
                //URL url = new URL("http://10.28.7.47:8080/cobapost/cekpost.php");

                JSONObject postDataParams = new JSONObject();


                postDataParams.put("username", mNIK);
                postDataParams.put("password", mPassword);

                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                HttpHandler hdl = new HttpHandler();
                writer.write(hdl.getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    nilaiKembalian = sb.toString();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //mAuthTask = null;
            if (result == null){
                Toast.makeText(getActivity().getApplicationContext(),
                        "IP salah",
                        Toast.LENGTH_LONG).show();
            }
            else {
                //get data POST JSON dengan ngelmpar parameter result
                try {
                    JSONObject getJsonPost = new JSONObject(nilaiKembalian);
                    returnTokenStatus = getJsonPost.getString("status");
                    returnTokenCode = getJsonPost.getString("token");
                    returnTokenNik = getJsonPost.getString("username");

                    if(returnTokenStatus.equals("true")){
                        progDialogAuthLog.cancel();
                        //jika diijinkan masuk
//                        session.createLoginSession(returnTokenNik,"nama saya","direktur","yes");
//                        Intent masuk = new Intent(LoginActivity.this, UtamaActivity.class);
//                        startActivity(masuk);
//                        getActivity().finish();
                        new GetDataDiri(returnTokenNik,returnTokenCode).execute();
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(), "Username / Password yang Anda masukan salah!",
                                Toast.LENGTH_LONG).show();
                        progDialogAuthLog.cancel();
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }



                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage()+" Periksa juga koneksi internet Anda");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        loginIdPegawai.setError(null);
        loginPass.setError(null);

        // Store values at the time of the login attempt.
//        String empNIK = mNikEmployee.getText().toString();
//        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (TextUtils.isEmpty(inputanPassword) && !isPasswordValid(inputanPassword)) {
//            loginPass.setError(getString(R.string.error_invalid_password));
//            focusView = loginPass;
//            cancel = true;
//        }

        // Check for a valid ID.
//        if (TextUtils.isEmpty(inputanEmpNIK)) {
//            loginIdPegawai.setError(getString(R.string.error_field_required));
//            focusView = loginIdPegawai;
//            cancel = true;
//        }
//        else if (!isEmailValid(email)) {
//            mNikEmployee.setError(getString(R.string.error_invalid_email));
//            focusView = mNikEmployee;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mAuthTask = new UserLoginTask(ed_loginIdPegawai, ed_loginPassi);
            mAuthTask.execute();
        }
    }

    private class GetDataDiri extends AsyncTask<String, String, String> {
        ProgressDialog progDialogDataDiri;
        int kondisi = 1;
        private String result;
        String lemparannip,lemparantokenget,username,akses;
        public GetDataDiri(String lemparnip, String lempartokenget){
            this.lemparannip = lemparnip;
            this.lemparantokenget = lempartokenget;
        }
        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            String url = Server.URL+"profil?token="+lemparantokenget;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject empObj = new JSONObject(jsonStr);
                    JSONObject dataDiri = empObj.getJSONObject("data");
                    //nipBaru = empObj.getString("nip_baru");
                    username = dataDiri.getString("username");
                    akses = dataDiri.getString("akses");
                    namaPegawai = dataDiri.getString("nama");

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            kondisi=0;
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "ID Pegawai Tidak Ditemukan" + e.getMessage(),
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
                                "Couldn't get data from server. Contact to Administrator for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (kondisi==0){
                Intent intentLogin = getActivity().getIntent();
                getActivity().finish();
                startActivity(intentLogin);
                Toast.makeText(getActivity().getApplicationContext(),
                        "ID User Tidak Terdaftar!",
                        Toast.LENGTH_LONG).show();
            }else{
                session.createSessionSimkominfo(lemparannip,namaPegawai,akses,returnTokenCode);
                Intent masuk = new Intent(getActivity(), TabDashboardActivity.class);
                startActivity(masuk);
                getActivity().finish();
                progDialogDataDiri.cancel();
            }

        }

        @Override
        protected void onPreExecute() {
            progDialogDataDiri = ProgressDialog.show(getActivity(),
                    "Proses",
                    "Menarik data diri,..");
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}
