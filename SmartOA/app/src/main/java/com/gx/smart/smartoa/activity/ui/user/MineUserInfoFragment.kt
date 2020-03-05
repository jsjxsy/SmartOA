package com.gx.smart.smartoa.activity.ui.user


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
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.protobuf.ByteString
import com.gx.smart.smartoa.BuildConfig
import com.gx.smart.smartoa.R
import com.gx.smart.lib.http.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.lib.http.base.GrpcAsyncTask
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_mine_user_info.*
import kotlinx.android.synthetic.main.layout_common_title.*
import top.limuyang2.customldialog.BottomTextListDialog
import top.limuyang2.customldialog.adapter.BottomTextListAdapter
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class MineUserInfoFragment : Fragment(), View.OnClickListener {

    private var nick: String = ""
    private var verify: Int = 2
    private var task: GrpcAsyncTask<String, Void, AppInfoResponse>? = null
    private var callBack: CallBack<AppInfoResponse?>? = null
    //调用照相机返回图片文件
    private var tempFile: File? = null
    private var mobile: String? = null
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_user_info, container, false)
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
            it.text = getString(R.string.my_info)
        }
    }

    private fun initContent() {
        getUserInfo()

        nickNameLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(MineUserInfoModifyNickNameFragment.ARG_NICK_NAME, nick)
            findNavController().navigate(
                R.id.action_mineUserInfoFragment_to_mineUserInfoModifyNickNameFragment,
                bundle
            )
        }
        suggestionLayout.setOnClickListener {
            val dialog = BottomTextListDialog.init(fragmentManager!!)
            dialog.setTextList(listOf("男", "女"))
                .setOnItemClickListener(object : BottomTextListAdapter.OnItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        when (position) {
                            0 -> {
                                changeUserGender("1");
                                if (GrpcAsyncTask.isFinish(task)) {
                                    task =
                                        UserCenterService.getInstance()
                                            .changeUserGender(1, callBack);
                                }
                                dialog.dismiss()
                            }
                            1 -> {
                                changeUserGender("2")

                                if (GrpcAsyncTask.isFinish(task)) {
                                    task =
                                        UserCenterService.getInstance()
                                            .changeUserGender(2, callBack);
                                }
                                dialog.dismiss()
                            }

                        }
                    }

                }).show()
        }
        aboutLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(MineUserInfoModifyPhoneFragment.ARG_PHONE_NUMBER, mobile)
            findNavController().navigate(
                R.id.action_mineUserInfoFragment_to_mineUserInfoModifyPhoneFragment,
                bundle
            )
        }

        verifyLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(MineVerifyFragment.ARG_VERIFY, verify)
            findNavController().navigate(
                R.id.action_mineUserInfoFragment_to_mineVerifyFragment,
                bundle
            )
        }

        modifyPasswordLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(MineUserInfoModifyPasswordFragment.ARG_PHONE_NUMBER, mobile)
            findNavController().navigate(
                R.id.action_mineUserInfoFragment_to_mineUserInfoModifyPasswordFragment,
                bundle
            )
        }
        headLayout.setOnClickListener {
            uploadHeadImage()
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            getUserInfo()
        }
    }


    private fun getUserInfo() {
        UserCenterService.getInstance().getAppUserInfo(object : CallBack<AppInfoResponse>() {
            override fun callBack(result: AppInfoResponse?) {
                if (!ActivityUtils.isActivityAlive(activity)) {
                    return
                }
                if (result?.code == 100) {
                    val userInfo = result.appUserInfoDto
                    val headImageUrl =
                        userInfo.imageUrl + "?v=" + SystemClock.currentThreadTimeMillis()
                        Glide.with(activity!!)
                            .load(headImageUrl)
                            .error(R.drawable.ic_head)
                            .placeholder(R.drawable.ic_head)
                            .apply(RequestOptions.bitmapTransform(CircleCrop()))
                            .skipMemoryCache(true) // 不使用内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(headImage)

                    if (userInfo.nickName.isNotBlank()) {
                        nick = userInfo.nickName
                        nickName.text = userInfo.nickName
                    }
                    when (userInfo.gender) {
                        1 -> sex.text = "男"
                        2 -> sex.text = "女"
                        else -> sex.text = "未知"
                    }

                    if (userInfo.mobile.isNotBlank()) {
                        mobile = userInfo.mobile
                        val phoneNo =
                            userInfo.mobile.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
                        phone.text = phoneNo
                    } else {
                        phone.text = "请绑定手机"
                    }
                    verify = userInfo.verified
                    when (userInfo.verified) {
                        1 -> identificationVerify.let {
                            it.text = "已认证"
                            it.setTextColor(resources.getColor(R.color.font_color_style_night))
                        }

                        2 -> identificationVerify.let {
                            it.text = "未认证"
                            it.setTextColor(resources.getColor(R.color.font_color_style_five))
                        }
                    }
                }
            }

        })
    }

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
                val uri = result.uri
                val cropImagePath: String? = getRealFilePathFromUri(activity!!, uri)
                //此处后面可以将bitMap转为二进制上传后台网络
                //上传头像
                uploadHeadImageCallBack(cropImagePath)
                val bitmap =
                    BitmapFactory.decodeFile(cropImagePath)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val byteArray = baos.toByteArray()
                val imageString = ByteString.copyFrom(byteArray)
                if (GrpcAsyncTask.isFinish(task)) {
                    task = UserCenterService.getInstance().updateAppUserImage(imageString, callBack)
                }
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


    private fun uploadHeadImageCallBack(cropImagePath: String?) {
        callBack = object : CallBack<AppInfoResponse?>() {
            override fun callBack(result: AppInfoResponse?) {

                if (result == null) {
                    ToastUtils.showLong("头像上传超时")
                    return
                }
                if (result.code == 100) {
                    if (ActivityUtils.isActivityAlive(activity)) {
                        Glide.with(activity!!).load(cropImagePath)
                            .apply(RequestOptions.bitmapTransform(CircleCrop()))
                            .into(headImage)
                    }
                    ToastUtils.showLong("头像上传成功")
                } else {
                    ToastUtils.showLong(result.msg)
                }
            }
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


    fun changeUserGender(gender: String) {
        callBack = object : CallBack<AppInfoResponse?>() {
            override fun callBack(result: AppInfoResponse?) {
                if (!ActivityUtils.isActivityAlive(activity)) {
                    return
                }
                if (result == null) {
                    ToastUtils.showLong("修改性别超时")
                    return
                }
                if (result.code == 100) {
                    if (result.msg == "成功") {
                        ToastUtils.showLong("保存成功")
                        sex.text =
                            if (gender == "1")
                                "男"
                            else
                                "女"
                    } else {
                        ToastUtils.showLong("保存失败")
                    }

                } else {
                    ToastUtils.showLong(result.msg)
                }
            }
        }
    }

}
