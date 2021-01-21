package com.pontoon.scanbt;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSIONS_REQUEST_CODE = 11;
    ArrayList<Map<String, Integer>> deviceInfo = new ArrayList<>();

    List<String> headerList = new ArrayList<>();
    List<String> dataList = new ArrayList<>();
    Snapobj snap ;
    List<Snapobj > snapList = new ArrayList<Snapobj>();
    public final int WRITE_PERMISSON_REQUEST_CODE =111;

    ListView listDevicesFound;
    Button btnScanDevice;
    Button btnStopScanning;
    TextView stateBluetooth;
    BluetoothAdapter bluetoothAdapter;

    ArrayAdapter<String> btArrayAdapter;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef;
    private DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("BluetoothDevicesInfo");

    List<DeviceInfo> deviceList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!havePermissions()) {
            Log.d("TAG", "Requesting permissions needed for this app.");
            requestPermissions();
        }

        setContentView(R.layout.activity_main);

        btnScanDevice = (Button)findViewById(R.id.scandevice);
        btnStopScanning = (Button)findViewById(R.id.stopscanning);

        stateBluetooth = (TextView)findViewById(R.id.bluetoothstate);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        listDevicesFound = (ListView)findViewById(R.id.devicesfound);
        btArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
        listDevicesFound.setAdapter(btArrayAdapter);

        CheckBlueToothState();
        deviceList = new ArrayList<>();
        btnScanDevice.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                btArrayAdapter.clear();
                if (bluetoothAdapter == null){
                    Toast.makeText(getBaseContext(), "Bluetooth is not supported on this hardware platform", Toast.LENGTH_SHORT).show();
                    Log.d("Output", "Bluetooth is not supported on this hardware platform");
                    return;
                }
                bluetoothAdapter.startDiscovery();
            }
        });



        registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        btnStopScanning.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            //On click function
            public void onClick(View view) {
                btArrayAdapter.clear();
                //unregisterReceiver(ActionFoundReceiver);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(ActionFoundReceiver);

       // System.out.println("this is onDestroy : "+deviceInfo);

       /* Map<String, Integer> sumMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();
        Map<String, Double> meanMap = new HashMap<>();
        for (Map<String, Short> map : deviceInfo) {
            for (Map.Entry<String, Short> entry : map.entrySet()) {
                sumMap.put(entry.getKey(), sumMap.getOrDefault(entry.getKey(), 0) + entry.getValue());
                countMap.put(entry.getKey(), countMap.getOrDefault(entry.getKey(), 0) + 1);
                meanMap.put(entry.getKey(),
                        (double) sumMap.getOrDefault(entry.getKey(), 0) / countMap.getOrDefault(entry.getKey(), 1));
            }
        }

        // Display
        System.out.println("mean map : "+meanMap);

        EditText x = (EditText) findViewById(R.id.distance);
        String distance = x.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String id = dateFormat.format(new Date());

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;
        databaseReference = db.getReference("BluetoothDevicesInfor");
        //String id = databaseReference.push().getKey();
        JSONObject json = new JSONObject();*/
       // DeviceInfo deviceInfoObj = new DeviceInfo(distance,meanMap);
        //databaseReference.child(id).setValue(deviceInfoObj);

    }

    private void CheckBlueToothState(){
        if (bluetoothAdapter == null){
           // stateBluetooth.setText("Bluetooth NOT support");
        }else{
            if (bluetoothAdapter.isEnabled()){
                if(bluetoothAdapter.isDiscovering()){
                   // stateBluetooth.setText("Bluetooth is currently in device discovery process.");
                }else{
                    //stateBluetooth.setText("Bluetooth is Enabled.");
                    btnScanDevice.setEnabled(true);
                }
            }else{
               // stateBluetooth.setText("Bluetooth is NOT Enabled!");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(requestCode == REQUEST_ENABLE_BT){
            CheckBlueToothState();
        }
        if(requestCode == PERMISSIONS_REQUEST_CODE){
            CheckBlueToothState();
        }
    }

    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            // TODO Auto-generated method stub
            String action = intent.getAction();
            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

            Map<String, Integer> rssiMapper = new HashMap<>();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //double distance = Math.pow(10d, ((double) (-65) - rssi) / (10 * 2));

                if (device == null) {
                    Toast.makeText(getApplicationContext(), "No devices found", Toast.LENGTH_SHORT).show();
                    Log.d("Output", "No devices found");
                    return;
                }

                if(device.getName() == null){
                    Toast.makeText(getApplicationContext(), "No device name found", Toast.LENGTH_SHORT).show();
                    Log.d("Output", "No device name found");
                    return;
                }

                btArrayAdapter.add(device.getName() + "\nAddress : " + device.getAddress() + "\nRSSI : " + rssi+"\n");
                btArrayAdapter.notifyDataSetChanged();

                rssiMapper.put(device.getName(), rssi);
                deviceInfo.add(rssiMapper);
                System.out.println("hashmap : "+rssiMapper);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                EditText x = (EditText) findViewById(R.id.distance);
                x.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                String distanceVal = x.getText().toString();

                EditText y = findViewById(R.id.deviceName);
                String deviceName = y.getText().toString();

                    if (device.getName().equals(deviceName) && device.getName() != null) {
                        databaseReference = db.getReference("BluetoothDevicesInfo");
                        DeviceInfo deviceInfoObj = new DeviceInfo(device.getAddress(), dateFormat.format(new Date()), rssi, distanceVal, device.getName());
                        String id = databaseReference.push().getKey();
                        databaseReference.child(id).setValue(deviceInfoObj);
                        Toast.makeText(getApplicationContext(), "Successfully Saved.", Toast.LENGTH_SHORT).show();
                    } else {
                       // Toast.makeText(getApplicationContext(), "Couldn't find a devices that matches your input", Toast.LENGTH_SHORT).show();
                    }

            }else{
                Toast.makeText(getApplicationContext(), "No Bluetooth device is found", Toast.LENGTH_SHORT).show();
            }

        }

    };

    private void saveValues(Map<String, Short> rssiMapper) {
        Log.d("Output", rssiMapper.toString());
    }


    private boolean havePermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
        Log.d("TAG", "requestPermissions");
    }

}
