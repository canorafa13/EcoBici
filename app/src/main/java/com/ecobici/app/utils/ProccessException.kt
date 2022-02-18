package com.ecobici.app.utils

object ProccessException {
    fun getError(e: Exception): String{
        return try{
            getCause(e)
        }catch (ex: Exception){
            try {
                getMessage(e)
            }catch (exc: Exception){
                ""
            }
        }
    }

    @Throws(Exception::class)
    private fun getCause(e: Exception): String{
        return e.cause.toString()
    }

    @Throws(Exception::class)
    private fun getMessage(e: Exception): String{
        return e.message.toString()
    }
}