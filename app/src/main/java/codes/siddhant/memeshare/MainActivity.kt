package codes.siddhant.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    private var imageUrl: String? = null
    private val imgView: ImageView by lazy {
        findViewById(R.id.memeIv)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme(imgView)
    }

    private fun loadMeme(view: ImageView) {
        progressBar.visibility = View.VISIBLE
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonReq = JsonObjectRequest(
            Request.Method.GET, url, null, { response ->
                imageUrl = response.getString("url")
                Glide.with(this).load(imageUrl).listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(view)
            },
            {
                Log.e("loadImageError", it.message.toString())
            }
        )

        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonReq)
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/pain"
        intent.putExtra(Intent.EXTRA_TEXT, "Meme Alert!\n$imageUrl")
        val chooser = Intent.createChooser(intent, "Share Meme!")
        startActivity(chooser)
    }

    fun nextMeme(view: View) {
        loadMeme(imgView)
    }
}