package com.administrator.seawindow.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Random;

/**
 * Created by Administrator on 2017/6/23.
 */

public class FilterUtils {
    /**
     * 模糊处理
     *
     * @param bitmap 原图
     * @param Blur   模糊的半径
     * @return 模糊图
     */
    public static Bitmap Mohu(Bitmap bitmap, int Blur) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int pixels[] = new int[width * height];
        int pixelsRawSource[] = new int[width * height * 3];
        int pixelsRawNew[] = new int[width * height * 3];

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int k = 1; k <= Blur; k++) {
            for (int i = 0; i < pixels.length; i++) {
                pixelsRawSource[i * 3 + 0] = Color.red(pixels[i]);
                pixelsRawSource[i * 3 + 1] = Color.green(pixels[i]);
                pixelsRawSource[i * 3 + 2] = Color.blue(pixels[i]);
            }

            int CurrentPixel = width * 3 + 3;

            for (int i = 0; i < height - 3; i++) {
                for (int j = 0; j < width * 3; j++) {
                    CurrentPixel += 1;
                    int sumColor = 0;
                    sumColor = pixelsRawSource[CurrentPixel - width * 3];
                    sumColor = sumColor + pixelsRawSource[CurrentPixel - 3];
                    sumColor = sumColor + pixelsRawSource[CurrentPixel + 3];
                    sumColor = sumColor + pixelsRawSource[CurrentPixel + width * 3];
                    pixelsRawNew[CurrentPixel] = Math.round(sumColor / 4);
                }
            }

            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = Color.rgb(pixelsRawNew[i * 3 + 0],
                        pixelsRawNew[i * 3 + 1], pixelsRawNew[i * 3 + 2]);
            }
        }
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    //毛玻璃
    public static Bitmap Blur(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int color = 0;

        Random rnd = new Random();
        int iModel = 10;
        int i = width - iModel;
        while (i > 1) {
            int j = height - iModel;
            while (j > 1) {
                int iPos = rnd.nextInt(100000) % iModel;
                color = bitmap.getPixel(i + iPos, j + iPos);
                result.setPixel(i, j, color);
                j = j - 1;
            }
            i = i - 1;
        }
        return result;
    }

    //浮雕
    public static Bitmap Carving(Bitmap bm) {
        int Width = bm.getWidth();
        int Height = bm.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(Width, Height, Bitmap.Config.ARGB_8888);

        int color = 0, colorBefore = 0;
        int a, r, g, b;
        int r1, g1, b1;

        int[] oldPx = new int[Width * Height];
        int[] newPx = new int[Width * Height];

        bm.getPixels(oldPx, 0, Width, 0, 0, Width, Height);
        for (int i = 1; i < Width * Height; i++) {
            colorBefore = oldPx[i - 1];
            a = Color.alpha(colorBefore);
            r = Color.red(colorBefore);
            g = Color.green(colorBefore);
            b = Color.blue(colorBefore);

            color = oldPx[i];

            r1 = Color.red(color);
            g1 = Color.green(color);
            b1 = Color.blue(color);

            r = (r - r1 + 127);
            g = (g - g1 + 127);
            b = (b - b1 + 127);

            //检查各点像素值是否超出范围
            if (r > 255) {
                r = 255;
            }

            if (g > 255) {
                g = 255;
            }

            if (b > 255) {
                b = 255;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }

        bitmap.setPixels(newPx, 0, Width, 0, 0, Width, Height);
        return bitmap;
    }

    //霓虹
    public static Bitmap Neon(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        int color = 0;
        int r, g, b, r1, g1, b1, r2, g2, b2;

        int[] oldPx = new int[w * h];
        int[] newPx = new int[w * h];

        bitmap.getPixels(oldPx, 0, w, 0, 0, w, h);
        for (int y = 0; y < h - 1; y++) {
            for (int x = 0; x < w - 1; x++) {
                color = oldPx[x + y * w];

                r = (color >> 16) & 0xFF;
                g = (color >> 8) & 0xFF;
                b = (color >> 0) & 0xFF;

                int newcolor = oldPx[x + 1 + y * w];

                r1 = (newcolor >> 16) & 0xFF;
                g1 = (newcolor >> 8) & 0xFF;
                b1 = (newcolor >> 0) & 0xFF;

                int newcolor2 = oldPx[x + (y + 1) * w];

                r2 = (newcolor2 >> 16) & 0xFF;
                g2 = (newcolor2 >> 8) & 0xFF;
                b2 = (newcolor2 >> 0) & 0xFF;

                int tr = (int) (2 * Math.sqrt(((r - r1) * (r - r1) + (r - r2) * (r - r2))));
                int tg = (int) (2 * Math.sqrt(((g - g1) * (g - g1) + (g - g2) * (g - g2))));
                int tb = (int) (2 * Math.sqrt(((b - b1) * (b - b1) + (b - b2) * (b - b2))));

                newPx[x + y * w] = (255 << 24) | (tr << 16) | (tg << 8) | (tb);
            }
        }
        resultBitmap.setPixels(newPx, 0, w, 0, 0, w, h);
        return resultBitmap;
    }

    //紫色
    public static Bitmap Purple(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        int color = 0;
        int r, g, b, r1, b1;

        int[] oldPx = new int[w * h];
        int[] newPx = new int[w * h];

        bitmap.getPixels(oldPx, 0, w, 0, 0, w, h);
        for (int i = 0; i < w * h; i++) {
            color = oldPx[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);

            //红色通道的值和绿色通道的值增加50（红色+绿色 =黄色）
            r1 = r + 50;
            b1 = b + 50;

            if (r1 > 255) {
                r1 = 255;
            }

            if (b1 > 255) {
                b1 = 255;
            }

            newPx[i] = Color.argb(1, r1, g, b1);
        }
        resultBitmap.setPixels(newPx, 0, w, 0, 0, w, h);

        return resultBitmap;
    }

    //lomo
    public static Bitmap Lomo1(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        int color = 0;
        int a, r, g, b, r1, g1, b1;

        int[] oldPx = new int[w * h];
        int[] newPx = new int[w * h];

        bitmap.getPixels(oldPx, 0, w, 0, 0, w, h);
        for (int i = 0; i < w * h; i++) {
            color = oldPx[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            r1 = (int) (1.7 * r + 0.1 * g + 0.1 * b - 73.1);
            g1 = (int) (0 * r + 1.7 * g + 0.1 * b - 73.1);
            b1 = (int) (0 * r + 0.1 * g + 1.6 * b - 73.1);

            //检查各通道值是否超出范围
            if (r1 > 255) {
                r1 = 255;
            }

            if (g1 > 255) {
                g1 = 255;
            }

            if (b1 > 255) {
                b1 = 255;
            }

            newPx[i] = Color.argb(a, r1, g1, b1);
        }
        resultBitmap.setPixels(newPx, 0, w, 0, 0, w, h);

        return resultBitmap;
    }

    //泛黄
    public static Bitmap Yellow(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        int color = 0;
        int r, g, b, r1, g1;

        int[] oldPx = new int[w * h];
        int[] newPx = new int[w * h];

        bitmap.getPixels(oldPx, 0, w, 0, 0, w, h);
        for (int i = 0; i < w * h; i++) {
            color = oldPx[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);

            //红色通道的值和绿色通道的值增加50（红色+绿色 =黄色）
            r1 = r + 50;
            g1 = g + 50;

            if (r1 > 255) {
                r1 = 255;
            }

            if (g1 > 255) {
                g1 = 255;
            }

            newPx[i] = Color.argb(1, r1, g1, b);
        }
        resultBitmap.setPixels(newPx, 0, w, 0, 0, w, h);

        return resultBitmap;
    }

    //宝石蓝
    public static Bitmap Blue(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        int color = 0;
        int r, g, b, b1;

        int[] oldPx = new int[w * h];
        int[] newPx = new int[w * h];

        bitmap.getPixels(oldPx, 0, w, 0, 0, w, h);
        for (int i = 0; i < w * h; i++) {
            color = oldPx[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);

            //蓝色通道的值变为原来的1.6倍
            b1 = (int) (0 * r + 0 * g + 1.6 * b);

            //检查蓝色通道值是否超出范围
            if (b1 > 255) {
                b1 = 255;
            }
            newPx[i] = Color.argb(1, r, g, b1);
        }
        resultBitmap.setPixels(newPx, 0, w, 0, 0, w, h);
        return resultBitmap;
    }

    //宝丽来色
    public static Bitmap Polaroid(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        int color = 0;
        int r, g, b, a1, r1, g1, b1;

        int[] oldPx = new int[w * h];
        int[] newPx = new int[w * h];

        bitmap.getPixels(oldPx, 0, w, 0, 0, w, h);
        for (int i = 0; i < w * h; i++) {
            color = oldPx[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);

            //宝丽来色数组
            r1 = (int) (1.438 * r + (-0.062) * g + (-0.062) * b);
            g1 = (int) ((-0.122) * r + 1.378 * g + (-0.122) * b);
            b1 = (int) ((-0.016) * r + (-0.016) * g + 1.483 * b);
            a1 = (int) ((-0.03) * r + 0.05 * g + (-0.02) * b);

            //检查各点像素值是否超出范围
            if (r1 > 255) {
                r1 = 255;
            }

            if (g1 > 255) {
                g1 = 255;
            }

            if (b1 > 255) {
                b1 = 255;
            }

            if (a1 > 255) {
                a1 = 255;
            }

            newPx[i] = Color.argb(a1, r1, g1, b1);
        }
        resultBitmap.setPixels(newPx, 0, w, 0, 0, w, h);
        return resultBitmap;
    }

    //泛红
    public static Bitmap Red(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        int color = 0;
        int a, r, g, b, r1;

        int[] oldPx = new int[w * h];
        int[] newPx = new int[w * h];

        bitmap.getPixels(oldPx, 0, w, 0, 0, w, h);
        for (int i = 0; i < w * h; i++) {
            color = oldPx[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            //红色通道的值变为原来的两倍
            r1 = r * 2;

            //检查红色通道的像素值是否超出范围
            if (r1 > 255) {
                r1 = 255;
            }

            newPx[i] = Color.argb(a, r1, g, b);
        }
        resultBitmap.setPixels(newPx, 0, w, 0, 0, w, h);
        return resultBitmap;
    }

    //荧光绿
    public static Bitmap Green(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        int color = 0;
        int r, g, b, g1;

        int[] oldPx = new int[w * h];
        int[] newPx = new int[w * h];

        bitmap.getPixels(oldPx, 0, w, 0, 0, w, h);
        for (int i = 0; i < w * h; i++) {
            color = oldPx[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);

            //绿色通道得值变为原来的1.4倍
            g1 = (int) (0 * r + 1.4 * g + 0 * b);

            //检查绿色通道值是否超出范围
            if (g1 > 255) {
                g1 = 255;
            }
            newPx[i] = Color.argb(1, r, g1, b);
        }
        resultBitmap.setPixels(newPx, 0, w, 0, 0, w, h);
        return resultBitmap;
    }
}
