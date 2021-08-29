package com.omnilog.eurosport.utils

import android.content.Context
import com.omnilog.eurosport.R
import com.omnilog.eurosport.utils.Utils.DAY
import com.omnilog.eurosport.utils.Utils.HOUR
import com.omnilog.eurosport.utils.Utils.MIN
import com.omnilog.eurosport.utils.Utils.dd_MMMM_yyyy
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object Utils {
    const val dd_MMMM_yyyy = "dd MMMM yyyy"
    const val DAY = 1000 * 60 * 60 * 24
    const val HOUR = 1000 * 60 * 60
    const val MIN = 1000 * 60
}

fun Float.toDateTime(context: Context): String {
    val diff = Date().time - this * 1000
    return if (diff > DAY) {
        (this*1000).toddMMMMyyyyDate()
    } else {
        diff.tohhmmTime(context)
    }
}

fun Float.tohhmmTime(context: Context) = if (this > HOUR) {
    // xx h ago
    context.getString(R.string.hago, (this / HOUR).roundToInt())
} else {
    // xx min ago
    context.getString(R.string.mago, (this / MIN).roundToInt())
}

fun Float.toddMMMMyyyyDate() = SimpleDateFormat(dd_MMMM_yyyy).format(this)

fun <T> List<T>.mix(list2: List<T>): List<T> {
    val first = iterator()
    val second = list2.iterator()
    val list = ArrayList<T>(this.size + list2.size)
    while (first.hasNext() || second.hasNext()) {
        if (first.hasNext()) {
            list.add(first.next())
        }
        if (second.hasNext()) {
            list.add(second.next())
        }
    }
    return list
}