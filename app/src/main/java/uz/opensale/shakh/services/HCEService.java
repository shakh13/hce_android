package uz.opensale.shakh.services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.app.Activity;
import android.widget.Toast;

import java.util.List;

import uz.opensale.shakh.HCERequestActivity;
import uz.opensale.shakh.R;
import uz.opensale.shakh.activity_home;
import uz.opensale.shakh.models.Cards;

/**
 * Created by shakh on 12.01.18.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class HCEService extends HostApduService {
    @RequiresApi(api = Build.VERSION_CODES.N)

    private Cards active_card = activity_home.getMaincard();


    @SuppressLint("NewApi")
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {
        byte[] a = {};

        Intent intent = new Intent(this, HCERequestActivity.class);
        startActivity(intent);

        // bytes[0] -> command

        /*
            -- Commands --
            0x00 -> i'm terminal my ID is ((pocket) 1->auth_key_length 2..auth_key_length->auth_key auth_key+1->end_cmd) (return user_id, card_id and card_auth_key)
            0x01 -> i need xxx.xxx UZS ((pocket) 1->uzs_length 2..uzs_length->uzs(should convert to double)) (return (check (balance >= uzs) ? ok : less_balance))
            0x02 -> check_balance
            0x03 -> // something ...

         */
        Intent inte = new Intent(this, HCERequestActivity.class);
        inte.putExtra("bytes", bytes);

        switch (bytes[0]){
            case 0x00:
                // I'm terminal
                Intent in = new Intent(this, HCERequestActivity.class);
                in.putExtra("terminal", bytes.toString());
                startActivity(in);
                break;
            case 0x01:
                // I need xxx.xxx uzs
                double uzs = Integer.valueOf(String.valueOf(bytes[1]))*100 + Integer.valueOf(String.valueOf(bytes[2]))*10 + Integer.valueOf(String.valueOf(bytes[3])) + Integer.valueOf(String.valueOf(bytes[5]))/10 + Integer.valueOf(String.valueOf(bytes[6]))/100 + Integer.valueOf(String.valueOf(bytes[7]))/1000;
                Toast.makeText(activity_home.getContext(), "Need " + String.valueOf(uzs), Toast.LENGTH_SHORT).show();
                break;
            case 0x02:

                break;

            case 0x03:

                break;

            default:
                Toast.makeText(activity_home.getContext(), "This is not trusted teminal. Please, remove your phone from this device.", Toast.LENGTH_LONG).show();
                break;
        }

        return a;
    }

    @Override
    public void onDeactivated(int i) {

    }

    public boolean isForeground(String PackageName){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> task = manager.getRunningTasks(1);

        ComponentName componentInfo = task.get(0).topActivity;

        return componentInfo.getPackageName().equals(PackageName);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if (intent.getExtras() != null){
            String massage = intent.getExtras().getString("message");
        }

        return START_NOT_STICKY;
    }
}
