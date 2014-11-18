package lkgsolutions.telugudailycomics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Help extends Activity { 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		TextView tv1 = (TextView) findViewById(R.id.textView1);
		tv1.setText("This is the android application which allows the user to select different popular telugu news papers." +
				"On choosing the desired newspaper, you can view the comics of the day printed in the paper of the day." +
				"Please allows some time to load the picture in the space provided.");
		Button b1= (Button) findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Intent myIntent = new Intent(Help.this,TeluguDailyComics.class);
				 startActivity(myIntent);
				
			}
		});
		
		
	}

}
