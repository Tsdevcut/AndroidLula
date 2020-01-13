package www.tsdevcut.co.za.luladrivedemo2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsHome extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SharedPreferences sharedPre;
    GPSTracker gps;
    Marker mkrPass, mkrDriv;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnBack                 = (ImageButton) findViewById(R.id.buttonBack);

        sharedPre               = getSharedPreferences("LulaDriverApp", Context.MODE_PRIVATE);

        //    DisplayUser();
        //    DisplayDriverUser();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsHome.this, MainScreen.class));
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       // LatLng jozy = new LatLng(-26.0, 28);
        // mMap.addMarker(new MarkerOptions().position(jozy).title("Jozy"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(jozy));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-26.0, 28), 10));
    }

    private void DisplayUser(){
        sharedPre = getSharedPreferences("LulaDriverApp", Context.MODE_PRIVATE);
        String passenger = sharedPre.getString("UserID","User");
        double platitude = 0 , plongitude =  0;
        platitude = Double.parseDouble(sharedPre.getString("PLatit","0").toString());
        plongitude = Double.parseDouble(sharedPre.getString("PLongit", "0").toString());




        if(mkrPass != null)
        {
            mkrPass.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(platitude, plongitude))
                .title(passenger)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.desticircsm)
                );
        mkrPass = mMap.addMarker(markerOptions);

    }

    private void DisplayDriverUser(){
        gps = new GPSTracker(MapsHome.this);
        sharedPre = getSharedPreferences("LulaDriverApp", Context.MODE_PRIVATE);
        String passenger = sharedPre.getString("DriverName","Drive") + ", \n" + sharedPre.getString("DriverID","ID");
        double platitude = 0 , plongitude =  0;
        platitude = Double.parseDouble(sharedPre.getString("DriverLati","0").toString());
        plongitude = Double.parseDouble(sharedPre.getString("DriverLongit", "0").toString());


        if (platitude == 0) {
            if (gps.canGetLocation()) {
                platitude = gps.getLatitude();
                plongitude = gps.getLongitude();

            }
        }
        if (mkrDriv != null) {
            mkrDriv.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(platitude, plongitude))
                .title(passenger)
                .anchor(.5f, .5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxmybook)
                );
        mkrDriv = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(platitude, plongitude), 15));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lumen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings View", Toast.LENGTH_LONG).show();

            return true;
        }
        else if (id == R.id.action_about) {
            Toast.makeText(this, "About View", Toast.LENGTH_LONG).show();

            return true;
        }
        else if (id == R.id.action_logoff) {
            SharedPreferences shardPrefs = getSharedPreferences("LulaDriverApp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editLog = shardPrefs.edit();
            editLog.clear();
            editLog.commit();

            Toast.makeText(this,"Good Bye",Toast.LENGTH_LONG).show();

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickViewDriver(View view) {
        DisplayDriverUser();
        LinearLayout lay = (LinearLayout) findViewById(R.id.layoutDriverIcon);
        lay.setVisibility(View.INVISIBLE);
    }


}
