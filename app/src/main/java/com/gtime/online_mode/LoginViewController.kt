package com.gtime.online_mode

import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.example.antip.R
import com.example.antip.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.gtime.general.Constants

class LoginViewController(
    private val binding: FragmentLoginBinding,
    private val viewModel: LoginViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val navController: NavController
) {
    fun setupViews() {
        setupFields()
        setupObservers()
        setupOnClick()
    }

    private fun setupFields() = with(binding) {
        val accountInfo = viewModel.accountInfoForDisplay ?: return
        Glide.with(iconIv).load(accountInfo.urlAvatar + Constants.AVATAR_URL_200_END)
            .centerCrop().into(iconIv)
        nameTv.text = accountInfo.name
        emailField.setText(accountInfo.email)
    }

    private fun setupOnClick() = with(binding) {
        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            if (!viewModel.checkIfPasswordIsNormal(password)) {
                showSnackBar(R.string.password_requires)
                return@setOnClickListener
            }
            viewModel.createAccount(email, password)
        }

    }

    private fun setupObservers() {
        viewModel.stateOfAuth.observe(viewLifecycleOwner) { state ->
            when (state) {
                is StateOfAuth.CreateError.AlreadyExistError -> {
                    viewModel.signInAccount(state.email, state.password)
                }
                is StateOfAuth.SignInError -> {
                    showSnackBar(R.string.error_authorization)
                }
                is StateOfAuth.CreateSuccess -> {
                    viewModel.successCreateNewAccount()
                    showSnackBar(R.string.account_created)
                    navController.popBackStack()
                }
                is StateOfAuth.SignInSuccess -> {
                    viewModel.successSignIn()
                    showSnackBar(R.string.sign_in_account)
                    navController.popBackStack()
                }
                else -> {}
            }
        }
    }

    private fun showSnackBar(id: Int) {
        Snackbar.make(
            binding.root,
            id,
            Snackbar.LENGTH_LONG
        ).show()
    }

}