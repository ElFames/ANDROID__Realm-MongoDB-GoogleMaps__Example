package com.example.descubertorr.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.descubertorr.ui.models.Site
import com.example.descubertorr.R
import com.example.descubertorr.databinding.SiteLayoutBinding
import com.squareup.picasso.Picasso

class SiteAdapter(private var sites: List<Site>?, private val listener: OnClickSiteListener): RecyclerView.Adapter<SiteAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = SiteLayoutBinding.bind(view)

        fun setListener(site: Site) {
            binding.viewSite.setOnClickListener {
                listener.onClickSite(site)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.site_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val site = sites!![position]
        with(holder) {
            setListener(site)
            binding.siteName.text = site.name
            binding.siteDescription.text = site.description
            Picasso.get()
                .load("images/nada")
                .placeholder(R.drawable.logo)
                .into(binding.image)
        }
    }

    override fun getItemCount(): Int {
        return sites!!.size
    }
}