package com.example.receptionmedia;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class ClientBluetooth extends Thread{
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothDevice deviceServer;
	private BluetoothSocket socket;
	private final UUID uuid;
	private BluetoothDevice device;
	private boolean connected= false;
	
	public ClientBluetooth(BluetoothDevice device)
	{
		deviceServer = device;
		System.out.println("le device server est :" + deviceServer.getName());
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		uuid = UUID.fromString("80a6f6d4-8c79-4eed-b391-7fefd6a259ec");
	}

	public void connectServer() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		BluetoothSocket tmp = null;
		try {
			// MY_UUID is the app's UUID string, also used by the server code
			System.out.println("ettttttttttttttttttttttttttttttttttt 1");
			tmp = deviceServer.createInsecureRfcommSocketToServiceRecord(uuid);
			System.out.println("ettttttttttttttttttttttttttttttttttt 2");
		} catch (IOException e) { }
		socket = tmp;
		System.out.println(socket.toString());
		/*device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(MAC);
		Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[] { int.class });
		socket = (BluetoothSocket)m.invoke(device, Integer.valueOf(1));
		System.out.println(socket.toString());*/
	}

	public void run() {

		try {
			// Connect the device through the socket. This will block
			// until it succeeds or throws an exception
			System.out.println("ettttttttttttttttttttttttttttttttttt 3");
			System.out.println(socket.toString());
			socket.connect();
			System.out.println("Connecté");
			connected = true;
			while(true);
		} catch (IOException connectException) {
			// Unable to connect; close the socket and get out
		}

	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public BluetoothSocket getSocket() {
		return socket;
	}

	public void setSocket(BluetoothSocket socket) {
		this.socket = socket;
	}

	/** Will cancel an in-progress connection, and close the socket */
	public void cancel() {
		try {
			socket.close();
		} catch (IOException e) { }
	}

}
