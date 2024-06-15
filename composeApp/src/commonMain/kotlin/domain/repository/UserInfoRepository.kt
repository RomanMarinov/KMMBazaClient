package domain.repository

import domain.model.auth.firebase.FirebaseRequestBody
import domain.model.auth.firebase.FirebaseRequestBodyTEST
import domain.model.user_info.UserInfo

interface UserInfoRepository {

    suspend fun getUserInfo() : UserInfo

    // потом убрать
    suspend fun sendSelfPush(body: FirebaseRequestBodyTEST)
}