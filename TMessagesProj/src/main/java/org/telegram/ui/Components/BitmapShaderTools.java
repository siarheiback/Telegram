package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import org.telegram.bautrukevich.AndroidUtilities;

public class BitmapShaderTools {

    final public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Canvas canvas;
    final Bitmap bitmap;

    private final static int INTERNAL_WIDTH = 30;
    private final static int INTERNAL_HEIGHT = 40;

    final RectF bounds = new RectF();
    final Shader shader;
    final Matrix matrix = new Matrix();

    public BitmapShaderTools() {
        bitmap = Bitmap.createBitmap(INTERNAL_WIDTH, INTERNAL_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint.setShader(shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        updateBounds();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setBounds(RectF bounds) {
        if (this.bounds.top == bounds.top && this.bounds.bottom == bounds.bottom && this.bounds.left == bounds.left && this.bounds.right == bounds.right) {
            return;
        }
        this.bounds.set(bounds);
        updateBounds();
    }

    private void updateBounds() {
        if (shader == null) {
            return;
        }
        float sx = bounds.width() / (float) bitmap.getWidth();
        float sy = bounds.height() / (float) bitmap.getHeight();

        matrix.reset();
        matrix.postTranslate(bounds.left, bounds.top);
        matrix.preScale(sx, sy);

        shader.setLocalMatrix(matrix);
    }

    public void setBounds(float left, float top, float right, float bottom) {
        AndroidUtilities.rectTmp.set(left, top, right, bottom);
        setBounds(AndroidUtilities.rectTmp);
    }
}
