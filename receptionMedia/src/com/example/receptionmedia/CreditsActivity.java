package com.example.receptionmedia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CreditsActivity extends Activity {
	TextView contact;
	//Button mail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits);
		contact = (TextView) findViewById(R.id.contact);
		contact.setPaintFlags(contact.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
		contact.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
				emailintent.setType("plain/text");
				emailintent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {"corto.maltese.aetos@gmail.com" });
				startActivity(Intent.createChooser(emailintent, "Send mail..."));
			}
		});
	}

}
