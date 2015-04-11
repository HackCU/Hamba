package com.hamba.hambameet;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class EventActivity extends FragmentActivity{
    private GoogleMap eventMap;
    private LatLng eventLocation = new LatLng(40.0067, -105.2672);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.eventMap)).getMap();
        eventMap.getUiSettings().setZoomControlsEnabled(true);
        eventMap.getUiSettings().setAllGesturesEnabled(false);
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(eventLocation, 12);
        eventMap.animateCamera(location);
        eventMap.addMarker(new MarkerOptions().position(eventLocation).title("University of Colorado Boulder"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
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
}
