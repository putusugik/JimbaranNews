package narucodes.jimbarannews.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import narucodes.jimbarannews.Login;
import narucodes.jimbarannews.R;
import narucodes.jimbarannews.SharedPref;

public class AdminDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentPostPusat.OnFragmentInteractionListener, FragmentPostDesa.OnFragmentInteractionListener {

    private SharedPref sharedPref;
    int id, sts_trx, sts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = new SharedPref(getApplicationContext());
        id = sharedPref.getUserID();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = FragmentPostPusat.class;
        try{
            fragment = (Fragment)fragmentClass.newInstance();
        } catch (InstantiationException e){
            e.printStackTrace();
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
       /* // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_postpusat) {
            // Handle the camera action
        } else if (id == R.id.nav_postdesa) {

        } else if (id == R.id.nav_logout){
            logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        displaySelected(item.getItemId());
        return true;
    }

    private void displaySelected(int itemId) {
        Fragment fragment = null;
        switch (itemId){
            case R.id.nav_postpusat:
                fragment = new FragmentPostPusat();
                break;
            case R.id.nav_postdesa:
                fragment = new FragmentPostDesa();
                break;
            case R.id.deletepusat:
                fragment = new DeletePostPusat();
                break;
            case R.id.deletedesa:
                fragment = new DeletePostDesa();
                break;
            case R.id.nav_logout:
                logoutUser();
                break;
        }

        if (fragment!=null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flcontent, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void logoutUser() {
        sharedPref.setLoginAdmin(false);
        Intent intent = new Intent(AdminDashboard.this, Login.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
