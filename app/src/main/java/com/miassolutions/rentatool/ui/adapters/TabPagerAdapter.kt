package com.miassolutions.rentatool.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.miassolutions.rentatool.BillFragment
import com.miassolutions.rentatool.RentedFragment
import com.miassolutions.rentatool.RetainedFragment
import com.miassolutions.rentatool.ReturnedFragment

class TabPagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RentedFragment()
            1-> ReturnedFragment()
            2 -> RetainedFragment()
            3 -> BillFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}