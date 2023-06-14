package com.crucialtech.foundit.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.crucialtech.foundit.R

import com.crucialtech.foundit.databinding.FragmentSignInBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SigninFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtToregister.setOnClickListener {
            Log.d("TAG : SignInFragment", "onViewCreated: To register button clicked")
            it.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.btnSignin.setOnClickListener {
            it.findNavController().navigate(R.id.action_SignInFragment_to_homeActivity)
        }


    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()

    }
}