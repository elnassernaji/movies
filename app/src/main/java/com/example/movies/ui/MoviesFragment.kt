package com.example.movies.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.Binder

import com.example.movies.R
import com.example.movies.find
import com.example.movies.model.Request
import com.example.movies.model.ResultsItem
import com.squareup.picasso.Picasso

class MoviesFragment : BaseFragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: Adapter

    private lateinit var callback: Callback

    interface Callback {
        fun onShowMovieDetails(movieId: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Callback) {
            callback = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view)
            initRecyclerView()
            loadData()
        }
    }

    private fun loadData() {
        disposables.add(viewModel.getMovies(Request(url = "https://api.themoviedb.org/3/movie/popular", params = hashMapOf(
            Pair("page", "1"),
            Pair("language", "en")
        ))).subscribe({
            adapter.update(it.results?.filterNotNull()?: listOf())
        }, {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
        }))
    }

    private fun initRecyclerView() {
        adapter = Adapter(listOf()) {
            callback.onShowMovieDetails(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private class Adapter(private var items: List<ResultsItem>, private val cb: (Int) -> Unit): RecyclerView.Adapter<Adapter.ViewHolder>() {

        fun update(items: List<ResultsItem>) {
            this.items = items
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_movie_row, parent, false))

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), Binder<ResultsItem>, View.OnClickListener {

            init {
                itemView.setOnClickListener(this)
            }

            override fun bind(item: ResultsItem) {
                with(item) {
                    itemView.apply {
                        Picasso.with(context)
                            .load("https://image.tmdb.org/t/p/w500/${posterPath}")
                            .into(find<ImageView>(R.id.poster_img))

                        find<TextView>(R.id.release_date).text = releaseDate
                        find<TextView>(R.id.title).text = title
                        find<TextView>(R.id.description).text = overview
                        find<TextView>(R.id.rating).text = voteAverage?.toString()
                    }
                }
            }

            override fun onClick(p0: View?) {
                cb(items[adapterPosition].id?: 0)
            }
        }
    }

    companion object {
        val TAG: String = MoviesFragment::class.java.simpleName
    }
}
