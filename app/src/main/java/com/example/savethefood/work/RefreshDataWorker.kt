package com.example.savethefood.work

import android.app.Application
import android.content.Context
import androidx.hilt.Assisted
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.lang.Exception
import javax.inject.Inject

class RefreshDataWorker @Inject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val foodDataRepository: com.example.savethefood.shared.data.source.repository.FoodRepository
): CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    // TODO add refresh local recipes
    override suspend fun doWork(): Result {
        return try {
            foodDataRepository.refreshData()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}