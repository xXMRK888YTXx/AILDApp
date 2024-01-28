package com.xxmrk888ytxx.aildapp.domain

sealed class RecordException(
    m: String? = null
) : Exception(m) {
    class ImpossibleConnectToServiceException(m: String? = null) : RecordException(m)
    class RemoteException(m:String? = null) : RecordException(m)
}
