package com.codelog.arke.ui.home

import android.database.CharArrayBuffer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.codelog.arke.api.User
import com.codelog.arke.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import java.nio.CharBuffer
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val chatModels: ArrayList<ChatModel> = ArrayList()
        homeViewModel.users.observe(viewLifecycleOwner) { users ->
            users.forEach { user ->
                chatModels.add(ChatModel(user))
            }
        }

        val layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
        binding.chats.layoutManager = layoutManager
        binding.chats.adapter = ChatAdapter(chatModels)



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}