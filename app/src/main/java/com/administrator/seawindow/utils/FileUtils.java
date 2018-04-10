package com.administrator.seawindow.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/8.
 */
public class FileUtils {

//    public static String DCIMCamera_PATH = Environment .getExternalStorageDirectory();
    private static FileUtils mInstance;
    public static FileUtils getInst() {
        if (mInstance == null) {
            synchronized (FileUtils.class) {
                if (mInstance == null) {
                    mInstance = new FileUtils();
                }
            }
        }
        return mInstance;
    }



    public static String getNewFileName()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());

        return formatter.format(curDate);
    }

    /**
     *
     * @param bitmap
     * @param destPath
     * @param quality
     */
    public static void writeImage(Bitmap bitmap, String destPath, int quality)
    {
        try
        {
            deleteFile(destPath);
            if (createFile(destPath))
            {
                FileOutputStream out = new FileOutputStream(destPath);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out))
                {
                    out.flush();
                    out.close();
                    out = null;
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 删除一个文件
     *
     * @param filePath
     *            要删除的文件路径名
     * @return true if this file was deleted, false otherwise
     */
    public static boolean deleteFile(String filePath)
    {
        try
        {
            File file = new File(filePath);
            if (file.exists())
            {
                return file.delete();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createFile(String filePath)
    {
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                if (!file.getParentFile().exists())
                {
                    file.getParentFile().mkdirs();
                }

                return file.createNewFile();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
