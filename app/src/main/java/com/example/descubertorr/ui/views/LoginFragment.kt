package com.example.descubertorr.ui.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import com.example.descubertorr.MainActivity
import com.example.descubertorr.data.ServiceLocator
import com.example.descubertorr.databinding.FragmentLoginBinding
import io.realm.kotlin.mongodb.exceptions.ConnectionException
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import io.realm.kotlin.mongodb.exceptions.ServiceException
import kotlinx.coroutines.*

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var myPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstSharedConfiguration()
        checkLastConection()
        onClicksListeners()
    }

    private fun firstSharedConfiguration() {
        myPreferences = requireActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE).apply {
            edit {
                putString("username", "")
                putString("password", "")
                putBoolean("active", false)
                apply()
            }
        }
    }
    private fun checkLastConection() {
        val active = myPreferences.getBoolean("active", false)
        binding.switchButton.isChecked = active
        if(active) {
            val username = myPreferences.getString("username", "")
            val password = myPreferences.getString("password", "")
            login(username!!, password!!)
        }
    }
    private fun login(username: String, password: String) {
        checkSharedPreferencesButton(binding.switchButton.isChecked)
            runCatching {
                CoroutineScope(Dispatchers.IO).launch{
                    ServiceLocator.realmManager.login(username, password)
                }.start()
            }.onFailure { e ->
                when(e) {
                    is ServiceException -> Toast.makeText(requireContext(), "Usuario ya registrado", Toast.LENGTH_LONG).show()
                    is InvalidCredentialsException -> Toast.makeText(requireContext(), "Usuario i/o contraseña incorrectas", Toast.LENGTH_LONG).show()
                    is ConnectionException -> Toast.makeText(requireContext(), "Revise su conexión a internet o inténtelo de nuevo más tarde ", Toast.LENGTH_LONG).show()
                    is IllegalArgumentException -> Toast.makeText(requireContext(), "Los campos no pueden estar vacios", Toast.LENGTH_LONG).show()
                    else -> Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }
            }.onSuccess {
                startApplication()
            }
    }
    private fun checkSharedPreferencesButton(active: Boolean) {
        if(active) {
            myPreferences.edit {
                putString("username", binding.username.text.toString())
                putString("password", binding.password.text.toString())
                putBoolean("active", active)
                apply()
            }
        } else {
            myPreferences.edit {
                putString("username", "")
                putString("password", "")
                putBoolean("active", active)
                apply()
            }
        }
    }
    private fun getCredentials(): Pair<String,String>{
        val username: String
        val password: String
        val active = myPreferences.getBoolean("active", false)
        if(active) {
            username = myPreferences.getString("username", "") ?: ""
            password = myPreferences.getString("password", "") ?: ""
        } else {
            username = binding.username.text.toString().trimIndent().trim()
            password = binding.password.text.toString().trimIndent().trim()
        }
        return Pair(username, password)
    }

    private fun onClicksListeners() {
        binding.loginButton.setOnClickListener {
            val cred = getCredentials()
            login(cred.first, cred.second)
        }
        binding.registerButton.setOnClickListener {
            runBlocking {
                val cred = getCredentials()
                ServiceLocator.realmManager.register(cred.first, cred.second)
                startApplication()
            }
        }
    }
    private fun startApplication() {
        parentFragmentManager.clearBackStack("")
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }
}