package com.angad.xapo.activities

import android.app.PendingIntent.getActivity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.angad.xapo.AppController
import com.angad.xapo.R
import com.angad.xapo.adapters.RepoListAdapter
import com.angad.xapo.helpers.AppUtils
import com.angad.xapo.models.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_repo_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author Angad Tiwari
 * @Date 11 Oct 2018
 * @comment Repo list Activity for github trending android repos
 */
class MainActivity : AppCompatActivity() {

    private var repos_adapter: RepoListAdapter? = null
    private var repos: MutableList<Repo>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        fetchRepos()
    }

    /**
     * fetch the trending github android repos via github v3 api
     */
    private fun fetchRepos() {
        AppController.service?.getAndroidTrendingRepos(AppUtils.query, AppUtils.sort, AppUtils.order)?.enqueue(object: Callback<AndroidTrendingRepo> {
            override fun onResponse(call: Call<AndroidTrendingRepo>?, response: Response<AndroidTrendingRepo>?) {
                when(response?.code()) {
                    200 -> {
                        response.body()?.items?.let {
                            repos?.clear()
                            repos = response.body()?.items?.toMutableList()

                            repos_adapter = RepoListAdapter(this@MainActivity, repos)
                            recycler_repos.adapter = repos_adapter
                        }
                    }
                    else -> {
                        Toast.makeText(this@MainActivity, "Error while fetching android trending repos", Toast.LENGTH_LONG).show()
                    }
                }
                layout_progress.visibility = View.GONE
            }

            override fun onFailure(call: Call<AndroidTrendingRepo>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Error while fetching android trending repos", Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * initializing the views
     */
    private fun initView() {
        recycler_repos.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RepoEvent) {

    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this) //register to eventbus
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this) //unregister to eventbus
    }
}
