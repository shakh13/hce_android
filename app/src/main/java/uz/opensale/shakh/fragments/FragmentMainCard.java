package uz.opensale.shakh.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import uz.opensale.shakh.LoginActivity;
import uz.opensale.shakh.R;
import uz.opensale.shakh.Server;
import uz.opensale.shakh.activity_home;
import uz.opensale.shakh.models.Cards;
import uz.opensale.shakh.models.User;

/**
 * Created by shakh on 07.03.18.
 */

public class FragmentMainCard extends Fragment {

    ImageView nfc_service_toggler;
    TextView main_card_name;
    TextView main_card_num;
    TextView main_card_balance;
    TextView main_card_expdate;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    Realm realm;

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_main_card, container, false);

        realm = Realm.getDefaultInstance();

        main_card_name = view.findViewById(R.id.main_card_name);
        main_card_num = view.findViewById(R.id.main_card_num);
        main_card_balance = view.findViewById(R.id.main_card_balance);
        main_card_expdate = view.findViewById(R.id.main_card_expdate);


        pref = (SharedPreferences) view.getContext().getSharedPreferences("ssh13", 0);
        editor = (SharedPreferences.Editor) pref.edit();

        String username = pref.getString("username", null);
        String password = pref.getString("password", null);

        User user = realm.where(User.class).equalTo("username", username).equalTo("password", password).findFirst();

        if (user != null){
            Map<String, String> params = new HashMap<>();
            params.put("auth_key", pref.getString("auth_key", null));

            final ProgressDialog progressDialog = new ProgressDialog(activity_home.getContext());
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            Server server = new Server(activity_home.getContext(), "cards/getmaincard", params);

            server.setListener(new Server.ServerListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void OnResponse(String data) {
                    try{
                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getBoolean("status")){
                            JSONObject obj = jsonObject.getJSONObject("content");
                            //pl

                            main_card_name.setText(obj.getString("name"));
                            main_card_num.setText(obj.getString("cnumb"));
                            main_card_balance.setText(obj.getString("cash") + " UZS");
                            main_card_expdate.setText("Действителен до " + obj.getString("exp_date"));

                            Cards card = new Cards(obj.getInt("id"), obj.getInt("bank_id"), obj.getString("bank_name"), obj.getString("cnumb"), obj.getString("exp_date"), obj.getString("phone"), obj.getString("name"), obj.getString("key"), obj.getInt("cash"));
                            nfc_service_toggler.setVisibility(View.VISIBLE);
                            activity_home.setMaincard(card);
                        }
                        else {
                            // print content
                            nfc_service_toggler.setVisibility(View.INVISIBLE);
                            Toast.makeText(activity_home.getContext(), jsonObject.getString("content"), Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        nfc_service_toggler.setVisibility(View.INVISIBLE);
                        Log.i("HCE", e.toString());
                         e.printStackTrace();
                    }
                }

                @Override
                public void OnError(String error) {
                    nfc_service_toggler.setVisibility(View.INVISIBLE);
                    Log.i("HCE", error);
                }
            });

        }
        else {
            Intent intent = new Intent(activity_home.getContext(), LoginActivity.class);
            startActivity(intent);
        }






        nfc_service_toggler = view.findViewById(R.id.nfc_service_toggler);
        nfc_service_toggler.setImageResource(activity_home.HCE_ENABLED ? R.drawable.logo0 : R.drawable.logo0disabled);

        nfc_service_toggler.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                nfc_service_toggler.setImageResource(activity_home.HCE_ENABLED ? R.drawable.logo0disabled : R.drawable.logo0);
                activity_home.HCE_ENABLED = !activity_home.HCE_ENABLED;

                Toast.makeText(getContext(), activity_home.HCE_ENABLED ? "Включено" : "Выключено", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }
}
