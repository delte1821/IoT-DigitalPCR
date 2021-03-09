package com.example.IoT_dPCR_V2_1;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemClock;
import android.os.Handler;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {

    private BluetoothSPP bt;

    //Thermocycling (TC) parameters
    EditText mTemp1;
    EditText mTemp2;
    EditText mTemp3;
    EditText mTime1;
    EditText mTime2;
    EditText mTime3;
    EditText mNcyc;
    Button mbtnTC;


    //Fluimaging(FI) parameters
    EditText mISO;
    EditText mExpTime;
    EditText mShuTime;
    EditText mLivtime;
    EditText mFlu;
    Button mbtnFI;

    //Analysis(AN) parameters
    EditText mDetparm1;
    EditText mDetparm2;
    EditText mMinrad;
    EditText mMaxrad;
    EditText mMindist;
    Button mbtnAN;

    //Target concentration
    TextView mCon1;
    TextView mCon2;
    TextView mCon3;
    TextView mCon4;
    TextView mCon5;
    TextView mCon6;
    TextView mCon7;
    TextView mCon8;
    TextView mCon9;

    //Send data to server
    EditText mDataid;
    Button mbtnSE;


    Handler mBluetoothHandler;

//    ConnectedBluetoothThread mThreadConnectedBluetooth;
//    BluetoothDevice mBluetoothDevice;
//    BluetoothSocket mBluetoothSocket;
//
//    final static int BT_REQUEST_ENABLE = 1;
//    final static int BT_MESSAGE_READ = 2;
//    final static int BT_CONNECTING_STATUS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = new BluetoothSPP(this); //Initializing

        if (!bt.isBluetoothAvailable()) { //Unable to connect bluetooth
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //Collecting data
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //Connected to device
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //Unpairing device
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceConnectionFailed() {

            }

            public void onDeviceCo00nnectionFailed() { //Fail to connect
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = findViewById(R.id.btnConnect); //Attempting to connect
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //Stop bluetooth
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID
                setup();
            }
        }
    }

    //Button Activity
    public void setup() {
        //TC ID
        mbtnTC = findViewById(R.id.btnThermalcycling);
        mTemp1 = findViewById(R.id.Temp1);
        mTemp2 = findViewById(R.id.Temp2);
        mTemp3 = findViewById(R.id.Temp3);
        mTime1 = findViewById(R.id.Time1);
        mTime2 = findViewById(R.id.Time2);
        mTime3 = findViewById(R.id.Time3);
        mNcyc = findViewById(R.id.Ncyc);

        //FI ID
        mbtnFI = findViewById(R.id.btnFluimaing);
        mISO = findViewById(R.id.ISO);
        mExpTime = findViewById(R.id.ExpTime);
        mShuTime = findViewById(R.id.ShuTime);
        mLivtime = findViewById(R.id.LivTime);
        mFlu = findViewById(R.id.Flu);

        //AN ID
        mbtnAN = findViewById(R.id.btnAnalysis);
        mDetparm1 = findViewById(R.id.Detparm1);
        mDetparm2 = findViewById(R.id.Detparm2);
        mMinrad = findViewById(R.id.Minrad);
        mMaxrad = findViewById(R.id.Maxrad);
        mMindist = findViewById(R.id.Mindist);

        //Data ID
        mCon1 = findViewById(R.id.Con1);
        mCon2 = findViewById(R.id.Con2);
        mCon3 = findViewById(R.id.Con3);
        mCon4 = findViewById(R.id.Con4);
        mCon5 = findViewById(R.id.Con5);
        mCon6 = findViewById(R.id.Con6);
        mCon7 = findViewById(R.id.Con7);
        mCon8 = findViewById(R.id.Con8);
        mCon9 = findViewById(R.id.Con9);

        //Send data ID
        mDataid = findViewById(R.id.Dataid);
        mbtnSE = findViewById(R.id.btnSend);

        //TC button click
        mbtnTC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("TC" + "_" + mTemp1.getText().toString()
                        + "_" + mTemp2.getText().toString() + "_" + mTemp3.getText().toString()
                        + "_" + mTime1.getText().toString() + "_" + mTime2.getText().toString()
                        + "_" + mTime3.getText().toString() + "_" + mNcyc.getText().toString()
                        + "_" + mDataid.getText().toString() + "_", true);
            }
        });

        //FI button click
        mbtnFI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("FI" + "_" + mISO.getText().toString()
                        + "_" + mExpTime.getText().toString() + "_" + mShuTime.getText().toString()
                        + "_" + mLivtime.getText().toString() + "_" + mFlu.getText().toString()
                        + "_" + mDataid.getText().toString() + "_", true);
            }
        });

        //AN button click
        mbtnAN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                bt.send("AN" + "_" + mDetparm1.getText().toString()
                        + "_" + mDetparm2.getText().toString() + "_" + mMinrad.getText().toString()
                        + "_" + mMaxrad.getText().toString() + "_" + mMindist.getText().toString()
                        + "_" + mDataid.getText().toString() + "_", true);
            }
        });

        //Data upload click
        mbtnSE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            0 );
                }
                else{
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String provider = location.getProvider();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    double altitude = location.getAltitude();
                    bt.send("ID" + "_" + mDataid.getText().toString() + "_" + provider + "_" + longitude + "_: " + latitude + "_: " + altitude + "_", true);
                }
            }
        });
    }

//    //Socket interface
//    private class ConnectedBluetoothThread extends Thread{
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedBluetoothThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//                Toast.makeText(getApplicationContext(), "Failed to connect Socket", Toast.LENGTH_LONG).show();
//            }
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//        public void run() {
//            byte[] buffer = new byte[1024];
//            int bytes;
//
//            while (true) {
//                try {
//                    bytes = mmInStream.available();
//                    if (bytes != 0) {
//                        SystemClock.sleep(100);
//                        bytes = mmInStream.available();
//                        bytes = mmInStream.read(buffer, 0, bytes);
//                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
//                    }
//                } catch (IOException e) {
//                    break;
//                }
//            }
//    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
