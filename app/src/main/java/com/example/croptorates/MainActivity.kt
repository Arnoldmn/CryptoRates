package com.example.croptorates

import android.app.DownloadManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.Toast
import com.example.croptorates.Common.Common
import com.example.croptorates.Interface.ILoadMore
import com.example.croptorates.Interface.RetrofitClient
import com.example.croptorates.adapter.CoinAdapter
import com.example.croptorates.model.CoinModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity(), ILoadMore {

    internal var items: MutableList<CoinModel> = ArrayList()

    internal lateinit var adapter: CoinAdapter

    internal lateinit var client: OkHttpClient

    internal lateinit var request: Request


    override fun onLoadMore() {

        if (items.size <= Common.MAX_COIN_LOAD)
            loadFirst10Coin()
        else
            Toast.makeText(this, "Data max is " + Common.MAX_COIN_LOAD, Toast.LENGTH_SHORT).show()
    }

    private fun loadFirst10Coin() {
         client = OkHttpClient()
         request = Request.Builder()
             .url(String.format("https://api.coinmarketcap.com/v1/ticker/?start=0&limit=10"))
             .build()

         swipe_to_refresh.isRefreshing = true
         client.newCall(request)
             .enqueue(object : Callback {
                 override fun onFailure(call: Call, e: IOException) {
                     Log.d("ERROR", e.toString())
                 }

                 override fun onResponse(call: Call, response: Response) {
                     val body = response.body()!!.string()

                     val gson = Gson()

                     items = gson.fromJson(body, object: TypeToken<List<CoinModel>>(){}.type)

                     runOnUiThread{

                         adapter.updateData(items)


                     }
                 }
             })


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe_to_refresh.post { loadFirst10Coin() }

        swipe_to_refresh.setOnRefreshListener {
            items.clear()

            loadFirst10Coin()

            setupAdapter()

        }

        recycler.layoutManager = LinearLayoutManager(this)
        setupAdapter()
    }

    private fun setupAdapter() {
        adapter = CoinAdapter(recycler, this, items)
        recycler.adapter = adapter

        adapter.setLoadMore(this)

    }
}
