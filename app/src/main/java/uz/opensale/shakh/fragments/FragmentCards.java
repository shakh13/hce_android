package uz.opensale.shakh.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import uz.opensale.shakh.R;
import uz.opensale.shakh.Server;
import uz.opensale.shakh.activity_home;
import uz.opensale.shakh.adapters.CardsAdapter;
import uz.opensale.shakh.models.Cards;
import uz.opensale.shakh.models.User;

/**
 * Created by shakh on 07.03.18.
 */

public class FragmentCards extends Fragment {

    RecyclerView recyclerView;
    Realm realm;
    RecyclerView.Adapter adapter;
    List<Cards> cardsList;
    Context context;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_cards, container, false);

        context = view.getContext();

        realm = Realm.getDefaultInstance();

        recyclerView = view.findViewById(R.id.cards);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        pref = (SharedPreferences) context.getSharedPreferences("ssh13", 0);
        editor = (SharedPreferences.Editor) pref.edit();

        cardsList = new ArrayList<>();

        //loadCards();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        loadCards();
    }

    public void loadCards(){

        final ProgressDialog progressDialog = new ProgressDialog(activity_home.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        String username = pref.getString("username", null);
        String password = pref.getString("password", null);

        User user = realm.where(User.class).equalTo("username", username).equalTo("password", password).findFirst();
        realm.beginTransaction();
        if (user != null){
            Map<String, String> params = new HashMap<String, String>();
            params.put("auth_key", pref.getString("auth_key", null));

            cardsList.clear();

            Server server = new Server(context,"auth/getcards", params);
            server.setListener(new Server.ServerListener() {
                @Override
                public void OnResponse(String data) {
                    progressDialog.dismiss();
                    try{
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getBoolean("status")){
                            JSONArray array = jsonObject.getJSONArray("content");

                            cardsList.clear();

                            for (int i = 0; i < array.length(); i++){
                                JSONObject jo = array.getJSONObject(i);

                                Cards card = new Cards(
                                        jo.getInt("id"),
                                        jo.getInt("bank_id"),
                                        jo.getString("bank_name"),
                                        jo.getString("cnumb"),
                                        jo.getString("exp_date"),
                                        jo.getString("phone"),
                                        jo.getString("name"),
                                        jo.getString("key"),
                                        jo.getInt("cash")
                                );
                                cardsList.add(card);
                            }
                            adapter = new CardsAdapter(cardsList, context);
                            recyclerView.setAdapter(adapter);
                        }
                        else {
                            /////
                        }

                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnError(String error) {
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                }
            });
        }

        realm.commitTransaction();
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
