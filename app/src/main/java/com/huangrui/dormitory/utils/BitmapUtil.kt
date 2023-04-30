package com.huangrui.dormitory.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.baidu.idl.face.platform.utils.Base64Utils
import java.io.ByteArrayOutputStream


fun stringToBitmap(string: String?): Bitmap? {
    //将字符串转换成Bitmap类型
    var bitmap: Bitmap? = null
    try {
        val bitmapArray: ByteArray
        bitmapArray = Base64.decode(string, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}


fun bitmapToString(bitmap: Bitmap): String? {
    //将Bitmap转换成字符串
    var string: String? = null
    val bStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream)
    val bytes: ByteArray = bStream.toByteArray()
    string = Base64.encodeToString(bytes, Base64.DEFAULT)
    return string
}
fun base64ToBitmap(base64Data: String): Bitmap? {
    val bytes = Base64Utils.decode(base64Data, Base64Utils.NO_WRAP)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}