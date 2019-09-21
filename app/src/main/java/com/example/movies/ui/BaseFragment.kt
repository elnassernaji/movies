package com.example.movies.ui

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.movies.MainActivity
import com.example.movies.data.MoviesViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment: Fragment() {
    protected lateinit var viewModel: MoviesViewModel

    protected val disposables = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = (context as MainActivity).viewModel
    }

    override fun onPause() {
        super.onPause()
        if (activity?.isFinishing == true) {
            disposables.clear()
        }
    }

}