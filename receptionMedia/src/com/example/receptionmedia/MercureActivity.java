package com.example.receptionmedia;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ProgressBar;

public class MercureActivity extends Activity {

	private ProgressBar pg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mercure);
		pg = (ProgressBar)findViewById(R.id.barChargement);
		ChargementPageGarde cpg = new ChargementPageGarde(MercureActivity.this);
		cpg.execute();
	}

}
