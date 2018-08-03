package narucodes.jimbarannews.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import narucodes.jimbarannews.JSONParser;
import narucodes.jimbarannews.Login;
import narucodes.jimbarannews.Pusat_Adapter;
import narucodes.jimbarannews.R;
import narucodes.jimbarannews.SharedPref;

public class DeletePostPusat extends Fragment {

    SharedPref sharedPref;
    JSONParser jsonParser = new JSONParser();
    private static String url_selectpusat = "http://sirent.esy.es/jimnews/select_postpusat.php";
    private static String url_deletepost = "http://sirent.esy.es/jimnews/delete_postpusat.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_POST = "pspusat";
    public static final String TAG_ID = "ID";
    public static final String TAG_JUDUL = "judul_pusat";
    public static final String TAG_INFO = "info_pusat";
    public static final String TAG_GAMBAR = "img_pusat";
    public static final String TAG_INDEX = "index";

    private Pusat_Adapter pusat_adapter;

    int id;
    String pID;
    ListView lv;
    JSONArray postpusat = null;
    ArrayList<HashMap<String, String>> pusatlist = new ArrayList<HashMap<String, String>>();

    public DeletePostPusat(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_delete_post, container, false);
        lv = (ListView) rootView.findViewById(R.id.lsdel);
        /*lv.setDivider(getActivity().getDrawable(R.drawable.divider));
        lv.setDividerHeight(2);*/

        new loadAllPost().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pID = ((TextView)view.findViewById(R.id.pusat)).getText().toString();
                Toast.makeText(getActivity(), ""+pID, Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setMessage("Hapus Postingan ini ?");
                ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new deletePost().execute();
                    }
                });
                ad.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = ad.create();
                alert.show();
            }
        });

        return rootView;
    }

    private class loadAllPost extends AsyncTask<String, String, String> {
        ProgressDialog pDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID_user", "2"));

            JSONObject obj = jsonParser.makeHttpRequest(url_selectpusat, "POST", params);
            Log.d("INFO", obj.toString());

            try {
                int sukses = obj.getInt(TAG_SUCCESS);
                if (sukses==1){
                    postpusat = obj.getJSONArray(TAG_POST);
                    pusatlist.removeAll(pusatlist);
                    for (int i = 0; i<postpusat.length();i++){
                        JSONObject jobj = postpusat.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        String ID = jobj.getString(TAG_ID);
                        String judul = jobj.getString(TAG_JUDUL);
                        String info = jobj.getString(TAG_INFO);
                        String gbr = jobj.getString(TAG_GAMBAR).replace("\\", "");

                        map.put(TAG_ID, ID);
                        map.put(TAG_INFO, info);
                        map.put(TAG_JUDUL, judul);
                        map.put(TAG_GAMBAR, gbr);
                        map.put(TAG_INDEX, ""+i);
                        pusatlist.add(map);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if (pusat_adapter != null){
                pusat_adapter.notifyDataSetChanged();
                return;
            }
            pusat_adapter = new Pusat_Adapter(getActivity(), pusatlist);
            lv.setAdapter(pusat_adapter);

        }
    }

    private class deletePost extends AsyncTask <String, String, String> {
        ProgressDialog pDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Deleting...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String id = pID;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID", ""+id));
            JSONObject obj = jsonParser.makeHttpRequest(url_deletepost, "POST", params);
            Log.d("DEL", obj.toString());

            try{
                int sukses = obj.getInt(TAG_SUCCESS);
                if (sukses==1){
                    Intent i = new Intent(getActivity(), Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    getActivity().finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
