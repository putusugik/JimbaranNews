package narucodes.jimbarannews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class DetailPostPusat extends AppCompatActivity {

    String  id, post;
    ImageView img;
    TextView judul, info;

    JSONParser jsonParser = new JSONParser();
    private static String url_selectpusat = "http://sirent.esy.es/jimnews/select_dpusat.php";
    private static String url_selectdesa = "http://sirent.esy.es/jimnews/select_ddesa.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_POST = "pspusat";
    public static final String TAG_ID = "ID";
    public static final String TAG_JUDUL = "judul_pusat";
    public static final String TAG_INFO = "info_pusat";
    public static final String TAG_GAMBAR = "img_pusat";
    public static final String TAG_INDEX = "index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post_pusat);
        Intent i = getIntent();
        id = i.getStringExtra("ID");
        post = i.getStringExtra("post");


        img = (ImageView)findViewById(R.id.imgdetail);
        judul = (TextView)findViewById(R.id.juduldetail);
        info = (TextView)findViewById(R.id.infodetail);

        if (post.equals("pusat")){
            new loadDetailPost().execute();
        } else if (post.equals("desa")){
            new loadDetailDesa().execute();
        }




    }

    private class loadDetailPost extends AsyncTask<String, String, List<String>> {
        ProgressDialog pDialog = new ProgressDialog(DetailPostPusat.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected List<String> doInBackground(String... args) {

            List<String>list = new ArrayList<String>();
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", ""+id));
                JSONObject obj = jsonParser.makeHttpRequest(url_selectpusat, "GET", params);


                    int sukses = obj.getInt(TAG_SUCCESS);
                    if (sukses==1){
                        JSONArray array = obj.getJSONArray("pspusat");
                        JSONObject object = array.getJSONObject(0);
                        list.add(object.getString("ID"));
                        list.add(object.getString("judul_pusat"));
                        list.add(object.getString("info_pusat"));
                        list.add(object.getString("img_pusat").replace("\\", ""));
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            judul.setText(result.get(1));
            info.setText(result.get(2));
            Picasso.with(DetailPostPusat.this).load(result.get(3)).into(img);
            pDialog.dismiss();
        }
    }

    private class loadDetailDesa extends AsyncTask<String, String, List<String>> {
        ProgressDialog pDialog = new ProgressDialog(DetailPostPusat.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected List<String> doInBackground(String... args) {

            List<String>list = new ArrayList<String>();
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", ""+id));
                JSONObject obj = jsonParser.makeHttpRequest(url_selectdesa, "GET", params);


                int sukses = obj.getInt(TAG_SUCCESS);
                if (sukses==1){
                    JSONArray array = obj.getJSONArray("psdesa");
                    JSONObject object = array.getJSONObject(0);
                    list.add(object.getString("ID"));
                    list.add(object.getString("judul_desa"));
                    list.add(object.getString("info_desa"));
                    list.add(object.getString("img_desa").replace("\\", ""));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            judul.setText(result.get(1));
            info.setText(result.get(2));
            Picasso.with(DetailPostPusat.this).load(result.get(3)).into(img);
            pDialog.dismiss();
        }
    }
}
