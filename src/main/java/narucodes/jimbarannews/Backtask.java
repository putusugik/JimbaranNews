package narucodes.jimbarannews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import narucodes.jimbarannews.Admin.AdminDashboard;

/**
 * Created by User on 7/30/2018.
 */

public class Backtask extends AsyncTask<String, Void, JSONObject> {
    Context context;
    ProgressDialog alertDialog;
    SharedPreferences preferences;

    int id;
    String methode;
    JSONParser jsonParser;
    SharedPref sharedPref;

    private static final String TAG_SUCCESS = "sukses";

    public Backtask (Context ctx){
        context = ctx;
        jsonParser = new JSONParser();
        sharedPref = new SharedPref(ctx);
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new ProgressDialog(context);
        alertDialog.setMessage("Mohon Tunggu");
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        methode = params[0];
        String type =  params[0];
        String login_url = "http://sirent.esy.es/jimnews/loginjim.php";
        String regis_url = "http://sirent.esy.es/jimnews/register.php";
        String insertpostdesa = "http://sirent.esy.es/jimnews/insert_postdesa.php";
        String insertpostpusat = "http://sirent.esy.es/jimnews/insert_postpusat.php";

        if (type.equals("login")){
            List<NameValuePair> paket = new ArrayList<NameValuePair>();
            paket.add(new BasicNameValuePair("user_name", params[1]));
            paket.add(new BasicNameValuePair("user_pass", params[2]));
            return jsonParser.makeHttpRequest(login_url, "POST", paket);
        } else if (type.equals("register")){
            List<NameValuePair> paket = new ArrayList<NameValuePair>();
            paket.add(new BasicNameValuePair("user_name", params[1]));
            paket.add(new BasicNameValuePair("user_pass", params[2]));
            paket.add(new BasicNameValuePair("nama_user", params[3]));
            return jsonParser.makeHttpRequest(regis_url, "POST", paket);
        } else if (type.equals("pusat")){
            List<NameValuePair> paket = new ArrayList<NameValuePair>();
            paket.add(new BasicNameValuePair("judul_pusat", params[1]));
            paket.add(new BasicNameValuePair("info_pusat", params[2]));
            //paket.add(new BasicNameValuePair("nama_user", params[3]));
            return jsonParser.makeHttpRequest(insertpostpusat, "POST", paket);
        } else if (type.equals("desa")){
            List<NameValuePair> paket = new ArrayList<NameValuePair>();
            paket.add(new BasicNameValuePair("judul_desa", params[1]));
            paket.add(new BasicNameValuePair("info_desa", params[2]));
            //paket.add(new BasicNameValuePair("nama_user", params[3]));
            return jsonParser.makeHttpRequest(insertpostdesa, "POST", paket);
        }
        /*else if (type.equals("insertKend")){
            List<NameValuePair> paket = new ArrayList<NameValuePair>();
            paket.add(new BasicNameValuePair("nama_kendaraan", params[1]));
            paket.add(new BasicNameValuePair("no_mesin", params[2]));
            paket.add(new BasicNameValuePair("no_plat", params[3]));
            return jsonParser.makeHttpRequest(insertkend_url, "POST", paket);
        }*/
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        Log.d("onPostExecute: ", String.valueOf(result));
        try
        {
            if (methode.equals("login"))
            {
                if (result.toString().equals(null)) {
                    Toast.makeText(context, "Periksa jaringan anda", Toast.LENGTH_LONG).show();
                } else {
                    if (result.getInt("sukses") == 1) {
                        if (result.getInt("ID_role") == 1) {
                            sharedPref.setLogin(true);
                            sharedPref.setUserID(result.getInt("ID"));
                            id = sharedPref.getUserID();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                     else if (result.getInt("ID_role") == 2) {
                            sharedPref.setLoginAdmin(true);
                            sharedPref.setUserID(result.getInt("ID"));
                            id = sharedPref.getUserID();
                            Intent intent = new Intent(context, AdminDashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    } else if (result.getInt("sukses") == 0) {
                        Toast.makeText(context, "Username/Password anda salah", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (methode.equals("register")){
                if(result.getInt("sukses") == 1) {
                    Toast.makeText(context, "Register berhasil! Silahkan login kembali menggunakan username dan password yang telah didaftarkan", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                } else if(result.getInt("sukses") == 2) {
                    Toast.makeText(context, "Email sudah terdaftar", Toast.LENGTH_LONG).show();
                }
            } else if (methode.equals("pusat")){
                if (result.getInt("sukses")==1){
                    Toast.makeText(context, "Info Pusat berhasil ditambahkan.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, Login.class);
                    context.startActivity(i);

                }
            }  else if (methode.equals("desa")){
                if (result.getInt("sukses")==1){
                    Toast.makeText(context, "Info Desa berhasil ditambahkan.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, Login.class);
                    context.startActivity(i);

                }
            }
        }
        catch (JSONException e)
        {
            Log.e("jsonobjecterror", "onPostExecute: " + e.getMessage());
        }

        alertDialog.dismiss();
    }
}
