package com.example.zyuidemo.ui

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.example.zyuidemo.R
import com.example.zyuidemo.base.BaseVMFragment
import com.example.zyuidemo.beans.UserInfoBean
import com.example.zyuidemo.databinding.FragmentMainBinding
import com.example.zyuidemo.router.ILoginService
import com.example.zyuidemo.router.PagePath
import com.example.zyuidemo.router.RouterConstants
import com.example.zyuidemo.router.fragmentPage
import com.example.zyuidemo.vm.MainViewModel
import com.example.zyuidemo.vm.TabInfoBean
import com.google.android.material.tabs.TabLayout

/**
 * @desc: App主页
 */
class MainFragment : BaseVMFragment<FragmentMainBinding>(R.layout.fragment_main) {

    @Autowired
    lateinit var loginService: ILoginService
    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val mTabList = mutableListOf<TabInfoBean>()
    override fun startObserve() {
        mainViewModel.tabInfoList.observe(this, Observer {
            mTabList.clear()
            mTabList.addAll(it)
            binding.mainViewPager.adapter?.notifyDataSetChanged()
        })
    }

    private var userInfo: UserInfoBean? = null
    override fun initView() {
        userInfo = loginService.getUserInfo()
        initViewPager()
    }

    private fun initViewPager() {
        binding.run {
            mainViewPager.offscreenPageLimit = 4
            mainViewPager.adapter = object : FragmentStatePagerAdapter(childFragmentManager, POSITION_NONE) {

                override fun getCount(): Int = mTabList.size

                override fun getItem(position: Int): Fragment {
                    return selectFragment(mTabList[position])
                }

                override fun getPageTitle(position: Int): CharSequence? {
                    return mTabList[position].name
                }
            }
            tabLayout.setupWithViewPager(mainViewPager)
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })

            bmPersonal.setOnClickListener {
                ARouter.getInstance()
                        .fragmentPage(PagePath.PERSONAL_CENTER)
                        .navigation(activity)
            }

            when (userInfo) {
                null -> {
                    bmPublish.isGone = true
                }
                else -> {
                    bmPublish.isVisible = true
                }
            }
            bmPublish.setOnClickListener {
                ARouter.getInstance()
                        .fragmentPage(PagePath.NEVBASE_FRAGMENT_CONTAINER)
                        .withString(RouterConstants.EXTRA_KEY_FRAGMENT_PATH, PagePath.PUBLISH_MESSAGE)
                        .navigation(activity)
            }
        }
    }

    override fun initData() {
        mainViewModel.run {
            getTabList()
        }
    }

    private fun selectFragment(tabInfoBean: TabInfoBean): Fragment {

        return ARouter.getInstance().build(tabInfoBean.routePath).navigation() as Fragment
    }

    override fun onBack(): Boolean {
        return false
    }

}