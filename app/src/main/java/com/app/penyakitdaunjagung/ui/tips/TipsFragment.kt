package com.app.penyakitdaunjagung.ui.tips

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.app.penyakitdaunjagung.R
import com.app.penyakitdaunjagung.databinding.FragmentTipsBinding

class TipsFragment : Fragment() {

    private var _binding: FragmentTipsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.webView?.loadUrl("file:///android_asset/content_tips.html")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}