package com.example.movie_app.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie_app.R
import com.example.movie_app.model.Movie
import com.example.movie_app.network.NetworkState
import com.example.movie_app.network.POSTER_BASE_URL


class PopularMvoiePagedListAdapter(public val context: Context): PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if(viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return MovieItemViewHolder(view)
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE) (holder as MovieItemViewHolder).bind(getItem(position), context)
        else (holder as NetworkStateItemViewHolder).bind(networkState)
    }

    private fun hasExtraRox(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRox()) 1 else 0
    }


    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRox() && position == itemCount -1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }


    class MovieDiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {
        private val title = view?.findViewById<TextView>(R.id.cv_movie_title)
        private val releaseDate = view?.findViewById<TextView>(R.id.cv_movie_release_date)
        private val posterImage = view?.findViewById<ImageView>(R.id.cv_movie_poster)
        fun bind(movie: Movie?, context: Context) {

            title?.text = movie?.title
            releaseDate?.text = movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context).load(moviePosterURL).into(posterImage!!)

            itemView.setOnClickListener {
                val intent = Intent(context, DetailMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {
        private val progressBar = view?.findViewById<TextView>(R.id.progress_bar)
        private val errorMsg = view?.findViewById<TextView>(R.id.error_msg)
        fun bind(networkState: NetworkState?) {
            if(networkState != null && networkState == NetworkState.LOADING) progressBar?.visibility = View.VISIBLE
            else progressBar?.visibility = View.GONE

            if(networkState != null && networkState == NetworkState.ERROR) {
                errorMsg?.visibility = View.VISIBLE
                errorMsg?.text = networkState.msg
            } else if (networkState != null && networkState == NetworkState.ENDLIST) {
                errorMsg?.visibility = View.VISIBLE
                errorMsg?.text = networkState.msg
            } else {
                errorMsg?.visibility = View.GONE
            }
        }
    }


    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRox()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRox()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }

    }
}