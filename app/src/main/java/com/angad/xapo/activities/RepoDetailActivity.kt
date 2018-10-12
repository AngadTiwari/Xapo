package com.angad.xapo.activities

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.angad.xapo.R
import com.angad.xapo.models.RepoEvent
import kotlinx.android.synthetic.main.activity_repo_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import android.content.Intent
import android.view.MenuItem
import android.view.View
import com.angad.xapo.helpers.AppUtils
import kotlinx.android.synthetic.main.content_repo_detail.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Angad Tiwari
 * @Date 11 Oct 2018
 * @comment Detail Activity for github trending android repos
 */
class RepoDetailActivity : AppCompatActivity(), View.OnClickListener {

    private val format: SimpleDateFormat = SimpleDateFormat(AppUtils.GITHUB_DATE_FORMAT, Locale.ENGLISH)
    private val formatToShow: SimpleDateFormat = SimpleDateFormat(AppUtils.APP_DATE_FORMAT, Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        fab.setOnClickListener(this)
        // val repo: Repo? = intent.extras.getParcelable<Repo>("repo") Caused by: java.lang.IllegalArgumentException: Parameter specified as non-null is null: method kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull, parameter mirror_url
    }

    /**
     * onclick handler
     */
    override fun onClick(view: View?) {
        when(view?.id) {
            //clicking to this floating btn, opens mine portfolio
            R.id.fab -> {
                val url = AppUtils.MINE_PORTFOLIO_URL
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }
    }

    /**
     * eventbus handler to fetch the repository data from repo-list screen
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RepoEvent) {
        toolbar_layout.title = event.repo?.name
        header.setImageURI(event.repo?.owner?.avatar_url)

        txt_repo_created_on?.text = AppUtils.makeTextBold(String.format(resources.getString(R.string.label_created_on, formatToShow.format(format.parse(event.repo?.created_at)))), 0, 11)
        txt_repo_updated_on?.text = AppUtils.makeTextBold(String.format(resources.getString(R.string.label_updated_on, formatToShow.format(format.parse(event.repo?.updated_at)))), 0, 11)
        txt_repo_stars?.text = AppUtils.makeTextBold(String.format(resources.getString(R.string.label_stars, event.repo?.stargazers_count)), 0, 6)
        txt_repo_forks?.text = AppUtils.makeTextBold(String.format(resources.getString(R.string.label_forks, event.repo?.forks)), 0, 6)

        txt_description.text = event.repo?.description
        txt_description.append("\n\n"+ event.repo?.license?.name)
        txt_url.text = event.repo?.html_url
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this) //register to eventbus
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this) //unregister to eventbus
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            //On back btn click, close the screen back to repo-list screen
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
