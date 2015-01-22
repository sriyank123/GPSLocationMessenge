package com.sriyank.shreks.gpslocationmessenge;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    private double mGpslatitude = 0;
    private double mGpslongitude = 0;
    private double mNetGpsLatitude = 0;
    private double mNetGpsLongitude = 0;
    SmsManager mManager = SmsManager.getDefault();
    private String phone1, phone2, phone3, name1, name2, name3, message;

    private String locality = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.defaultCustomToolbar);
        toolbar.setTitle("SOS");
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.aboutapp:
                        AlertDialog.Builder alertapp = new AlertDialog.Builder(
                                MainActivity.this);
                        alertapp.setTitle("Application Tips");
                        alertapp.setMessage("Make sure you are connected to GPS or InterNET. This application fetches your exact location with the help of GPS or approx location with the help of NET and then on a single tap it sends emergency message to your buddy along with a link to google map of your position.");

                        alertapp.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog2, int which) {

                                        dialog2.dismiss();

                                    }
                                });
                        alertapp.create();
                        alertapp.show();
                        break;
                    case R.id.aboutus:


                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("ABOUT US");
                        alert.setMessage("Application Developed by SRIYANK SIDDHARTHA");
                        alert.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();

                                    }
                                });
                        alert.create();
                        alert.show();

                        break;

                    case R.id.settings:

                        nextActivity();

                        break;


                }


                return true;
            }
        });



        netCordinatesInitialize();
        gpsInitialize();
        getMyLocationName();

    }

    public void nextActivity() {

        AlertDialog.Builder alertapp = new AlertDialog.Builder(
                MainActivity.this);
        alertapp.setTitle("Alert..!!");
        alertapp.setMessage("Make sure you are connected to GPS or InterNET. " +
                "This application fetches your exact location with the help" +
                " of GPS or approx location with the help of NET and then on" +
                " a single tap it sends emergency message to your buddy along with" +
                " a link to google map of your position.");

        alertapp.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog2, int which) {

                Intent i = new Intent(MainActivity.this, SecondPage.class);
                startActivity(i);

                dialog2.dismiss();

            }
        });
        alertapp.create();
        alertapp.show();

    }

    private void getvalues() {
        // TODO Auto-generated method stub
        SharedPreferences spget = getSharedPreferences("savecontacts",
                MODE_PRIVATE);
        name1 = spget.getString("name1", "Contact 1");
        name2 = spget.getString("name2", "Contact 2");
        name3 = spget.getString("name3", "Contact 3");

        message = spget.getString("message", "I am in emergency. Help");
        // Log.i("main", message);
        phone1 = spget.getString("phone1", "");
        phone2 = spget.getString("phone2", "");
        phone3 = spget.getString("phone3", "");
    }

    public void btn1_Onclick(View v) {

        getvalues();

        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(100);

        // Toast.makeText(MainActivity.this, name1+name2+name3+phone1,
        // Toast.LENGTH_LONG).show();
        if (phone1.equals("") && phone2.equals("") && phone3.equals("")) {

            AlertDialog.Builder alertapp = new AlertDialog.Builder(
                    MainActivity.this);
            alertapp.setTitle("Alert..!!");
            alertapp.setMessage("Make sure you are connected to GPS or InterNET. This application fetches your exact location with the help of GPS or approx location with the help of NET and then on a single tap it sends emergency message to your buddy along with a link to google map of your position.");

            alertapp.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog2, int which) {

                            Toast.makeText(MainActivity.this,
                                    "Please set atleast ONE contact",
                                    Toast.LENGTH_LONG).show();

                            Intent i = new Intent(MainActivity.this,
                                    SecondPage.class);
                            startActivity(i);

                            dialog2.dismiss();

                        }
                    });
            alertapp.create();
            alertapp.show();

        }
        // Main 1
        else if ((mGpslatitude == 0 || mGpslongitude == 0) && locality == null) {

            if (mNetGpsLatitude != 0 || mNetGpsLongitude != 0) {
                Toast.makeText(MainActivity.this,
                        "Emergency msg + Approx Map location LINK  sent. ",
                        Toast.LENGTH_LONG).show();
                if (!phone1.equals("")) {
                    mManager.sendTextMessage(phone1, null, message
                                    + "plz click/find me: "
                                    + "http://maps.google.com/maps?q=" + mNetGpsLatitude
                                    + "," + mNetGpsLongitude + "+(My+Point)&z=14&ll="
                                    + mNetGpsLatitude + "," + mNetGpsLongitude, null,
                            null);
                }
                if (!phone2.equals("")) {
                    mManager.sendTextMessage(phone2, null, message
                                    + "plz click/find me: "
                                    + "http://maps.google.com/maps?q=" + mNetGpsLatitude
                                    + "," + mNetGpsLongitude + "+(My+Point)&z=14&ll="
                                    + mNetGpsLatitude + "," + mNetGpsLongitude, null,
                            null);
                }
                if (!phone3.equals("")) {

                    mManager.sendTextMessage(phone3, null, message
                                    + "plz click/find me: "
                                    + "http://maps.google.com/maps?q=" + mNetGpsLatitude
                                    + "," + mNetGpsLongitude + "+(My+Point)&z=14&ll="
                                    + mNetGpsLatitude + "," + mNetGpsLongitude, null,
                            null);
                }

            } else {
                Toast.makeText(
                        MainActivity.this,
                        "Only Emergency msg  sent. Please enable your NET or GPS ",
                        Toast.LENGTH_LONG).show();
                if (!phone1.equals("")) {
                    mManager.sendTextMessage(phone1, null, message, null, null);
                }
                if (!phone2.equals("")) {
                    mManager.sendTextMessage(phone2, null, message, null, null);
                }
                if (!phone3.equals("")) {

                    mManager.sendTextMessage(phone3, null, message, null, null);
                }
            }

        }
        // Main 2
        else if (locality != null && (mGpslatitude == 0 || mGpslongitude == 0)) {

            if (mNetGpsLatitude != 0 || mNetGpsLongitude != 0) {
                Toast.makeText(
                        MainActivity.this,
                        "Emergency msg + Your Address + Approx Map Location link sent.",
                        Toast.LENGTH_LONG).show();
                if (!phone1.equals("")) {

                    mManager.sendTextMessage(phone1, null, message + "My address:"
                                    + locality + "plz click/find me: "
                                    + "http://maps.google.com/maps?q=" + mNetGpsLatitude
                                    + "," + mNetGpsLongitude + "+(My+Point)&z=14&ll="
                                    + mNetGpsLatitude + "," + mNetGpsLongitude, null,
                            null);
                }
                if (!phone2.equals("")) {
                    mManager.sendTextMessage(phone2, null, message + "My address:"
                                    + locality + "plz click/find me: "
                                    + "http://maps.google.com/maps?q=" + mNetGpsLatitude
                                    + "," + mNetGpsLongitude + "+(My+Point)&z=14&ll="
                                    + mNetGpsLatitude + "," + mNetGpsLongitude, null,
                            null);
                }
                if (!phone3.equals("")) {

                    mManager.sendTextMessage(phone2, null, message + "My address:"
                                    + locality + "plz click/find me: "
                                    + "http://maps.google.com/maps?q=" + mNetGpsLatitude
                                    + "," + mNetGpsLongitude + "+(My+Point)&z=14&ll="
                                    + mNetGpsLatitude + "," + mNetGpsLongitude, null,
                            null);
                }

            } else {
                Toast.makeText(MainActivity.this,
                        "Emergency msg + Your Address sent.", Toast.LENGTH_LONG)
                        .show();
                if (!phone1.equals("")) {
                    mManager.sendTextMessage(phone1, null, message + "My address :"
                            + locality, null, null);
                }
                if (!phone2.equals("")) {
                    mManager.sendTextMessage(phone2, null, message + "My address :"
                            + locality, null, null);
                }
                if (!phone3.equals("")) {

                    mManager.sendTextMessage(phone3, null, message + "My address :"
                            + locality, null, null);
                }
            }

        }
        // Main 3
        else if (locality == null && (mGpslatitude != 0 || mGpslongitude != 0)) {
            Toast.makeText(MainActivity.this,
                    "Eemergency msg + your Exact Map link sent.",
                    Toast.LENGTH_LONG).show();
            if (!phone1.equals("")) {
                mManager.sendTextMessage(phone1, null, message
                        + "plz click/find me: "
                        + "http://maps.google.com/maps?q=" + mGpslatitude + ","
                        + mGpslongitude + "+(My+Point)&z=14&ll=" + mGpslatitude
                        + "," + mGpslongitude, null, null);
            }
            if (!phone2.equals("")) {
                mManager.sendTextMessage(phone2, null, message
                        + "plz click/find me: "
                        + "http://maps.google.com/maps?q=" + mGpslatitude + ","
                        + mGpslongitude + "+(My+Point)&z=14&ll=" + mGpslatitude
                        + "," + mGpslongitude, null, null);
            }
            if (!phone3.equals("")) {

                mManager.sendTextMessage(phone3, null, message
                        + "plz click/find me: "
                        + "http://maps.google.com/maps?q=" + mGpslatitude + ","
                        + mGpslongitude + "+(My+Point)&z=14&ll=" + mGpslatitude
                        + "," + mGpslongitude, null, null);
            }

        }
        // Main 4
        else if (locality != null && (mGpslatitude != 0 || mGpslongitude != 0)) {
            Toast.makeText(
                    MainActivity.this,
                    "Emergency msg + Your location + Exact Map Location link of your position sent.",
                    Toast.LENGTH_LONG).show();
            if (!phone1.equals("")) {

                mManager.sendTextMessage(phone1, null, message + "My address:"
                        + locality + "plz click/find me: "
                        + "http://maps.google.com/maps?q=" + mGpslatitude + ","
                        + mGpslongitude + "+(My+Point)&z=14&ll=" + mGpslatitude
                        + "," + mGpslongitude, null, null);
            }
            if (!phone2.equals("")) {
                mManager.sendTextMessage(phone2, null, message + "My address:"
                        + locality + "plz click/find me: "
                        + "http://maps.google.com/maps?q=" + mGpslatitude + ","
                        + mGpslongitude + "+(My+Point)&z=14&ll=" + mGpslatitude
                        + "," + mGpslongitude, null, null);
            }
            if (!phone3.equals("")) {

                mManager.sendTextMessage(phone2, null, message + "My address:"
                        + locality + "plz click/find me: "
                        + "http://maps.google.com/maps?q=" + mGpslatitude + ","
                        + mGpslongitude + "+(My+Point)&z=14&ll=" + mGpslatitude
                        + "," + mGpslongitude, null, null);
            }

        }

    }

    // Initial GPS settings
    public void gpsInitialize() {

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MyGPSLocationListener mlocListener = new MyGPSLocationListener();

        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                mlocListener);

    }

    // Initial NET GPS Cordinates settings
    public void netCordinatesInitialize() {

        LocationManager netmlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MyNETGPSLocationListener netlocListener = new MyNETGPSLocationListener();

        netmlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                0, 0, netlocListener);

    }

    public class MyGPSLocationListener implements LocationListener

    {

        @Override
        public void onLocationChanged(Location loc)

        {
            mGpslatitude = loc.getLatitude();
            mGpslongitude = loc.getLongitude();

        }

        @Override
        public void onProviderDisabled(String provider)

        {

        }

        @Override
        public void onProviderEnabled(String provider)

        {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)

        {

        }

    }/* End of Class MyLocationListener */

    public class MyNETGPSLocationListener implements LocationListener

    {

        @Override
        public void onLocationChanged(Location netloc)

        {
            mNetGpsLatitude = netloc.getLatitude();
            mNetGpsLongitude = netloc.getLongitude();

        }

        @Override
        public void onProviderDisabled(String provider)

        {

        }

        @Override
        public void onProviderEnabled(String provider)

        {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)

        {

        }

    }/* End of Class MyLocationListener */

    public void getMyLocationName() {
        Geocoder gCoder = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gCoder.getFromLocation(mNetGpsLatitude, mNetGpsLongitude, 1);
            if (addresses != null && addresses.size() > 0) {
                // Constants.locality = addresses.get(0).getLocality();
                try {
                    Address adrs = addresses.get(0);

                    locality = adrs.getFeatureName() + ","
                            + adrs.getSubLocality() + "," + adrs.getLocality()
                            + "," + adrs.getAdminArea() + ","
                            + adrs.getPostalCode() + ","
                            + adrs.getCountryName() + ","
                            + adrs.getCountryCode();

                } catch (Exception e) {

                }

            } else {
                // Toast.makeText(getApplicationContext(),
                // "Unable to get the location name", Toast.LENGTH_LONG)
                // .show();
            }
        } catch (IOException e) {


            e.printStackTrace();
        }
    }


}
