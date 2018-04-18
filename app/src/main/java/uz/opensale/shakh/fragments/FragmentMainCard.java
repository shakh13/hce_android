package uz.opensale.shakh.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import uz.opensale.shakh.models.User;

/**
 * Created by shakh on 07.03.18.
 */

public class FragmentMainCard extends Fragment {

    ImageView nfc_service_toggler;
    TextView main_card_name;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    Realm realm;

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_main_card, container, false);

        realm = Realm.getDefaultInstance();

        main_card_name = view.findViewById(R.id.main_card_name);


        pref = (SharedPreferences) view.getContext().getSharedPreferences("ssh13", 0);
        editor = (SharedPreferences.Editor) pref.edit();

        String username = pref.getString("username", null);
        String password = pref.getString("password", null);

        User user = realm.where(User.class).equalTo("username", username).equalTo("password", password).findFirst();

        if (user != null){
            Map<String, String> params = new HashMap<>();
            params.put("auth_key", pref.getString("auth_key", null));

            Server server = new Server(activity_home.getContext(), "getmaincard", params);

            server.setListener(new Server.ServerListener() {
                @Override
                public void OnResponse(String data) {
                    try{
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getBoolean("status")){
                            JSONObject obj = jsonObject.getJSONObject("content");

                            main_card_name.setText(obj.getString("name"));
                        }
                        else {
                            // print content
                            Toast.makeText(activity_home.getContext(), jsonObject.getString("content"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(activity_home.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnError(String error) {
                    Toast.makeText(activity_home.getContext(), error, Toast.LENGTH_LONG).show();
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
