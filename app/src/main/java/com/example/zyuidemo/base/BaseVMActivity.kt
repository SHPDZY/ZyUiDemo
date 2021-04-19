package com.example.zyuidemo.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.arouter.launcher.ARouter

abstract class BaseVMActivity<T : ViewDataBinding>(@LayoutRes val layoutId: Int) : StatusBarEnhanceActivity() {

    lateinit var binding: T

    protected fun <T : ViewDataBinding> binding(@LayoutRes resId: Int): T =
            DataBindingUtil.setContentView<T>(this, resId)
                    .apply { }


    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
        binding = binding(layoutId)
        startObserve()
        initView()
        initData()
    }

    fun getLayoutResId(): Int = 0
    open fun startObserve() {
    }

    open fun initView() {
    }

    open fun initData() {
    }
}