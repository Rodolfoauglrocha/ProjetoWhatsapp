package rodolfo.projects.projetowhatsapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import rodolfo.projects.projetowhatsapp.databinding.ActivityLoginBinding
import rodolfo.projects.projetowhatsapp.utils.exibirMensagem

class LoginActivity : AppCompatActivity() {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inicializarEventosClique()
        //firebaseAuth.signOut()
    }

    override fun onStart(){
        super.onStart()
        verificarUsuarioLogado()
    }

    private fun verificarUsuarioLogado() {
        val usuarioAtual = firebaseAuth.currentUser

        if(usuarioAtual !=null){
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    private fun inicializarEventosClique() {
        binding.textCadastro.setOnClickListener {
            startActivity(
                Intent(this, CadastroActivity::class.java)
            )
        }

        binding.btnLogar.setOnClickListener {

            if(validarCampos()){
                logarUsuario()
            }
        }
    }

    private fun logarUsuario() {

        val email = binding.editLoginEmail.text.toString()
        val senha = binding.editLoginSenha.text.toString()

        firebaseAuth.signInWithEmailAndPassword(email,senha)
            .addOnSuccessListener {
                exibirMensagem("Logado com sucesso")
                startActivity(
                    Intent(this, MainActivity::class.java)
                )
            }.addOnFailureListener { erro ->
                try {
                    throw erro
                } catch(erroCredenciaisInvalidas: FirebaseAuthInvalidUserException) {
                    exibirMensagem("Email invalido")
                } catch(erroSenhaFraca: FirebaseAuthInvalidCredentialsException){
                    exibirMensagem("Email ou senha invalida")
                }
            }

    }

    private fun validarCampos(): Boolean {

        val email = binding.editLoginEmail.text.toString()
        val senha = binding.editLoginSenha.text.toString()

        if(email.isNotEmpty()){
            binding.textInputLayoutLoginEmail.error = null
            if(senha.isNotEmpty()){
                binding.textInputLayoutLoginSenha.error = null
                return true
            }else {
                binding.textInputLayoutLoginSenha.error = "Preencha a senha"
                return false

            }
        } else {
            binding.textInputLayoutLoginEmail.error = "Preencha o e-mail"
            return false
        }
    }
}