package narucodes.jimbarannews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

public class Main_Pusat extends android.support.v4.app.Fragment {

    SharedPref sharedPref;
    JSONParser jsonParser = new JSONParser();
    private static String url_selectpusat = "http://sirent.esy.es/jimnews/select_postpusat.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_POST = "pspusat";
    public static final String TAG_ID = "ID";
    public static final String TAG_JUDUL = "judul_pusat";
    public static final String TAG_INFO = "info_pusat";
    public static final String TAG_GAMBAR = "img_pusat";
    public static final String TAG_INDEX = "index";

    private Pusat_Adapter pusat_adapter;

    int id;
    ListView lv;
    JSONArray postpusat = null;
    ArrayList<HashMap<String, String>> pusatlist = new ArrayList<HashMap<String, String>>();

    public Main_Pusat(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pusat, container, false);
        lv = (ListView) rootView.findViewById(R.id.listpusat);
        lv.setDivider(getActivity().getDrawable(R.drawable.divider));
        lv.setDividerHeight(2);

        new loadPostpusat().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pID = ((TextView)view.findViewById(R.id.pusat)).getText().toString();
                String post = "pusat";
                Intent intent = new Intent(getActivity().getApplicationContext(), DetailPostPusat.class);
                intent.putExtra("ID", pID);
                intent.putExtra("post", post);
                startActivityForResult(intent, 100);
            }
        });

        return rootView;
    }

    private OnFragmentInteractionListener mListener;

    public void onButtonPressed (Uri uri){
        if (mListener!=null){
            mListener.onFragment(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            mListener = (OnFragmentInteractionListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragment (Uri uri);
    }

    private class loadPostpusat extends AsyncTask<String, String ,String> {

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
            lv.setEmptyView(getView().findViewById(R.id.empty_list));
        }
    }
}
