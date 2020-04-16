package com.gx.smart.lib.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;


/**
 * 基础fragment
 */

public abstract class BaseMVVMRefreshFragment<T extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment
        implements OnRefreshListener, OnLoadMoreListener {

    public static final int PAGE_SIZE = 10;
    protected VM viewModel;
    protected T binding;
    protected int currentPage = 1;
    private RefreshLayout refreshLayout;
    private View noDataView;
    private RecyclerView recyclerView;
    private BaseAdapter adapter;
    private boolean isBackRefresh = false;//返回刷新
    private boolean isBack = false;//返回刷新

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(onBindViewModel());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = onBindViewBinding(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initContent();
        initData();
        initObserver();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isBackRefresh && isBack) {
            refreshLayout.autoRefresh();
            isBack = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isBack = true;
    }

    @Deprecated
    @Override
    public int onBindLayout() {
        return 0;
    }

    protected abstract Class<VM> onBindViewModel();

    protected abstract T onBindViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent);

    /**
     * 使用viewBinding
     */
    protected void initView(RecyclerView recyclerView, BaseAdapter adapter, RefreshLayout refreshLayout, View noDataView) {
        this.refreshLayout = refreshLayout;
        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setOnLoadMoreListener(this);
        this.noDataView = noDataView;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化内容
     */
    @Override
    public void initContent() {

    }

    /**
     * 网络请求数据
     */
    @Override
    public void initData() {
    }


    protected void setBackRefresh() {
        isBackRefresh = true;
    }

    /**
     * 获取viewModel中的数据
     */
    public void initObserver() {
        viewModel.getError().observe(getViewLifecycleOwner(), result -> {
            if (result) {
                handleErrorResult();
            }
        });
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        currentPage = 1;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
    }

    /**
     * 处理请求返回后统一的分页和加载更多处理
     *
     * @param list
     */
    protected void handleResult(List list) {
        if (refreshLayout == null || noDataView == null) {
            return;
        }

        if (currentPage == 1 || currentPage == 0) {
            refreshLayout.finishRefresh();
        }

        if (list.isEmpty() && (currentPage == 1 || currentPage == 0)) {
            noDataView.setVisibility(View.VISIBLE);
            refreshLayout.setEnableLoadMore(false);
            return;
        }
        if (list.size() < PAGE_SIZE) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.finishLoadMore();
            refreshLayout.setEnableLoadMore(true);
        }

        if (noDataView.getVisibility() == View.VISIBLE) {
            noDataView.setVisibility(View.GONE);
        }

        if (currentPage == 1 || currentPage == 0) {
            adapter.clear();
        }
        adapter.addAll(list);
    }


    private void handleErrorResult() {
        if (refreshLayout == null || noDataView == null) {
            return;
        }
        refreshLayout.finishLoadMore();
        noDataView.setVisibility(View.VISIBLE);
    }
}
