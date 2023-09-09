package com.example.descubertorr.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.descubertorr.data.ServiceLocator
import com.example.descubertorr.databinding.FragmentSiteBinding
import com.example.descubertorr.ui.models.Site

class SiteFragment(private val site: Site) : Fragment() {
    lateinit var binding: FragmentSiteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSiteBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSiteAtributes()
    }

    private fun setSiteAtributes() {
        binding.nameValue.text = site.name
        binding.description.text = site.description
        binding.username.text = ServiceLocator.realmManager.currentUser?.provider?.name ?: "Usuario anónimo"
    }

}