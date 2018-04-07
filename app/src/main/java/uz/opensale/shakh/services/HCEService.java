package uz.opensale.shakh.services;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import uz.opensale.shakh.activity_home;

/**
 * Created by shakh on 12.01.18.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class HCEService extends HostApduService {
    @Override
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {
        byte[] a = {};

        if (activity_home.HCE_ENABLED){
            a = new byte[]{
                    (byte) 0x6F, (byte) 0x00
            };
        }
        else {

            Toast.makeText(activity_home.getContext(), "Эмуляция карты не включено.", Toast.LENGTH_LONG).show();
        }
        return a;
    }

    @Override
    public void onDeactivated(int i) {

    }
}
