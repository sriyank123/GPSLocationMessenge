package com.sriyank.shreks.gpslocationmessenge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecondPage extends ActionBarActivity implements OnClickListener,
		OnLongClickListener{

	Dialog d, e;

	EditText etmsg;
	EditText etdialogcustommsg;
	TextView tv, tvphone1, tvphone2, tvphone3, tvname1, tvname2, tvname3;
	LinearLayout root, l1, l2, l3;
	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.mymenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.aboutus:

			AlertDialog.Builder alert = new AlertDialog.Builder(SecondPage.this);
			alert.setTitle("ABOUT US");
			alert.setMessage("Application Developed by SRIYANK SIDDHARTHA");
			alert.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();

						}
					});
			alert.create();
			alert.show();

			break;

		case R.id.aboutapp:
			AlertDialog.Builder alertapp = new AlertDialog.Builder(
					SecondPage.this);
			alertapp.setTitle("Application Tips");
			alertapp.setMessage("Make sure you are connected to GPS or InterNET. This application fetches your exact location with the help of GPS or approx location with the help of NET and then on a single tap it sends emergency message to your buddy along with a link to google map of your position.");

			alertapp.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog2, int which) {

							dialog2.dismiss();

						}
					});
			alertapp.create();
			alertapp.show();

			break;

		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.secondpage);



		tv = (TextView) findViewById(R.id.tv);
		tvphone1 = (TextView) findViewById(R.id.tvphone1);
		tvphone2 = (TextView) findViewById(R.id.tvphone2);
		tvphone3 = (TextView) findViewById(R.id.tvphone3);
		tvname3 = (TextView) findViewById(R.id.tvname3);
		tvname1 = (TextView) findViewById(R.id.tvname1);
		tvname2 = (TextView) findViewById(R.id.tvname2);
		etmsg = (EditText) findViewById(R.id.etmsg);

		root = (LinearLayout) findViewById(R.id.rootlayout);
		l1 = (LinearLayout) findViewById(R.id.l1);
		l2 = (LinearLayout) findViewById(R.id.l2);
		l3 = (LinearLayout) findViewById(R.id.l3);

		SharedPreferences spget = getSharedPreferences("savecontacts",
				MODE_PRIVATE);
		etmsg.setText(spget.getString("message", "I am in emergency. Help"));
		tvname1.setText(spget.getString("name1", "Contact 1"));
		tvname2.setText(spget.getString("name2", "Contact 2"));
		tvname3.setText(spget.getString("name3", "Contact 3"));

		tvphone1.setText(spget.getString("phone1", ""));
		tvphone2.setText(spget.getString("phone2", ""));
		tvphone3.setText(spget.getString("phone3", ""));

		l1.setOnClickListener(this);
		l2.setOnClickListener(this);
		l3.setOnClickListener(this);
		etmsg.setOnClickListener(this);

		l1.setOnLongClickListener(this);
		l2.setOnLongClickListener(this);
		l3.setOnLongClickListener(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			Uri uri = data.getData();

			if (uri != null) {
				Cursor c = null;
				try {
					c = getContentResolver()
							.query(uri,
									new String[] {
											ContactsContract.CommonDataKinds.Phone.NUMBER,
											ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
									null, null, null);

					if (c != null && c.moveToFirst()) {
						String number = c.getString(0);
						String Name = c.getString(1);

						if (requestCode == 1) {

							tvname1.setText(Name);
							tvphone1.setText(number);

						} else if (requestCode == 2) {
							tvname2.setText(Name);
							tvphone2.setText(number);

						} else if (requestCode == 3) {
							tvname3.setText(Name);
							tvphone3.setText(number);

						}

					}
				} finally {
					if (c != null) {
						c.close();
					}
				}
			}

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
		switch (v.getId()) {
		case R.id.l1:

			startActivityForResult(intent, 1);

			break;
		case R.id.l2:

			startActivityForResult(intent, 2);

			break;
		case R.id.l3:

			startActivityForResult(intent, 3);

			break;
		case R.id.etmsg:

			d = new Dialog(SecondPage.this);
			d.setContentView(R.layout.custom_message_dialog);
			d.setTitle("Your Message");

			etdialogcustommsg = (EditText) d.findViewById(R.id.etcustommessage);
			d.show();

			Button bOK,
			bCANCEL;
			bOK = (Button) d.findViewById(R.id.bpositive);
			bCANCEL = (Button) d.findViewById(R.id.bnegative);

			bOK.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					etmsg.setText(etdialogcustommsg.getText().toString());
					d.dismiss();
				}
			});

			bCANCEL.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					d.dismiss();
				}
			});

		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences sp = getSharedPreferences("savecontacts",
				MODE_PRIVATE);

		SharedPreferences.Editor e = sp.edit();

		e.putString("message", etmsg.getText().toString());

		e.putString("phone1", tvphone1.getText().toString());
		e.putString("phone2", tvphone2.getText().toString());
		e.putString("phone3", tvphone3.getText().toString());

		e.putString("name1", tvname1.getText().toString());
		e.putString("name2", tvname2.getText().toString());
		e.putString("name3", tvname3.getText().toString());

		e.commit();

	}

	@Override
	public boolean onLongClick(View arg0) {
		// TODO Auto-generated method stub
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(100);

		switch (arg0.getId()) {

		case R.id.l1:

			AlertDialog.Builder alert = new AlertDialog.Builder(SecondPage.this);
			alert.setTitle("Delete Contact");
			alert.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							tvphone1.setText("");
							tvname1.setText("Contact 1");

						}
					});
			alert.setNegativeButton("CANCEL",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			alert.show();
			alert.create();
			break;
		case R.id.l2:

			AlertDialog.Builder alert2 = new AlertDialog.Builder(
					SecondPage.this);
			alert2.setTitle("Delete Contact");
			alert2.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							tvphone2.setText("");
							tvname2.setText("Contact 2");

						}
					});
			alert2.setNegativeButton("CANCEL",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			alert2.show();
			alert2.create();
			break;
		case R.id.l3:

			AlertDialog.Builder alert3 = new AlertDialog.Builder(
					SecondPage.this);
			alert3.setTitle("Delete Contact");
			alert3.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							tvphone3.setText("");
							tvname3.setText("Contact 3");

						}
					});
			alert3.setNegativeButton("CANCEL",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			alert3.show();
			alert3.create();
			break;

		}
		return false;

	}


}