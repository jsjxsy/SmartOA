package com.gx.smart.smartoa.data.network.api.base;

import android.os.AsyncTask;
import android.os.Build;

import com.gx.smart.smartoa.data.network.ApiConfig;
import com.gx.smart.smartoa.data.network.api.interceptor.WsClientInterceptor;
import com.gx.smart.smartoa.data.network.api.interceptor.WsProvider;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


/**
 * 异步请求封装
 * Created by
 * ouyangyi on 17/8/10.
 */

public abstract class GrpcAsyncTask<Params, Progress, Data> extends AsyncTask<Params, Progress, Data> {
    private String host = ApiConfig.USER_SERVER_URL;
    private String port = ApiConfig.USER_SERVER_PORT;
    private WsProvider PROVIDER = new WsProvider();
    private WsClientInterceptor INTERCEPTOR = new WsClientInterceptor(PROVIDER);

    private ManagedChannel mChannel;
    private CallBack<Data> callBack;

    public GrpcAsyncTask(CallBack<Data> callBack) {
        this.callBack = callBack;
    }

    /**
     * 请求是否结束
     *
     * @param task
     * @return
     */
    public static boolean isFinish(AsyncTask<?, ?, ?> task) {
        return task == null || Status.FINISHED.equals(task.getStatus());
    }

    public CallBack<Data> getCallBack() {
        return callBack;
    }

    public GrpcAsyncTask setHost(String host, String port) {
        this.host = host;
        this.port = port;
        return this;
    }

    public GrpcAsyncTask setPort(String port) {
        this.port = port;
        return this;
    }

    @Override
    protected void onPreExecute() {
        //做一些前提工作
    }

    /**
     * 异步操作
     *
     * @param channel
     * @return
     */
    protected abstract Data doRequestData(ManagedChannel channel);

    @Override
    protected Data doInBackground(Params... params) {
        try {
            mChannel = getChannel();
            return doRequestData(mChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Data result) {
        if (callBack != null) {
            callBack.callBack(result);
        }
        try {
            if (mChannel != null) {
                mChannel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    public GrpcAsyncTask<Params, Progress, Data> doExecute(Params... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            execute(params);
        }
        return this;
    }

    /**
     * 取消任务
     *
     * @param mayInterruptIfRunning
     * @return
     */
    public boolean cancelTask(boolean mayInterruptIfRunning) {
        boolean cancel = cancel(mayInterruptIfRunning);
        callBack = null;
        return cancel;
    }


    /**
     * 获取唯一的连接
     *
     * @return
     */
    private ManagedChannel getChannel() {
        ManagedChannel mChannel = ManagedChannelBuilder.forAddress(host, Integer.parseInt(port))
                .usePlaintext(true)
                .intercept(INTERCEPTOR)
                .build();
        return mChannel;
    }

}
