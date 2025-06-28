package com.zyc.clover.di


import com.zyc.db.database.DatabaseInitialize
import com.zyc.db.database.DatabaseRepository
import com.zyc.db.database.DatabaseRepositoryImpl
import com.zyc.db.utils.db.DatabaseDriverFactory
import org.koin.dsl.module
val databaseModel = module {
    single {
        DatabaseDriverFactory(
            context = get()
        )
    }
    single {
        DatabaseInitialize(
            database = get()
        )
    }
    single<DatabaseRepository> {
        DatabaseRepositoryImpl(
            databaseQuery = get()
        )
    }
}
