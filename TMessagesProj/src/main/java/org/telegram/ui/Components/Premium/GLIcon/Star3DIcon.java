package org.telegram.ui.Components.Premium.GLIcon;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import org.telegram.bautrukevich.R;
import org.telegram.bautrukevich.SvgHelper;
import org.telegram.bautrukevich.Utilities;
import org.telegram.ui.ActionBar.Theme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;

public class Star3DIcon {
    private int mProgramObject;
    private int mMVPMatrixHandle;
    private int mWorldMatrixHandle;
    private FloatBuffer mVertices;
    private FloatBuffer mTextures;
    private FloatBuffer mNormals;

    private int mTextureUniformHandle;
    private int mNormalMapUniformHandle;
    private int mBackgroundTextureUniformHandle;
    private int mBackgroundTextureHandle;
    private int mTextureCoordinateHandle;
    private int mNormalCoordinateHandle;
    private int xOffsetHandle;
    private int alphaHandle;
    private int mTextureDataHandle;
    float xOffset;

    int trianglesCount;
    float enterAlpha = 0f;

    public float spec1 = 2f;
    public float spec2 = 0.13f;
    public float diffuse = 1f;
    public int gradientColor1;
    public int gradientColor2;
    public float normalSpec = 0.2f;
    public int normalSpecColor = Color.WHITE;
    public int specColor = Color.WHITE;

    int specHandleTop;
    int specHandleBottom;
    int diffuseHandle;
    int gradientColor1Handle;
    int gradientColor2Handle;
    int normalSpecHandle;
    int normalSpecColorHandle;
    int specColorHandle;
    int resolutionHandle;
    int gradientPositionHandle;

    Bitmap texture;
    Bitmap backgroundBitmap;

    public Star3DIcon(Context context) {
        ObjLoader starObj = new ObjLoader(context, "models/star.binobj");

        mVertices = ByteBuffer.allocateDirect(starObj.positions.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(starObj.positions).position(0);

        mTextures = ByteBuffer.allocateDirect(starObj.textureCoordinates.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextures.put(starObj.textureCoordinates).position(0);

        mNormals = ByteBuffer.allocateDirect(starObj.normals.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mNormals.put(starObj.normals).position(0);

        trianglesCount = starObj.positions.length;

        generateTexture();

        int vertexShader;
        int fragmentShader;
        int programObject;
        int[] linked = new int[1];

        vertexShader = GLIconRenderer.loadShader(GLES20.GL_VERTEX_SHADER, loadFromAsset(context, "shaders/vertex2.glsl"));
        fragmentShader = GLIconRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, loadFromAsset(context, "shaders/fragment2.glsl"));

        programObject = GLES20.glCreateProgram();
        GLES20.glAttachShader(programObject, vertexShader);
        GLES20.glAttachShader(programObject, fragmentShader);
        GLES20.glBindAttribLocation(programObject, 0, "vPosition");
        GLES20.glLinkProgram(programObject);
        GLES20.glGetProgramiv(programObject, GLES20.GL_LINK_STATUS, linked, 0);

        mProgramObject = programObject;
        init(context);
    }

    private void init(Context context) {
        GLES20.glUseProgram(mProgramObject);

        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramObject, "a_TexCoordinate");
        mNormalCoordinateHandle = GLES20.glGetAttribLocation(mProgramObject, "a_Normal");

        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramObject, "u_Texture");
        mNormalMapUniformHandle = GLES20.glGetUniformLocation(mProgramObject, "u_NormalMap");
        mBackgroundTextureUniformHandle = GLES20.glGetUniformLocation(mProgramObject, "u_BackgroundTexture");
        xOffsetHandle = GLES20.glGetUniformLocation(mProgramObject, "f_xOffset");
        alphaHandle = GLES20.glGetUniformLocation(mProgramObject, "f_alpha");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramObject, "uMVPMatrix");
        mWorldMatrixHandle = GLES20.glGetUniformLocation(mProgramObject, "world");

        specHandleTop = GLES20.glGetUniformLocation(mProgramObject, "spec1");
        specHandleBottom = GLES20.glGetUniformLocation(mProgramObject, "spec2");
        diffuseHandle = GLES20.glGetUniformLocation(mProgramObject, "u_diffuse");
        gradientColor1Handle = GLES20.glGetUniformLocation(mProgramObject, "gradientColor1");
        gradientColor2Handle = GLES20.glGetUniformLocation(mProgramObject, "gradientColor2");
        normalSpecColorHandle = GLES20.glGetUniformLocation(mProgramObject, "normalSpecColor");
        normalSpecHandle = GLES20.glGetUniformLocation(mProgramObject, "normalSpec");
        specColorHandle = GLES20.glGetUniformLocation(mProgramObject, "specColor");
        resolutionHandle = GLES20.glGetUniformLocation(mProgramObject, "resolution");
        gradientPositionHandle = GLES20.glGetUniformLocation(mProgramObject, "gradientPosition");

