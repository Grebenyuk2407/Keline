package dev.androidbroadcast.keline.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.keline.R
import dev.androidbroadcast.keline.data.User
import dev.androidbroadcast.keline.databinding.FragmentRegisterBinding
import dev.androidbroadcast.keline.util.Resource
import dev.androidbroadcast.keline.viewmodel.RegisterViewModel


private val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding :FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnRegisterRegister.setOnClickListener {
                val user = User(
                    edFirstNameRegister.text.toString().trim(),
                    edLastNameRegister.text.toString().trim(),
                    edEmailRegister.text.toString().trim()

                )
                val password = edPasswordRegister.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading ->{
                        binding.btnRegisterRegister.startAnimation()

                    }
                    is Resource.Success ->{
                        Log.d("test", it.data.toString())
                        binding.btnRegisterRegister.revertAnimation()
                    }
                    is Resource.Error ->{
                        Log.e(TAG, it.message.toString())
                        binding.btnRegisterRegister.revertAnimation()

                    }
                    else -> Unit

                }

            }
        }
    }
}