package com.example.receptionmedia;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RecuperationDonnees extends Activity{


	private String adresseIP;
	private Socket socket;
	private ProgressBar pg;
	private ListView lv;
	private BufferedReader inFromServer;
	private List <String> bufferReception;
	private ArrayAdapter <String> array_reception;
	private HashMap<String, String> fichierATelecharger = new HashMap<String, String>();
	private static int cpt = 0;
	private String Promotion = "promotion ";

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
			String fichier = null;
			if(value.contains("http"))
			{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(value)));
			}
			else
			{
				try {
					
					for (Map.Entry<String, String> entry : fichierATelecharger.entrySet()) {
						if(entry.getKey().equals(value))
						{
							fichier = entry.getValue();
							System.out.println("fichier : " + fichier);
						}
					}
					File f = getFile(fichier, ".");
					//String a ="\\Phone\\Download\\Autefage1-article.pdf";
					//File f = getFile(a,".");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	};


	public File getFile(String urlStr, String destFilePath) throws IOException, URISyntaxException {
		int current;
		if (urlStr == null) {
			Log.d("getFile", "null");
			return null;
		}
		URL url = null;
		url = new URL(urlStr);
		HttpURLConnection con;
		con = (HttpURLConnection) url.openConnection();
		con.setUseCaches(true);
		InputStream is = con.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is, 8192);

		File streamFile = new File(destFilePath);

		if (!streamFile.exists()) {
			FileOutputStream fw = new FileOutputStream(streamFile);

			byte[] buffer = new byte[1024];
			int bytes_read;
			while ((bytes_read = is.read(buffer)) != -1) {
				fw.write(buffer, 0, bytes_read);
			}


			fw.flush();
			fw.close();
		} else {
			return streamFile;
		}
		return streamFile;
	}

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
				InetAddress serverAddr = InetAddress.getByName("192.168.1.27"/*adresseIP*/);
				Log.e("RecuperationDonnees", "je passsssssssssssssssssssse laaaaaaaaaaaaaa");
				socket = new Socket(serverAddr, 8080);
				Log.e("RecuperationDonnees", "je passsssssssssssssssssssse hhhhhhhhhhhhhhhhhhhh");
				inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while (!(message = inFromServer.readLine()).equals("FIN"))
				{	
					if(!(message.startsWith("http")))
					{
						System.out.println( "dans le if : " + message);
						String clef = Promotion.concat(String.valueOf(cpt));
						cpt++;
						System.out.println("la clef est : " + clef);
						fichierATelecharger.put(clef, message);
						array_reception.add(clef);
					}
					else
					{
						array_reception.add(message);
						System.out.println( "dans le else : " + message);
					}
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
			WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
			wifiManager.setWifiEnabled(false);
		}
	}

}

