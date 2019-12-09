package com.gx.smart.smartoa.activity.ui.environmental.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author: 13750831423
 * Time: 2018/9/6  10:39
 * Email: guolejun1990@163.com
 * Web: www.dd121.com
 * Description:
 */
public class ZGSceneListBean {

    /**
     * dataResponse : {"result":0,"data":[{"sceneNature":1,"iconSign":1,"sceneType":0,"sceneName":"一键离家","sceneId":2},{"sceneNature":1,"iconSign":30,"sceneType":0,"sceneName":"一键归家","sceneId":3},{"sceneNature":1,"iconSign":61,"sceneType":0,"sceneName":"会客","sceneId":4}]}
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
        /**
         * result : 0
         * data : [{"sceneNature":1,"iconSign":1,"sceneType":0,"sceneName":"一键离家","sceneId":2},{"sceneNature":1,"iconSign":30,"sceneType":0,"sceneName":"一键归家","sceneId":3},{"sceneNature":1,"iconSign":61,"sceneType":0,"sceneName":"会客","sceneId":4}]
         */

        private int result;
        private List<SceneBean> data;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public List<SceneBean> getData() {
            return data;
        }

        public void setData(List<SceneBean> data) {
            this.data = data;
        }

        public static class SceneBean implements Parcelable {
            /**
             * sceneNature : 1
             * iconSign : 1
             * sceneType : 0
             * sceneName : 一键离家
             * sceneId : 2
             */

            private int sceneNature;
            private int iconSign;
            private int sceneType;
            private String sceneName;
            private int sceneId;

            public int getSceneNature() {
                return sceneNature;
            }

            public void setSceneNature(int sceneNature) {
                this.sceneNature = sceneNature;
            }

            public int getIconSign() {
                return iconSign;
            }

            public void setIconSign(int iconSign) {
                this.iconSign = iconSign;
            }

            public int getSceneType() {
                return sceneType;
            }

            public void setSceneType(int sceneType) {
                this.sceneType = sceneType;
            }

            public String getSceneName() {
                return sceneName;
            }

            public void setSceneName(String sceneName) {
                this.sceneName = sceneName;
            }

            public int getSceneId() {
                return sceneId;
            }

            public void setSceneId(int sceneId) {
                this.sceneId = sceneId;
            }

            @Override
            public String toString() {
                return "sceneNature:" + sceneNature + ",iconSign:" + iconSign
                        + ",sceneType:" + sceneType + ",sceneName:" + sceneName
                        + ",sceneId:" + sceneId;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                // 序列化过程：必须按成员变量声明的顺序进行封装
                dest.writeInt(sceneNature);
                dest.writeInt(iconSign);
                dest.writeInt(sceneType);
                dest.writeString(sceneName);
                dest.writeInt(sceneId);
            }

            // 反序列过程：必须实现Parcelable.Creator接口，并且对象名必须为CREATOR
            // 读取Parcel里面数据时必须按照成员变量声明的顺序，Parcel数据来源上面writeToParcel方法，读出来的数据供逻辑层使用
            public static final Parcelable.Creator<SceneBean> CREATOR = new Creator<SceneBean>() {

                @Override
                public SceneBean createFromParcel(Parcel source) {
                    SceneBean bean = new SceneBean();
                    bean.setSceneNature(source.readInt());
                    bean.setIconSign(source.readInt());
                    bean.setSceneType(source.readInt());
                    bean.setSceneName(source.readString());
                    bean.setSceneId(source.readInt());
                    return bean;
                }

                @Override
                public SceneBean[] newArray(int size) {
                    return new SceneBean[size];
                }
            };
        }
    }
}
