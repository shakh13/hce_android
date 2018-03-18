package uz.opensale.shakh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import uz.opensale.shakh.models.User;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView signinUsername;
    private EditText signinPassword;
    private View mProgressView;
    private View mLoginFormView;
    private TextView signuptxt;
    private Button signinBtn;
    CheckUser checkUser;
    TextView tw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        signinUsername = (AutoCompleteTextView) findViewById(R.id.email);
        signinPassword = (EditText) findViewById(R.id.password);
        signinBtn = (Button) findViewById(R.id.email_sign_in_button);
        signuptxt = (TextView) findViewById(R.id.signup_link_btn);

        Realm.init(activity_home.getContext());
        Realm realm = Realm.getDefaultInstance();
        checkUser = new CheckUser();
        tw = (TextView) findViewById(R.id.result);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkUser.check(false)){
            Intent intent = new Intent(this, activity_home.class);
            startActivity(intent);
        }
    }



    public void onClick(View view){
        if (view == signuptxt){
            Intent signupform = new Intent(this, SignupActivity.class);
            startActivity(signupform);
        }

        if (view == signinBtn){
            if (signinUsername.getText().length() > 5){
                if (signinPassword.getText().length() > 5){
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Wait...");
                    progressDialog.show();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", signinUsername.getText().toString());
                    params.put("password", signinPassword.getText().toString());

                    Server server = new Server(getApplicationContext(), "auth/login", params);
                    server.setListener(new Server.ServerListener() {
                        @Override
                        public void OnResponse(String data) {
                            progressDialog.dismiss();
                            try {
                                JSONObject json = new JSONObject(data);
                                boolean status = json.getBoolean("status");
                                if (status){
                                    checkUser.login(signinUsername.getText().toString(), signinPassword.getText().toString());

                                    Realm realm = Realm.getDefaultInstance();

                                    User result = realm.where(User.class).greaterThan("id", -1).findFirst();

                                    realm.beginTransaction();
                                    if (result != null)
                                        result.deleteFromRealm();
                                    User user = realm.createObject(User.class, json.getLong("id"));
                                    user.setUsername(signinUsername.getText().toString());
                                    user.setPassword(signinPassword.getText().toString());
                                    user.setName(json.getString("name"));
                                    user.setSurname(json.getString("surname"));
                                    user.setLastname(json.getString("lastname"));
                                    user.setAddress(json.getString("address"));
                                    user.setPhone(json.getString("phone"));
                                    user.setPostcode(json.getLong("postcode"));
                                    user.setCreated_at(json.getString("created_at"));
                                    user.setKey(json.getString("auth_key"));
                                    checkUser.addAuthKey(json.getString("auth_key"));
                                    realm.commitTransaction();

                                    Intent intent = new Intent(activity_home.getContext(), activity_home.class);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void OnError(String error) {
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                        }
                    });

                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(this, "Введите пароль", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, "Введите логин", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }



}

