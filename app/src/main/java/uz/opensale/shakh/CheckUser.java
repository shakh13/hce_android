package uz.opensale.shakh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import io.realm.Realm;
import uz.opensale.shakh.models.User;


/**
 * Created by shakh on 11.01.18.
 */

public class CheckUser {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    @SuppressLint("StaticFieldLeak")
    private static Realm realm;

    private User user;
    private String username;
    private String password;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    CheckUser(){
        context = activity_home.getContext();
        Realm.init(context);
        realm = Realm.getDefaultInstance();

        pref = (SharedPreferences) context.getSharedPreferences("ssh13", Activity.MODE_PRIVATE);
        editor = (SharedPreferences.Editor) pref.edit();
    }

    void addAuthKey(String key){
        editor.putString("auth_key", key);
        editor.commit();
    }

    String getUserFullname(){
        if (user != null){
            String name = user.getName() != null ? user.getName() + " " : "";
            name += user.getSurname() != null ? user.getSurname() + " " : "";
            name += user.getLastname() != null ? user.getLastname() + " " : "";
            return name;
        }

        return "Unknown user";
    }

    User getUser(){
        return user;
    }

    void login(String un, String psw){
        editor.putString("username", un);
        editor.putString("password", psw);
        this.password = psw;
        this.username = un;

        editor.commit();
    }

    public void signup(int id, String login, String passw, String name, String surname, String lastname, String address, String phone, long postcode, String created_at, String authkey){

        User result = realm.where(User.class).greaterThan("id", -1).findFirst();

        realm.beginTransaction();
        if (result != null)
            result.deleteFromRealm();

        User user = realm.createObject(User.class, id);
        user.setUsername(login);
        user.setPassword(passw);
        user.setName(name);
        user.setSurname(surname);
        user.setLastname(lastname);
        user.setAddress(address);
        user.setPhone(phone);
        user.setPostcode(postcode);
        user.setCreated_at(created_at);
        user.setKey(authkey);
        realm.commitTransaction();

        login(login, passw);

        Intent intent = new Intent(context, activity_home.class);
        context.startActivity(intent);
    }

    public boolean check(boolean startactivity){

        Intent loginForm = new Intent(context, LoginActivity.class);
        //loginForm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        username = pref.getString("username", null);
        password = pref.getString("password", null);

        if (username != null){
            if (password != null) {
                user = realm.where(User.class).equalTo("username", username).equalTo("password", password).findFirst();

                if (user == null){
                    if (startactivity) context.startActivity(loginForm);
                    else return false;
                }
            }
            else {
                if (startactivity) context.startActivity(loginForm);
                else return false;
            }
        }
        else {
            if (startactivity) context.startActivity(loginForm);
            else return false;
        }

        return true;
    }

    public String getAuthKey(){
        return user.getKey();
    }

    public String toString(){
        return pref.getString("username", null) +" "+ pref.getString("password", null);
    }
}
