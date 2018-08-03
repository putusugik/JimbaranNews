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
import narucodes.jimbarannews.Desa_Adapter;
import narucodes.jimbarannews.JSONParser;
import narucodes.jimbarannews.Login;
import narucodes.jimbarannews.R;
import narucodes.jimbarannews.SharedPref;

public class DeletePostDesa extends Fragment {

    SharedPref sharedPref;
    JSONParser jsonParser = new JSONParser();
    private static String url_selectdesa = "http://sirent.esy.es/jimnews/select_postdesa.php";
    private static String url_deletepost = "http://sirent.esy.es/jimnews/delete_postdesa.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_POST = "psdesa";
    public static final String TAG_ID = "ID";
    public static final String TAG_JUDUL = "judul_desa";
    public static final String TAG_INFO = "info_desa";
    public static final String TAG_GAMBAR = "img_desa";
    public static final String TAG_INDEX = "index";

    private Desa_Adapter desa_adapter;

    int id;
    String pID;
    ListView lv;
    JSONArray postdesa = null;
    ArrayList<HashMap<String, String>> desalist = new ArrayList<HashMap<String, String>>();

    public DeletePostDesa(){

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
                pID = ((TextView)view.findViewById(R.id.desa)).getText().toString();
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

            JSONObject obj = jsonParser.makeHttpRequest(url_selectdesa, "POST", params);
            Log.d("INFO", obj.toString());

            try {
                int sukses = obj.getInt(TAG_SUCCESS);
                if (sukses==1){
                    postdesa = obj.getJSONArray(TAG_POST);
                    desalist.removeAll(desalist);
                    for (int i = 0; i<postdesa.length();i++){
                        JSONObject jobj = postdesa.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        String ID = jobj.getString(TAG_ID);
                        String judul = jobj.getString(TAG_JUDUL);
                        String info = jobj.getString(TAG_INFO);
                        String gbr = jobj.getString(TAG_GAMBAR).replace("\\", "");

                        map.put(TAG_ID, ID);
                        map.put(TAG_JUDUL, judul);
                        map.put(TAG_INFO, info);
                        map.put(TAG_GAMBAR, gbr);
                        map.put(TAG_INDEX, ""+i);
                        desalist.add(map);
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
            /*if (desa_adapter != null){
                desa_adapter.notifyDataSetChanged();
                return;
            }*/
            desa_adapter = new Desa_Adapter(getActivity(), desalist);
            lv.setAdapter(desa_adapter);

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
