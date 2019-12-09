package com.gx.smart.smartoa.activity.ui.environmental.bean;

import java.util.List;

/**
 * Author: 13750831423
 * Time: 2018/9/14  11:04
 * Email: guolejun1990@163.com
 * Web: www.dd121.com
 * Description:
 */
public class AirBoxBean {

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
        private List<DataBean> data;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {

            private int devId;
            private String areaName;
            private String devName;
            private List<EnvDataBean> envData;

            public int getDevId() {
                return devId;
            }

            public void setDevId(int devId) {
                this.devId = devId;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public String getDevName() {
                return devName;
            }

            public void setDevName(String devName) {
                this.devName = devName;
            }

            public List<EnvDataBean> getEnvData() {
                return envData;
            }

            public void setEnvData(List<EnvDataBean> envData) {
                this.envData = envData;
            }

            public static class EnvDataBean {
                /**
                 * date : 2018-09-14
                 * time : 11:02:15
                 * value : 26.0,61.2,21,71,360,0.01
                 */

                private String date;
                private String time;
                private String value;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }
    }
}
