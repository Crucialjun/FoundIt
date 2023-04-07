package com.crucialtech.foundit.views

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.crucialtech.foundit.R
import com.crucialtech.foundit.authrepo.AuthViewModel
import com.crucialtech.foundit.databinding.FragmentSignUpBinding
import com.crucialtech.foundit.repository.AuthRepo
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import kotlin.contracts.contract

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class Signupfragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private var _binding: FragmentSignUpBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtToSignIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        val oneTapClient = Identity.getSignInClient(requireActivity())

        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = AuthRepo(
                        oneTapClient = oneTapClient,
                        context = requireContext(),
                    ).signInWithIntent(it.data ?: return@launch)

                    authViewModel.signUpResult(signInResult)
                }


            }
        }

            binding.btnGoogleSignup.setOnClickListener {
                Log.d("TAG", "onViewCreated: Signupbutton clicked ")
                lifecycleScope.launch {
                    val signInIntent = authViewModel.signUpWithOneTap(requireActivity())
                    IntentSenderRequest.Builder(
                        signInIntent ?: return@launch
                    ).build()
                }

            }

        }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}