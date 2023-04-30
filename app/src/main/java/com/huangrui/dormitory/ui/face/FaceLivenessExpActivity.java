package com.huangrui.dormitory.ui.face;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.idl.face.platform.FaceStatusNewEnum;
import com.baidu.idl.face.platform.model.ImageInfo;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.baidu.idl.face.platform.ui.utils.IntentUtils;
import com.huangrui.dormitory.MainActivity;
import com.huangrui.dormitory.test.CompleteActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceLivenessExpActivity extends FaceLivenessActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    // 开始采集
    @Override
    public void onLivenessCompletion(FaceStatusNewEnum status, String message,
                                     HashMap<String, ImageInfo> base64ImageCropMap,
                                     HashMap<String, ImageInfo> base64ImageSrcMap, int currentLivenessCount) {
        super.onLivenessCompletion(status, message, base64ImageCropMap, base64ImageSrcMap, currentLivenessCount);

        if (status == FaceStatusNewEnum.OK && mIsCompletion) {
            // 获取最优图片
            /***
             * base64ImageCropMap 裁剪 图片 集合
             *  base64ImageSrcMap 原图集合
             * */
            getBestImage(base64ImageCropMap, base64ImageSrcMap);
            // 在规定时间内没有完成
        } else if (status == FaceStatusNewEnum.DetectRemindCodeTimeout) {
            // 提示 动画
            if (mViewBg != null) {
                mViewBg.setVisibility(View.VISIBLE);
            }
            Toast.makeText(this, "您在规定时间内尚未完成", Toast.LENGTH_SHORT).show();
        }

    }



    /**
     * 获取最优图片
     * @param imageCropMap 抠图集合
     * @param imageSrcMap  原图集合
     */
    private void getBestImage(HashMap<String, ImageInfo> imageCropMap,
                              HashMap<String, ImageInfo> imageSrcMap) {
        String bmpStr = null;
        // 将抠图集合中的图片按照质量降序排序，最终选取质量最优的一张抠图图片
        if (imageCropMap != null && imageCropMap.size() > 0) {
            List<Map.Entry<String, ImageInfo>> list1 = new ArrayList<>(imageCropMap.entrySet());
            // 返回来的 值 在list1 中
            Collections.sort(list1, new Comparator<Map.Entry<String, ImageInfo>>() {

                @Override
                public int compare(Map.Entry<String, ImageInfo> o1,
                                   Map.Entry<String, ImageInfo> o2) {
                    String[] key1 = o1.getKey().split("_");
                    String score1 = key1[2];
                    String[] key2 = o2.getKey().split("_");
                    String score2 = key2[2];
                    //  return 用于 结束循环    并返回 排完的集合
                    // 降序排序
                    return Float.valueOf(score2).compareTo(Float.valueOf(score1));
                }
            });

            // 获取抠图中的加密或非加密的base64
//            int secType = mFaceConfig.getSecType();
//            String base64;
//            if (secType == 0) {
//                base64 = list1.get(0).getValue().getBase64();
//            } else {
//                base64 = list1.get(0).getValue().getSecBase64();
//            }
        }

        // 将原图集合中的图片按照质量降序排序，最终选取质量最优的一张原图图片
        if (imageSrcMap != null && imageSrcMap.size() > 0) {
            List<Map.Entry<String, ImageInfo>> list2 = new ArrayList<>(imageSrcMap.entrySet());
            // 排序方法  返回的值在list2中
            Collections.sort(list2, new Comparator<Map.Entry<String, ImageInfo>>() {

                @Override
                public int compare(Map.Entry<String, ImageInfo> o1,
                                   Map.Entry<String, ImageInfo> o2) {
                    String[] key1 = o1.getKey().split("_");
                    String score1 = key1[2];
                    String[] key2 = o2.getKey().split("_");
                    String score2 = key2[2];
                    // 降序排序
                    return Float.valueOf(score2).compareTo(Float.valueOf(score1));
                }
            });
            // 获取 原图 集合中最优的一张图片
            // 给他转化为 Base64 码
            // 这个码 就是我们人脸的信息
            bmpStr = list2.get(0).getValue().getBase64();

            // 获取原图中的加密或非加密的base64
//            int secType = mFaceConfig.getSecType();
//            String base64;
//            if (secType == 0) {
//                base64 = bmpStr;
//            } else {
//                base64 = list2.get(0).getValue().getBase64();
//            }
//            Log.i("base64",  base64);
        }


        //  获取完 人脸信息后 页面跳转
        //  IntentUtils 导入的包 封装好的
//        IntentUtils.getInstance().setBitmap(bmpStr);
//        Intent intent = new Intent(FaceLivenessExpActivity.this,
//                CompleteActivity.class);
//        startActivity(intent);
        Intent intent = new Intent(FaceLivenessExpActivity.this, MainActivity.class);
        intent.putExtra("data", bmpStr);
        setResult(RESULT_OK, intent);
        finish();
    }



    @Override
    public void finish() {
        super.finish();
    }


}