package uz.opensale.shakh.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
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

import uz.opensale.shakh.R;
import uz.opensale.shakh.activity_home;

/**
 * Created by shakh on 07.03.18.
 */

public class FragmentMainCard extends Fragment {

    ImageView nfc_service_toggler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_main_card, container, false);

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
