package com.example.savethefood.shared.data.source.local.datasource

import com.example.savethefood.shared.cache.SaveTheFoodDatabase
import com.example.savethefood.shared.cache.SaveTheFoodDatabaseQueries
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.data.domain.asDatabaseModel
import com.example.savethefood.shared.data.source.UserDataSource
import com.example.savethefood.shared.data.source.local.entity.UserEntity
import com.example.savethefood.shared.data.source.local.entity.asDomainModel

class UserLocalDataSource(
    private val foodDatabase: SaveTheFoodDatabase
) : UserDataSource {

    private val dbQuery: SaveTheFoodDatabaseQueries = foodDatabase.saveTheFoodDatabaseQueries

    override suspend fun getUser(email: String, password: String): UserDomain {
        return dbQuery.selectUserByEmailPsw(email, password, ::mapToUserEntity)
            .executeAsOne().asDomainModel()
    }

    override suspend fun saveUser(user: UserDomain): Long {
        return user.asDatabaseModel().run {
            dbQuery.transactionWithResult {
                dbQuery.insertUser(
                    id = this@run.id,
                    username = this@run.userName,
                    email = this@run.email,
                    password = this@run.password
                )
                dbQuery.lastInsertRowId().executeAsOne()
            }
        }
    }

    private fun mapToUserEntity(
        id: Long,
        username: String,
        email: String,
        password: String
    ): UserEntity {
        return UserEntity(
            id = id,
            userName = username,
            email = email,
            password = password
        )
    }
}