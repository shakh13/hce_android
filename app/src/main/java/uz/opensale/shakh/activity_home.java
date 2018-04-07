package uz.opensale.shakh;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import io.realm.Realm;
import uz.opensale.shakh.fragments.FragmentCards;
import uz.opensale.shakh.fragments.FragmentHistory;
import uz.opensale.shakh.fragments.FragmentMainCard;
import uz.opensale.shakh.fragments.FragmentSettings;

public class activity_home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnNavigationItemSelectedListener {

    public static boolean HCE_ENABLED = false;

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    Realm realm;
    CheckUser checkUser;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    @SuppressLint({"CommitPrefEdits", "SetTextI18n", "CommitTransaction", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //toolbar.setTitle("OK");
        
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);   // Navigation View init
        navigationView.setNavigationItemSelectedListener(this); // Navigation view listener init


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation); // Bottom Navigation View init
        bottomNavigationView.setOnNavigationItemSelectedListener(this); // Bottom navigation view listener init


        context = this;
        Realm.init(this);   // Realm init
        realm = Realm.getDefaultInstance();

        checkUser = new CheckUser();
        checkUser.check(true);

        pref = getSharedPreferences("ssh13", 0);
        editor = pref.edit();
        editor.putBoolean("canHCE", true);
        editor.apply();


        View mainMenuHeaderView = navigationView.getHeaderView(0); // Get main menu header view
        TextView mainMenuName = mainMenuHeaderView.findViewById(R.id.main_menu_username);   // Get main menu username text
        TextView mainMenuPhone = mainMenuHeaderView.findViewById(R.id.main_menu_userphone); // Get main menu user phone text


        mainMenuName.setText(checkUser.getUserFullname());  // Set main menu username
        //mainMenuPhone.setText("+998" + checkUser.getUser().getPhone()); // Set main menu user phone number

        getFragmentManager().beginTransaction().replace(R.id.fragments, new FragmentMainCard()).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUser.check(true);
        editor.putBoolean("canHCE", true);
        editor.commit();
        //loadCards(); // Load cards on resume app
    }

    @Override
    protected void onPause(){
        super.onPause();
        editor.putBoolean("canHCE", false);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        editor.clear();
        editor.commit();
    }

    public static Context getContext(){
        return context;
    } // Get activity_home Context

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        int id = item.getItemId();

        if (id == R.id.menu_home) {
            ft.replace(R.id.fragments, new FragmentMainCard());
            ft.commit();
        } else if (id == R.id.menu_profile) {

        } else if (id == R.id.menu_favorites) {

        } else if (id == R.id.menu_news){

        } else if (id == R.id.menu_settings) {
            ft.replace(R.id.fragments, new FragmentSettings());
            ft.commit();
        } else if (id == R.id.menu_exit) {

        } else if (id == R.id.menu_share){

        } else if (id == R.id.menu_history){
            ft.replace(R.id.fragments, new FragmentHistory());
            ft.commit();
        } else if (id == R.id.navigation_mycards) {
            ft.replace(R.id.fragments, new FragmentCards());
            ft.commit();
        } else if (id == R.id.navigation_history) {
            ft.replace(R.id.fragments, new FragmentHistory());
            ft.commit();
        } else if (id == R.id.navigation_home){
            ft.replace(R.id.fragments, new FragmentMainCard());
            ft.commit();
        } else if (id == R.id.hce_request){
            Intent intent = new Intent(this, HCERequestActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
