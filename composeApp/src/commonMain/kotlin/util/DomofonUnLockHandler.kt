package util

import domain.repository.DomofonRepository

object DomofonUnLockHandler {
    suspend fun onClickLock(
        deviceId: String,
//        statusDomofonUnlockDoor: MutableStateFlow<Boolean?>,
        domofonRepository: DomofonRepository
    ) : Boolean? {
        val domofonUnlockResponse = domofonRepository.sendOpenDomofon(
            deviceId = deviceId
        )
        return domofonUnlockResponse?.data?.status
    }
}