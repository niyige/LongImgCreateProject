package com.oyy.longimgcreate;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oyy.longimgcreate.adapter.DataAdapter;
import com.oyy.longimgcreate.utils.ImageUtils;
import com.oyy.longimgcreate.utils.PermissionUtil;
import com.oyy.longimgcreate.utils.SystemShareUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成长图，图片保存到相册
 * create by 893007592@qq.com
 * on 2018-08-02
 */
public class MainActivity extends AppCompatActivity {

    public MainActivity mContext;

    private RecyclerView recycleView;

    private ProgressBar progressBar;

    private TextView titleRightText;

    private DataAdapter adapter;

    private List<String> datas;

    LinearLayoutManager linearLayoutManager;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String path = (String) msg.obj;

            Toast.makeText(mContext, "长图生成以保存到相册", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            SystemShareUtils.getInstance().shareSysFile(mContext, new File(path));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 权限处理相关
         */
        PermissionUtil.requestMultiPermissions(mContext, new String[]{
                PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE,
                PermissionUtil.PERMISSION_WRITE_EXTERNAL_STORAGE}, mPermissionGrant);
    }

    /**
     * 初始化
     */
    private void initView() {
        recycleView = this.findViewById(R.id.recycleView);
        titleRightText = this.findViewById(R.id.titleRightText);
        progressBar = this.findViewById(R.id.progressBar);
        datas = new ArrayList<>();

        for (int i = 0; i < 18; i++) {

            datas.add("这是第" + i + "本书");
        }

        adapter = new DataAdapter(datas, this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(linearLayoutManager);
        recycleView.setAdapter(adapter);

        titleRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap bitmap = ImageUtils.getRecyclerItemsToBitmap(MainActivity.this, recycleView, datas);
                            String path = ImageUtils.saveImage(MainActivity.this, bitmap, "长图" + System.currentTimeMillis());
                            Thread.sleep(1000);
                            Message message = Message.obtain();
                            message.obj = path;
                            handler.sendMessage(message);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
        });

    }

    /**
     * 权限申请回掉
     */

    private PermissionUtil.PermissionGrant mPermissionGrant = new PermissionUtil.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            if (requestCode == PermissionUtil.CODE_MULTI_PERMISSION) {
                //申请权限成功，做一些处理
            } else {
                Toast.makeText(mContext, "取消", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

}
