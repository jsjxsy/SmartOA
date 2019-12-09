package com.gx.smart.smartoa.activity.ui.environmental.bean;

import java.util.List;

/**
 * Author: 13750831423
 * Time: 2018/9/17  17:30
 * Email: guolejun1990@163.com
 * Web: www.dd121.com
 * Description:
 */
public class SceneInfoWrapper {

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
        private List<SceneDevInfo> data;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<SceneDevInfo> getData() {
            return data;
        }

        public void setData(List<SceneDevInfo> data) {
            this.data = data;
        }

        public static class SceneDevInfo {
            /**
             * val : 1
             * linkId : 84
             * channel : 3
             * model : zf_three_switch
             * category : single_zf_switch
             * uuid : 5
             */

            private String val;
            private int linkId;
            private int channel;
            private String model;
            private String category;
            private int uuid;

            public String getVal() {
                return val;
            }

            public void setVal(String val) {
                this.val = val;
            }

            public int getLinkId() {
                return linkId;
            }

            public void setLinkId(int linkId) {
                this.linkId = linkId;
            }

            public int getChannel() {
                return channel;
            }

            public void setChannel(int channel) {
                this.channel = channel;
            }

            public String getModel() {
                return model;
            }

            public void setModel(String model) {
                this.model = model;
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

            @Override
            public String toString() {
                return "val:" + val + ",linkId:" + linkId + ",channel:"
                        + channel + ",model:" + model + ",category:"
                        + category + ",uuid:" + uuid;
            }
        }
    }
}
