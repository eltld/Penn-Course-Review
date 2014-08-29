package edu.upenn.pcr.activity;

import edu.upenn.pcr.R;

import edu.upenn.pcr.controller.LoginController;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends ActivityBase {

	private LoginController controller;
	private EditText text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = new LoginController(this);
		controller.initCheck();
		this.setContentView(R.layout.login_layout);
		text = (EditText) this.findViewById(R.id.serial_number_text);
		text.setText(controller.getStoredSerialNumber());
		Button buttonLogin = (Button) this.findViewById(R.id.login_login);
		buttonLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				controller.onLogin(text.getText().toString().trim());
			}
		});
		
		TextView textRegister = (TextView) this.findViewById(R.id.login_register_block);
		textRegister.setMovementMethod(LinkMovementMethod.getInstance());
		String registerLink = "<a href=\"http://www.penncoursereview.com/androidapp\">Click here to register</a>";
		textRegister.setText(Html.fromHtml(registerLink));
		
	}
}
