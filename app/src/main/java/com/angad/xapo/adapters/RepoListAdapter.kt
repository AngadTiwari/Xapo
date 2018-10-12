package com.angad.xapo.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.angad.xapo.R
import com.angad.xapo.models.Repo
import kotlinx.android.synthetic.main.adapter_repos_list.view.*
import android.content.Intent
import com.angad.xapo.activities.RepoDetailActivity
import com.angad.xapo.helpers.AppUtils
import com.angad.xapo.models.RepoEvent
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Angad Tiwari
 * @Date 11 Oct 2018
 * @comment Repo list Adapter for github trending android repos
 */
class RepoListAdapter(val context: Context, val repos: MutableList<Repo>?): RecyclerView.Adapter<RepoListAdapter.RepoListAdapterViewHolder>() {

    private val format: SimpleDateFormat = SimpleDateFormat(AppUtils.GITHUB_DATE_FORMAT, Locale.ENGLISH)
    private val formatToShow: SimpleDateFormat = SimpleDateFormat(AppUtils.APP_DATE_FORMAT, Locale.ENGLISH)

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RepoListAdapterViewHolder {
        return RepoListAdapterViewHolder(this, LayoutInflater.from(context).inflate(R.layout.adapter_repos_list, parent, false))
    }

    /**
     * bind the data to the view
     */
    override fun onBindViewHolder(holder: RepoListAdapterViewHolder, position: Int) {
        val repo: Repo? = repos?.getOrNull(position)

        holder.repo_name?.text = repo?.full_name
        holder.repo_created_on?.text = AppUtils.makeTextBold(String.format(context.resources.getString(R.string.label_created_on, formatToShow.format(format.parse(repo?.created_at)))), 0, 11)
        holder.repo_updated_on?.text = AppUtils.makeTextBold(String.format(context.resources.getString(R.string.label_updated_on, formatToShow.format(format.parse(repo?.updated_at)))), 0, 11)
        holder.repo_stars?.text = AppUtils.makeTextBold(String.format(context.resources.getString(R.string.label_stars, repo?.stargazers_count)), 0, 6)
        holder.repo_forks?.text = AppUtils.makeTextBold(String.format(context.resources.getString(R.string.label_forks, repo?.forks)), 0, 6)

        holder.repo_avatar?.setImageURI(repo?.owner?.avatar_url)
    }

    override fun getItemCount(): Int {
        if(repos == null)
            return 0
        return repos.size
    }

    /**
     * adapter's viewholder to initialize views & bind the listeners
     */
    class RepoListAdapterViewHolder(adapter: RepoListAdapter, view: View): RecyclerView.ViewHolder(view) {
        val repo_name = view.txt_repo_name
        val repo_created_on = view.txt_repo_created_on
        val repo_updated_on = view.txt_repo_updated_on
        val repo_stars = view.txt_repo_stars
        val repo_forks = view.txt_repo_forks
        val repo_avatar = view.img_repo_avatar
        val card_repo = view.card_repo

        init {
            // opens the repos detail screen on list item click
            card_repo.setOnClickListener(View.OnClickListener {
                val intent: Intent = Intent(adapter.context, RepoDetailActivity::class.java)
                adapter.context.startActivity(intent)

                // sending the repo data with a-bit delay, while the detail screen will register to eventbus
                card_repo.postDelayed(Runnable {
                    EventBus.getDefault().post(RepoEvent(repo = adapter.repos?.get(adapterPosition)))
                }, 600)
            })
        }
    }
}