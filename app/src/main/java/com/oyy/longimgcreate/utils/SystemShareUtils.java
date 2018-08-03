package com.oyy.longimgcreate.utils;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;

/**
 * create by ouyangyi@manqian.cn
 * on 2018/8/3 10:59
 * Description: 调用系统分享
 */
public class SystemShareUtils {

    public static SystemShareUtils shareUtils;

    public static SystemShareUtils getInstance() {
        if (shareUtils == null) {
            shareUtils = new SystemShareUtils();
        }
        return shareUtils;
    }

    /**
     * 系统自带的分享 (纯文本)
     *
     * @param activityTitle
     * @param msgTitle
     * @param msgText
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText, Activity mActivity) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivity(Intent.createChooser(intent, activityTitle));
        } else {
            Toast.makeText(mActivity, "没有可分享的应用", Toast.LENGTH_SHORT).show();
        }

    }

    // 調用系統方法分享文件
    public void shareSysFile(Activity mActivity, File file) {
        if (null != file && file.exists()) {
            Intent share = new Intent(Intent.ACTION_SEND);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                Uri uriForFile = FileProvider.getUriForFile(mActivity, mActivity.getApplicationContext().getPackageName() + ".provider", file);

                share.setType(mActivity.getContentResolver().getType(uriForFile));
                share.putExtra(Intent.EXTRA_STREAM, uriForFile);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                share.setType(getMimeType(file.getAbsolutePath()));//此处可发送多种文件
            }

            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(Intent.createChooser(share, "分享文件"));
        } else {
            Toast.makeText(mActivity, "分享文件不存在", Toast.LENGTH_SHORT).show();
        }
    }

    // 根据文件后缀名获得对应的MIME类型。
    private static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }
}
