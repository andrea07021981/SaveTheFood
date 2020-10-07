package com.example.savethefood.work

import android.app.Application
import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.data.source.repository.FoodRepository
import java.lang.Exception

class RefreshDataWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val foodDataRepository: FoodRepository
): CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        return try {
            foodDataRepository.refreshData()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}