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
	private List <ScanResult> Wlan_list;

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
			String value = (String)net_array_adapter.getItem(arg2);
			Log.e("WifiActivity", value);
			String name = value.substring(6, value.length());
			Log.e("WifiActivity", name);
			
			String SSID = Wlan_list.get(arg2).SSID;
			Log.e("WifiActivity" , SSID);
			
			String BSSID = Wlan_list.get(arg2).BSSID;
			
			Log.e("WifiActivity" , BSSID);
			int Key = 0; 
			
			String PW = "FE3CCDD1E4935AFA56A91A9A3A";

			connectToNetwork(BSSID, Key, PW, SSID);
			//finish();
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
					net_array_adapter.add("Name: " + Wlan_list.get(i).SSID);
					if (net_array_adapter.getCount() == 0){
						net_array_adapter.add("No network avaible");
					}
				}   
			}
		}
	};


	public void connectToNetwork(String sBSSID, int iSecurityType, String sSecurityKey, String sSSID){
		
		
		WifiConfiguration wfc = new WifiConfiguration();
		 
		wfc.SSID = "\"".concat(sSSID).concat("\"");
		wfc.status = WifiConfiguration.Status.DISABLED;
		wfc.priority = 40;
		wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		 
		wfc.preSharedKey = "\"".concat("FE3CCDD1E4935AFA56A91A9A3A").concat("\"");
		WifiManager wfMgr = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		int networkId = wfMgr.addNetwork(wfc);
		Log.e("WifiActivity", "networkId : " + networkId);
		if (networkId != -1) {
			Log.e("WifiActivity", "ca marche !!!!!!!!!!!!!!!!!!!!!!!!!!");
			wfMgr.enableNetwork(networkId, true);
		}
		else
			Log.e("WifiActivity", "ca marche passssssssssssssss");
		
/********************************** Pour une connexion WEP ************************/
	/*	wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		 
		if (isHexString(password)) wfc.wepKeys[0] = password;
		else wfc.wepKeys[0] = "\"".concat(password).concat("\"");
		wfc.wepTxKeyIndex = 0;*/
/**********************************************************************************/
		
/********************************** Pour une connexion sans cl� ********************/
	/*	wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		wfc.allowedAuthAlgorithms.clear();
		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);*/
/***********************************************************************************/
		
	}
}
