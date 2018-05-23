package uz.opensale.shakh.services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import uz.opensale.shakh.HCERequestActivity;
import uz.opensale.shakh.activity_home;
import uz.opensale.shakh.models.Cards;

/**
 * Created by shakh on 12.01.18.
 */

@SuppressLint("Registered")
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class HCEService extends HostApduService {
    @RequiresApi(api = Build.VERSION_CODES.N)

    byte[] prev_bytes = null;
    byte[] return_ok = "ok".getBytes();
    byte[] repeat_request = "repeat".getBytes();

    private Cards active_card = activity_home.getMaincard();

    private byte[] getWelcomeMessage(){
        return "Hello Terminal".getBytes();
    }

    @SuppressLint("NewApi")
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {

        /*
            -- Commands --
            0x00 -> i'm terminal my ID is ((pocket) 1->auth_key_length 2..auth_key_length->auth_key auth_key+1->end_cmd) (return user_id, card_id and card_auth_key)
            0x01 -> i need xxx.xxx UZS ((pocket) 1->uzs_length 2..uzs_length->uzs(should convert to double)) (return (check (balance >= uzs) ? ok : less_balance))
            0x02 -> check_balance
            0x03 -> // something ...

         */

        byte[] a = {};

        if (selectAID(bytes)){
            Log.i("HCE", "Application selected");
            return getWelcomeMessage();
        }
        else {

            if (Arrays.equals(bytes, prev_bytes)){ //
                // repeat question

                if (activity_home.HCE_REQUEST_ALLOW == -1){
                    return repeat_request;
                }
                else {
                    activity_home.HCE_CURRENT_TERMINAL_NAME = "";
                    activity_home.HCE_CURRENT_TERMINAL_ID = 0;
                    activity_home.HCE_REQUEST_ALLOW = -1;

                    prev_bytes = null;

                    return (activity_home.HCE_REQUEST_ALLOW == 1 ? "yes" : "no").getBytes();
                }
            }
            else {
                prev_bytes = bytes;

                String json_string = Arrays.toString(bytes);
                try {
                    JSONObject json = new JSONObject(json_string);
                    if (json_string == "ok"){
                        // activity_main reload main_card_fragment

                    }
                    else {

                        if (json.getInt("c") == 0) { // c -> command
                            // I'm terminal
                            activity_home.HCE_CURRENT_TERMINAL_ID = json.getInt("t");
                            activity_home.HCE_CURRENT_TERMINAL_NAME = json.getString("n");
                            return return_ok;
                        }
                        else if (json.getInt("c") == 1) {
                            // I need xxx UZS
                            double s = json.getDouble("s");
                            Intent intent = new Intent(activity_home.getContext(), HCERequestActivity.class);
                            intent.putExtra("sum", s);
                            intent.putExtra("terminal_name", activity_home.HCE_CURRENT_TERMINAL_NAME);
                            intent.putExtra("terminal_id", activity_home.HCE_CURRENT_TERMINAL_ID);
                            startActivity(intent);
                            return repeat_request;
                        }
                        else if (json.getInt("c") == 2){
                            // check balance
                            //......................................................................
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

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
}