        mTextures.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0, mTextures);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

        mNormals.position(0);
        GLES20.glVertexAttribPointer(mNormalCoordinateHandle, 3, GLES20.GL_FLOAT, false, 0, mNormals);
        GLES20.glEnableVertexAttribArray(mNormalCoordinateHandle);

        mVertices.position(0);
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, mVertices);
        GLES20.glEnableVertexAttribArray(0);


        Bitmap bitmap = SvgHelper.getBitmap(R.raw.start_texture, 80, 80, Color.WHITE);
        Utilities.stackBlurBitmap(bitmap, 3);

        final int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        final int[] textureDatHandle = new int[1];
        GLES20.glGenTextures(1, textureDatHandle, 0);
        mTextureDataHandle = textureDatHandle[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDatHandle[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);

        Bitmap bitmap1 = getBitmapFromAsset(context, "flecks.png");

        final int[] normalMap = new int[1];
        GLES20.glGenTextures(1, normalMap, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, normalMap[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap1, 0);
        bitmap1.recycle();

        final int[] backgroundBitmapHandel = new int[1];
        GLES20.glGenTextures(1, backgroundBitmapHandel, 0);
        mBackgroundTextureHandle = backgroundBitmapHandel[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, backgroundBitmapHandel[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBackgroundTextureHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        GLES20.glUniform1i(mTextureUniformHandle, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, normalMap[0]);
        GLES20.glUniform1i(mNormalMapUniformHandle, 1);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, backgroundBitmapHandel[0]);
        GLES20.glUniform1i(mBackgroundTextureUniformHandle, 2);

    }

    private void generateTexture() {
        texture = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(texture);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0, 100, 150, 0, new int[]{Theme.getColor(Theme.key_premiumGradient1), Theme.getColor(Theme.key_premiumGradient2), Theme.getColor(Theme.key_premiumGradient3), Theme.getColor(Theme.key_premiumGradient4)}, new float[]{0, 0.5f, 0.78f, 1f}, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, 100, 100, paint);

        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, texture, 0);
        mTextureDataHandle = textureHandle[0];
    }

    public void draw(float[] mvpMatrix, float[] worldMatrix, int width, int height, float gradientStartX, float gradientScaleX, float gradientStartY, float gradientScaleY) {
        if (backgroundBitmap != null) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBackgroundTextureHandle);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, backgroundBitmap, 0);
            backgroundBitmap = null;
        }
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        GLES20.glUniform1f(xOffsetHandle, xOffset);
        GLES20.glUniform1f(alphaHandle, enterAlpha);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(mWorldMatrixHandle, 1, false, worldMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, trianglesCount / 3);

        GLES20.glUniform1f(specHandleTop, spec1);
        GLES20.glUniform1f(specHandleBottom, spec2);
        GLES20.glUniform1f(diffuseHandle, diffuse);
        GLES20.glUniform1f(normalSpecHandle, normalSpec);

        GLES20.glUniform3f(gradientColor1Handle, Color.red(gradientColor1) / 255f, Color.green(gradientColor1) / 255f, Color.blue(gradientColor1) / 255f);
        GLES20.glUniform3f(gradientColor2Handle, Color.red(gradientColor2) / 255f, Color.green(gradientColor2) / 255f, Color.blue(gradientColor2) / 255f);
        GLES20.glUniform3f(normalSpecColorHandle, Color.red(normalSpecColor) / 255f, Color.green(normalSpecColor) / 255f, Color.blue(normalSpecColor) / 255f);
        GLES20.glUniform3f(specColorHandle, Color.red(specColor) / 255f, Color.green(specColor) / 255f, Color.blue(specColor) / 255f);
        GLES20.glUniform2f(resolutionHandle, width, height);
        GLES20.glUniform4f(gradientPositionHandle, gradientStartX, gradientScaleX, gradientStartY, gradientScaleY);

        if (enterAlpha < 1f) {
            enterAlpha += 16 / 220f;
            if (enterAlpha > 1) {
                enterAlpha = 1f;
            }
        }
        xOffset += 0.0005f;
        if (xOffset > 1) {
            xOffset -= 1f;
        }
    }

    public String loadFromAsset(Context context, String name) {
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        try {
            is = context.getAssets().open(name);

            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String str;
            while ((str = br.readLine()) != null) {
                if (str.startsWith("//")) {
                    continue;
                }
                sb.append(str);
            }
            br.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    public void setBackground(Bitmap gradientTextureBitmap) {
        backgroundBitmap = gradientTextureBitmap;
    }

    public void destroy() {
        GLES20.glDeleteProgram(mProgramObject);
    }

}