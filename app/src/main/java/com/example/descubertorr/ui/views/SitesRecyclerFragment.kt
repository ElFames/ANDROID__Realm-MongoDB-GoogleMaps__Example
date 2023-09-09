package com.example.descubertorr.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.descubertorr.R
import com.example.descubertorr.ui.adapters.SiteAdapter
import com.example.descubertorr.databinding.FragmentSitesRecyclerBinding
import com.example.descubertorr.ui.adapters.OnClickSiteListener
import com.example.descubertorr.ui.models.Site
import io.realm.kotlin.types.ObjectId

class SitesRecyclerFragment : Fragment(), OnClickSiteListener {
    private lateinit var binding: FragmentSitesRecyclerBinding
    private lateinit var siteAdapter: SiteAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private var recyclerSitesList = listOf<Site>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSitesRecyclerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var sites: List<Site> = listOf(
            Site(ObjectId.create(),"Castillo Romano",
            "castilloromano.jpg",
            "Descripcion del lugar historico recien descubierto",
            "23.43245",
            "50.24235",
            "anonimo")
        )
        // logica para cargar la lista de Sites
        recyclerSitesList = sites
        siteAdapter = SiteAdapter(recyclerSitesList, this)
        startRecyclerView()
    }

    private fun startRecyclerView() {
        parentFragmentManager.clearBackStack("todo")
        linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerListView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = siteAdapter
        }
    }

    override fun onClickSite(site: Site) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, SiteFragment(site))
            setReorderingAllowed(true)
            addToBackStack("")
            commit()
        }
    }

}