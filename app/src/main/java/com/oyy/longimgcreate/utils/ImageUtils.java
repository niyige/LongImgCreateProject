package com.oyy.longimgcreate.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.oyy.longimgcreate.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * create by 893007592@qq.com
 * on 2018/8/2 16:40
 * Description: 图片处理工具类
 */
public class ImageUtils {

    public static String saveImage(Activity mActivity, Bitmap bitmap, String imgName) {

        String dir = Environment.getExternalStorageDirectory().getPath() + "/长图/";
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }

        String imagePath = dir + imgName + ".jpg";
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                Log.d("test", e.getMessage());
            }
        }

        saveBitmap(bitmap, imagePath);
        insertImage(mActivity, imagePath);

        return imagePath;
    }

    /**
     * 把bitmap保存到文件中
     *
     * @param bitmap
     * @param filePath
     */
    public static void saveBitmap(Bitmap bitmap, String filePath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            Log.d("test", e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.d("test", e.getMessage());
            }
        }
    }

    private static MediaScannerConnection sMediaScannerConnection;

    public static void insertImage(Context context, final String filePath) {
        sMediaScannerConnection = new MediaScannerConnection(context, new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                Log.d("test", "scannerConnected, scan local path:" + filePath);
                sMediaScannerConnection.scanFile(filePath, "image/*");
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                Log.d("test", "scan complete");
                sMediaScannerConnection.disconnect();
            }
        });
        sMediaScannerConnection.connect();
    }

    /**
     * 生成长图 (列表之类的)bitmap
     * <p>
     * 绘制一张长图
     *
     * @param activity
     * @param view
     * @param mList
     * @return
     */
    public static Bitmap getRecyclerItemsToBitmap(Activity activity, View view, List<String> mList) {

        int allItemsHeight = 0;
        int itemWidth = view.getWidth();
        List<Bitmap> bitmaps = new ArrayList<>();

        //如果有头部就写上
        View headerView = activity.getLayoutInflater().inflate(R.layout.header_layout, null);
        headerView.measure(View.MeasureSpec.makeMeasureSpec(itemWidth, View.MeasureSpec.EXACTLY), 0);
        headerView.layout(0, 0, itemWidth, headerView.getMeasuredHeight());
        headerView.setDrawingCacheEnabled(true);
        headerView.buildDrawingCache();
        bitmaps.add(headerView.getDrawingCache());
        allItemsHeight += headerView.getMeasuredHeight();


        for (int i = 0; i < mList.size(); i++) {
            View childView = LayoutInflater.from(activity).inflate(R.layout.content_layout, null);
            childView.measure(View.MeasureSpec.makeMeasureSpec(itemWidth, View.MeasureSpec.EXACTLY), 0);
            childView.layout(0, 0, itemWidth, childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bitmaps.add(childView.getDrawingCache());
            allItemsHeight += childView.getMeasuredHeight();

        }

        //如果有尾部就写上
        View footerView = activity.getLayoutInflater().inflate(R.layout.footer_layout, null);
        footerView.measure(View.MeasureSpec.makeMeasureSpec(itemWidth, View.MeasureSpec.EXACTLY), 0);
        footerView.layout(0, 0, itemWidth, footerView.getMeasuredHeight());
        footerView.setDrawingCacheEnabled(true);
        footerView.buildDrawingCache();
        bitmaps.add(footerView.getDrawingCache());
        allItemsHeight += footerView.getMeasuredHeight();


        Bitmap bigBitmap = Bitmap.createBitmap(itemWidth, allItemsHeight, Bitmap.Config.ARGB_4444);
        Canvas bigCanvas = new Canvas(bigBitmap);

        bigCanvas.drawColor(ContextCompat.getColor(activity, R.color.bg_f2f2f2));
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int iHeight = 0;
        for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bmp = bitmaps.get(i);

            bigCanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight += bmp.getHeight();
            bmp.recycle();
        }

        return bigBitmap;

    }

}
