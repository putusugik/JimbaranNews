package narucodes.jimbarannews.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import narucodes.jimbarannews.JSONParser;
import narucodes.jimbarannews.Login;
import narucodes.jimbarannews.R;
import narucodes.jimbarannews.SharedPref;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentPostPusat.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPostPusat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPostPusat extends Fragment {

    EditText edpus, ifpus;
    Button submit, gambar;
    ImageView img;
    String imgPath, fileName, imgres, judulpusat, infopusat, encodedString;
    int id;
    Bitmap bitmap;

    SharedPref sharedPref;
    private static int RESULT_LOAD_IMG = 1;
    public ImageLoader imageLoader;
    private ProgressDialog pDialog;

    RequestParams params = new RequestParams();
    JSONParser jsonParser = new JSONParser();
    private static String url_insertpost = "http://sirent.esy.es/jimnews/insert_postpusat.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_JUDUL = "judul_pusat";
    public static final String TAG_INFO = "info_pusat";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentPostPusat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPostPusat.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPostPusat newInstance(String param1, String param2) {
        FragmentPostPusat fragment = new FragmentPostPusat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_post_pusat, container, false);
        sharedPref = new SharedPref(getActivity().getApplicationContext());
        id = sharedPref.getUserID();

        edpus = (EditText)v.findViewById(R.id.edpusat);
        ifpus =(EditText)v.findViewById(R.id.ifpusat);
        submit = (Button)v.findViewById(R.id.submitpost);
        gambar =  (Button)v.findViewById(R.id.gambarpost);
        img = (ImageView)v.findViewById(R.id.imgpusat);
        imageLoader = new ImageLoader(getActivity().getApplicationContext());


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edpus.getText().toString().isEmpty() || ifpus.getText().toString().isEmpty() ){
                    Toast.makeText(v.getContext(), "Isian belum lengkap", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(v.getContext(), "Posted", Toast.LENGTH_SHORT).show();
                    /*String judul = edpus.getText().toString();
                    String info = ifpus.getText().toString();
                    String type = "pusat";
                    Backtask backtask = new Backtask(v.getContext());
                    backtask.execute(type, judul, info);*/
                    new insertPusat().execute();
                }
            }
        });

        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, RESULT_LOAD_IMG);
            }
        });


        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
                if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                        && null != data) {
                    // Get the Image from data

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgPath = cursor.getString(columnIndex);
                    cursor.close();

                    // Set the Image in ImageView
                    img.setVisibility(View.VISIBLE);
                    img.setImageBitmap(BitmapFactory
                            .decodeFile(imgPath));
                    // Get the Image's file name
                    String fileNameSegments[] = imgPath.split("/");
                    fileName = fileNameSegments[fileNameSegments.length - 1];
                    Log.d("POTO: ",""+fileName);
                    // Put file name in Async Http Post Param which will used in Php web app
                    params.put("filename", fileName);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class insertPusat extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Mohon Tunggu");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            judulpusat = edpus.getText().toString();
            infopusat = ifpus.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("judul_pusat", judulpusat));
            params.add(new BasicNameValuePair("info_pusat", infopusat));

            JSONObject object = jsonParser.makeHttpRequest(url_insertpost, "POST", params);
            Log.d("Pusat", object.toString());

            try {
                int sukses = object.getInt(TAG_SUCCESS);
                if (sukses==1){
                    return String.valueOf(sukses);
                } else {
                    Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("1")){
                Intent i = new Intent(getContext(), Login.class);
                getActivity().finish();
                startActivity(i);
            }
            uploadImage();
            pDialog.dismiss();
        }
    }

    private void uploadImage() {
        if (imgPath != null && !imgPath.isEmpty()) {
            pDialog.setMessage("Converting Image to Binary Data");
            pDialog.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {

        }
    }

    private void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                pDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);


                // Trigger Image upload
                //triggerImageUpload();
                makeHTTPCall();
            }
        }.execute(null, null, null);
    }

    private void makeHTTPCall() {
        pDialog.setMessage("Please Wait");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://sirent.esy.es/jimnews/insert_fotopost.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                pDialog.hide();
                /*Toast.makeText(getActivity().getApplicationContext(), "Upload successfull",
                        Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.hide();
                if (statusCode == 404) {
                    /*Toast.makeText(getActivity().getApplicationContext(),
                            "Requested resource not found",
                            Toast.LENGTH_LONG).show();*/
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    /*Toast.makeText(getActivity().getApplicationContext(),
                            "Something went wrong at server end",
                            Toast.LENGTH_LONG).show();*/
                }
                // When Http response code other than 404, 500
                else {
                    /*Toast.makeText(
                            getActivity().getApplicationContext(),
                            "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                    + statusCode, Toast.LENGTH_LONG)
                            .show();*/
                }
            }
        });
    }
}
