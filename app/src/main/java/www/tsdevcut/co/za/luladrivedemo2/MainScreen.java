package www.tsdevcut.co.za.luladrivedemo2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class MainScreen extends AppCompatActivity {

    GPSTracker gps;
    SharedPreferences sharedPre;
    TextView tvName, tvRequest;;
    Switch swShift;
    FloatingActionButton fabDrive;
    ProgressBar pbSpinner;
    Toolbar toolar;
    Boolean shiftStarted = false;
    Button btnAccept, btnBeginRide;
    ImageButton ibtnColorChange;
    String name = "";//Driver Name
    Boolean awaitRequest = false;
    String key;
    Boolean emergancyBlock = false;
    ImageView ivHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        InitializationPlace();

        setSupportActionBar(toolar);
        sharedPre = getSharedPreferences("LulaDriverApp", Context.MODE_PRIVATE);
        awaitRequest = sharedPre.getBoolean("DriverAwaitRequest", false);
        tvName.setText(sharedPre.getString("DriverName", "unknown"));


        fabDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainScreen.this, MapsHome.class));
            }
        });
        if (awaitRequest == true) {
            swShift.setChecked(true);
        }
        swShift.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shiftStarted = isChecked;

                if(shiftStarted == true){
                    //If it is the 1st time then switch it in the database to true
                    if(awaitRequest == false) {
                        SharedPreferences.Editor editShare = sharedPre.edit();
                        editShare.putBoolean("DriverAwaitRequest", true);
                        editShare.commit();
                    }
                    pbSpinner.setVisibility(View.VISIBLE);
                    ivHome.setVisibility(View.INVISIBLE);
                    swShift.setText("Shift On");
                   classToAwaitRequest();
                }
                else{
                    swShift.setText("Begin Shift");
                    ivHome.setVisibility(View.VISIBLE);
                    pbSpinner.setVisibility(View.INVISIBLE);

                    if(btnAccept.getVisibility() == View.VISIBLE){
                        btnAccept.setVisibility(View.INVISIBLE);
                    }
                    emergancyBlock = true;
                }
            }
        });

        ////////////////////////////////////////////////////////////////////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }



    private void InitializationPlace() {

        fabDrive                = (FloatingActionButton) findViewById(R.id.fabFindMap);
        toolar                  = (Toolbar) findViewById(R.id.toolbar);

        tvName                  = (TextView)findViewById(R.id.textViewDriverName);
        tvRequest               = (TextView)findViewById(R.id.textViewRequestText);
        swShift                 = (Switch) findViewById(R.id.switchBeginTheShift);

        ivHome                  = (ImageView) findViewById(R.id.imageViewLogoPLACE);
        pbSpinner               = (ProgressBar) findViewById(R.id.progressBarProcessingWaitForREQUEST);

        btnAccept               = (Button)findViewById(R.id.buttonAcceptRequest);
        btnBeginRide            = (Button) findViewById(R.id.buttonBeginTheRide);

        pbSpinner.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

    }


    private void classToAwaitRequest() {
        new AwaitClasTask().execute(new ApiConnector());
    }

    public void btnAcceptTheRequestAction(View view) {
        gps = new GPSTracker(this);
        //ReWork this method
        //Increase the button
        Toast.makeText(getApplicationContext(), "Let Us Accept", Toast.LENGTH_LONG).show();
        //btnAccept.setLayoutParams(new LinearLayout.LayoutParams(200, 70));
        sharedPre = getSharedPreferences("LulaDriverApp", Context.MODE_PRIVATE);
        String userUsername = sharedPre.getString("PassUserID", "userID");
        String pLata = sharedPre.getString("PLatit", "0");
        String pLong = sharedPre.getString("PLongit","0");
        String drLat = gps.getLatitude() + "";
        String drLon = gps.getLongitude() + "";

        SharedPreferences.Editor shareEdit= sharedPre.edit();
        shareEdit.putString("DriverLatit", drLat);
        shareEdit.putString("DriverLongit", drLon);
        shareEdit.commit();
        new AcceptClasTask(userUsername, pLata, pLong, drLat, drLon).execute(new ApiConnector());
        // btnAccept.getLayoutParams().width = 200;
        btnAccept.setEnabled(false);
        btnBeginRide.setVisibility(View.VISIBLE);
        /*
        boolean accept = sharedPre.getBoolean("OnAcceptButton",true);
        if(accept == true) {
            Toast.makeText(getApplicationContext(), "Let Us Prepare to Accept", Toast.LENGTH_LONG).show();
            new AcceptClasTask().execute(new ApiConnector());
        }
        else
        {
            SharedPreferences.Editor reChange = sharedPre.edit();
            reChange.putBoolean("OnAcceptButton", true);
            reChange.commit();
            swShift.setVisibility(View.VISIBLE);
            btnAccept.setVisibility(View.VISIBLE);
            btnAccept.setText("Accept");
            tvRequest.setText("-");
        }
        */
    }

    public void onClickBeginTheRide(View view) {


    }
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
    private class AwaitClasTask extends AsyncTask<ApiConnector, Long, JSONArray> {


        @Override
        public JSONArray doInBackground(ApiConnector... params) {

            return params[0].AwaitATrip();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            try {
                JSONObject vResult = jsonArray.getJSONObject(0);
                key = vResult.getString("myKey");
                if (key.equals("Available")) {

                    btnAccept.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor edits = sharedPre.edit();
                    edits.putString("PassUserID",vResult.getString("username"));
                    edits.putString("PLatit", vResult.getString("pLat"));
                    edits.putString("PLongit", vResult.getString("pLong"));
                    edits.commit();

                    pbSpinner.setVisibility(View.INVISIBLE);
                    ivHome.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "You have a request", Toast.LENGTH_LONG).show();

                }
                else if(emergancyBlock == true){
                    Log.d("Emergancy Block","Task Terminated");
                    //Toast.makeText(getApplicationContext(), "Emergency Exit", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Thread timers = new Thread(){
                        public void run(){

                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                                sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finally {
                                classToAwaitRequest();
                            }
                        }
                    };
                    timers.start();

                }
            } catch (Exception er) {
                er.printStackTrace();
            }
        }

    }

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
    private class AcceptClasTask extends AsyncTask<ApiConnector, Long, JSONArray> {

        String drMyLat;
        String drMyLon;
        String userNaaam;
        String drivID;
        String lat;
        String lon;
    public AcceptClasTask(String userUsername, String la, String lo, String drLt, String drLn) {
        this.userNaaam = userUsername;
        this.lat = la;
        this.lon = lo;
        this.drMyLat = drLt;
        this.drMyLon = drLn;
    }

    @Override
        public JSONArray doInBackground(ApiConnector... params) {

            drivID = "1004";
            return params[0].AcceptTrip(userNaaam, drivID, drMyLat, drMyLon);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            try {

                JSONObject vResult = jsonArray.getJSONObject(0);
                String key = vResult.getString("acceptNum");
                Log.d("AcceptClassTrip","Line 253");
                if (key.equals("Updated")) {
                    swShift.setVisibility(View.INVISIBLE);
                    btnAccept.setText("Cancel");
                    tvRequest.setText("Cab Request");

                    SharedPreferences.Editor ed = sharedPre.edit();
                    ed.putString("DefaultsChanged", "Yes");
                    ed.putString("DriverName", name);
                    ed.putString("DriverID",drivID);
                    ed.putString("DriverLati", drMyLat);
                    ed.putString("DriverLongit", drMyLon);
                    //Passenger
                    ed.putString("PassUserID",userNaaam);
                    ed.putString("PLatit",lat);
                    ed.putString("PLongit", lon);
                    //..
                    ed.putBoolean("OnAcceptButton", true);

                    ed.commit();



                    Toast.makeText(getApplicationContext(), "You have a Trip", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();

                }
            } catch (Exception er) {
                er.printStackTrace();
            }
        }

    }

//*************************************************************************************************
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
            emergancyBlock = true;
            Toast.makeText(this,"Good Bye",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Exit me", true);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
