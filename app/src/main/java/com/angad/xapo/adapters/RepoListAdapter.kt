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
import com.angad.xapo.models.RepoEvent
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class RepoListAdapter(val context: Context, val repos: MutableList<Repo>?): RecyclerView.Adapter<RepoListAdapter.RepoListAdapterViewHolder>() {

    private val format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    private val formatToShow: SimpleDateFormat = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RepoListAdapterViewHolder {
        return RepoListAdapterViewHolder(this, LayoutInflater.from(context).inflate(R.layout.adapter_repos_list, parent, false))
    }

    override fun onBindViewHolder(holder: RepoListAdapterViewHolder, position: Int) {
        val repo: Repo? = repos?.getOrNull(position)

        holder.repo_name?.text = repo?.full_name
        holder.repo_created_on?.text = String.format(context.resources.getString(R.string.label_created_on, formatToShow.format(format.parse(repo?.created_at))))
        holder.repo_updated_on?.text = String.format(context.resources.getString(R.string.label_updated_on, formatToShow.format(format.parse(repo?.updated_at))))
        holder.repo_stars?.text = String.format(context.resources.getString(R.string.label_stars, repo?.stargazers_count))
        holder.repo_forks?.text = String.format(context.resources.getString(R.string.label_forks, repo?.forks))

        holder.repo_avatar?.setImageURI(repo?.owner?.avatar_url)
    }

    override fun getItemCount(): Int {
        if(repos == null)
            return 0
        return repos.size
    }

    class RepoListAdapterViewHolder(adapter: RepoListAdapter, view: View): RecyclerView.ViewHolder(view) {
        val repo_name = view.txt_repo_name
        val repo_created_on = view.txt_repo_created_on
        val repo_updated_on = view.txt_repo_updated_on
        val repo_stars = view.txt_repo_stars
        val repo_forks = view.txt_repo_forks
        val repo_avatar = view.img_repo_avatar
        val card_repo = view.card_repo

        init {
            card_repo.setOnClickListener(View.OnClickListener {
                val intent: Intent = Intent(adapter.context, RepoDetailActivity::class.java)
                adapter.context.startActivity(intent)

                card_repo.postDelayed(Runnable {
                    EventBus.getDefault().post(RepoEvent(repo = adapter.repos?.get(adapterPosition)))
                }, 600)
            })
        }
    }
}