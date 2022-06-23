package com.sfs.artery.certification.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import com.sfs.artery.certification.app.viewmodel.BaseViewModel

abstract class BaseActivity<DataBiding : ViewDataBinding, model : BaseViewModel>
    : AppCompatActivity(), LifecycleObserver {

    private var _binding: DataBiding? = null
    val viewBinding get() = _binding

    abstract val layoutId: Int
    abstract val variable: Int
    abstract val viewModel: model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<DataBiding>(this, layoutId).apply {
            lifecycleOwner = this@BaseActivity
            setVariable(variable, viewModel)
            _binding = this
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.run {
            viewModel.clearDisposable()
        }
    }
}