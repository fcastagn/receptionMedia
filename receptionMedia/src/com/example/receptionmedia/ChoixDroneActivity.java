package com.example.receptionmedia;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChoixDroneActivity extends Activity {

	private Button choixDrone;
	private Button credit;
	private boolean Wifi = false;
	private boolean Bluetooth = false;
	private WifiManager wifiManager;
	private BluetoothAdapter bluetoothAdapter;
	private static final String TAG = "MercureActivity";
	private static final long delay = 2000L;
	private boolean mRecentlyBackPressed = false;
	private Handler mExitHandler = new Handler();
	private Runnable mExitRunnable = new Runnable() {


		@Override
		public void run() {
			mRecentlyBackPressed=false;   
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choix_drone);
		choixDrone = (Button) findViewById(R.id.ConnexionDrone);
		credit = (Button)findViewById(R.id.boutonCredit);

		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE); 
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		choixDrone.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(wifiManager.isWifiEnabled())
				{
					Intent intent = new Intent(ChoixDroneActivity.this, WifiActivity.class);
					startActivity(intent);
				}
				else if(bluetoothAdapter.isEnabled())
				{
					Intent intent = new Intent(ChoixDroneActivity.this, BluetoothActivity.class);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Veuillez choisir un mode de communication dans le menu (bouton gauche)", Toast.LENGTH_LONG).show();
				}
			}
		});

		credit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ChoixDroneActivity.this, CreditsActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (mRecentlyBackPressed) {
			mExitHandler.removeCallbacks(mExitRunnable);
			mExitHandler = null;
			WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE); 
			wifiManager.setWifiEnabled(false);
			finish();  
		}
		else
		{
			mRecentlyBackPressed = true;
			Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();
			mExitHandler.postDelayed(mExitRunnable, delay);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connection_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE); 
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		switch (item.getItemId()) {
		case R.id.Bluetooth:	
			if (!bluetoothAdapter.isEnabled()) {
				try {
					bluetoothAdapter.enable();
					wifiManager.setWifiEnabled(false);
				}
				catch(Exception e)
				{
					Log.d(TAG, "BLUETOOTH activation EXCEPTION", e);
				}
			}
			Wifi = false;
			Bluetooth = true;
			return true;
		case R.id.WiFi:
			wifiManager.setWifiEnabled(true);
			bluetoothAdapter.disable();
			Wifi = true;
			Bluetooth = false;
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
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

}
