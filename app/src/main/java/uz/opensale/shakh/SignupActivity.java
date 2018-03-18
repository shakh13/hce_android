package uz.opensale.shakh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import uz.opensale.shakh.models.User;

public class SignupActivity extends AppCompatActivity{

    EditText elogin;
    EditText epassword;
    EditText epassword2;
    EditText eemail;
    EditText ephone;
    Button esubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        elogin = (EditText) findViewById(R.id.signup_login);
        epassword = (EditText) findViewById(R.id.signup_password);
        epassword2 = (EditText) findViewById(R.id.signup_password2);
        eemail = (EditText) findViewById(R.id.signup_email);
        ephone = (EditText) findViewById(R.id.signup_phone);
        esubmit = (Button) findViewById(R.id.signup_submit);
    }

    public void onClick(View view) {
        if (view == esubmit){
            final String login = elogin.getText().toString();
            final String password = epassword.getText().toString();
            String password2 = epassword2.getText().toString();
            String email = eemail.getText().toString();
            final String phone = ephone.getText().toString();

            if (login.length() > 5){
                if (password.length() > 5){
                    if (password.equals(password2)){
                        if (email.length() > 9){
                            if (phone.length() == 9){
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("login", login);
                                params.put("password", password);
                                params.put("password2", password2);
                                params.put("email", email);
                                params.put("phone", phone);
                                Server server = new Server(this, "auth/signup", params);
                                server.setListener(new Server.ServerListener() {
                                    @Override
                                    public void OnResponse(String data) {

                                        try {
                                            JSONObject json = new JSONObject(data);
                                            boolean status = json.getBoolean("status");
                                            if (status){

                                                Realm.init(getBaseContext());
                                                Realm realm = Realm.getDefaultInstance();

                                                User result = realm.where(User.class).greaterThan("id", -1).findFirst();

                                                realm.beginTransaction();
                                                if (result != null)
                                                    result.deleteFromRealm();

                                                User user = realm.createObject(User.class, json.getInt("id"));
                                                user.setUsername(login);
                                                user.setPassword(password);
                                                user.setName(json.getString("name"));
                                                user.setSurname(json.getString("surname"));
                                                user.setLastname(json.getString("lastname"));
                                                user.setAddress(json.getString("address"));
                                                user.setPhone(phone);
                                                user.setPostcode( json.getLong("postcode"));
                                                user.setCreated_at(json.getString("created_at"));
                                                user.setKey(json.getString("auth_key"));
                                                realm.commitTransaction();

                                                CheckUser checkUser = new CheckUser();
                                                checkUser.login(login, password);
                                                checkUser.addAuthKey(json.getString("auth_key"));

                                                Intent intent = new Intent(getBaseContext(), activity_home.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                Toast.makeText(getBaseContext(), json.getString("content"), Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }

                                    @Override
                                    public void OnError(String error) {
                                        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else {
                                Toast.makeText(this, phone.length() == 0 ? "Введите Номер телефона" : "Введен несуществующий номер телефона", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(this, email.length() == 0 ? "Введен несуществующий Email" : "Введите Email", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this, password.length() == 0 ? "Пароль дожен содержать не менее 6 символов" : "Введите пароль", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, login.length() == 0 ? "Введите логин" : "Логин должен содержать не менее 6 символов", Toast.LENGTH_LONG).show();
            }
        }
    }
}
