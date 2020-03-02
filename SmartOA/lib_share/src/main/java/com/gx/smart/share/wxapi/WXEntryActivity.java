package com.gx.smart.share.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gx.smart.share.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信分享回调界面
 * 界面为透明界面
 *
 * @author qb
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    //微信分享
    private IWXAPI api;
    //分享成功标志
    public static boolean shareSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_entry);
        initData();
    }

    /**
     * 获取api
     */
    private void initData() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Config.WX_APP_ID, false);
        //回调监听
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    /**
     * 微信发送请求到第三方应用时，会回调到该方法
     */
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }

    /**
     * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     */
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                shareSuccess = true;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                shareSuccess = false;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                shareSuccess = false;
                break;
            default:
                shareSuccess = false;
                break;
        }
        finish();
    }
}
