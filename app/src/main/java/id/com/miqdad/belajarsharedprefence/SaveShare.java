package id.com.miqdad.belajarsharedprefence;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveShare {
    private static final String USER_SHARED = "user_shared";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String KELAMIN = "kelamin";
    private static final String AGREE = "AGREE";
    private static final String LOGIN = "status_login";

    private final SharedPreferences prefences;


    SaveShare (Context context){
        prefences = context.getSharedPreferences(USER_SHARED, Context.MODE_PRIVATE);
    }
    public void setUser(UserModel value){
        SharedPreferences.Editor editor = prefences.edit();
        editor.putString(NAME, value.getNama());
        editor.putString(EMAIL, value.getEmail());
        editor.putString(PASSWORD, value.getPassword());
        editor.putString(KELAMIN, value.getKelamin());
        editor.putString(AGREE, value.getAgree());
        editor.putBoolean(LOGIN, value.isStatusLogin());
        editor.apply();
    }

    UserModel getUser(){
        UserModel model = new UserModel();
        model.setNama(prefences.getString(NAME, ""));
        model.setEmail(prefences.getString(EMAIL, ""));
        model.setPassword(prefences.getString(PASSWORD, ""));
        model.setAgree(prefences.getString(AGREE, ""));
        model.setKelamin(prefences.getString(KELAMIN, ""));
        model.setStatusLogin(prefences.getBoolean(LOGIN,false));
        return model;


    }
}
