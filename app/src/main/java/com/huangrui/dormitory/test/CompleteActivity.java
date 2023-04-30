package com.huangrui.dormitory.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.ui.utils.IntentUtils;
import com.baidu.idl.face.platform.utils.Base64Utils;
import com.baidu.idl.face.platform.utils.DensityUtils;
import com.huangrui.dormitory.R;


public class CompleteActivity extends Activity {
    private ImageView image;
    private String bmpStr;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        image = findViewById(R.id.image);
        Intent intent = getIntent();
        if (intent != null) {
            // 把 传过来的 Bitmp 流转化为 bitmmap 图片
            bmpStr = IntentUtils.getInstance().getBitmap();
            if (TextUtils.isEmpty(bmpStr)) {
                return;
            }
            Bitmap bmp = base64ToBitmap(bmpStr);
            // 进行裁剪
            bmp = FaceSDKManager.getInstance().scaleImage(bmp,
                    DensityUtils.dip2px(getApplicationContext(), 97),
                    DensityUtils.dip2px(getApplicationContext(), 97));
            image.setImageBitmap(bmp);
            Log.i("base64", "initData: " + bmpStr);
        }

    }

    private Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64Utils.decode(base64Data, Base64Utils.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
// 释放资源
        IntentUtils.getInstance().release();
    }
}