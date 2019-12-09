package com.gx.smart.smartoa.activity.ui.environmental.utils;


import com.gx.smart.smartoa.activity.ui.environmental.bean.AddDevActionBean;
import com.gx.smart.smartoa.activity.ui.environmental.bean.ZGDevListBean;

import java.util.ArrayList;
import java.util.List;

public class ZGUtil {

    /**
     * 获取紫光设备类型工具接口
     *
     * @param type    (0：灯光；1：窗帘；2：空调；3：新风；4：地暖；6：背景音乐  7：空气盒子)
     * @param devList 所有设备列表
     * @return 相应设备类型列表
     */
    public static List<AddDevActionBean> getDevListByAddDev(int type, List<AddDevActionBean> devList) {
        List<AddDevActionBean> list = null;
        switch (type) {
            case ZGManager.DEV_TYPE_LIGHT://灯光
                list = new ArrayList<>();
                for (AddDevActionBean bean : devList) {
                    if (bean.getCategory().equals("single_zf_switch")//零火线触摸开关
                            || bean.getCategory().equals("single_fire_switch")
                            || bean.getCategory().equals("mechanical_switch")
                            || bean.getCategory().equals("light_dimmer_switch")//调光开关
                            || bean.getCategory().equals("led_dimmer_controller")
                            || bean.getModel().equals("l_color_dimmer_controller")) {
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_CURTAIN://窗帘
                list = new ArrayList<>();
                for (AddDevActionBean bean : devList) {
                    if (bean.getCategory().equals("curtain_controller")//窗帘控制器
                            || bean.getCategory().equals("window_controller")) {//窗帘控制器
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_AIR_CONDITIONER://空调
                list = new ArrayList<>();
                for (AddDevActionBean bean : devList) {
                    if (bean.getCategory().equals("air_conditioner")) {//中央空调
                        list.add(bean);
                    } else if (bean.getCategory().equals("infrared_device") //红外空调
                            && bean.getModel().equals("ir_air_conditioner")) {
                        list.add(bean);
                    } else if (bean.getCategory().equals("infrared_device") //红外码库空调
                            && bean.getModel().equals("ir_air_conditioner_code")) {
                        list.add(bean);
                    }else if (bean.getCategory().equals("infrared_device") //红外码库空调2
                            && bean.getModel().equals("ir_air_conditioner_code_b")) {
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_NEW_WIND://新风
                list = new ArrayList<>();
                for (AddDevActionBean bean : devList) {
                    if ((bean.getCategory().equals("thermostat")//风机盘管温控器（3H1）
                            && bean.getModel().equals("th_ducted_3h1_conditioner"))
                            || (bean.getCategory().equals("thermostat")//风机盘管中央空调温控器
                            && bean.getModel().equals("th_ducted_air_conditioner"))
                            || (bean.getCategory().equals("thermostat")//新风控制器
                            && bean.getModel().equals("th_fresh_air_conditioner"))
                            || (bean.getCategory().equals("thermostat")//新风温控器（3H1)
                            && bean.getModel().equals("th_new_wind_conditioner"))) {
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_FLOOR_HEAT://地暖
                list = new ArrayList<>();
                for (AddDevActionBean bean : devList) {
                    if ((bean.getCategory().equals("thermostat")//水地暖温控器（3H1）
                            && bean.getModel().equals("th_water_3h1_conditioner"))
                            || (bean.getCategory().equals("thermostat")//水地暖温控器
                            && bean.getModel().equals("th_water_floor_conditioner"))
                            || (bean.getCategory().equals("thermostat")//电地暖温控器（I）
                            && bean.getModel().equals("th_eleheating_3h1_conditioner"))
                            || (bean.getCategory().equals("thermostat")//电地暖温控器（II）
                            && bean.getModel().equals("th_eleheating_conditioner"))) {
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_MONITOR://监控
                break;
            case ZGManager.DEV_TYPE_MEDIA://背景音乐
                list = new ArrayList<>();
                for (AddDevActionBean bean : devList) {
                    if (bean.getCategory().equals("background_music")) {//背景音乐
                        list.add(bean);
                    }
                }
                break;
            case 7://空气盒子
                list = new ArrayList<>();
                for (AddDevActionBean bean : devList) {
                    if (bean.getCategory().equals("env_probe")) {//空气盒子
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_AIR_CLEANER://空气盒子
                list = new ArrayList<>();
                for (AddDevActionBean bean : devList) {
                    if (bean.getCategory().equals("air_cleaner")) {
                        list.add(bean);
                    }
                }
                break;
            default:
                list = devList;
                break;
        }

        return list;
    }

    /**
     * 获取紫光设备类型工具接口
     *
     * @param type    (0：灯光；1：窗帘；2：空调；3：新风；4：地暖；5：监控 6：背景音乐  7：空气盒子
     *                8:空气净化器 9:安防 10:场景设备)
     * @param devList 所有设备列表
     * @return 相应设备类型列表
     */
    public static List<ZGDevListBean.DataResponseBean.DevBean> getDevList(int type, List<ZGDevListBean.DataResponseBean.DevBean> devList) {
        List<ZGDevListBean.DataResponseBean.DevBean> list;
        switch (type) {
            case ZGManager.DEV_TYPE_LIGHT://灯光
                list = new ArrayList<>();
                for (ZGDevListBean.DataResponseBean.DevBean bean : devList) {
                    if (bean.getCategory().equals("single_zf_switch")//零火线触摸开关
                            || bean.getCategory().equals("single_fire_switch")//单火线触摸开关
                            || bean.getCategory().equals("mechanical_switch")) {//机械开关
                        list.add(bean);
                    } else if (bean.getCategory().equals("light_dimmer_switch")//调光开关
                            || bean.getCategory().equals("led_dimmer_controller")//LED调光控制器
                            ) {
                        if (bean.getModel().equals("l_color_dimmer_controller")) {
                        } else {
                        }
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_CURTAIN://窗帘
                list = new ArrayList<>();
                for (ZGDevListBean.DataResponseBean.DevBean bean : devList) {
                    if (bean.getCategory().equals("curtain_controller")//窗帘控制器
                            || bean.getCategory().equals("window_controller")) {//开窗器
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_AIR_CONDITIONER://空调
                list = new ArrayList<>();
                for (ZGDevListBean.DataResponseBean.DevBean bean : devList) {
                    if (bean.getCategory().equals("air_conditioner")) {//中央空调
                        list.add(bean);
                    } else if (bean.getCategory().equals("infrared_device") //红外空调
                            && bean.getModel().equals("ir_air_conditioner")) {
                        list.add(bean);
                    } else if (bean.getCategory().equals("infrared_device") //红外码库空调
                            && bean.getModel().equals("ir_air_conditioner_code")) {
                        list.add(bean);
                    }
                    else if (bean.getCategory().equals("infrared_device") //红外码库空调2
                            && bean.getModel().equals("ir_air_conditioner_code_b")) {
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_NEW_WIND://新风
                list = new ArrayList<>();
                for (ZGDevListBean.DataResponseBean.DevBean bean : devList) {
                    if ((bean.getCategory().equals("thermostat")//风机盘管温控器（3H1）
                            && bean.getModel().equals("th_ducted_3h1_conditioner"))
                            || (bean.getCategory().equals("thermostat")//风机盘管中央空调温控器
                            && bean.getModel().equals("th_ducted_air_conditioner"))
                            || (bean.getCategory().equals("thermostat")//新风控制器
                            && bean.getModel().equals("th_fresh_air_conditioner"))
                            || (bean.getCategory().equals("thermostat")//新风温控器（3H1)
                            && bean.getModel().equals("th_new_wind_conditioner"))
                            ) {
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_FLOOR_HEAT://地暖
                list = new ArrayList<>();
                for (ZGDevListBean.DataResponseBean.DevBean bean : devList) {
                    if ((bean.getCategory().equals("thermostat")//水地暖温控器（3H1）
                            && bean.getModel().equals("th_water_3h1_conditioner"))
                            || (bean.getCategory().equals("thermostat")//水地暖温控器
                            && bean.getModel().equals("th_water_floor_conditioner"))
                            || (bean.getCategory().equals("thermostat")//电地暖温控器（I）
                            && bean.getModel().equals("th_eleheating_3h1_conditioner"))
                            || (bean.getCategory().equals("thermostat")//电地暖温控器（II）
                            && bean.getModel().equals("th_eleheating_conditioner"))
                            ) {
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_MONITOR://监控
                list = new ArrayList<>();
                for (ZGDevListBean.DataResponseBean.DevBean bean : devList) {
                    if (bean.getCategory().equals("camera")) {
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_MEDIA://背景音乐
                list = new ArrayList<>();
                for (ZGDevListBean.DataResponseBean.DevBean bean : devList) {
                    if (bean.getCategory().equals("background_music")) {//背景音乐
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_AIR_BOX://空气盒子
                list = new ArrayList<>();
                for (ZGDevListBean.DataResponseBean.DevBean bean : devList) {
                    if (bean.getCategory().equals("env_probe")) {//空气盒子
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_AIR_CLEANER://空气净化器
                list = new ArrayList<>();
                for (ZGDevListBean.DataResponseBean.DevBean bean : devList) {
                    if (bean.getCategory().equals("air_cleaner")) {
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_SECURITY_SENSOR:
                list = new ArrayList<>();
                for (ZGDevListBean.DataResponseBean.DevBean bean : devList) {
                    if (bean.getCategory().equals("security_sensor")
                            || bean.getCategory().equals("protocol_converter")
                            || bean.getCategory().equals("door_lock")) {
                        list.add(bean);
                    }
                }
                break;
            case ZGManager.DEV_TYPE_SCENE_PLANE://场景设备
                list = new ArrayList<>();
                for (ZGDevListBean.DataResponseBean.DevBean bean : devList) {
                    if (bean.getCategory().equals("scene_dev")) {
                        list.add(bean);
                    }
                }
                break;
            default:
                list = devList;
                break;
        }
        return list;
    }

    public static int getDevTypeByAddDev(AddDevActionBean dev) {
        if (dev.getCategory().equals("single_zf_switch")//零火线触摸开关
                || dev.getCategory().equals("single_fire_switch")) {//单火线触摸开关
            return ZGManager.DEV_TYPE_LIGHT;
        } else if (dev.getCategory().equals("curtain_controller")) {//窗帘控制器
            return ZGManager.DEV_TYPE_CURTAIN;
        }
        if (dev.getCategory().equals("air_conditioner")) {//中央空调
            return ZGManager.DEV_TYPE_AIR_CONDITIONER;
        } else if (dev.getCategory().equals("infrared_device") //红外空调
                && dev.getModel().equals("ir_air_conditioner")) {
            return ZGManager.DEV_TYPE_AIR_CONDITIONER;
        } else if (dev.getCategory().equals("infrared_device") //红外码库空调
                && dev.getModel().equals("ir_air_conditioner_code")) {
            return ZGManager.DEV_TYPE_AIR_CONDITIONER;
        }else if (dev.getCategory().equals("infrared_device") //红外码库空调2
                && dev.getModel().equals("ir_air_conditioner_code_b")) {
            return ZGManager.DEV_TYPE_AIR_CONDITIONER;
        }
        if (dev.getCategory().equals("thermostat")//新风
                && dev.getModel().equals("th_ducted_3h1_conditioner")) {
            return ZGManager.DEV_TYPE_NEW_WIND;
        }
        if (dev.getCategory().equals("thermostat")//地暖
                && dev.getModel().equals("th_eleheating_3h1_conditioner")) {
            return ZGManager.DEV_TYPE_FLOOR_HEAT;
        }
        if (dev.getCategory().equals("background_music")) {//背景音乐
            return ZGManager.DEV_TYPE_MEDIA;
        }
        if (dev.getCategory().equals("air_cleaner")) {//空气净化机
            return ZGManager.DEV_TYPE_AIR_CLEANER;
        }
        return ZGManager.DEV_TYPE_LIGHT;
    }

    public static int getDevType(ZGDevListBean.DataResponseBean.DevBean dev) {
        if (dev.getCategory().equals("single_zf_switch")//零火线触摸开关
                || dev.getCategory().equals("single_fire_switch")) {//单火线触摸开关
            return ZGManager.DEV_TYPE_LIGHT;
        } else if (dev.getCategory().equals("curtain_controller")) {//窗帘控制器
            return ZGManager.DEV_TYPE_CURTAIN;
        }
        if (dev.getCategory().equals("air_conditioner")) {//中央空调
            return ZGManager.DEV_TYPE_AIR_CONDITIONER;
        } else if (dev.getCategory().equals("infrared_device") //红外空调
                && dev.getModel().equals("ir_air_conditioner")) {
            return ZGManager.DEV_TYPE_AIR_CONDITIONER;
        } else if (dev.getCategory().equals("infrared_device") //红外码库空调
                && dev.getModel().equals("ir_air_conditioner_code")) {
            return ZGManager.DEV_TYPE_AIR_CONDITIONER;
        }else if (dev.getCategory().equals("infrared_device") //红外码库空调2
                && dev.getModel().equals("ir_air_conditioner_code_b")) {
            return ZGManager.DEV_TYPE_AIR_CONDITIONER;
        }
        if (dev.getCategory().equals("thermostat")//新风
                && dev.getModel().equals("th_ducted_3h1_conditioner")) {
            return ZGManager.DEV_TYPE_NEW_WIND;
        }
        if (dev.getCategory().equals("thermostat")//地暖
                && dev.getModel().equals("th_eleheating_3h1_conditioner")) {
            return ZGManager.DEV_TYPE_FLOOR_HEAT;
        }
        if (dev.getCategory().equals("background_music")) {//背景音乐
            return ZGManager.DEV_TYPE_MEDIA;
        }
        if (dev.getCategory().equals("air_cleaner")) {//空气净化器
            return ZGManager.DEV_TYPE_AIR_CLEANER;
        }
        return ZGManager.DEV_TYPE_LIGHT;
    }

    public static String getDevCategory(ZGDevListBean.DataResponseBean.DevBean dev) {
        if (dev.getCategory().equals("single_zf_switch")) {//零火线触摸开关
            return "零火线触摸开关";
        }
        if (dev.getCategory().equals("single_fire_switch")) {//单火线触摸开关
            return "单火线触摸开关";
        }
        if (dev.getCategory().equals("curtain_controller")) {//窗帘控制器
            return "窗帘控制器";
        }
        if (dev.getCategory().equals("air_conditioner")) {//中央空调
            return "中央空调";
        }
        if (dev.getCategory().equals("infrared_device") //红外空调
                && dev.getModel().equals("ir_air_conditioner")) {
            return "红外空调";
        }
        if (dev.getCategory().equals("infrared_device") //红外码库空调
                && dev.getModel().equals("ir_air_conditioner_code")) {
            return "红外码库空调";
        }
        if (dev.getCategory().equals("infrared_device") //红外码库空调2
                && dev.getModel().equals("ir_air_conditioner_code_b")) {
            return "红外码库空调2";
        }
        if (dev.getCategory().equals("thermostat")//新风
                && dev.getModel().equals("th_ducted_3h1_conditioner")) {
            return "新风";
        }
        if (dev.getCategory().equals("thermostat")//地暖
                && dev.getModel().equals("th_eleheating_3h1_conditioner")) {
            return "地暖";
        }
        if (dev.getCategory().equals("background_music")) {//背景音乐
            return "背景音乐";
        }
        if (dev.getCategory().equals("air_cleaner")) {//空净
            return "空净";
        }
        return "未知";
    }
}
