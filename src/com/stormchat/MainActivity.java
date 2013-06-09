package com.stormchat;

import java.io.IOException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.gvsu.cis.masl.channelAPI.ChannelAPI;
import edu.gvsu.cis.masl.channelAPI.ChannelAPI.ChannelException;

public class MainActivity extends Activity {
	private TextView textView;
	private EditText textInput;
	private ChatListener chatListener;
	private ChannelAPI channel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView = (TextView) findViewById(R.id.textView1);

		textInput = (EditText) findViewById(R.id.editText1);
		Button connectButton = (Button) findViewById(R.id.connectBtn);
		Button disconnectButton = (Button) findViewById(R.id.discBtn);
		Button sendButton = (Button) findViewById(R.id.sendBtn);
		connectButton.setOnClickListener(connectListener);
		disconnectButton.setOnClickListener(disconnectListener);
		sendButton.setOnClickListener(sendMessageListener);
		
	}
	
	public void updateTextView(final String message){
		MainActivity.this.runOnUiThread(new Thread(){
			public void run(){
				textView.append(message+"\n");
				textInput.setText("");
			}
		});	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private OnClickListener connectListener = new OnClickListener() {
		public void onClick(View v) {
			chatListener = new ChatListener(MainActivity.this);
			new ConnectTask().execute("http://testdstorm.appspot.com", "key");	
			
		}
	};
	
	private class ConnectTask extends AsyncTask<String, Void, ChannelAPI> {
		protected ChannelAPI doInBackground(String... codes) {
			try {
				ChannelAPI tmp = new ChannelAPI(codes[0], codes[1], chatListener);
				tmp.open();
				return tmp;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ChannelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(ChannelAPI channelApi) {
			channel = channelApi;
		}
     }

	private class DiscTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... codes) {
			try {
				if (channel != null) {
					channel.close();
				}
			} catch (Exception e) {
				System.out.println("Problem Closing Channel");
			}

			return null;
		}
	}
	
	private OnClickListener disconnectListener = new OnClickListener() {
		public void onClick(View v) {
			new DiscTask().execute();
		}
	};

	private OnClickListener sendMessageListener = new OnClickListener() {
		public void onClick(View v) {
			new SendTask().execute(textInput.getText().toString());			
		}
	};
	
	private class SendTask extends AsyncTask<String, Void, Void> {
		protected Void doInBackground(String... codes) {
			try {
				channel.send(codes[0], "/chat");
			} catch (IOException e) {
				System.out.println("Problem Sending the Message");
			}	
			return null;
		}
	}

}
