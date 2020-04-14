package com.gx.smart.smartoa.activity.ui.company


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
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.gx.smart.lib.base.BaseFragment
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.google.protobuf.ByteString
import com.gx.smart.smartoa.R
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.AppStructureService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.smartoa.utils.DataCheckUtil
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.fragment_mine_company_employees.*
import kotlinx.android.synthetic.main.layout_common_title.*
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class MineCompanyEmployeesFragment : BaseFragment(), View.OnClickListener {

    private var companyId: Long = 0
    private var companyName: String? = null
    private var placeName: String? = null
    private var employeeName: String? = null
    private var phoneNumber: String? = null
    private var imageString: ByteString? = null
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submitApply -> {
                employeeName = employeeNameEdit.text.toString()
                phoneNumber = phone.text.toString()
                if (TextUtils.isEmpty(phoneNumber)) {
                    ToastUtils.showLong("手机号不能为空")
                } else if (phoneNumber!!.length != 11 || !DataCheckUtil.isMobile(phoneNumber)) {
                    ToastUtils.showLong("非法手机号")
                } else if (TextUtils.isEmpty(employeeName)) {
                    ToastUtils.showLong("姓名不能为空")
                }
                applyEmployee(employeeName!!, phoneNumber!!, imageString, companyId.toLong())
            }
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.addImage -> uploadHeadImage()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            companyId = it.getLong(ARG_COMPANY_ID)
            companyName = it.getString(ARG_COMPANY_NAME)
            placeName = it.getString(ARG_BUILDING_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_company_employees, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTitle()
        initContent()

    }


    override fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.employee_information)
        }

    }

    override fun initContent() {
        companyNameText.text = companyName
        submitApply.setOnClickListener(this)
        val phoneValue = SPUtils.getInstance().getString(AppConfig.SH_USER_ACCOUNT, "")
        phone.setText(phoneValue)
        phone.setSelection(phoneValue.length)
        employeeNameEdit.setText(SPUtils.getInstance().getString(AppConfig.SH_USER_REAL_NAME, ""))
        addImage.setOnClickListener(this)
    }


    private fun applyEmployee(
        name: String,
        mobile: String,
        image: ByteString?,
        companyId: Long
    ) {
        loadingView.visibility = View.VISIBLE
        AppStructureService.getInstance()
            .applyEmployee(name, mobile, image, companyId,
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        if (!ActivityUtils.isActivityAlive(activity)) {
                            return
                        }

                        loadingView.visibility = View.GONE
                        if (result == null) {
                            ToastUtils.showLong("申请加入公司超时!")
                            return
                        }
                        if (result.code == 100) {
                            ToastUtils.showLong("申请成功!")
                            activity?.finish()
                            SPUtils.getInstance().put(AppConfig.PLACE_NAME, placeName)
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
                AppUtils.getAppName() + ".fileProvider",
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

                val uri = Uri.fromFile(tempFile)
                displayImage(uri)
                UploadImageAsyncTask<Uri, Process, Unit>().execute(uri)
            }
            REQUEST_PICK -> if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data
                displayImage(uri)
                UploadImageAsyncTask<Uri, Process, Unit>().execute(uri)
            }
        }
    }


    private fun uploadImage(uri: Uri) {
        val cropImagePath: String? = getRealFilePathFromUri(activity!!, uri)
        //此处后面可以将bitMap转为二进制上传后台网络
        val bitmap =
            BitmapFactory.decodeFile(cropImagePath)
        val maxSize: Long = 500 * 500L
        val newBitMap = ImageUtils.compressByQuality(bitmap, maxSize)
        val byteArray = ImageUtils.bitmap2Bytes(newBitMap, Bitmap.CompressFormat.JPEG)
        imageString = ByteString.copyFrom(byteArray)
    }

    private fun displayImage(uri: Uri?) {
        val cropImagePath: String? = getRealFilePathFromUri(activity!!, uri)
        Glide.with(this).load(cropImagePath).into(addImage)
    }


    inner class UploadImageAsyncTask<Uri, Progress, Unit> :
        AsyncTask<android.net.Uri, Progress, kotlin.Unit>() {

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


    companion object {
        const val ARG_COMPANY_ID = "company_id"
        const val ARG_COMPANY_NAME = "company_name"
        const val ARG_BUILDING_NAME = "building_name"
    }

}
