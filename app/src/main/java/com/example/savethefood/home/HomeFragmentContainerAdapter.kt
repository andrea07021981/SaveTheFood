package com.example.savethefood.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.savethefood.constants.Constants.FILTER_LIST
import com.example.savethefood.constants.StorageType
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragmentContainerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = StorageType.values().size

    override fun createFragment(position: Int): Fragment {
        val storageType: StorageType = when (position) {
            1 -> StorageType.FRIDGE
            2 -> StorageType.FREEZER
            3 -> StorageType.DRY
            else -> StorageType.ALL
        }
        return HomeFragment().apply {
            arguments = Bundle().apply {
                putSerializable(FILTER_LIST, storageType)
            }
        }
    }
}