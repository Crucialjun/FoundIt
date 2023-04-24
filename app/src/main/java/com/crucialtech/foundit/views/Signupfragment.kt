package com.crucialtech.foundit.views

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.crucialtech.foundit.R
import com.crucialtech.foundit.authrepo.AuthViewModel
import com.crucialtech.foundit.databinding.FragmentSignUpBinding
import com.crucialtech.foundit.repository.AuthRepo
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.contracts.contract

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class Signupfragment : Fragment() {

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


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        oneTapClient = Identity.getSignInClient(requireActivity())
        //authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]



        binding.txtToSignIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }



        binding.btnGoogleSignup.setOnClickListener {
            Log.d("TAG", "onViewCreated:tapped ")

            lifecycleScope.launch {
                activityLauncher.launch(IntentSenderRequest.Builder(authViewModel.signUpWithGoogle(oneTapClient,requireContext())!!).build())
            }


        }

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}