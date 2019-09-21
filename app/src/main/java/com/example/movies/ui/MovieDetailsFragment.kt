package com.example.movies.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.Binder

import com.example.movies.R
import com.example.movies.find
import com.example.movies.model.MovieDetailResponse
import com.example.movies.model.Request
import com.example.movies.model.ResultsItem
import com.squareup.picasso.Picasso
import java.lang.StringBuilder

class MovieDetailsFragment : BaseFragment() {
    private lateinit var recyclerView: RecyclerView

    private val adapter = Adapter(listOf())

    private var movieId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieId = arguments?.getInt(EXTRA_MOVIE_ID)?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false).apply {
            recyclerView = find(R.id.recycler_view)
            initRecyclerView()
            loadData()
        }
    }

    private fun loadData() {
        disposables.add(viewModel.getMovieDetails(Request("https://api.themoviedb.org/3/movie/${movieId}"))
            .doOnNext { bindData(it) }
            .flatMap { viewModel.getSimilarMovies(Request("https://api.themoviedb.org/3/movie/${movieId}/similar")) }
            .subscribe({
                adapter.update(it.results?.filterNotNull()?: listOf())
            }, {

            }))
    }

    private fun bindData(response: MovieDetailResponse) {
        with(response) {
            Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/${backdropPath}")
                .into(find<ImageView>(R.id.main_img))

            Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/${posterPath}")
                .into(find<ImageView>(R.id.logo))

            find<TextView>(R.id.title)?.text = title

            val sb = StringBuilder()
            genres?.forEachIndexed { index, genresItem ->
                sb.append(genresItem?.name)
                if(index != genres.size - 1) {
                    sb.append(", ")
                }
            }

            find<TextView>(R.id.sub_title)?.text = sb.toString()
            find<TextView>(R.id.bottom_title)?.apply {
                tagline?.let {
                    if(it.isEmpty()) {
                        visibility = View.GONE
                    }
                }
            }
            find<TextView>(R.id.bottom_title)?.text = tagline
            find<TextView>(R.id.rating)?.text = voteAverage?.toString()
            find<TextView>(R.id.overview)?.text = overview
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private class Adapter(private var items: List<ResultsItem>): RecyclerView.Adapter<Adapter.ViewHolder>() {

        fun update(items: List<ResultsItem>) {
            this.items = items
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_similar_row, null, false))

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), Binder<ResultsItem> {
            override fun bind(item: ResultsItem) {
                Picasso.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500/${item.posterPath}")
                    .into(itemView.find<ImageView>(R.id.similar_img))
            }
        }
    }

    companion object {
        val TAG: String = MovieDetailsFragment::class.java.simpleName
        private const val EXTRA_MOVIE_ID = "movie_id"

        fun newInstance(movieId: Int): MovieDetailsFragment {
            return MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_MOVIE_ID, movieId)
                }
            }
        }
    }

}
