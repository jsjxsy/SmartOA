package com.gx.smart.smartoa.activity.ui.repair

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.repair_fragment.*
import java.io.File

class RepairFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = RepairFragment()
        const val ARG_TYPE = "type"
        const val REQUEST_TYPE = 200
        const val REQUEST_UPLOAD_IMAGE_ENABLE = 10
    }

    private lateinit var viewModel: RepairViewModel
    private var type: RepairType = RepairType(1, "设备损坏")
    private var images: MutableList<String> = arrayListOf()

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
        initContent()
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
        save.setTag(R.id.save, true)
        save.setOnClickListener(this)
        companyName.setText(SPUtils.getInstance().getString(AppConfig.COMPANY_NAME, ""))
        val phoneValue = SPUtils.getInstance().getString(AppConfig.SH_USER_ACCOUNT, "")
        phone.setText(phoneValue)
        contentEdit.setSelection(0)
        repair_type.setOnClickListener(this)
        addImage1Layout.setOnClickListener(this)
        addImage2Layout.setOnClickListener(this)
        addImage3Layout.setOnClickListener(this)

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
                val enable = save.getTag(R.id.save) as Boolean
                if (enable) {
                    val employeePhone = phone.text.toString()
                    if(employeePhone.isNullOrBlank()) {
                        ToastUtils.showLong("报修人手机不能为空！")
                        return
                    }

                    val address = companyName.text.toString()
                    if(address.isNullOrBlank()) {
                        ToastUtils.showLong("报修地址不能为空！")
                        return
                    }
                    val content = contentEdit.text.toString()
                    if(content.isNullOrBlank()) {
                        ToastUtils.showLong("报修内容不能为空！")
                        return
                    }
                    addRepair(content, type.type, address, employeePhone, images)
                } else {
                    ToastUtils.showLong("正在上传文件!")
                }

            }

            R.id.addImage1Layout,
            R.id.addImage2Layout,
            R.id.addImage3Layout -> uploadHeadImage()
        }


    }


    private fun addRepair(
        content: String,
        type: Int,
        address: String,
        employee_phone: String,
        images: List<String>
    ) {
        loadingView.visibility = View.VISIBLE
        AppRepairService.getInstance()
            .addRepair(
                content,
                type,
                address,
                employee_phone,
                images,
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        loadingView.visibility = View.GONE
                        if (result == null) {
                            ToastUtils.showLong("添加超时!")
                            return
                        }
                        if (result?.code == 100) {
                            ToastUtils.showLong("报修成功!")
                            activity?.finish()
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
                            uploadImageFail("上传图片超时!")
                            if (requestUploadImageCount == position) {
                                if (ActivityUtils.isActivityAlive(activity)) {
                                    save.setTag(R.id.save, true)
                                }
                            }
                            position++
                            return
                        }
                        if (result?.code == 100) {
                            val uploadImage =
                                JSON.parseObject(result.jsonstr, UploadImage::class.java)
                            images.add(uploadImage.path)
                            if (ActivityUtils.isActivityAlive(activity)) {
                                when (position) {
                                    1 -> {
                                        state1.text = "上传完成"
                                    }
                                    2 -> {
                                        state2.text = "上传完成"
                                    }
                                    3 -> {
                                        state3.text = "上传完成"
                                    }
                                }
                            }

                        } else {
                            uploadImageFail(result.msg)
                        }
                        if (requestUploadImageCount == position) {
                            if (ActivityUtils.isActivityAlive(activity)) {
                                save.setTag(R.id.save, true)
                            }
                        }
                        position++
                    }

                })
    }


    private fun uploadImageFail(msg: String) {
        ToastUtils.showLong(msg)
        if (ActivityUtils.isActivityAlive(activity)) {
            when (position) {
                1 -> {
                    state1.text = "上传失败"
                }
                2 -> {
                    state2.text = "上传失败"
                }
                3 -> {
                    state3.text = "上传失败"
                }
            }
        }
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

    var requestUploadImageCount = 0
    @Suppress("INACCESSIBLE_TYPE")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CAPTURE -> if (resultCode == Activity.RESULT_OK) {
                requestUploadImageCount++
                save.setTag(R.id.save, false)
                val uri = Uri.fromFile(tempFile)
                displayImage(uri)
                UploadImageAsyncTask<Uri, Process, Unit>().execute(uri)
            }
            REQUEST_PICK -> if (resultCode == Activity.RESULT_OK) {
                requestUploadImageCount++
                save.setTag(R.id.save, false)
                val uri = data?.data
                displayImage(uri)
                UploadImageAsyncTask<Uri, Process, Unit>().execute(uri)
            }
            REQUEST_TYPE -> if (resultCode == Activity.RESULT_OK) {
                type = data?.getSerializableExtra(ARG_TYPE) as RepairType
                repair_type.text = type?.content
            }
        }
    }

    private fun displayImage(uri: Uri?) {
        val cropImagePath: String? = getRealFilePathFromUri(activity!!, uri)
        when (position) {
            1 -> {
                Glide.with(this@RepairFragment).load(cropImagePath).into(addImage1)
                addImage2Layout.visibility = View.VISIBLE
            }
            2 -> {
                Glide.with(this@RepairFragment).load(cropImagePath).into(addImage2)
                addImage3Layout.visibility = View.VISIBLE
            }
            3 -> {
                Glide.with(this@RepairFragment).load(cropImagePath).into(addImage3)
            }
        }
    }


    fun uploadImage(uri: Uri) {
        val cropImagePath: String? = getRealFilePathFromUri(activity!!, uri)
        //此处后面可以将bitMap转为二进制上传后台网络
        val bitmap = BitmapFactory.decodeFile(cropImagePath)
        val maxSize: Long = 500 * 500L
        val newBitMap = ImageUtils.compressByQuality(bitmap, maxSize)
        val byteArray = ImageUtils.bitmap2Bytes(newBitMap, Bitmap.CompressFormat.JPEG)
        val imageString = ByteString.copyFrom(byteArray)
        var fileName =
            System.currentTimeMillis().toString() + FileUtils.getFileExtension(cropImagePath)
        uploadByByte(fileName, imageString)
    }

    var position = 1

    inner class UploadImageAsyncTask<Uri, Progress, Unit> :
        AsyncTask<android.net.Uri, Progress, kotlin.Unit>() {
        override fun onPreExecute() {
            super.onPreExecute()
            when (position) {
                1 -> {
                    state1.visibility = View.VISIBLE
                }
                2 -> {
                    state2.visibility = View.VISIBLE
                }
                3 -> {
                    state3.visibility = View.VISIBLE
                }
            }
        }

        override fun doInBackground(vararg params: android.net.Uri) {
            return uploadImage(params[0])
        }
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
