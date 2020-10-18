package radikz.id

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        popularMovies = findViewById(R.id.popular_movies)
        popularMovies.layoutManager = LinearLayoutManager(this)
        popularMoviesAdapter = MoviesAdapter(listOf())
        popularMovies.adapter = popularMoviesAdapter

        progressBar = findViewById(R.id.progress_movies)

        MoviesRepository.getPopularMovies(
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
        popularMoviesAdapter.updateMovies(movies)
    }
}