package org.telegram.ui.Components.Premium;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.telegram.bautrukevich.AndroidUtilities;
import org.telegram.bautrukevich.LocaleController;
import org.telegram.bautrukevich.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class AboutPremiumView extends LinearLayout {

    public AboutPremiumView(@NonNull Context context) {
        super(context);
        setOrientation(VERTICAL);
        setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(16), AndroidUtilities.dp(16), AndroidUtilities.dp(16));

        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setText(LocaleController.getString("AboutPremiumTitle", R.string.AboutPremiumTitle));
        addView(textView);

        TextView description = new TextView(context);
        description.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        description.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        description.setText(AndroidUtilities.replaceTags(LocaleController.getString("AboutPremiumDescription", R.string.AboutPremiumDescription)));
        addView(description, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 0, 0, 0));

        TextView description2 = new TextView(context);
        description2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        description2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        description2.setText(AndroidUtilities.replaceTags(LocaleController.getString("AboutPremiumDescription2", R.string.AboutPremiumDescription2)));
        addView(description2, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 24, 0, 0));
    }
}
