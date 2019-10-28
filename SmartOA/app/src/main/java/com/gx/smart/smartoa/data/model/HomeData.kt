package com.gx.smart.smartoa.data.model

/**
 *@author xiaosy
 *@create 2019-10-25
 *@Describe
 **/
data class HomeBanner(
    val imageResId: List<Int>
)

data class HomeActionRecommend(
    val imageResId: Int,
    val title: String,
    val time: String,
    val number: Int
)


data class HomeCompanyAdvise(
    val imageResId: Int,
    val title: String,
    val time: String
)