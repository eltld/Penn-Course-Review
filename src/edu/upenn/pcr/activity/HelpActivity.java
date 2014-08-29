package edu.upenn.pcr.activity;

import edu.upenn.pcr.R;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class HelpActivity extends ActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.help_layout);
		TextView home = (TextView) this.findViewById(R.id.help_home_page);
		home.setMovementMethod(LinkMovementMethod.getInstance());
		String homeLink = "<a href=\"http://www.penncoursereview.com\">here</a>";
		String homeText = "Click " + homeLink + " to visit our home page";
		home.setText(Html.fromHtml(homeText));
	
		TextView faq = (TextView) this.findViewById(R.id.help_faq);
		faq.setMovementMethod(LinkMovementMethod.getInstance());
		String faqLink = "<a href=\"http://www.penncoursereview.com/faq\">here</a>";
		String faqText = "Visit " + faqLink + " for FAQs";
		faq.setText(Html.fromHtml(faqText));
	}
	
}
