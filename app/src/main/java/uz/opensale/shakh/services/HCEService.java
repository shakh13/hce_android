package uz.opensale.shakh.services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.EditText;

import java.util.List;

import uz.opensale.shakh.HCERequestActivity;
import uz.opensale.shakh.activity_home;
import uz.opensale.shakh.models.Cards;

/**
 * Created by shakh on 12.01.18.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class HCEService extends HostApduService {
    @RequiresApi(api = Build.VERSION_CODES.N)

    private Cards active_card = activity_home.getMaincard();

    private byte[] getWelcomeMessage(){
        return "Hello Terminal".getBytes();
    }

    @SuppressLint("NewApi")
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {
        byte[] a = {};

        if (selectAID(bytes)){
            Log.i("HCE", "Application selected");
            return getWelcomeMessage();
        }
        else {
            Log.i("HCE", "Received: " + new String(bytes));

            promptForResult(new PromptRunnable() {
                @Override
                public void run() {
                    super.run();
                    String value = this.getValue();
                    Intent i = new Intent(getApplication(), HCERequestActivity.class);
                    i.putExtra("extraValue", value);
                    startActivity(i);
                }
            });
            // continue............................
        }
/*
        Intent intent = new Intent(this, HCERequestActivity.class);
        intent.putExtra("bytes", bytes);
        startActivity(intent);
*/
        // bytes[0] -> command

        /*
            -- Commands --
            0x00 -> i'm terminal my ID is ((pocket) 1->auth_key_length 2..auth_key_length->auth_key auth_key+1->end_cmd) (return user_id, card_id and card_auth_key)
            0x01 -> i need xxx.xxx UZS ((pocket) 1->uzs_length 2..uzs_length->uzs(should convert to double)) (return (check (balance >= uzs) ? ok : less_balance))
            0x02 -> check_balance
            0x03 -> // something ...

         */
/*
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
*/
        return a;
    }

    @Override
    public void onDeactivated(int i) {

    }

    private boolean selectAID(byte[] apdu){
        return apdu.length > 2 && apdu[0] == (byte)0 && apdu[1] == (byte) 0xa4;
    }


    public boolean isForeground(String PackageName){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> task = manager.getRunningTasks(1);

        ComponentName componentInfo = task.get(0).topActivity;

        return componentInfo.getPackageName().equals(PackageName);
    }

    private void promptForResult(final PromptRunnable postrun){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Request");
        alert.setMessage("Requesting xxx.xx UZS");
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("PAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = input.getText().toString();
                dialogInterface.dismiss();

                postrun.setValue(value);
                postrun.run();
                return;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                return;
            }
        });

        alert.show();
    }

    private class PromptRunnable implements Runnable{

        private String v;

        void setValue(String inv){
            this.v = inv;
        }

        String getValue(){
            return this.v;
        }

        @Override
        public void run() {
            this.run();
        }
    }
}
