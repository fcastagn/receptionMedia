package com.example.receptionmedia;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class BluetoothActivity extends ListActivity{

	private ProgressBar pg;
	private BluetoothAdapter bluetoothAdaptateur;
	private boolean D=false;
	private static final String TAG = "ClasseBluetooth";
	private Button btn_scan;
	private ArrayAdapter<String> btArrayAdapter;
	Set<BluetoothDevice> pairedDevices;
	public static ArrayList<BluetoothDevice> foundDevices = new ArrayList<BluetoothDevice>();
	String mode_de_connection = "Bluetooth";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		pg = (ProgressBar) findViewById(R.id.progressBarDetectBlu);

		bluetoothAdaptateur = BluetoothAdapter.getDefaultAdapter();
		btArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);


		btn_scan = (Button)findViewById(R.id.btn_scan_blu);
		btn_scan.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Attente at = new Attente();
				at.execute();
				bluetoothAdaptateur.startDiscovery();
				IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
				registerReceiver(discoveryResult, filter);
				pairedDevices = bluetoothAdaptateur.getBondedDevices();
				btArrayAdapter.clear();
				if (pairedDevices.size() > 0) {
					for (BluetoothDevice device : pairedDevices) {
						String deviceBTName = device.getName();
						foundDevices.add(device);
						btArrayAdapter.add(deviceBTName);
					}
				}
				setListAdapter(btArrayAdapter);
			}       
		});

	}



	BroadcastReceiver discoveryResult = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent){
			String remoteDeviceName = intent.getStringExtra(
					BluetoothDevice.EXTRA_NAME);
			BluetoothDevice remoteDevice = intent.getParcelableExtra(
					BluetoothDevice.EXTRA_DEVICE);
			Log.e(TAG,"jy suisssssssssssssssssssssssss 7");
			btArrayAdapter.add(remoteDevice.getName());
			foundDevices.add(remoteDevice);
			btArrayAdapter.notifyDataSetChanged();
		}
	};


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String value = (String)btArrayAdapter.getItem(position);
		Log.e("BluetoothActivity", value);
		Intent intent = new Intent(BluetoothActivity.this, RecuperationDonnees.class);
		intent.putExtra("modeComm", mode_de_connection);
		intent.putExtra("nameBluetoot", value);
		startActivity(intent);
	} 

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public class Attente extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(12000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pg.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pg.setVisibility(View.VISIBLE);
		}

	}
}

