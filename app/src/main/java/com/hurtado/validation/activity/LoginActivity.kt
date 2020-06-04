package com.hurtado.validation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.hurtado.validation.R
import com.hurtado.validation.databinding.ActivityMainBinding
import com.hurtado.validation.model.LoginFormResult
import java.text.SimpleDateFormat
import java.util.Locale

class LoginActivity : AppCompatActivity() {

    private val localFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        // observer form
        binding.loginForm.of<LoginFormResult>()
                .observe(this, loginResultObserver())
        // make validations mandatory
        binding.loginForm.requireValidation()
    }

    private fun loginResultObserver() = Observer<LoginFormResult> { result ->
        Toast.makeText(applicationContext,
                "username:${result.username} " +
                        "\n password:${result.password} " +
                        "\n birth date:${localFormat.format(result.birthDate)}",
                Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
    }
}
