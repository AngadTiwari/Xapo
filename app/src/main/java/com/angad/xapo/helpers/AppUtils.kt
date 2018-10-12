package com.angad.xapo.helpers

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
/**
 * @author Angad Tiwari
 * @Date 11 Oct 2018
 * @comment Utils class for app
 */
class AppUtils {

    companion object {
        val GITHUB_API_ENDPOINT: String = "https://api.github.com"
        val GITHUB_DATE_FORMAT: String? = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val APP_DATE_FORMAT: String? = "MMM yyyy"
        val MINE_PORTFOLIO_URL: String? = "https://angtwr31.github.io"

        val query = "topic:android+language:java+language:kotlin"
        val sort = "stars"
        val order = "desc"

        /**
         * bold the text
         * @param str: string to make bold
         * @param start: start index
         * @param end: end index
         */
        fun makeTextBold(str:String, start:Int, end:Int):SpannableStringBuilder {
            val sb = SpannableStringBuilder(str)

            val bss = StyleSpan(Typeface.BOLD)
            sb.setSpan(bss, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            return sb
        }
    }
}