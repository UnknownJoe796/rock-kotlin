package com.ivieleague.mega.old

/**
 *
 * Created by josep on 2/25/2017.
 */
class RealizationException(val realization: CallRealization, message: String = "?", cause: Throwable? = null) : Exception(realization.debugInfo() + ": " + message, cause) {
}