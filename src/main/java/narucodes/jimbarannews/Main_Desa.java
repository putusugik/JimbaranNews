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
        lv.setDivider(getActivity().getDrawable(R.drawable.divider));
        lv.setDividerHeight(2);

        new loadPostdesa().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pID = ((TextView)view.findViewById(R.id.desa)).getText().toString();
                String post = "desa";
                Intent intent = new Intent(getActivity().getApplicationContext(), DetailPostPusat.class);
                intent.putExtra("ID", pID);
                intent.putExtra("post", post);
                startActivityForResult(intent, 100);
            }
        });


        return rootView;
    }

    private Main_Pusat.OnFragmentInteractionListener mListener;

    public void onButtonPressed (Uri uri){
        if (mListener!=null){
            mListener.onFragment(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Main_Pusat.OnFragmentInteractionListener){
            mListener = (Main_Pusat.OnFragmentInteractionListener)context;
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
            lv.setEmptyView(getView().findViewById(R.id.empty_listdesa));
        }
    }
}