package com.maxsavitsky.notifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private ArrayList<String> mMessages;
	private int mCurMessageId = 0;
	private NotificationManagerCompat mNotificationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		mNotificationManager = NotificationManagerCompat.from( this );
	}

	private void sendMessage(String text){
		NotificationCompat.Builder builder = new NotificationCompat.Builder( this )
				.setContentTitle( "Notifier" )
				.setContentText( text )
				.setSmallIcon(R.mipmap.ic_launcher);
		try {
			if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
				String id = "notifier_app_max_savitsky";
				NotificationChannel channel = new NotificationChannel( id, "channel", NotificationManager.IMPORTANCE_HIGH );
				mNotificationManager.createNotificationChannel( channel );
				builder.setChannelId( id );
			}
			mNotificationManager.notify( mCurMessageId++, builder.build() );
		}catch (Exception e){
			Toast.makeText( this, e.toString(), Toast.LENGTH_LONG ).show();
		}
	}

	public void send(View v){

		EditText editTextCount = findViewById(R.id.editTextCount );
		EditText editTextMain = findViewById(R.id.editTextMainText );
		CheckBox checkBox = findViewById( R.id.checkBox );
		CheckBox checkBox2 = findViewById( R.id.checkBox2 );

		if(editTextCount.getText().toString().equals( "" ) || editTextMain.getText().toString().equals( "" )) {
			Toast.makeText( this, "Заполните пустые поля", Toast.LENGTH_LONG ).show();
			return;
		}
		int count = Integer.parseInt( editTextCount.getText().toString() );
		String text = editTextMain.getText().toString();
		if(!checkBox.isChecked())
			text = text.replaceAll( "\n", "" );

		if(mCurMessageId != 0)
			mNotificationManager.cancelAll();

		mMessages = new ArrayList<>();
		mCurMessageId = 0;
		for(int i = 0; i < text.length(); i++){
			String m = "";
			int j;
			for(j = 0; j < count && i + j < text.length(); j++){
				m = String.format( "%s%c", m, text.charAt( i + j ) );
			}
			i += j-1;
			mMessages.add( m );
		}
		for(int i = 0; i < mMessages.size(); i++){
			String s;
			if(checkBox2.isChecked()){
				s = mMessages.get( mMessages.size() - i - 1 );
			}else{
				s = mMessages.get( i );
			}
			sendMessage( s );
		}
	}
}