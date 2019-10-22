package com.soaic.startproject.base

import com.soaic.startproject.BuildConfig

object AppConstants {

    val Debug = BuildConfig.DEBUG
    const val IsReleaseModel = "rc" == BuildConfig.FLAVOR
    const val IsQAModel = "qa" == BuildConfig.FLAVOR
    const val IsStagModel = "stag" == BuildConfig.FLAVOR

    val TAG = "sst-"
    val TEST = "-test-"
    val UsrToken = 0
    val GuestToken = 1

    var ClientId = ""
    var ShippingAddressNeedValidation = false


    object PageId {
        val Exit = -1000
        val Unknow = -1
        val Home = 0
        val Classify = 1
        val Deals = 2
        val Cart = 3
        val Personal = 4
        val OrderList = 5
        val OrderDetail = 6
        val PaymentSelector = 7
        val PaymentResult = 8
        val PushMessage = 10
        val AmountDueOrder = 11
        val OrderPaymentOptions = 12
        val ChangePassword = 13
        val StockReminder = 14
    }

    object Key {
        val RequestCode = "requestCode"
    }

    object Value {
        val UnKnowValue = Integer.MAX_VALUE

        const val DEV_SERVER = "Dev"
        const val STAG_SERVER = "Stag"
        const val QA_SERVER = "QA"
        const val RELEASE_SERVER = "Release"
    }

    object Address {
        val DefaultCountryName = "United States"
        val DefaultCountryCode = "US"
        val ShippingAdress = 0
        val BillingAdress = 1
    }

    object PayPalType {
        val WEB = 0
        val SDK = 1
    }


}
