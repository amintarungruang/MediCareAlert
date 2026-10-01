package com.example.medicarealert.ui.manage

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.security.SecureRandom

fun genInviteCode(): String {
    val alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    val r = SecureRandom()
    val sb = StringBuilder("MF-")
    repeat(8) { sb.append(alphabet[r.nextInt(alphabet.length)]) }
    return sb.toString()
}

fun makeQr(text: String, size: Int = 640): Bitmap {
    val matrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
    return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also { bmp ->
        for (x in 0 until size) for (y in 0 until size) {
            bmp.setPixel(x, y, if (matrix[x, y]) Color.BLACK else Color.WHITE)
        }
    }
}

// TODO: ดึงจาก session/auth จริงของ Bonus
fun currentUserId(): Long = 1L
