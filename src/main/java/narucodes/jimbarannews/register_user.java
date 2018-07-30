package narucodes.jimbarannews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register_user extends AppCompatActivity {
    EditText namadep, namabel, email, username, password, repassword;
    String nama, user, pass, repass;
    Button regis ;
    private static final String TAG = "your activity name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        namadep = (EditText) findViewById(R.id.nama_depan);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        regis = (Button)findViewById(R.id.register);
        regis.setEnabled(false);
        namadep.requestFocus();

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Toast.makeText(register_user.this, "Password minimal harus berisi 6 karakter", Toast.LENGTH_SHORT).show();
                } else {

                }

            }
        });
        repassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus ){
                    Toast.makeText(register_user.this, "Password minimal harus berisi 6 karakter", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

        repassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                regis.setEnabled(false);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>6){
                    regis.setEnabled(true);
                }else {
                    regis.setEnabled(false);
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(register_user.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void onregister(View view) {
        nama = namadep.getText().toString();
        user = username.getText().toString();
        pass = password.getText().toString();
        repass = repassword.getText().toString();

            if (!nama.equals("")  && !user.equals("")
                    && !pass.equals("")) {
                if (pass.equals(repass)) {
                    String type = "register";
                    Backtask backtask = new Backtask(this);
                    backtask.execute(type, user, pass, nama);
                }
                else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage("Password tidak sama!");
                    alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            password.requestFocus();
                        }
                    });
                    alertDialog.show();
                }
            }

            if (nama.isEmpty() ||  user.isEmpty() || pass.isEmpty() || repass.isEmpty()){
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Masih ada data yang belum di isi!");
                alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }

    }
}

