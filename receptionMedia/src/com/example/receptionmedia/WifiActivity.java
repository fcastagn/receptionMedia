package com.example.receptionmedia;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class WifiActivity extends Activity {

	private static WifiManager WifiManager;
	private ArrayAdapter <String> net_array_adapter;
	private List <ScanResult >Wlan_list;

	public static final int WPA = 1;
	public static final int WEP = 2;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_wifi);

	    WifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);

	    net_array_adapter = new ArrayAdapter <String> (this, android.R.layout.simple_list_item_1);
	    ListView lv_net = (ListView)findViewById(R.id.lv_net);
	    lv_net.setAdapter(net_array_adapter);
	    lv_net.setOnItemClickListener(Net);

	    IntentFilter filter = new IntentFilter(android.net.wifi.WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
	    this.registerReceiver(Wifi, filter);

	    //Scan
	    Button btn_scan = (Button)findViewById(R.id.btn_scan);
	    btn_scan.setOnClickListener(new OnClickListener(){
	        public void onClick(View v) {
	            Discovery();
	        }       
	    });
	}

	protected void Discovery() {
	    WifiManager.startScan();
	}

	private OnItemClickListener Net = new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

	        WifiInfo WifiInfo = WifiManager.getConnectionInfo();
	        //Name
	        String SSID = WifiInfo.getSSID();
	        //MAC-Adresse
	        String BSSID = WifiInfo.getBSSID();
	        //Sichertheit
	        int Key = 0; //!!! EDIT: Key= 1 or 2 --> still dont work
	        //Passwort
	        String PW = "123";

	       // connectToNetwork(BSSID, Key, PW, SSID);
	        finish();
	    }
	};

	private final BroadcastReceiver Wifi = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        net_array_adapter.clear();
	        Wlan_list = WifiManager.getScanResults();
	        if (WifiManager.isWifiEnabled() == false){
	            net_array_adapter.add("WLAN is currently disabled");
	        } else {
	            for(int i = 0; i < Wlan_list.size(); i++){
	                net_array_adapter.add("Name: " + Wlan_list.get(i).SSID + "\nMAC-Adresse: " + Wlan_list.get(i).BSSID);
	                if (net_array_adapter.getCount() == 0){
	                    net_array_adapter.add("No network avaible");
	                }
	            }   
	        }
	    }
	};


	/*public static boolean connectToNetwork(String sBSSID, int iSecurityType, String sSecurityKey, String sSSID){

	    WifiConfiguration tmpConfig;

	    List <WifiConfiguration> listConfig = WifiManager.getConfiguredNetworks();
	    Log.e("WifiActivity",String.valueOf(listConfig.size()));
	    for (int i = 0; i<listConfig.size(); i++){  	
	        tmpConfig = listConfig.get(i);
	        if (tmpConfig.BSSID.equalsIgnoreCase(sBSSID)){
	            return WifiManager.enableNetwork(tmpConfig.networkId, true);
	        }
	    }

	    tmpConfig = new WifiConfiguration();
	    tmpConfig.BSSID = sBSSID;
	    tmpConfig.SSID = sSSID; 
	    tmpConfig.priority = 1;

	    switch(iSecurityType){
	    //WPA
	    case WPA:
	        tmpConfig.preSharedKey = sSecurityKey;
	        break;
	    //WEP
	    case WEP:
	        tmpConfig.wepKeys[0] = sSecurityKey;
	        tmpConfig.wepTxKeyIndex = 0;
	        break;
	    default:
	        break;
	    }
	    tmpConfig.status = WifiConfiguration.Status.ENABLED;

	    int netId = WifiManager.addNetwork(tmpConfig);

	    return WifiManager.enableNetwork(netId, true);
	}*/
}
