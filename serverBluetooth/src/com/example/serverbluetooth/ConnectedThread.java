package com.example.serverbluetooth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.bluetooth.BluetoothSocket;
import android.content.res.AssetManager;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private BufferedReader plec;
    private PrintWriter pred;
    private BufferedReader lecteurAvecBuffer;
	private String ligne;
	private InputStream in;
 
    public ConnectedThread(BluetoothSocket socket, InputStream input) throws IOException {
        mmSocket = socket;
        plec = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()));
        pred = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mmSocket.getOutputStream())),true);
        lecteurAvecBuffer = null;
        in = input;
        lecteurAvecBuffer = new BufferedReader(new InputStreamReader(in));
    }
 
    public void run() {
        
		try {
			
			while ((ligne = lecteurAvecBuffer.readLine()) != null)
			{
				System.out.println(ligne);
				write(ligne);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			lecteurAvecBuffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    /* Call this from the main activity to send data to the remote device */
    public void write(String chaine) {
        pred.println(chaine);
    }
 
    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}