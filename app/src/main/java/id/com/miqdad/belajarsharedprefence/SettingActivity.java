package id.com.miqdad.belajarsharedprefence;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText etNama, etPassword, etEmail, etConfirm;
    Button  btConfirm;
    UserModel userModel;
    SaveShare saveShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        etNama = findViewById(R.id.tiEditNama);
        etEmail = findViewById(R.id.tiEditEmail);
        etPassword = findViewById(R.id.tiEditPassword);
        etConfirm = findViewById(R.id.tiEditConfirmPass);
        btConfirm = findViewById(R.id.btConfirm);

        getSupportActionBar().setDisplayShowHomeEnabled(true);


        saveShare =  new SaveShare(this);
        setData();
        btConfirm.setOnClickListener(this);
        etEmail.setFocusable(false);

    }
    private void setData(){
        userModel = saveShare.getUser();
        String nama = userModel.getNama();
        String email = userModel.getEmail();
        String password = userModel.getPassword();

        etNama.setText(nama);
        etEmail.setText(email);
        etPassword.setText(password);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btConfirm:
                confirmSetting();
                break;
        }
    }

    private void confirmSetting(){
        userModel = saveShare.getUser();
        String confirmPassword = etConfirm.getText().toString();
        String oldPassword = userModel.getPassword();
        if (confirmPassword.equals(oldPassword)){
            userModel.setNama(etNama.getText().toString());
            userModel.setPassword(etPassword.getText().toString());
            saveShare.setUser(userModel);
            Toast.makeText(this, "Data berhasil di ganti", Toast.LENGTH_SHORT).show();

        }
    }

}
