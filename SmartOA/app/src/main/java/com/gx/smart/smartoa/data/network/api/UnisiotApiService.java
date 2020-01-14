package com.gx.smart.smartoa.data.network.api;

import com.gx.smart.smartoa.data.network.ApiConfig;
import com.gx.smart.smartoa.data.network.api.base.CallBack;
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.AirBoxDataGetReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.AirBoxDataGetResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.AreaDeviceListReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.AreaDeviceListResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.AreaSceneListReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.AreaSceneListResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevComReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDelReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevEditReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevListReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevListResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DeviceOrderDto;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.HostSysDisarmReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.HostSysDisarmResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.HostSysProtectionReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.HostSysProtectionResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.OptionLogListReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.OptionLogListResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneAddDevListReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneAddReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneComReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneDelDevListReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneDelReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneEditDevListReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneEditIconReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneEditNameReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneEditReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneGetReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneGetResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneListReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneListResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.ScenePannelBindReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.ScenePannelBindResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.ScenePannelGetReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.ScenePannelGetResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.ScenePannelUnbindReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.ScenePannelUnbindResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.TokenGetReq;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.TokenGetResp;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.UnisiotApiGrpc;
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.UnisiotResp;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;


public class UnisiotApiService {

    //25秒，网络请求超时
    public static final int TIMEOUT_NETWORK = 25;

    private static UnisiotApiService UserCenterClient;

    private UnisiotApiService() {

    }

    public static UnisiotApiService getInstance() {
        if (UserCenterClient == null) {
            UserCenterClient = new UnisiotApiService();
        }
        return UserCenterClient;
    }

    /**
     * 获取UserCenterstub  设置超时时间  8秒
     *
     * @param channel
     * @return
     */
    public static UnisiotApiGrpc.UnisiotApiBlockingStub getZiGuangStub(ManagedChannel channel) {
        return UnisiotApiGrpc.newBlockingStub(channel)
                .withDeadlineAfter(TIMEOUT_NETWORK, TimeUnit.SECONDS);

    }


