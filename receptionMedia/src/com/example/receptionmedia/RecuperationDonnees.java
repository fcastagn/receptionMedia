package com.example.receptionmedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ActivityNotFoundException;
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
	private HashMap<String, String> site_internet = new HashMap<String, String>();
	private static int cpt_promotion = 0;
	private static int cpt_site_ent = 0;
	private static int cpt_site_pro = 0;
	private String Promotion = "Télécharger promotion ";
	private String entete_dl = "DL :";
	private String entete_site_ent = "SITE ENT :";
	private String entete_site_pro = "SITE PRO :";
	private String site_ent = "Site de l'entreprise ";
	private String site_pro = "Site du produit ";

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
			System.out.println("VALUE : " + value);
			String fichier = null;
			if(value.contains("Site"))
			{
				for (Map.Entry<String, String> entry  : site_internet.entrySet()) {
					if(entry.getKey().equals(value))
					{
						fichier = entry.getValue();
						System.out.println("fichier : " + fichier);
					}
				}
				WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
				if(wifiManager.isWifiEnabled())
					wifiManager.setWifiEnabled(false);
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(fichier)));
			}
			else
			{
				try {

					for (Map.Entry<String, String> entry  : fichierATelecharger.entrySet()) {
						if(entry.getKey().equals(value))
						{
							fichier = entry.getValue();
							System.out.println("fichier : " + fichier);
							if(!fichier.contains("http"))
							{
								WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
								wifiManager.setWifiEnabled(true);
							}
						}
					}

					getFile(fichier, ".");
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


	public void getFile(String urlStr, String destFilePath) throws IOException, URISyntaxException {
		/*int current;
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
		return streamFile;*/
		File file = new File(".\\Phone");

		if (file.exists()) {
			Uri path = Uri.fromFile(file);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(path, "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			try {
				startActivity(intent);
			} 
			catch (ActivityNotFoundException e) {
				Toast.makeText(RecuperationDonnees.this, "No Application Available to View PDF",
						Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			System.out.println("probleme le fichier n'existe pas");
		}
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
			String clef;
			try {
				Log.e("RecuperationDonnees", "je passsssssssssssssssssssse ici");
				InetAddress serverAddr = InetAddress.getByName("192.168.1.27"/*adresseIP*/);
				Log.e("RecuperationDonnees", "je passsssssssssssssssssssse laaaaaaaaaaaaaa");
				socket = new Socket(serverAddr, 8080);
				Log.e("RecuperationDonnees", "je passsssssssssssssssssssse hhhhhhhhhhhhhhhhhhhh");
				inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while (!(message = inFromServer.readLine()).equals("FIN"))
				{	
					if((message.startsWith(entete_dl)))
					{
						System.out.println( "dans le if : " + message);
						if(cpt_promotion != 0)
							clef = Promotion.concat(String.valueOf(cpt_promotion));
						else
							clef = Promotion;
						cpt_promotion++;
						System.out.println("la clef est : " + clef);
						String tmp = message.substring(entete_dl.length(), message.length());
						System.out.println("tmp : " + tmp);
						fichierATelecharger.put(clef, tmp);
						array_reception.add(clef);
						clef="";
					}
					if((message.startsWith(entete_site_ent)))
					{
						String tmp = message.substring(entete_site_ent.length(), message.length());
						if(cpt_site_ent !=0)
							clef = site_ent.concat(String.valueOf(cpt_site_ent));
						else
							clef = site_ent;
						cpt_site_ent++;
						site_internet.put(clef, tmp);
						array_reception.add(clef);
						System.out.println( "dans le else : " + message);
						clef="";
					}
					if((message.startsWith(entete_site_pro)))
					{
						String tmp = message.substring(entete_site_pro.length(), message.length());
						if(cpt_site_pro !=0)
							clef = site_pro.concat(String.valueOf(cpt_site_pro));
						else
							clef = site_pro;
						cpt_site_pro++;
						site_internet.put(clef, tmp);
						array_reception.add(clef);
						System.out.println( "dans le else : " + message);
						clef="";
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

