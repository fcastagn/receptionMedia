package com.example.serverbluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class ServeurBluetooth extends Thread {
	private static final UUID MON_UUID = UUID.fromString("80a6f6d4-8c79-4eed-b391-7fefd6a259ec");
	private static final String NOM = "Server";
	private final BluetoothServerSocket blueServerSocket;
	private BluetoothAdapter bluetoothAdaptateur;
	private InputStream in;
	public ServeurBluetooth(InputStream input) {
		bluetoothAdaptateur = BluetoothAdapter.getDefaultAdapter();
		in= input;
		BluetoothServerSocket tmp = null;
		try {
			System.out.println("jy suisssssssssssssssssssssssssssss 1");
			tmp = bluetoothAdaptateur.listenUsingRfcommWithServiceRecord(NOM, MON_UUID);
			System.out.println("jy suisssssssssssssssssssssssssssss 2");
		} catch (IOException e) { }
		blueServerSocket = tmp;
	}

	public void run() {
		BluetoothSocket blueSocket = null;
		System.out.println("jy suisssssssssssssssssssssssssssss 3");
		while (true) {
			try {
				System.out.println("jy suisssssssssssssssssssssssssssss 4");
				blueSocket = blueServerSocket.accept();
				System.out.println("jy suisssssssssssssssssssssssssssss 5");
			} catch (IOException e) {
				break;
			}
			// Si une connexion est acceptée
			if (blueSocket != null) {
				// On fait ce qu'on veut de la connexion (dans un thread séparé), à vous de la créer
				System.out.println("jy suisssssssssssssssssssssssssssss 6");
				ConnectedThread ct = null;
				try {
					ct = new ConnectedThread(blueSocket,in);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ct.start();

				break;
			}
		}
	}

	// On stoppe l'écoute des connexions et on tue le thread
	public void cancel() {
		try {
			blueServerSocket.close();
		} catch (IOException e) { }
	}
}
