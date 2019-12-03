package id.com.miqdad.belajarsharedprefence;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import id.com.miqdad.belajarsharedprefence.service.ApiClient;
import id.com.miqdad.belajarsharedprefence.service.BaseApiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    UserModel userModel;
    SaveShare saveShare;
    TextView tvFajr, tvSyuruq, tvDhuhr, tvAsr, tvMagrib, tvIsha, tvTanggal, tvLocation, tvTime;
    TextView txtFajr, txtsunrise, txtasar, txtDhuhr, txtMagrib, txtIsha;
    ImageView ivBackground;
    BaseApiService apiService;
    ProgressBar progressBar;
    ScrollView scrollView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveShare = new SaveShare(this);
        tvFajr = findViewById(R.id.tvFajr);
        tvSyuruq = findViewById(R.id.tvSunrise);
        tvDhuhr = findViewById(R.id.tvDhuhr);
        tvAsr = findViewById(R.id.tvAsr);
        tvMagrib = findViewById(R.id.tvMaghrib);
        tvIsha = findViewById(R.id.tvIsha);
        tvTanggal = findViewById(R.id.tvCalendar);
        tvLocation = findViewById(R.id.tvLocation);
        tvTime = findViewById(R.id.tvTime);
        ivBackground = findViewById(R.id.ivBg);
        scrollView = findViewById(R.id.scrollView);

        //deklarasi untuk view textnya
        txtFajr = findViewById(R.id.fajr);
        txtsunrise = findViewById(R.id.sunrise);
        txtDhuhr = findViewById(R.id.dhuhr);
        txtasar = findViewById(R.id.asr);
        txtMagrib = findViewById(R.id.maghrib);
        txtIsha = findViewById(R.id.isha);

        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite three = new ThreeBounce();
        progressBar.setIndeterminateDrawable(three);

        apiService = ApiClient.getJadwal();
        getJadwalSholatMethod("Condet");
        ImageView ivOk = findViewById(R.id.btOk);
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText etCityName = new EditText(MainActivity.this);
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("City Name").setMessage("Masukkan nama kota yang di inginkan ").setView(etCityName);
                alert.setPositiveButton("Change City", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String city = etCityName.getText().toString();
                        getJadwalSholatMethod(city);
                    }
                });
                alert.show();
            }
        });

        showDynamisTime();

    }


    private void showDynamisTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMM yyy");
        tvTanggal.setText(simpleDateFormat.format(new Date()));

        //// Setting dynamis nya mengikiuti waktunya
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        if (time >= 4 && time <= 5) {
            tvTime.setText(getString(R.string.Fajr));
            ivBackground.setImageResource(R.drawable.pagi_bg);
            txtFajr.setTextColor(getResources().getColor(R.color.hijauTua));
            txtFajr.setTextColor(getResources().getColor(R.color.hijauTua));
        } else if (time > 5 && time <= 6) {
            tvTime.setText(getString(R.string.Sunrise));
            txtsunrise.setTextColor(getResources().getColor(R.color.hijauTua));
            tvSyuruq.setTextColor(getResources().getColor(R.color.hijauTua));

        }  else if (time >= 7 && time <= 9) {
            tvTime.setText(getString(R.string.Morning));
            ivBackground.setImageResource(R.drawable.pagi_bg);
            tvDhuhr.setTextColor(getResources().getColor(R.color.hijauTua));
            txtDhuhr.setTextColor(getResources().getColor(R.color.hijauTua));
        }else if (time>=10 && time <=12){
            //do what you want
            tvTime.setText(getString(R.string.Dhuhr));
            ivBackground.setImageResource(R.drawable.siang_bg);
            tvDhuhr.setTextColor(getResources().getColor(R.color.hijauTua));
            txtDhuhr.setTextColor(getResources().getColor(R.color.hijauTua));
        }else if (time>=11 && time <= 15){
            tvTime.setText(getString(R.string.asar));
            ivBackground.setImageResource(R.drawable.siang_bg);
            txtasar.setTextColor(getResources().getColor(R.color.hijauTua));
            tvAsr.setTextColor(getResources().getColor(R.color.hijauTua));
        }
        else if (time>=16 && time <= 18){
            tvTime.setText(getString(R.string.magrib));
            ivBackground.setImageResource(R.drawable.dawn);
            tvMagrib.setTextColor(getResources().getColor(R.color.hijauTua));
            txtMagrib.setTextColor(getResources().getColor(R.color.hijauTua));
        }else if (time >= 19 && time <= 24) {
            tvTime.setText(getString(R.string.isyaa));
            ivBackground.setImageResource(R.drawable.malam_bg);
            tvIsha.setTextColor(getResources().getColor(R.color.hijauTua));
            txtIsha.setTextColor(getResources().getColor(R.color.hijauTua));

        }

    }

    private void getJadwalSholatMethod(String namaKota) {
        Call<ResponseBody> requestApi = apiService.getJadwalShalat(namaKota);
        requestApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.getString("status").equals("OK")) {
                            progressBar.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            String fajr = jsonObject.getJSONObject("data").getString("Fajr");
                            String syuruq = jsonObject.getJSONObject("data").getString("Sunrise");
                            String duhur = jsonObject.getJSONObject("data").getString("Dhuhr");
                            String asr = jsonObject.getJSONObject("data").getString("Asr");
                            String magrib = jsonObject.getJSONObject("data").getString("Maghrib");
                            String isha = jsonObject.getJSONObject("data").getString("Isha");
                            String address = jsonObject.getJSONObject("location").getString("address");

                            //set data to Textview
                            tvFajr.setText(fajr + " AM");
                            tvSyuruq.setText(syuruq + " AM");
                            tvDhuhr.setText(duhur + " PM");
                            tvAsr.setText(asr + " PM");
                            tvMagrib.setText(magrib + "PM");
                            tvIsha.setText(isha + " PM");
                            tvLocation.setText(address);


                            //set data waktu from server
                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh :mm");
                            try {
                                Date date = dateFormat.parse(fajr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "response gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Bad Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:
                userModel = saveShare.getUser();
                userModel.setStatusLogin(false);
                saveShare.setUser(userModel);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.accountSetting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));

                break;
            case R.id.Adzan:
                startActivity(new Intent(MainActivity.this,Adzan.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
