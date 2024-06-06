package domain.repository

import domain.model.user_info.UserInfo

interface UserInfoRepository {

    suspend fun getUserInfo() : UserInfo
}