package com.example.albazip.src.home.manager.opened.ui

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityQrShowingBinding
import com.example.albazip.src.home.manager.network.GetQRFragmentView
import com.example.albazip.src.home.manager.network.GetQRService
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class QRShowingActivity:BaseActivity<ActivityQrShowingBinding>(ActivityQrShowingBinding::inflate),GetQRFragmentView {

    var imgBitmap:Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // qr 이미지 받아오기
        GetQRService(this).tryGetQRImg()
        showLoadingDialog(this)

        requestPermission()

        // 다운받기 버튼 클릭
        binding.btnDownload.setOnClickListener {
            saveImageFile(newFileName(),"image/jpeg",imgBitmap!!)
            showCustomToast("이미지를 다운로드 했습니다.")
        }

        // 닫기
        binding.ibtnClose.setOnClickListener {
            finish()
        }

    }

    fun newFileName():String{
        // 시간으로 파일명 만들어주기
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return filename
    }

    override fun onGetQRSuccess(response: ResponseBody) {
        dismissLoadingDialog()
        val rb = response.byteStream()
        val bitmap = BitmapFactory.decodeStream(rb)

        imgBitmap = bitmap

        Glide.with(this).load(bitmap).centerCrop().into(binding.ivQrImg)
    }

    override fun onGetQRFailure(message: String) {
        dismissLoadingDialog()
    }

    fun saveImageFile(filename:String,mimeType:String,bitmap: Bitmap):Uri?{
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,filename)
        values.put(MediaStore.Images.Media.MIME_TYPE,mimeType)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            values.put(MediaStore.Images.Media.IS_PENDING,1)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        try{
            if(uri != null){
                var descriptor = contentResolver.openFileDescriptor(uri,"w")
                if(descriptor != null){
                    val fos = FileOutputStream(descriptor.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
                    fos.close()
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                        values.clear()
                        values.put(MediaStore.Images.Media.IS_PENDING,0)
                        contentResolver.update(uri,values,null,null)
                    }
                }
            }
        }catch (e:java.lang.Exception){
            Log.e("File","error=${e.localizedMessage}")
        }
        return uri
    }

    val PERMISSIONS_REQUEST_CODE = 100
    var REQUIRED_PERMISSIONS = arrayOf<String>( Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun requestPermission(){
        var permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            //설명이 필요한지
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //설명 필요 (사용자가 요청을 거부한 적이 있음)
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
            }else{
                //설명 필요하지 않음
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
            }

            showCustomToast("권한 허용 후 이미지 다운이 가능합니다.")
            // 이미지 다운 막기 (권한 x)
            binding.btnDownload.isEnabled = false

        }else{
            //권한 허용 - 이미지 다운 가능
            binding.btnDownload.isEnabled = true
        }
    }

}