package ru.mvlikhachev.babyfind.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import ru.mvlikhachev.babyfind.Model.User
import ru.mvlikhachev.babyfind.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "TAG"

    private lateinit var auth: FirebaseAuth


    private var database: FirebaseDatabase = Firebase.database
    private val usersDatabaseReference: DatabaseReference = database.reference.child("users")

    private var isLoginModeActive: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth

        supportActionBar?.hide()
        // first authorization
        authorizationUi()


        // Если пользователь авторизован - сразу открыть мэйн активити
        if (auth.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }


    }

    fun toggleLoginSignUp(view: View) {
        if (isLoginModeActive) {
            registarionUi();
        } else {
            authorizationUi();
        }

    }

    private fun authorizationUi() {
        isLoginModeActive = true
        loginSignUpButton.setText("Войти")
        toggleLoginSignUpTextView.setText("Или зарегистрируйтесь")
        textInputConfirmPassword.visibility = View.GONE
        textInputName.visibility = View.GONE
        checkBox.visibility = View.GONE
    }

    private fun registarionUi() {
        isLoginModeActive = false
        loginSignUpButton.setText("Зарегистрироваться")
        toggleLoginSignUpTextView.setText("Или авторизуйтесь")
        textInputConfirmPassword.visibility = View.VISIBLE
        textInputName.visibility = View.VISIBLE
        checkBox.visibility = View.VISIBLE
    }

    // Validation

    private fun validateEmail(): Boolean {
        val emailInput = textInputEmail
                .editText
                ?.getText()
                .toString()
                .trim()
        return if (emailInput.isEmpty()) {
            textInputEmail.error = "Введите email!"
            false
        } else {
            textInputEmail.error = ""
            true
        }
    }

    private fun validateName(): Boolean {
        val nameInput = textInputName
                .editText
                ?.getText()
                .toString()
                .trim()
        return if (nameInput.isEmpty()) {
            textInputName.error = "Введите Ваше имя!"
            false
        } else if (nameInput.length > 10) {
            textInputName.error = "Имя должно быть меньше 15 символов!"
            false
        } else {
            textInputName.error = ""
            true
        }
    }

    private fun validatePassword(): Boolean {
        val passwordInput = textInputPassword
                .editText
                ?.getText()
                .toString()
                .trim()
        return if (passwordInput.isEmpty()) {
            textInputPassword.error = "Введите Ваше имя!"
            false
        } else if (passwordInput.length < 6) {
            textInputPassword.error = "Пароль должно быть больше 6 символов!"
            false
        } else {
            textInputPassword.error = ""
            true
        }
    }

    private fun validateConfirmPassword(): Boolean {
        val passwordInput = textInputPassword
                .editText
                ?.getText()
                .toString()
                .trim()
        val confirmPasswordInput = textInputConfirmPassword
                .editText
                ?.getText()
                .toString()
                .trim()
        return if (passwordInput != confirmPasswordInput) {
            textInputPassword.error = "Пароли должны совпадать"
            false
        } else {
            textInputPassword.error = ""
            true
        }
    }




    fun loginSignUpUser(view: View) {
        val email = textInputEmail.editText?.text.toString().trim()
        val password = textInputPassword.editText?.text.toString().trim()

        if (!validateEmail() or !validatePassword()) {
            return
        }
        if (isLoginModeActive) {
            Log.d("clickButton", "Log in")
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            startApp()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                            ).show()
                            //updateUI(null)
                            // ...
                        }

                        // ...
                    }
        } else {
            if (!validateEmail() or !validateName() or !validatePassword() or !validateConfirmPassword()) {
                return
            }
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            createUser(user)
                            startApp()
                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                            ).show()
                            //updateUI(null)
                        }

                        // ...
                    }
        }
    }


    private fun createUser(user: FirebaseUser?) {

        var role = "test"
        if (checkBox.isChecked) role = "parent" else role = "child"

        val name = textInputName.editText?.text.toString().trim()

        val addUser = User(uid = user?.uid, displayName = name, email =  user?.email, role = role)

        Log.d("C_UID", user?.uid.toString())
        Log.d("C_UID", name)
        user?.email?.let { Log.d("C_UID", it) }
        Log.d("C_UID", role)

        usersDatabaseReference.child(user?.uid.toString()).setValue(addUser);
    }


    private fun startApp() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//        finish()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }



}