package uz.opensale.shakh.services;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

/**
 * Created by shakh on 12.01.18.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class HCEService extends HostApduService {
    @Override
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {
        byte[] a = {
            (byte) 0x6F, (byte) 0x00
        };
        return a;
    }

    @Override
    public void onDeactivated(int i) {

    }
}
