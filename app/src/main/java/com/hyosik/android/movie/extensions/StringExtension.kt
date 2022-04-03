package com.hyosik.android.movie.extensions

internal fun String.replaceBlank(remove : String) : String {
    return this.replace(remove, "")
}

internal fun String.replaceMultipleBlank(removeOne:String , removeAnother:String ) : String {
    return this.replace(removeOne, "").replace(removeAnother, "")
}