package com.gx.smart.smartoa.activity.ui.repair

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.google.protobuf.ByteString
import com.gx.smart.smartoa.BuildConfig
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AdminImageProviderService
import com.gx.smart.smartoa.data.network.api.AppRepairService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.data.network.api.lib.model.UploadImage
import com.gx.wisestone.upload.grpc.images.AdminImagesResponse
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.repair_fragment.*
import java.io.ByteArrayOutputStream
import java.io.File

class RepairFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = RepairFragment()
        const val ARG_TYPE = "type"
        const val REQUEST_TYPE = 200
    }

    private lateinit var viewModel: RepairViewModel
    private var type: RepairType = RepairType(1, "设备损坏")
    private var images: MutableList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.repair_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RepairViewModel::class.java)
        initTitle()
        initContent();
    }

    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.repair)
        }

        right_nav_text_view.let {
            it.setOnClickListener(this)
            it.visibility = View.VISIBLE
            it.text = getString(R.string.repair_record)
        }

    }

    private fun initContent() {
        save.setOnClickListener(this)
        placeName.text = SPUtils.getInstance().getString(AppConfig.PLACE_NAME, "")
        val phoneValue = SPUtils.getInstance().getString(AppConfig.SH_USER_ACCOUNT, "")
        phone.setText(phoneValue)
        phone.setSelection(phoneValue.length)
        repair_type.setOnClickListener(this)
        addImage.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.right_nav_text_view -> Navigation.findNavController(v).navigate(R.id.action_repairFragment_to_repairRecordFragment)
            R.id.repair_type -> {
                val intent = Intent(activity, RepairTypeActivity::class.java)
                startActivityForResult(intent, REQUEST_TYPE)
            }
            R.id.save -> {
                val content = contentEdit.text.toString()
                val employeePhone = phone.text.toString()
                val address = placeName.text.toString()
                addRepair(content, type.type, address, employeePhone, images)
            }

            R.id.addImage -> uploadHeadImage()
        }


    }


    private fun addRepair(
        content: String,
        type: Int,
        address: String,
        employee_phone: String,
        images: List<String>
    ) {
        AppRepairService.getInstance()
            .addRepair(
                content,
                type,
                address,
                employee_phone,
                images,
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        if (result == null) {
                            ToastUtils.showLong("添加超时!")
                            return
                        }
                        if (result?.code == 100) {
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
    }


    private fun uploadByByte(
        fileName: String,
        image_bytes: ByteString
    ) {
        AdminImageProviderService.getInstance()
            .uploadByByte(
                AppConfig.REPAIR_PREFIX,
                fileName,
                image_bytes,
                object : CallBack<AdminImagesResponse>() {
                    override fun callBack(result: AdminImagesResponse?) {
                        if (result == null) {
                            ToastUtils.showLong("上传图片超时!")
                            return
                        }
                        if (result?.code == 100) {
                            val uploadImage = JSON.parseObject(result.jsonstr, UploadImage::class.java)
                            images.add(uploadImage.path)
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
    }


    /**
     * 上传头像
     */
    private fun uploadHeadImage() {
        val items = arrayOf("选择本地照片", "拍照")
        AlertDialog.Builder(context!!)
            .setItems(items) { _, which ->
                when (which) {
                    0 ->
                        //跳转到相册
                        gotoPhoto()
                    1 ->  //权限判断
                        //跳转到调用系统相机
                        gotoCamera()
                }
            }.show()
    }

    private var tempFile: File? = null
    //请求相机
    private val REQUEST_CAPTURE = 100
    //请求相册
    private val REQUEST_PICK = 101

    /**
     * 跳转到相册
     */
    private fun gotoPhoto() {
        Log.d("evan", "*****************打开图库********************")
        //跳转到调用系统图库
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(
            Intent.createChooser(intent, "请选择图片"),
            REQUEST_PICK
        )
    }

    /**
     * 跳转到照相机
     */
    private fun gotoCamera() {
        Log.d("evan", "*****************打开相机********************")
        //创建拍照存储的图片文件
        val path = Environment.getExternalStorageDirectory()
            .path + "/image/"
        FileUtils.createOrExistsDir(path)
        tempFile = File(path, System.currentTimeMillis().toString() + ".jpg")
        //跳转到调用系统相机
        val intent =
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            val contentUri: Uri = FileProvider.getUriForFile(
                activity!!,
                BuildConfig.APPLICATION_ID + ".fileProvider",
                tempFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
        } else {
            intent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(tempFile)
            )
        }
        startActivityForResult(
            intent,
            REQUEST_CAPTURE
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CAPTURE -> if (resultCode == Activity.RESULT_OK) {
                uploadImage(Uri.fromFile(tempFile))
            }
            REQUEST_PICK -> if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data
                uploadImage(uri!!)
            }
            REQUEST_TYPE -> if (resultCode == Activity.RESULT_OK) {
                type = data?.getSerializableExtra(ARG_TYPE) as RepairType
                repair_type.text = type?.content
            }
        }
    }

    private fun uploadImage(uri: Uri) {
        val cropImagePath: String? = getRealFilePathFromUri(activity!!, uri)
        //此处后面可以将bitMap转为二进制上传后台网络
        val bitmap= BitmapFactory.decodeFile(cropImagePath)
        val maxSize:Long = 1024*1024*8L
        val newBitMap = ImageUtils.compressByQuality(bitmap, maxSize)
        val byteArray = ImageUtils.bitmap2Bytes(newBitMap,Bitmap.CompressFormat.JPEG)
        val imageString = ByteString.copyFrom(byteArray)
        var fileName= System.currentTimeMillis().toString()+FileUtils.getFileExtension(cropImagePath)
        uploadByByte(fileName, imageString)
        Glide.with(this).load(cropImagePath).into(addImage)
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    private fun getRealFilePathFromUri(
        context: Context,
        uri: Uri?
    ): String? {
        if (null == uri) return null
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) {
            data = uri.path
        } else if (ContentResolver.SCHEME_FILE.equals(scheme, ignoreCase = true)) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme, ignoreCase = true)) {
            val cursor = context.contentResolver.query(
                uri,
                arrayOf(MediaStore.Images.ImageColumns.DATA),
                null,
                null,
                null
            )
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }





}
