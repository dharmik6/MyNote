package com.project.app.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun convertDate(tempDate: String, from: String, to: String, isUTC: Boolean): String {
    val oldFormat = SimpleDateFormat(from, Locale.getDefault())
    if (isUTC) {
        oldFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    val newFormat = SimpleDateFormat(to, Locale.getDefault())
    newFormat.timeZone = TimeZone.getDefault()

    val date: Date
    var str = tempDate

    try {
        date = oldFormat.parse(tempDate) as Date
        str = newFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return str.uppercase()
}

fun convertDateInstance(tempDate: String, from: String, isUTC: Boolean): Date {
    val oldFormat = SimpleDateFormat(from, Locale.ENGLISH)
    if (isUTC) {
        oldFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    var date: Date? = null
    try {
        date = oldFormat.parse(tempDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return date!!
}

fun convertCalendarInstance(tempDate: String, from: String, isUTC: Boolean): Calendar {
    val oldFormat = SimpleDateFormat(from, Locale.ENGLISH)
    if (isUTC) {
        oldFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    val calendar = Calendar.getInstance()
    try {
        val date = oldFormat.parse(tempDate)
        if (date != null) {
            calendar.time = date
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return calendar
}

fun getTodayDate(format: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'z'"): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    val date = Date()
    return formatter.format(date)
}

fun getNextDate(dateString: String, dateFormat: String): String? {
    val format = SimpleDateFormat(dateFormat, Locale.getDefault())
    val date = format.parse(dateString)
    val calendar = Calendar.getInstance()
    calendar.time = date!!
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    return format.format(calendar.time)
}

fun getPreviousDate(dateString: String, dateFormat: String): String? {
    val format = SimpleDateFormat(dateFormat, Locale.getDefault())
    val date = format.parse(dateString)
    val calendar = Calendar.getInstance()
    calendar.time = date!!
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    return format.format(calendar.time)
}

fun convertDateFromCalendarInstance(calendar: Calendar, dateFormat: String): String {
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    return sdf.format(calendar.time)
}

fun convertDateInstanceIntoString(date: Date, dateFormat: String, isUTC: Boolean): String {
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    if (isUTC) {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
    }
    val calendar = Calendar.getInstance()
    calendar.time = date
    return sdf.format(calendar.time)
}

fun getCurrentDate(dateFormat: String, isUTC: Boolean): String {
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    if (isUTC) {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
    }
    val calendar = Calendar.getInstance()
    return sdf.format(calendar.time)
}

fun getAge(dobString: String, dateFormat: String): Int {
    var date: Date? = null
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    try {
        date = sdf.parse(dobString)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val dob = Calendar.getInstance()
    dob.time = date!!
    val today = Calendar.getInstance()
    var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
    if (today[Calendar.DAY_OF_MONTH] < dob[Calendar.DAY_OF_MONTH]) {
        age--
    }
    return age + 1
}

fun showDatePickerDialogForET(context: Context, editText: EditText, dateFormat: String) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context, { _, year, monthOfYear, dayOfMonth ->
            try {

                val date = convertDate(
                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd", dateFormat, false
                )



                editText.setText(date)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis
    datePickerDialog.show()
}

fun showDatePickerDialogForTV(context: Context, editText: TextView, dateFormat: String) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context, { _, year, monthOfYear, dayOfMonth ->
            try {

                val date = convertDate(
                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd", dateFormat, false
                )



                editText.text = date

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis
    datePickerDialog.show()
}

fun showTimePickerDialogForTV(context: Context, editText: EditText) {
    val currentTime = Calendar.getInstance()
    val hour = currentTime.get(Calendar.HOUR_OF_DAY)
    val minute = currentTime.get(Calendar.MINUTE)
    val mTimePicker = TimePickerDialog(
        context, { _, selectedHour, selectedMinute ->

            val date = convertDate(
                "$selectedHour:$selectedMinute", "HH:mm", "hh:mm a", false
            )
            editText.setText(date)
        }, hour, minute, false
    )
    mTimePicker.setTitle("Select Time")
    mTimePicker.show()
}

fun showTimePickerDialogForTV(context: Context, editText: TextView) {
    val currentTime = Calendar.getInstance()
    val hour = currentTime.get(Calendar.HOUR_OF_DAY)
    val minute = currentTime.get(Calendar.MINUTE)
    val mTimePicker = TimePickerDialog(
        context, { _, selectedHour, selectedMinute ->

            val date = convertDate(
                "$selectedHour:$selectedMinute", "HH:mm", "hh:mm a", false
            )
            editText.text = date
        }, hour, minute, false
    )
    mTimePicker.setTitle("Select Time")
    mTimePicker.show()
}

fun getTimeDifference(srcDate1: String, srcDate2: String, dateFormat: String): String {
    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    var date1: Date? = null
    var date2: Date? = null
    try {
        date1 = simpleDateFormat.parse(srcDate1)
        date2 = simpleDateFormat.parse(srcDate2)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    if (date1 != null && date2 != null) {
        val difference = date2.time - date1.time
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
        val min = (difference - (1000 * 60 * 60 * 24 * days).toLong() - (1000 * 60 * 60 * hours).toLong()).toInt() / (1000 * 60)
        val seconds = (difference / 1000 % 60).toInt()
        val strHours = if (hours <= 9) "0$hours" else "" + hours
        val strMinutes = if (min <= 9) "0$min" else "" + min
        val strSeconds = if (seconds <= 9) "0$seconds" else "" + seconds
        Log.e("Time", "getTimeDifference: $strMinutes $strHours")
        return if (hours <= 0) {
            "$strMinutes:$strSeconds"
        } else {
            "$strHours:$strMinutes:$strSeconds"
        }

    }
    return "00:00"
}

fun showBirthDatePickerDialogForText(
    context: Context,
    editText: TextView,
    dateFormat: String,
) {

    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context, { datePicker, year, monthOfYear, dayOfMonth ->
            try {

                val date = convertDate(
                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd", dateFormat, false
                )


                if (getAge(date, dateFormat) >= 18) {
                    editText.text = date
                } else {
                    Toast.makeText(context, "Age should be grater than 18.", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)
    )
    datePickerDialog.datePicker.maxDate = calendar.timeInMillis
    datePickerDialog.show()
}

fun isValidTime(
    srcDate1: String,
    srcDate2: String,
    dateFormat: String,
    isUTC: Boolean,
): Boolean {

    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    if (isUTC) {
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    var date1: Date? = null
    var date2: Date? = null
    try {
        date1 = simpleDateFormat.parse(srcDate1)
        date2 = simpleDateFormat.parse(srcDate2)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    var difference = 0.toLong()
    if (date1 != null && date2 != null) {
        difference = date2.time - date1.time

    }

    return difference > 0
}


fun convertSecondsToTime(time: Int): String {
    val hr = ((time / 3600))
    val min = (((time / 60)) % 60).toInt()
    val sec = (time % 60)


    val hrStr = if (hr > 9) {
        hr.toString()
    } else {
        "0$hr"
    }

    val minStr = if (min > 9) {
        min.toString()
    } else {
        "0$min"
    }

    val secStr = if (sec > 9) {
        sec.toString()
    } else {
        "0$sec"
    }
    return "$hrStr:$minStr:$secStr"
}