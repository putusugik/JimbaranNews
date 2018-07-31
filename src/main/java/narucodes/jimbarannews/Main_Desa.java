package narucodes.jimbarannews;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by User on 7/31/2018.
 */

public class Main_Desa extends android.support.v4.app.Fragment {

    SharedPref sharedPref;
    JSONParser jsonParser = new JSONParser();
    private static String url_selectdesa = "http://sirent.esy.es/jimnews/select_postdesa.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_POST = "psdesa";
    public static final String TAG_ID = "ID";
    public static final String TAG_JUDUL = "judul_desa";
    public static final String TAG_INFO = "info_desa";
    public static final String TAG_GAMBAR = "img_desa";
    public static final String TAG_INDEX = "index";

    private Desa_Adapter desa_adapter;

    int id;
    ListView lv;
    JSONArray postdesa = null;
    ArrayList<HashMap<String, String>> desalist = new ArrayList<HashMap<String, String>>();

    public Main_Desa(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_desa, container, false);
        lv = (ListView) rootView.findViewById(R.id.listdesa);
        new loadPostdesa().execute();


        return rootView;
    }


    private class loadPostdesa extends AsyncTask<String, String ,String> {

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
                        String judul = jobj.getString(TAG_JUDUL);
                        String info = jobj.getString(TAG_INFO);

                        map.put(TAG_JUDUL, judul);
                        map.put(TAG_INFO, info);
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
            if (desa_adapter != null){
                desa_adapter.notifyDataSetChanged();
                return;
            }
            desa_adapter = new Desa_Adapter(getActivity(), desalist);
            lv.setAdapter(desa_adapter);
            lv.setEmptyView(getView().findViewById(R.id.empty_listdesa));
        }
    }
}