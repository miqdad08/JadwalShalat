package id.com.miqdad.belajarsharedprefence;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    AutoCompleteTextView etUserName, etPassword;
    TextView tvRegister, tvForgot;
    Button btLogin;
    SaveShare saveShare;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPass);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgot = findViewById(R.id.forgot);
        btLogin = findViewById(R.id.btLogin);

        btLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvForgot.setOnClickListener(this);

        saveShare  = new SaveShare(this);
        checkLogin();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btLogin:
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(userName)){
                    etUserName.setError("User Name Tidak Boleh Kosong");

                }
                if (TextUtils.isEmpty(password)){
                    etPassword.setError("Password Tidak Boleh Kosong");
                    return;
                }

                if (!isValidEmail(userName)){
                    Toast.makeText(this, "Email Tidak Valid", Toast.LENGTH_SHORT).show();
                    return;
                }

//                Toast.makeText(this, "Email Tidak terdaftar, Silahkan Register", Toast.LENGTH_SHORT).show();
                validateLogin(userName, password);
                break;
            case R.id.tvRegister:
               startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.forgot:
               validateForgot();
                break;
        }
    }

    private void validateForgot(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        userModel = saveShare.getUser();
        String showPassword = userModel.getPassword();
        alert.setTitle("Password Kamu Adalah : "+showPassword);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private void validateLogin(String email, String password){
      userModel = saveShare.getUser();
      String saveEmail = userModel.getEmail();
      String savePassword = userModel.getPassword();
      if (email.equals(saveEmail)&& password.equals(savePassword)){
          startActivity(new Intent(LoginActivity.this, MainActivity.class));
          userModel.setStatusLogin(true);
          saveShare.setUser(userModel);
          finish();
      }else {
          AlertDialog.Builder alert = new AlertDialog.Builder(this);
          alert.setTitle(getString(R.string.accountNotRegist));
          alert.setTitle(getString(R.string.pleaseRegist));
          alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                  finish();
              }
          });
          alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

              }
          });
          alert.show();
      }
    }
    private void checkLogin(){
        userModel = saveShare.getUser();
        boolean statusLogin = userModel.isStatusLogin();
        if (statusLogin == true){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    private boolean isValidEmail(CharSequence email){
        return !TextUtils.isEmpty(email)&& Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

