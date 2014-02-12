package com.example.receptionmedia;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

public class ChargementPageGarde extends AsyncTask<Void, Boolean, Void>{

	private Activity activity;
	
	public ChargementPageGarde(Activity activity)
	{
		this.activity = activity;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		activity.startActivity(new Intent(activity, ChoixDroneActivity.class));	
	}
	
}
