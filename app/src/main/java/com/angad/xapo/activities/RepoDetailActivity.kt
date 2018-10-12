package com.angad.xapo.activities

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.angad.xapo.R
import com.angad.xapo.models.RepoEvent
import kotlinx.android.synthetic.main.activity_repo_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import android.content.Intent
import android.view.MenuItem
import kotlinx.android.synthetic.main.content_repo_detail.*
import java.text.SimpleDateFormat
import java.util.*


class RepoDetailActivity : AppCompatActivity() {
    private val format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    private val formatToShow: SimpleDateFormat = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        fab.setOnClickListener { view ->
            val url = "https://angtwr31.github.io"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        // val repo: Repo? = intent.extras.getParcelable<Repo>("repo") Caused by: java.lang.IllegalArgumentException: Parameter specified as non-null is null: method kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull, parameter mirror_url
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RepoEvent) {
        toolbar_layout.title = event.repo?.name
        header.setImageURI(event.repo?.owner?.avatar_url)
        txt_description.text = event.repo?.description + "\n\n"+ event.repo?.html_url + "\n\n"+ event.repo?.license?.name

        txt_repo_created_on?.text = String.format(resources.getString(R.string.label_created_on, formatToShow.format(format.parse(event.repo?.created_at))))
        txt_repo_updated_on?.text = String.format(resources.getString(R.string.label_updated_on, formatToShow.format(format.parse(event.repo?.updated_at))))
        txt_repo_stars?.text = String.format(resources.getString(R.string.label_stars, event.repo?.stargazers_count))
        txt_repo_forks?.text = String.format(resources.getString(R.string.label_forks, event.repo?.forks))
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
