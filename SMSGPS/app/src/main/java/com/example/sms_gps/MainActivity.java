package com.example.sms_gps;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    TextView tvLatitude;
    TextView tvLongitude;
    EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Método OnCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        phoneNumber = findViewById(R.id.phoneNumber);
        requestPermission();

    }

    public void pushed(View v) {
        if (localization() != null){
            double[] ubicationArray = localization();
            tvLatitude.setText((int) ubicationArray[0]);
            tvLongitude.setText("" + ubicationArray[1]);
            sendSMS(ubicationArray[0], ubicationArray[1]);
        }
    }

    private double[] localization() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager ubication1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location loc = ubication1.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double[] ubication = new double[2];
            ubication[0] = loc.getLatitude();
            ubication[1] = loc.getLongitude();
            return ubication;
        }else {
            // Ask for permission
            requestPermission();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }
        return null;
    }

    private void sendSMS(double Latitude, double Longitude) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            String phNumber = phoneNumber.getText().toString().trim();
            String SMS =
                    "UBICACION\n" +
                            "Latitud:     " + Latitude + "\n" +
                            "Longitud:    " + Longitude + "\n\n" +
                             "https://www.google.com/maps/place/" + Latitude + "," + Longitude;

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phNumber, null, SMS, null, null);
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to send", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Ask for permission
            requestPermission();
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.SEND_SMS}, 1);
        }

    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permission not granted!");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    125);

        }
    }

}








