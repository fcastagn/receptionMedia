package com.example.receptionmedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RecuperationDonnees extends Activity {


	private String adresseIP;
	private Socket socket;
	private ProgressBar pg;
	private ListView lv;
	private BufferedReader inFromServer;
	private List <String> bufferReception;
	private ArrayAdapter <String> array_reception;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recuperation_donnees);
		Bundle b = getIntent().getExtras();
		adresseIP = b.getString("IP");
		Log.e("RecuperationDonnees", "adresse IPPPPPPPPPPPPPPPP " + adresseIP);
		pg = (ProgressBar)findViewById(R.id.progressBarComm);
		array_reception = new ArrayAdapter <String> (this, android.R.layout.simple_list_item_1);
		lv = (ListView)findViewById(R.id.listViewComm);
		lv.setAdapter(array_reception);
		lv.setOnItemClickListener(Net);
		if (!adresseIP.equals("")) {  
			CommClient commClient=new CommClient();
			commClient.execute();
		}	
		
		
	}

	private OnItemClickListener Net = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			String value = (String)array_reception.getItem(arg2);
			
		}
	};
	
	private class CommClient extends AsyncTask<Void, Void, Void>
	{

		private String message;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(getApplicationContext(), "Recherche de documents provenant du drone", Toast.LENGTH_LONG).show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Log.e("RecuperationDonnees", "je passsssssssssssssssssssse ici");
				InetAddress serverAddr = InetAddress.getByName(adresseIP);
				Log.e("RecuperationDonnees", "je passsssssssssssssssssssse laaaaaaaaaaaaaa");
				socket = new Socket(serverAddr, 8080);
				Log.e("RecuperationDonnees", "je passsssssssssssssssssssse hhhhhhhhhhhhhhhhhhhh");
				inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while (!(message = inFromServer.readLine()).equals("FIN"))
				{
					message = "https://www.facebook.com/";
					array_reception.add(message);
					message= "FIN";
				}
				pg.setVisibility(View.INVISIBLE);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
		}
	}
}

