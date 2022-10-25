package dev.forcecodes.guru.utils.binding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dev.forcecodes.guru.BR
import dev.forcecodes.guru.R
import dev.forcecodes.guru.ui.ReactiveStateViewModel
import dev.forcecodes.guru.ui.UiEvent
import dev.forcecodes.guru.ui.UiState
import dev.forcecodes.guru.utils.extensions.launchWithViewLifecycle
import dev.forcecodes.guru.utils.extensions.setupToolbarNavigateUp

interface StateViewBinding<VB : ViewBinding> {
    fun <S : UiState, E : UiEvent, VM : ReactiveStateViewModel<S, E>>
            withState(viewModel: VM, lambda: VB.(S) -> Unit)
}

inline fun <VB : ViewDataBinding> VB.executeAfter(block: VB.() -> Unit) {
    block(); executePendingBindings()
}

abstract class ViewBindingFragment<VB : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : Fragment(layoutId), StateViewBinding<VB> {

    abstract fun invalidate()
    open fun viewDidLoad(binding: VB?) {
        // hello, r u there??
    }

    override fun <S : UiState, E : UiEvent, VM : ReactiveStateViewModel<S, E>>
            withState(viewModel: VM, lambda: VB.(S) -> Unit) {
        launchWithViewLifecycle {
            view?.let {
                DataBindingUtil.getBinding<VB>(it)?.let { binding ->
                    binding.executeAfter {
                        setVariable(BR.viewmodel, viewModel)
                    }
                    viewModel.state.collect { state ->
                        lambda.invoke(binding, state)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<VB>(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DataBindingUtil.getBinding<VB>(view)
        setupToolbarNavigateUp(view.findViewById(R.id.toolbar) ?: return)
        viewDidLoad(binding)
        invalidate()
    }
}