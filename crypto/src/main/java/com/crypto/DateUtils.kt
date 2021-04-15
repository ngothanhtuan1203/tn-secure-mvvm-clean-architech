package com.crypto

import com.tnsecure.logs.TNLog
import java.text.SimpleDateFormat
import java.util.*

internal class DateUtils {
    companion object {

        const val OUTPUT_DATE_FORMATE = "yyyy/MM/dd HH:mm:ss"
        
        fun localToGMT(): Date {
            val date = Date()
            val sdf =
                SimpleDateFormat(OUTPUT_DATE_FORMATE)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            return Date(sdf.format(date))
        }

        /**
         * get Date String follow format: yyyy/MM/dd HH:mm:ss
         * @return
         */
        fun getDatetimeUTCFormat(): String {
            val utcTime: Date = localToGMT()
            TNLog.d("TAG",
                "getDateFromUTCTimestamp: utcTime: $utcTime"
            )
            val time: String = getDateFromUTCTimestamp(
                OUTPUT_DATE_FORMATE,
                utcTime
            )
            return time.replace("/", "").replace(":", "").replace(" ", "")
        }

        fun getDateFromUTCTimestamp(
            mDateFormate: String,
            utcTime: Date
        ): String {
            var date: String? = null
            try {
                TNLog.d(
                    "TAG",
                    "getDateFromUTCTimestamp value: $utcTime"
                )
                val dateFormatter =
                    SimpleDateFormat(mDateFormate)
                date = dateFormatter.format(utcTime)
                TNLog.d(
                    "TAG",
                    "getDateFromUTCTimestamp dateFormatter with value: $date"
                )
                return date.replace("-", "").replace(":", "").replace(" ", "") + "UTC"
            } catch (e: Exception) {
                e.message?.let { TNLog.e("TAG", it) }
            }
            return date!!.replace("-", "").replace(":", "").replace(" ", "") + "UTC"
        }
    }
}