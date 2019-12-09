package com.gx.smart.smartoa.activity.ui.environmental.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author: 13750831423
 * Time: 2018/9/6  13:57
 * Email: guolejun1990@163.com
 * Web: www.dd121.com
 * Description:
 */
public class ZGDevListBean {
    /**
     * dataResponse : {"result":0,"data":[{"val":"25.2,0,4,57.0,0,3,47,1,2,70,0,3,360,0, ,0.01,0, ","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"env_box_sensor_a","devName":"智能环境","category":"env_probe","uuid":4},{"val":"-1","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"zf_three_switch","devName":"玄关筒灯","category":"single_zf_switch","uuid":5},{"val":"-1","linkState":1,"areaId":1,"controlType":1,"channel":"2","model":"zf_three_switch","devName":"玄关吊灯","category":"single_zf_switch","uuid":6},{"val":"-1","linkState":1,"areaId":1,"controlType":1,"channel":"3","model":"zf_three_switch","devName":"玄关射灯","category":"single_zf_switch","uuid":7},{"val":",","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"ss_gas_leaking_sensor","devName":"可燃气检测","category":"security_sensor","uuid":8},{"val":",","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"ss_alarm_button","devName":"紧急按钮","category":"security_sensor","uuid":10},{"val":"-1,3","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"ss_curtain_sensor","devName":"红外幕帘","category":"security_sensor","uuid":11},{"val":"-1,","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"ss_motion_sensor","devName":"人体探测","category":"security_sensor","uuid":12},{"val":"-1,","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"ss_door_sensor","devName":"智能门窗磁","category":"security_sensor","uuid":13},{"val":"","linkState":0,"areaId":1,"controlType":1,"channel":"1","model":"ir_controller_code","devName":"红外家电控制器","category":"infrared_transponder","uuid":14},{"val":"2,7,6","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"c_wisdom_motor","devName":"窗帘电机","category":"curtain_controller","uuid":16},{"val":",","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"sd_oled_panel","devName":"OLED场景面板","category":"scene_dev","uuid":19},{"val":"-1,,,25,45,36,38,,,,,,","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"th_eleheating_3h1_conditioner","devName":"地暖","category":"thermostat","uuid":30},{"val":"1,4,40,25,45,42,44,,,,,,","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"th_ducted_3h1_conditioner","devName":"新风","category":"thermostat","uuid":31},{"val":"2","linkState":0,"areaId":1,"controlType":1,"channel":"1","model":"ir_air_conditioner_code","devName":"主卧客调","category":"infrared_device","uuid":32},{"val":"2","linkState":0,"areaId":1,"controlType":1,"channel":"1","model":"ir_air_conditioner_code","devName":"dev_air_conditioner","category":"infrared_device","uuid":33},{"val":"2","linkState":0,"areaId":1,"controlType":1,"channel":"1","model":"ir_air_conditioner_code","devName":"玄关空调","category":"infrared_device","uuid":34},{"val":"2,,","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"c_single_control_pane","devName":"客厅窗帘","category":"curtain_controller","uuid":36},{"val":"0x-5","linkState":1,"areaId":1,"controlType":1,"channel":"1","model":"d_double_switch","devName":"客厅吊灯","category":"light_dimmer_switch","uuid":37},{"val":"0x-5","linkState":1,"areaId":1,"controlType":1,"channel":"2","model":"d_double_switch","devName":"客厅灯带","category":"light_dimmer_switch","uuid":38},{"val":"1,9,3,5","linkState":1,"areaId":1,"controlType":3,"channel":"1","model":"bm_backaudio_single_music","devName":"背景音乐","category":"background_music","uuid":20},{"areaId":1,"controlType":3,"channel":"1","model":"camera_ipc_wh01","devName":"玄关监控","category":"camera","uuid":35}]}
     * httpCode : 200
     */

    private DataResponseBean dataResponse;
    private int httpCode;

    public DataResponseBean getDataResponse() {
        return dataResponse;
    }

    public void setDataResponse(DataResponseBean dataResponse) {
        this.dataResponse = dataResponse;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public static class DataResponseBean {


        private int result;
        private List<DevBean> data;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<DevBean> getData() {
            return data;
        }

        public void setData(List<DevBean> data) {
            this.data = data;
        }

        public static class DevBean implements Serializable {

            @Override
            public String toString() {
                return "DevBean{" +
                        "val='" + val + '\'' +
                        ", linkState=" + linkState +
                        ", areaId=" + areaId +
                        ", controlType=" + controlType +
                        ", channel='" + channel + '\'' +
                        ", model='" + model + '\'' +
                        ", devName='" + devName + '\'' +
                        ", category='" + category + '\'' +
                        ", uuid=" + uuid +
                        '}';
            }

            /**
             * val : 25.2,0,4,57.0,0,3,47,1,2,70,0,3,360,0, ,0.01,0,
             * linkState : 1
             * areaId : 1
             * controlType : 1
             * channel : 1
             * model : env_box_sensor_a
             * devName : 智能环境
             * category : env_probe
             * uuid : 4
             */

            private String val;
            private int linkState;
            private int areaId;
            private int controlType;
            private String channel;
            private String model;
            private String devName;
            private String category;
            private int uuid;

            public String getVal() {
                return val;
            }

            public void setVal(String val) {
                this.val = val;
            }

            public int getLinkState() {
                return linkState;
            }

            public void setLinkState(int linkState) {
                this.linkState = linkState;
            }

            public int getAreaId() {
                return areaId;
            }

            public void setAreaId(int areaId) {
                this.areaId = areaId;
            }

            public int getControlType() {
                return controlType;
            }

            public void setControlType(int controlType) {
                this.controlType = controlType;
            }

            public String getChannel() {
                return channel;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }

            public String getModel() {
                return model;
            }

            public void setModel(String model) {
                this.model = model;
            }

            public String getDevName() {
                return devName;
            }

            public void setDevName(String devName) {
                this.devName = devName;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public int getUuid() {
                return uuid;
            }

            public void setUuid(int uuid) {
                this.uuid = uuid;
            }

        }
    }
}
