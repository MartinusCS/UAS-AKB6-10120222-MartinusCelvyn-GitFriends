package com.martinus.gitfriends.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martinus.gitfriends.adapter.MainAdapter
import com.martinus.gitfriends.databinding.FragmentFollowBinding
import com.martinus.gitfriends.repository.remote.response.GithubUser
import com.martinus.gitfriends.viewmodel.DetailViewModel
import com.martinus.gitfriends.viewmodel.ViewModelFactory

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val viewModel: DetailViewModel by viewModels() {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var rvUser: RecyclerView

    private var position: Int = 1
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvUser = binding.rvFollowers
        rvUser.setHasFixedSize(true)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        if (position == 1) {
            viewModel.getUserFollowers(username ?: "")
        } else {
            viewModel.getUserFollowing(username ?: "")
        }
        val layoutManager = LinearLayoutManager(requireActivity())
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.apply {
            rvFollowers.layoutManager = layoutManager
            rvFollowers.addItemDecoration(itemDecoration)
        }

        viewModel.listFollowing.observe(viewLifecycleOwner) { lisUsers ->
            showRecyclerList(lisUsers)
            checkData(lisUsers)

        }

        viewModel.listFollowers.observe(viewLifecycleOwner) { lisUsers ->
            showRecyclerList(lisUsers)
            checkData(lisUsers)

        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun checkData(list: List<GithubUser>) {
        if (list.isEmpty()) {
            binding.apply {
                ivNotFound.visibility = View.VISIBLE
                tvNotFound.visibility = View.VISIBLE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showRecyclerList(list: List<GithubUser>) {
        rvUser.layoutManager = LinearLayoutManager(context)
        val adapter = MainAdapter(list)
        rvUser.adapter = adapter
    }

    companion object {
        const val ARG_USERNAME = "arg_username"
        const val ARG_POSITION = "arg_position"
    }
}