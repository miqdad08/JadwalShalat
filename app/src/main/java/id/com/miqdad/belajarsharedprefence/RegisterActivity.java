package id.com.miqdad.belajarsharedprefence;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout tInputPass, tInputEmail, tInputConfirmPass, tInputName;

    AutoCompleteTextView etName, etEmail, etPass, etConfirmPass;

    RadioButton rbMale, rbFemale;

    RadioGroup rgGender;

    Button btnRegister;

    CheckBox cbAgree;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etConfirmPass = findViewById(R.id.etPassConfirm);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnRegister = findViewById(R.id.btRegister);
        cbAgree = findViewById(R.id.cbAgree);


        btnRegister.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btRegister) {
            String nama = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPass.getText().toString();
            String kelamin = String.valueOf(rgGender.getCheckedRadioButtonId());
            String agree = cbAgree.getText().toString();
            String confirm = etConfirmPass.getText().toString();

            if (TextUtils.isEmpty(nama)){
                etName.setError("Nama Tidak Boleh Kosong");
                return;
            }
            if (TextUtils.isEmpty(email)){
                etEmail.setError("Email Tidak Boleh Kosong");
            }

            if (TextUtils.isEmpty(password)){
                etPass.setError("Password Tidak Boleh Kosong");
                return;
            }

            if (TextUtils.isEmpty(confirm)){
                etConfirmPass.setError("Confirm Password Tidak Boleh Kosong");
                return;
            }
            if (!password.equals(confirm)){
                Toast.makeText(this, "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                return;
            }

            invalidUser(nama, email, password, kelamin, agree);
        }
    }

    private void invalidUser(String nama, String email, String password, String kelamin, String agree){
        SaveShare userShare = new SaveShare(this);
        UserModel userModel = userShare.getUser();

        String saveEmail = userModel.getEmail();
        if (email.equals(saveEmail)){
            Toast.makeText(this, "Email Sudah terdaftar Silahkan Gunakan Email Yang lainn", Toast.LENGTH_SHORT).show();
        }else {
            saveUser(nama, email, password, kelamin, agree);
        }
    }

    private void saveUser(String nama, String email, String password, String kelamin, String agree){
        SaveShare userShared = new SaveShare(this);
        UserModel userModel  = new UserModel();
        userModel.setNama(nama);
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.setAgree(agree);
        userModel.setKelamin(kelamin);

        userShared.setUser(userModel);
//        Toast.makeText(this, getString(R.string.succesRegister), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
        alert.setTitle(getString(R.string.succesRegister));
        alert.setTitle(getString(R.string.cautionSucces));
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
        alert.show();
    }

}
