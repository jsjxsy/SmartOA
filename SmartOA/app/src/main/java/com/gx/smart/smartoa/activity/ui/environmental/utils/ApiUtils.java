package com.gx.smart.smartoa.activity.ui.environmental.utils;

import com.gx.smart.smartoa.R;

/**
 * @author xiaosy
 * @create 2019-12-09
 * @Describe
 **/
public class ApiUtils {
    /**
     * 家居首页获取情景模式图标
     *
     * @param iconId 情景模式ID
     * @return 图片资源ID
     */
    public static int getImageResouce(String iconId) {

        int mIconId = 0;
        switch (iconId) {
            case "0":
                mIconId = R.drawable.ic_light_all_open;
                break;
            case "1":
                mIconId = R.drawable.ic_light_all_close;
                break;
            case "2":
                mIconId = R.drawable.ic_sleep;
                break;
            case "3":
                mIconId = R.drawable.ic_work_off;
                break;
            case "4":
                mIconId = R.drawable.ic_tea;
                break;
        }
        return mIconId;
    }
}
