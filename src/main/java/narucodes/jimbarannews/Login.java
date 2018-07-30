package narucodes.jimbarannews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class Login extends AppCompatActivity {

    private static String url_loaduser = "http://sirent.esy.es/jimnews/select_user.php";

    private static final String TAG_SUCCESS = "sukses";
    private EditText username, password;
    Button login, regis;
    private SharedPref sharedPref;
    int id, sts;
    SharedPreferences preferences;

    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btnLogin);
        regis = (Button) findViewById(R.id.btnRegis);
        login.setEnabled(false);

        sharedPref = new SharedPref(getApplicationContext());
        id= sharedPref.getUserID();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPref.isLoggedIn()) {
            new loadUser().execute();
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                login.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>3){
                    login.setEnabled(true);
                }else {
                    login.setEnabled(false);
                }
            }
        });
    }

    public void  onlogin (View view){
            if ((username.getText().toString()).isEmpty() || (password.getText().toString()).isEmpty()){
                Toast.makeText(this, "Kolom belum terisi dengan lengkap", Toast.LENGTH_SHORT).show();
            } else {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String type = "login";
                Backtask backtask = new Backtask(this);
                backtask.execute(type, user, pass);

            }
    }

    public void register (View view){
        Intent intent = new Intent(this, register_user.class);
        startActivity(intent);
        finish();
    }

    private class loadUser extends AsyncTask<String, String, List<String>> {
        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> list = new ArrayList<String>();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID", "" + id));

            JSONObject obj = jsonParser.makeHttpRequest(url_loaduser, "GET", params);

            try {
                int sukses = obj.getInt(TAG_SUCCESS);
                if (sukses == 1) {
                    JSONArray profil = obj.getJSONArray("profil");
                    JSONObject object = profil.getJSONObject(0);
                    list.add(object.getString("ID"));
                } else {
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return list;
        }
    }
}
