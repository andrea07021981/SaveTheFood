package com.example.savethefood.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.savethefood.util.StorageType
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragmentContainerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

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