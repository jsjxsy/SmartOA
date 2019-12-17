package com.gx.smart.smartoa.activity.ui.company


import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.google.protobuf.ByteString
import com.gx.smart.smartoa.BuildConfig
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppStructureService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.utils.DataCheckUtil
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_mine_company_employees.*
import kotlinx.android.synthetic.main.layout_common_title.*
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class MineCompanyEmployeesFragment : Fragment(), View.OnClickListener {

    private var companyId: Int = 0
    private lateinit var companyName: String
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
            companyId = it.getInt(ARG_COMPANY_ID)
            companyName = it.getString(ARG_COMPANY_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_company_employees, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
            it.text = getString(R.string.employee_information)
        }

    }

    private fun initContent() {
        companyNameText.text = companyName
        submitApply.setOnClickListener(this)
        phone.setText(SPUtils.getInstance().getString(AppConfig.SH_USER_ACCOUNT, ""))
        employeeNameEdit.setText(SPUtils.getInstance().getString(AppConfig.SH_USER_REAL_NAME, ""))
        addImage.setOnClickListener(this)
    }


    private fun applyEmployee(
        name: String,
        mobile: String,
        image: ByteString?,
        companyId: Long
    ) {
        AppStructureService.getInstance()
            .applyEmployee(name, mobile, image, companyId,
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        if (result == null) {
                            ToastUtils.showLong("申请加入公司超时!")
                            return
                        }
                        if (result?.code == 100) {
                            ToastUtils.showLong("申请成功!")
                            activity?.finish()
                            SPUtils.getInstance().put(AppConfig.PLACE_NAME,companyName)
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
                cropPhoto(Uri.fromFile(tempFile))
            }
            REQUEST_PICK -> if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data
                cropPhoto(uri)
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
                uploadImage(result.uri)
            }
        }
    }


    /**
     * 对指定图片进行裁剪。
     * @param uri
     * 图片的uri地址。
     */
    private fun cropPhoto(uri: Uri?) {
        if (uri == null) {
            return
        }
        val reqWidth = ScreenUtils.getScreenWidth()
        var reqHeight = reqWidth
        CropImage.activity(uri)
            .setAspectRatio(reqWidth, reqHeight)
            .setActivityTitle("裁剪")
            .setRequestedSize(reqWidth, reqHeight)
            .setCropMenuCropButtonIcon(R.mipmap.ic_crop)
            .start(activity!!)

    }


    private fun uploadImage(uri: Uri) {
        val cropImagePath: String? = getRealFilePathFromUri(activity!!, uri)
        //此处后面可以将bitMap转为二进制上传后台网络
        val bitmap =
            BitmapFactory.decodeFile(cropImagePath)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray = baos.toByteArray()
        imageString = ByteString.copyFrom(byteArray)
        Glide.with(this).load(cropImagePath)
            .into(addImage)
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
    }

}
