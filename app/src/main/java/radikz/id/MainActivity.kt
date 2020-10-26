package radikz.id

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import radikz.id.adapter.MoviesAdapter
import radikz.id.model.Movie
import radikz.id.repository.MoviesRepository

class MainActivity : AppCompatActivity() {
    private lateinit var popularMovies: RecyclerView
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager
    private lateinit var textPage: TextView

    private var popularMoviesPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        popularMovies = findViewById(R.id.popular_movies)
        popularMoviesLayoutMgr = LinearLayoutManager(this)
        popularMovies.layoutManager = popularMoviesLayoutMgr
        popularMoviesAdapter = MoviesAdapter(mutableListOf(), { movie -> showMovieDetails(movie) })
        popularMovies.adapter = popularMoviesAdapter

        textPage = findViewById(R.id.pagination_movies)
        progressBar = findViewById(R.id.progress_movies)

        getPopularMovies()
    }

    private fun getPopularMovies(){
        MoviesRepository.getPopularMovies(
            popularMoviesPage,
            onSuccess = { movies ->
                onPopularMoviesFetched(movies)
                progressBar.setVisibility(View.GONE)

            },
            onError = {
                Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
                progressBar.setVisibility(View.GONE)
            }
        )

    }

    private fun onPopularMoviesFetched(movies: List<Movie>) {
        popularMoviesAdapter.appendMovies(movies)
        attachPopularMoviesOnScrollListener()
    }

    private fun attachPopularMoviesOnScrollListener() {
        popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayoutMgr.itemCount
                val visibleItemCount = popularMoviesLayoutMgr.childCount
                val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()
                val page = firstVisibleItem / 20 + 1
                textPage.setText(getString(R.string.pagination_movies).plus(page))

                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    popularMovies.removeOnScrollListener(this)
                    popularMoviesPage++
                    getPopularMovies()
                }
            }
        })
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetail::class.java)
        intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
        intent.putExtra(MOVIE_POSTER, movie.posterPath)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RATING, movie.rating)
        intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
        intent.putExtra(MOVIE_OVERVIEW, movie.overview)
        startActivity(intent)
    }
}