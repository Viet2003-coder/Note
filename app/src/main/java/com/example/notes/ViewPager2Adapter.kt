package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter(activity: FragmentActivity, private val username: String) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                val fragment = Home()
                val bundle = Bundle()
                bundle.putString("usernamelogin", username)
                fragment.arguments = bundle
                fragment
            }
            1-> {
                val fragment = SpecialNotes()
                val bundle = Bundle()
                bundle.putString("usernamelogin", username)
                fragment.arguments = bundle
                fragment
            }
            else -> {
                    val fragment = Information()
                    val bundle = Bundle()
                    bundle.putString("usernamelogin", username)
                    fragment.arguments = bundle
                    fragment
            }
        }
    }

    override fun getItemCount(): Int =3
}