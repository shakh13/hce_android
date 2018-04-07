package uz.opensale.shakh.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uz.opensale.shakh.R;

/**
 * Created by shakh on 28.03.18.
 */

public class FragmentSettings extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
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
