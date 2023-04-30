package com.huangrui.dormitory.initial

import android.util.Log
import com.baidu.idl.face.platform.FaceEnvironment
import com.baidu.idl.face.platform.FaceSDKManager
import com.baidu.idl.face.platform.LivenessTypeEnum
import com.baidu.idl.face.platform.listener.IInitCallback
import com.baidu.mshield.x0.EngineImpl

private const val v = 0.95f
private lateinit var livenessList :ArrayList<LivenessTypeEnum>


fun setFaceConfig(){

    livenessList = ArrayList<LivenessTypeEnum>()
    livenessList.add(LivenessTypeEnum.Eye)
    livenessList.add(LivenessTypeEnum.Mouth)
    livenessList.add(LivenessTypeEnum.HeadLeft)
    val config = FaceSDKManager.getInstance().faceConfig
    // 设置模糊度阈值
    config.blurnessValue = 0.8f;
    // 设置最小光照阈值（范围0-255）
    config.brightnessValue = 30f;
    // 设置最大光照阈值（范围0-255）
    config.brightnessMaxValue = 240f;
    // 设置左眼遮挡阈值
    config.occlusionLeftEyeValue =0.95f
    // 设置右眼遮挡阈值
    config.occlusionRightEyeValue = v;
    // 设置鼻子遮挡阈值
    config.occlusionNoseValue = v;
    // 设置嘴巴遮挡阈值
    config.occlusionMouthValue = v;
    // 设置左脸颊遮挡阈值
    config.occlusionLeftContourValue = v;
    // 设置右脸颊遮挡阈值
    config.occlusionRightContourValue = v;
    // 设置下巴遮挡阈值
    config.occlusionChinValue = v;
    // 设置人脸姿态角阈值
    config.headPitchValue = 30;
    config.headYawValue = 18;
    config.headRollValue = 30;
    // 设置可检测的最小人脸阈值
    config.minFaceSize = FaceEnvironment.VALUE_MIN_FACE_SIZE;
    // 设置可检测到人脸的阈值
    config.notFaceValue = FaceEnvironment.VALUE_NOT_FACE_THRESHOLD;
    // 设置闭眼阈值
    config.eyeClosedValue = FaceEnvironment.VALUE_CLOSE_EYES;
    // 设置图片缓存数量
    config.cacheImageNum = FaceEnvironment.VALUE_CACHE_IMAGE_NUM;
    // 设置活体动作，通过设置list，LivenessTypeEunm.Eye, LivenessTypeEunm.Mouth,
    // LivenessTypeEunm.HeadUp, LivenessTypeEunm.HeadDown, LivenessTypeEunm.HeadLeft,
    // LivenessTypeEunm.HeadRight
    config.livenessTypeList = livenessList;
    // 设置动作活体是否随机
    config.isLivenessRandom = true;
    // 设置开启提示音
    config.isSound = true;
    // 原图缩放系数
    config.scale = FaceEnvironment.VALUE_SCALE;
    // 抠图宽高的设定，为了保证好的抠图效果，建议高宽比是4：3
    config.cropHeight = FaceEnvironment.VALUE_CROP_HEIGHT;
    config.cropWidth = FaceEnvironment.VALUE_CROP_WIDTH;
    // 抠图人脸框与背景比例
    config.enlargeRatio = FaceEnvironment.VALUE_CROP_ENLARGERATIO;
    // 加密类型，0：Base64加密，上传时image_sec传false；1：百度加密文件加密，上传时image_sec传true
    config.secType = FaceEnvironment.VALUE_SEC_TYPE;
    // 检测超时设置
    config.timeDetectModule = FaceEnvironment.TIME_DETECT_MODULE;
    // 检测框远近比率
    config.faceFarRatio = FaceEnvironment.VALUE_FAR_RATIO;
    config.faceClosedRatio = FaceEnvironment.VALUE_CLOSED_RATIO;
    // 保存 设置
    FaceSDKManager.getInstance().faceConfig = config;
}