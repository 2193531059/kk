package com.administrator.seawindow.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;


/**
 * Created by Administrator on 2017/5/25.
 */
public class PhotoUtils {

    private static int screenWidth;// 手机屏幕的宽（像素）
    private static int screenHeight;// 手机屏幕的高（像素）
    private static final String TAG = "PhotoUtils";
    private static PhotoUtils mPhotoUtils;
    private FragmentActivity mActivity;


    public static PhotoUtils getInstance(FragmentActivity activity){
        if (mPhotoUtils == null){
            synchronized (PhotoUtils.class) {
                if (mPhotoUtils == null) {
                    mPhotoUtils = new PhotoUtils(activity);
                    WindowManager wm = (WindowManager) activity
                            .getSystemService(Context.WINDOW_SERVICE);
                    screenWidth =wm.getDefaultDisplay().getWidth();
                }
            }
        }
        return mPhotoUtils;
    }

    private PhotoUtils(FragmentActivity activity) {
        mActivity = activity;
    }


    /**
     * 根据路径获取图片并且压缩，适应view
     *
     * @param filePath
     *            图片路径
     * @param contentView
     *            适应的view
     * @return Bitmap 压缩后的图片
     */
    public Bitmap compressionFiller(String filePath, View contentView)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, opt);
        if(bitmap == null){
            return null;
        }
        int layoutHeight = contentView.getHeight();
        float scale = 0f;
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();
        scale = bitmapHeight > bitmapWidth
                ? layoutHeight / (bitmapHeight * 1f)
                : screenWidth / (bitmapWidth * 1f);
        Bitmap resizeBmp;
        if (scale != 0)
        {
            int bitmapheight = bitmap.getHeight();
            int bitmapwidth = bitmap.getWidth();
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale); // 长和宽放大缩小的比例
            resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapwidth,
                    bitmapheight, matrix, true);
        } else
        {
//            resizeBmp = Bitmap.createScaledBitmap(bitmap, screenWidth, screenWidth, true);
            resizeBmp = bitmap;
        }
        return resizeBmp;
    }


}
