package com.crucialtech.foundit.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.crucialtech.foundit.R
import com.crucialtech.foundit.viewmodel.AuthViewModel
import com.crucialtech.foundit.databinding.FragmentSignUpBinding
import com.crucialtech.foundit.models.AppUser
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class SignupFragment : Fragment() {

    private  val authViewModel by viewModels<AuthViewModel>()
    private var _binding: FragmentSignUpBinding? = null
    private lateinit var oneTapClient: SignInClient
    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){result ->
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)

        val idToken = credential.googleIdToken
        lifecycleScope.launch{
            authViewModel.signinwithCredential(idToken)
        }
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        oneTapClient = Identity.getSignInClient(requireActivity())

        binding.txtToSignIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.btnGoogleSignup.setOnClickListener {
            Log.d("TAG", "onViewCreated:tapped ")

            lifecycleScope.launch {
                activityLauncher.launch(

                    //add result code

                    IntentSenderRequest.Builder(authViewModel.signUpWithGoogle(oneTapClient,requireContext())!!)
                    .build())
            }


        }

        return binding.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}