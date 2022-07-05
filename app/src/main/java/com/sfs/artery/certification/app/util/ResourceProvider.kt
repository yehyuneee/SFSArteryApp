package com.sfs.artery.certification.app.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ResourceProvider {
    fun getContext(): Context
}

class ResourceProviderImpl @Inject constructor
    (@ApplicationContext private val context: Context) :
    ResourceProvider {
    override fun getContext(): Context = context
}