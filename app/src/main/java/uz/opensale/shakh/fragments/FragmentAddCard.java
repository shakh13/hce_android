package uz.opensale.shakh.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import uz.opensale.shakh.R;
import uz.opensale.shakh.Server;
import uz.opensale.shakh.activity_home;

public class FragmentAddCard extends Fragment {

    public EditText card_number;
    public EditText exp_date;
    public EditText sms_code;
    public LinearLayout layout_form;
    public LinearLayout layout_verify;
    public Button add_btn;
    public Button verify_btn;

    private int verify_code;

    public SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_add_card, container, false);

        pref = activity_home.getContext().getSharedPreferences("ssh13", 0);

        card_number = view.findViewById(R.id.fragment_Add_card_card_number);
        exp_date = view.findViewById(R.id.fragment_add_card_expdate);
        layout_form = view.findViewById(R.id.fragment_add_card_form);
        layout_verify = view.findViewById(R.id.fragment_add_card_verify);
        add_btn = view.findViewById(R.id.fragment_add_card_btn);
        verify_btn = view.findViewById(R.id.fragment_add_card_verify_code_btn);
        sms_code = view.findViewById(R.id.fragment_add_card_sms_code);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card_number.getText().length() == 16){
                    if (exp_date.getText().length() == 5){
                        Map<String, String> params = new HashMap<>();
                        params.put("auth_key", pref.getString("auth_key", null));
                        params.put("card_number", String.valueOf(card_number.getText()));
                        params.put("exp_date", String.valueOf(exp_date.getText()));

                        Server server = new Server(activity_home.getContext(), "cards/add", params);
                        server.setListener(new Server.ServerListener() {
                            @Override
                            public void OnResponse(String data) {
                                try {
                                    JSONObject json = new JSONObject(data);
                                    if (json.getBoolean("status")){
                                        verify_code = json.getInt("content");
                                        layout_verify.setVisibility(View.VISIBLE);
                                        layout_form.setVisibility(View.GONE);

                                        Snackbar.make(view, json.getString("Enter SMS code"), Snackbar.LENGTH_LONG).show();


                                    }
                                    else {
                                        Snackbar.make(view, json.getString("content"), Snackbar.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void OnError(String error) {
                                Snackbar.make(view, error, Snackbar.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else {
                        Snackbar.make(view, "Please, enter card expiry date first", Snackbar.LENGTH_LONG).show();
                    }
                }
                else {
                    Snackbar.make(view, "Please, enter card number first", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        verify_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Map<String, String> params = new HashMap<>();
                params.put("auth_key", pref.getString("auth_key", null));
                params.put("code", sms_code.getText().toString());

                Server server = new Server(activity_home.getContext(), "cards/verifyadd", params);
                server.setListener(new Server.ServerListener() {
                    @Override
                    public void OnResponse(String data) {
                        try {
                            JSONObject json = new JSONObject(data);
                            if (json.getBoolean("status")){
                                FragmentManager fm = ((Activity) activity_home.getContext()).getFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.fragments, new FragmentCards());
                                ft.commit();
                            }
                            else {
                                sms_code.setText("");
                                Snackbar.make(view, json.getString("content"), Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Snackbar.make(view, e.toString(), Snackbar.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnError(String error) {
                        Snackbar.make(view, error, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    class timetask extends TimerTask {

        @Override
        public void run() {


        }
    }
}
