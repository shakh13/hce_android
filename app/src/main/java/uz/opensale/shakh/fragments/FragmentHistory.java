package uz.opensale.shakh.fragments;

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

import com.google.gson.JsonObject;

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
import uz.opensale.shakh.adapters.HistoryAdapter;
import uz.opensale.shakh.models.History;
import uz.opensale.shakh.models.User;

/**
 * Created by shakh on 07.03.18.
 */

public class FragmentHistory extends Fragment {

    Realm realm;
    Context context;
    List<History> historyList;
    RecyclerView recyclerView;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        context = view.getContext();
        realm = Realm.getDefaultInstance();

        recyclerView = view.findViewById(R.id.history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        pref = (SharedPreferences) context.getSharedPreferences("ssh13", 0);
        editor = (SharedPreferences.Editor) pref.edit();

        historyList = new ArrayList<>();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        final ProgressDialog dialog = new ProgressDialog(activity_home.getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        String username = pref.getString("username", null);
        String password = pref.getString("password", null);

        User user = realm.where(User.class).equalTo("username", username).equalTo("password", password).findFirst();
        realm.beginTransaction();;
        if (user != null){
            Map<String, String> params = new HashMap<>();
            params.put("auth_key", pref.getString("auth_key", null));
            historyList.clear();

            Server server = new Server(context, "cards/gethistory", params);
            server.setListener(new Server.ServerListener() {
                @Override
                public void OnResponse(String data) {
                    dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getBoolean("status")){
                            JSONArray array = jsonObject.getJSONArray("content");

                            historyList.clear();

                            for (int i=0; i<array.length(); i++){
                                JSONObject j = array.getJSONObject(i);

                                History history = new History(
                                        j.getInt("id"),
                                        j.getInt("user_id"),
                                        j.getInt("card_id"),
                                        j.getInt("terminal_id"),
                                        j.getInt("uzs"),
                                        j.getString("created_at"),
                                        j.getString("username"),
                                        j.getString("card_number"),
                                        j.getString("terminal_name")
                                );

                                historyList.add(history);

                            }

                            recyclerView.setAdapter(new HistoryAdapter(historyList, context));
                        }
                        else {
                            Toast.makeText(context, jsonObject.getString("content"), Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnError(String error) {

                }
            });

        }
        else {
            Toast.makeText(context, "Please, log in", Toast.LENGTH_SHORT).show();
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
