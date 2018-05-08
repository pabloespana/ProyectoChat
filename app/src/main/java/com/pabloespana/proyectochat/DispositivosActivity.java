package com.pabloespana.proyectochat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class DispositivosActivity extends AppCompatActivity {

    BluetoothConnect bluetoothConnect = new BluetoothConnect();
    ListView listaDisponibles;
    ArrayList<String> dispositivos;
    ArrayAdapter<String> adapter;
    BluetoothAdapter bluetooth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothConnect.habilitarBluetooth();
        setContentView(R.layout.activity_dispositivos);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDispositivos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaDisponibles = (ListView) findViewById(R.id.listViewDispositiovs);
        dispositivos = new ArrayList<String>();
        bluetooth = BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myReciever, intentFilter);
        buscarDispositivos();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, dispositivos);
        listaDisponibles.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothConnect.habilitarBluetooth();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        bluetooth.cancelDiscovery();
    }

    // Regresa a la pantalla anterior al presionar la flecha
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }


    // Empieza a descubrir dispositivos bluetooth
    public void buscarDispositivos(){
        if (bluetooth.isDiscovering()) {
            bluetooth.cancelDiscovery();
        }
        bluetooth.startDiscovery();
    }

    private final BroadcastReceiver myReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) { // acción de haber encontrado
                BluetoothDevice dispositivo = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                dispositivos.add(dispositivo.getName() + "\n" + dispositivo.getAddress());
                adapter.notifyDataSetChanged();
            }
        }
    };
}