    /**
     * 获取紫光token
     *
     * @param host_sn 主机SN
     * @param page    页码
     * @return callBack返回值
     */
    public static GrpcAsyncTask<String, Void, TokenGetResp> tokenGet(final String host_sn, final String page, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, TokenGetResp>(callBack) {
            @Override
            protected TokenGetResp doRequestData(ManagedChannel channel) {
                TokenGetReq message = TokenGetReq.newBuilder()
                        .build();

                TokenGetResp response = null;
                try {
                    response = getZiGuangStub(channel).tokenGet(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }

    /**
     * 操作记录
     *
     * @param host_sn 主机SN
     * @param page    页码
     * @return callBack返回值
     */
    public static GrpcAsyncTask<String, Void, OptionLogListResp> optionLogList(final String host_sn, final String page, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, OptionLogListResp>(callBack) {
            @Override
            protected OptionLogListResp doRequestData(ManagedChannel channel) {
                OptionLogListReq message = OptionLogListReq.newBuilder()
                        .setHostSn(host_sn)
                        .setPage(page)
                        .build();

                OptionLogListResp response = null;
                try {
                    response = getZiGuangStub(channel).optionLogList(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }

    /**
     * 设备列表
     *
     * @param host_sn 主机SN
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, DevListResp> devList(final String host_sn, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, DevListResp>(callBack) {
            @Override
            protected DevListResp doRequestData(ManagedChannel channel) {
                DevListReq message = DevListReq.newBuilder()
                        .setHostSn(host_sn)
                        .build();

                DevListResp response = null;
                try {
                    response = getZiGuangStub(channel).devList(message);
                } catch (Exception e) {
                    Logger.e(e, "devList");
                }
                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 修改设备
     *
     * @param host_sn     主机SN
     * @param uuid        设备id
     * @param devName     设备名
     * @param controlType 控制类型
     * @return callBack返回值
     */
    public static GrpcAsyncTask<String, Void, UnisiotResp> devEdit(final String host_sn, final String uuid, final String devName, final String controlType, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                DevEditReq message = DevEditReq.newBuilder()
                        .setHostSn(host_sn)
                        .setUuid(uuid)
                        .setDevName(devName)
                        .setControlType(controlType)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).devEdit(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }

    /**
     * 删除设备
     *
     * @param host_sn  主机SN
     * @param uuid     设备Id
     * @param opt_type 操作类型
     * @return callBack返回值
     */
    public static GrpcAsyncTask<String, Void, UnisiotResp> devDel(final String host_sn, final String uuid, final String opt_type, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                DevDelReq message = DevDelReq.newBuilder()
                        .setHostSn(host_sn)
                        .setUuid(uuid)
                        .setOptType(opt_type)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).devDel(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }

    /**
     * 控制设备
     *
     * @param host_sn    主机SN
     * @param uuid       设备id
     * @param category   设备类型
     * @param model      设备型号
     * @param channel_id 设备通道
     * @param val        指令值
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, UnisiotResp> devCom(final String host_sn, final String uuid, final String category, final String model, final String channel_id, final String val, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                DevComReq message = DevComReq.newBuilder()
                        .setHostSn(host_sn)
                        .setUuid(uuid)
                        .setCategory(category)
                        .setModel(model)
                        .setChannel(channel_id)
                        .setVal(val)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).devCom(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 获取环境数据
     *
     * @param host_sn 主机SN
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AirBoxDataGetResp> airBoxDataGet(final String host_sn, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AirBoxDataGetResp>(callBack) {
            @Override
            protected AirBoxDataGetResp doRequestData(ManagedChannel channel) {
                AirBoxDataGetReq message = AirBoxDataGetReq.newBuilder()
                        .setHostSn(host_sn)
                        .build();

                AirBoxDataGetResp response = null;
                try {
                    response = getZiGuangStub(channel).airBoxDataGet(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 获取所有场景列表
     *
     * @param host_sn 主机SN
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, SceneListResp> sceneList(final String host_sn, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, SceneListResp>(callBack) {
            @Override
            protected SceneListResp doRequestData(ManagedChannel channel) {
                SceneListReq message = SceneListReq.newBuilder()
                        .setHostSn(host_sn)
                        .build();

                SceneListResp response = null;
                try {
                    response = getZiGuangStub(channel).sceneList(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 获取场景详情
     *
     * @param host_sn  主机SN
     * @param scene_id 场景id
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, SceneGetResp> sceneGet(final String host_sn, final String scene_id, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, SceneGetResp>(callBack) {
            @Override
            protected SceneGetResp doRequestData(ManagedChannel channel) {
                SceneGetReq message = SceneGetReq.newBuilder()
                        .setHostSn(host_sn)
                        .setSceneId(scene_id)
                        .build();

                SceneGetResp response = null;
                try {
                    response = getZiGuangStub(channel).sceneGet(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 添加普通场景
     *
     * @param host_sn    主机SN
     * @param scene_name 场景id
     * @param icon_sign  场景图标
     * @param dev_list   设备动作列表
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, UnisiotResp> sceneAdd(final String host_sn, final String scene_name, final String icon_sign, final ArrayList<DeviceOrderDto> dev_list, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                SceneAddReq message = SceneAddReq.newBuilder()
                        .setHostSn(host_sn)
                        .setSceneName(scene_name)
                        .setIconSign(icon_sign)
                        .addAllDevList(dev_list)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).sceneAdd(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 修改普通场景
     *
     * @param host_sn    主机SN
     * @param scene_name 场景名称
     * @param scene_id   场景id
     * @param area_id    设备类型
     * @param icon_sign  设备型号
     * @param dev_list   设备通道
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, UnisiotResp> sceneEdit(final String host_sn, final String scene_name, final String scene_id, final String area_id, final String icon_sign, final ArrayList<DeviceOrderDto> dev_list, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                SceneEditReq message = SceneEditReq.newBuilder()
                        .setHostSn(host_sn)
                        .setSceneName(scene_name)
                        .setSceneId(scene_id)
                        .setAreaId(area_id)
                        .setIconSign(icon_sign)
                        .addAllDevList(dev_list)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).sceneEdit(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 删除普通场景
     *
     * @param host_sn  主机SN
     * @param scene_id 场景id
     * @return callBack返回值
     */
    public  GrpcAsyncTask<String, Void, UnisiotResp> sceneDel(final String host_sn, final String scene_id, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                SceneDelReq message = SceneDelReq.newBuilder()
                        .setHostSn(host_sn)
                        .setSceneId(scene_id)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).sceneDel(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 控制场景
     *
     * @param host_sn  主机SN
     * @param scene_id 场景id
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, UnisiotResp> sceneCom(final String host_sn, final String scene_id, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                SceneComReq message = SceneComReq.newBuilder()
                        .setHostSn(host_sn)
                        .setSceneId(scene_id)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).sceneCom(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }

    /**
     * 修改场景名称
     *
     * @param host_sn    主机SN
     * @param scene_id   场景id
     * @param scene_name 场景名称
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, UnisiotResp> sceneEditName(final String host_sn, final String scene_name, final String scene_id, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                SceneEditNameReq message = SceneEditNameReq.newBuilder()
                        .setHostSn(host_sn)
                        .setSceneName(scene_name)
                        .setSceneId(scene_id)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).sceneEditName(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 修改场景图标
     *
     * @param host_sn   主机SN
     * @param scene_id  场景id
     * @param icon_sign 场景图标
     * @return callBack返回值
     */
    public  GrpcAsyncTask<String, Void, UnisiotResp> sceneEditIcon(final String host_sn, final String icon_sign, final String scene_id, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                SceneEditIconReq message = SceneEditIconReq.newBuilder()
                        .setHostSn(host_sn)
                        .setIconSign(icon_sign)
                        .setSceneId(scene_id)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).sceneEditIcon(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 场景添加设备动作
     *
     * @param host_sn  主机SN
     * @param scene_id 场景id
     * @param dev_list 设备动作列表
     * @return callBack返回值
     */
    public  GrpcAsyncTask<String, Void, UnisiotResp> sceneAddDevList(final String host_sn, final String scene_id, final ArrayList<DeviceOrderDto> dev_list, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                SceneAddDevListReq message = SceneAddDevListReq.newBuilder()
                        .setHostSn(host_sn)
                        .addAllDevList(dev_list)
                        .setSceneId(scene_id)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).sceneAddDevList(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 场景修改设备动作
     *
     * @param host_sn  主机SN
     * @param scene_id 场景id
     * @param dev_list 设备动作列表
     * @return callBack返回值
     */
    public  GrpcAsyncTask<String, Void, UnisiotResp> sceneEditDevList(final String host_sn, final String scene_id, final ArrayList<DeviceOrderDto> dev_list, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                SceneEditDevListReq message = SceneEditDevListReq.newBuilder()
                        .setHostSn(host_sn)
                        .addAllDevList(dev_list)
                        .setSceneId(scene_id)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).sceneEditDevList(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 场景删除设备动作
     *
     * @param host_sn  主机SN
     * @param scene_id 场景id
     * @param link_id  关联id
     * @return callBack返回值
     */
    public  GrpcAsyncTask<String, Void, UnisiotResp> sceneDelDevList(final String host_sn, final String scene_id, final String link_id, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, UnisiotResp>(callBack) {
            @Override
            protected UnisiotResp doRequestData(ManagedChannel channel) {
                SceneDelDevListReq message = SceneDelDevListReq.newBuilder()
                        .setHostSn(host_sn)
                        .setLinkId(link_id)
                        .setSceneId(scene_id)
                        .build();

                UnisiotResp response = null;
                try {
                    response = getZiGuangStub(channel).sceneDelDevList(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 系统布防
     *
     * @param host_sn 主机SN
     * @return callBack返回值
     */
    public  GrpcAsyncTask<String, Void, HostSysProtectionResp> hostSysProtection(final String host_sn, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, HostSysProtectionResp>(callBack) {
            @Override
            protected HostSysProtectionResp doRequestData(ManagedChannel channel) {
                HostSysProtectionReq message = HostSysProtectionReq.newBuilder()
                        .setHostSn(host_sn)
                        .build();

                HostSysProtectionResp response = null;
                try {
                    response = getZiGuangStub(channel).hostSysProtection(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }

    /**
     * 系统撤防
     *
     * @param host_sn 主机SN
     * @return callBack返回值
     */
    public  GrpcAsyncTask<String, Void, HostSysDisarmResp> hostSysDisarm(final String host_sn, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, HostSysDisarmResp>(callBack) {
            @Override
            protected HostSysDisarmResp doRequestData(ManagedChannel channel) {
                HostSysDisarmReq message = HostSysDisarmReq.newBuilder()
                        .setHostSn(host_sn)
                        .build();

                HostSysDisarmResp response = null;
                try {
                    response = getZiGuangStub(channel).hostSysDisarm(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }

    /**
     * 获取场景面板详情
     *
     * @param host_sn 主机SN
     * @param uuid    设备Id
     * @return callBack返回值
     */
    public  GrpcAsyncTask<String, Void, ScenePannelGetResp> scenePannelGet(final String host_sn, final String uuid, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, ScenePannelGetResp>(callBack) {
            @Override
            protected ScenePannelGetResp doRequestData(ManagedChannel channel) {
                ScenePannelGetReq message = ScenePannelGetReq.newBuilder()
                        .setHostSn(host_sn)
                        .setUuid(uuid)
                        .build();

                ScenePannelGetResp response = null;
                try {
                    response = getZiGuangStub(channel).scenePannelGet(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 场景面板绑定场景
     *
     * @param host_sn 主机SN
     * @return callBack返回值
     */
    public  GrpcAsyncTask<String, Void, ScenePannelBindResp> scenePannelBind(final String host_sn, final String category, final String model, final String channelId, final String uuid, final String key_num, final String scene_id, final String scene_name, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, ScenePannelBindResp>(callBack) {
            @Override
            protected ScenePannelBindResp doRequestData(ManagedChannel channel) {
                ScenePannelBindReq message = ScenePannelBindReq.newBuilder()
                        .setHostSn(host_sn)
                        .setCategory(category)
                        .setModel(model)
                        .setChannel(channelId)
                        .setUuid(uuid)
                        .setKeyNum(key_num)
                        .setSceneId(scene_id)
                        .setSceneName(scene_name)
                        .build();

                ScenePannelBindResp response = null;
                try {
                    response = getZiGuangStub(channel).scenePannelBind(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 场景面板解绑场景
     *
     * @param host_sn 主机SN
     * @return callBack返回值
     */
    public  GrpcAsyncTask<String, Void, ScenePannelUnbindResp> scenePannelUnbind(final String host_sn, final String channelId, final String uuid, final String key_num, final String scene_id, final String scene_name, final String linkId, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, ScenePannelUnbindResp>(callBack) {
            @Override
            protected ScenePannelUnbindResp doRequestData(ManagedChannel channel) {
                ScenePannelUnbindReq message = ScenePannelUnbindReq.newBuilder()
                        .setHostSn(host_sn)
                        .setChannel(channelId)
                        .setUuid(uuid)
                        .setKeyNum(key_num)
                        .setSceneId(scene_id)
                        .setSceneName(scene_name)
                        .setLinkId(linkId)
                        .build();

                ScenePannelUnbindResp response = null;
                try {
                    response = getZiGuangStub(channel).scenePannelUnbind(message);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 获取房间下设备列表
     *
     * @param host_sn 主机SN
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AreaDeviceListResp> areaDeviceList(String areaId, final String host_sn, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AreaDeviceListResp>(callBack) {
            @Override
            protected AreaDeviceListResp doRequestData(ManagedChannel channel) {
                AreaDeviceListReq message = AreaDeviceListReq.newBuilder()
                        .setAreaId(areaId)
                        .setHostSn(host_sn)
                        .build();
                Logger.d(message);
                AreaDeviceListResp response = null;
                try {
                    response = getZiGuangStub(channel).areaDeviceList(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e( e, "areaDeviceList");
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }


    /**
     * 获取房间下设备列表
     *
     * @param host_sn 主机SN
     * @return callBack返回值
     */
    public GrpcAsyncTask<String, Void, AreaSceneListResp> areaSceneList(final String areaId, final String host_sn, CallBack callBack) {
        return new GrpcAsyncTask<String, Void, AreaSceneListResp>(callBack) {
            @Override
            protected AreaSceneListResp doRequestData(ManagedChannel channel) {
                AreaSceneListReq message = AreaSceneListReq.newBuilder()
                        .setAreaId(areaId)
                        .setHostSn(host_sn)
                        .build();
                Logger.d(message);
                AreaSceneListResp response = null;
                try {
                    response = getZiGuangStub(channel).areaSceneList(message);
                    Logger.d(response);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                return response;
            }
        }.setHost(ApiConfig.ZG_SERVICE_URL,ApiConfig.ZG_SERVICE_PORT).doExecute();
    }
}
