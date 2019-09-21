package com.example.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.movies.data.ApiImpl
import com.example.movies.data.DataModel
import com.example.movies.data.MoviesViewModel
import com.example.movies.ui.MovieDetailsFragment
import com.example.movies.ui.MoviesFragment
import com.google.gson.Gson
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity(), MoviesFragment.Callback {
    lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()

        val frag = savedInstanceState?.let {
            supportFragmentManager.findFragmentByTag(MoviesFragment.TAG)
        }?: MoviesFragment()

        showFragment(frag, MoviesFragment.TAG)
    }

    override fun onShowMovieDetails(movieId: Int) {
        showFragment(MovieDetailsFragment.newInstance(movieId), MovieDetailsFragment.TAG, true)
    }

    private fun initViewModel() {
        viewModel = MoviesViewModel(DataModel(ApiImpl(this, OkHttpClient()), Gson()))
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else super.onBackPressed()
    }

    private fun showFragment(fragment: Fragment, tag: String, addToBackStack: Boolean = false) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .apply { if(addToBackStack) addToBackStack(null) }
            .commit()
    }
}
