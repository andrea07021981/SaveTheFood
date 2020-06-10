package com.example.savethefood.work

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.data.source.repository.FoodRepository
import java.lang.Exception

class RefreshDataWorker(
    context: Context,
    params: WorkerParameters,
   val application: Application
): CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val foodRepository = FoodDataRepository.getRepository(application)
        return try {
            foodRepository.refreshData()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}