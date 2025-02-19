package org.telegram.ui.Charts.view_data;

import android.graphics.Paint;

import androidx.core.graphics.ColorUtils;

import org.telegram.bautrukevich.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Charts.data.ChartData;

public class StackBarViewData extends LineViewData {

    public final Paint unselectedPaint = new Paint();
    public int blendColor = 0;

    public void updateColors() {
        super.updateColors();
        blendColor = ColorUtils.blendARGB(Theme.getColor(Theme.key_windowBackgroundWhite),lineColor,0.3f);
    }

    public StackBarViewData(ChartData.Line line) {
        super(line);
        paint.setStrokeWidth(AndroidUtilities.dpf2(1));
        paint.setStyle(Paint.Style.STROKE);
        unselectedPaint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(false);
    }
}
