package com.gx.smart.share.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @author qb
 * @名称: 微信分享类
 * @描述:
 * @data:2016-6-12 下午4:48:51
 * @version:V2.4.4
 */
public class WXShareTo {

    private Context mContext;
    private IWXAPI api;

    public WXShareTo(Context mContext) {
        this.mContext = mContext;
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(mContext, Config.WX_APP_ID, false);
        api.registerApp(Config.WX_APP_ID);
    }

    /**
     * 图片分享
     */
    public void shareQrCodeToWX(Bitmap bp) {
        if (api.isWXAppInstalled()) {
            WXImageObject wxImageObject = new WXImageObject(bp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = wxImageObject;
            //设置缩略图
            Bitmap mBp = Bitmap.createScaledBitmap(bp, 120, 120, true);
            bp.recycle();
            msg.thumbData = DensityUtil.bmpToByteArray(mBp, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");//  transaction字段用
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
        } else {
            Toast.makeText(mContext, "请先安装微信客户端", Toast.LENGTH_SHORT).show();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

}
