package com.gx.smart.smartoa.activity.ui.company.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaosy
 * @create 2019-12-12
 * @Describe
 **/

/**
 * class SysTenants {
 * "name": "测试建库小区2",
 * "provinceName": "云南省",
 * "sysTenantNo": 3,
 * <p>
 * }
 */
public class SysTenantList implements Serializable {

    /**
     * sysTenants : [{"name":"测试小区1","provinceName":"浙江","sysTenantNo":3},{"name":"测试小区2","provinceName":"浙江","sysTenantNo":2},{"name":"测试小区3","provinceName":"浙江","sysTenantNo":4}]
     * title : 浙江
     */

    private String title;
    private List<SysTenantsBean> sysTenants;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SysTenantsBean> getSysTenants() {
        return sysTenants;
    }

    public void setSysTenants(List<SysTenantsBean> sysTenants) {
        this.sysTenants = sysTenants;
    }

    public static class SysTenantsBean {
        /**
         * name : 测试小区1
         * provinceName : 浙江
         * sysTenantNo : 3
         */

        private String name;
        private String provinceName;
        private int sysTenantNo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public int getSysTenantNo() {
            return sysTenantNo;
        }

        public void setSysTenantNo(int sysTenantNo) {
            this.sysTenantNo = sysTenantNo;
        }
    }
}

