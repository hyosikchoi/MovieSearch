package com.hyosik.android.movie.extensions

import android.content.Context
import android.widget.Toast

internal fun Context.toastShort(message : String) = Toast.makeText(this , message , Toast.LENGTH_SHORT).show()

internal fun Context.toastLong(message: String) = Toast.makeText(this , message , Toast.LENGTH_LONG).show()