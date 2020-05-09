package com.nyan.iso3pinblock

import android.content.Context
import com.nyan.iso3pinblock.views.MainContract
import com.nyan.iso3pinblock.views.MainPresenter

interface ServiceLocator {

    fun getMainPresenter(view: MainContract.View): MainContract.Presenter

    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator()
                }
                return instance!!
            }
        }


    }

}

open class DefaultServiceLocator() : ServiceLocator {
    override fun getMainPresenter(view: MainContract.View): MainContract.Presenter {
        return MainPresenter(view);
    }

}