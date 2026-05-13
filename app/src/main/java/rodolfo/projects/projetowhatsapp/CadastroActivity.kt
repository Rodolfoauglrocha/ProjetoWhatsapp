package rodolfo.projects.projetowhatsapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import rodolfo.projects.projetowhatsapp.databinding.ActivityCadastroBinding
import rodolfo.projects.projetowhatsapp.model.Usuario
import rodolfo.projects.projetowhatsapp.utils.exibirMensagem

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.includeToobar.tbPrincipal) { view, insets ->
            val topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, topInset, 0, 0)
            insets
        }

        inicializarToolbar()
        inicializarEventosClique()

    }

    private fun inicializarEventosClique() {
        binding.btnCadastrar.setOnClickListener {

            val nome = binding.editNome.text.toString()
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            if(validarCampos()){
                cadastrarUsuario(nome, email, senha)
            }
        }
    }

    private fun cadastrarUsuario(nome: String, email: String, senha: String) {
        firebaseAuth.createUserWithEmailAndPassword(
            email, senha
        ).addOnCompleteListener { resultado ->
            if (resultado.isSuccessful) {

                val idUsuario = resultado.result.user?.uid
                if(idUsuario != null){
                    val usuario = Usuario(
                        idUsuario, nome, email
                    )
                    salvarUsuarioFirestore(usuario)
                }
            }
        }.addOnFailureListener { erro ->
            try {
                throw erro
            } catch(erroCredenciaisInvalidas: FirebaseAuthInvalidCredentialsException) {
                exibirMensagem("Email invalido")
            } catch(erroUsuarioExistente: FirebaseAuthUserCollisionException){
                exibirMensagem("Email ja pertencente a outro usuário")
            } catch(erroSenhaFraca: FirebaseAuthWeakPasswordException){
                exibirMensagem("Senha fraca")
            }
        }
    }

    private fun salvarUsuarioFirestore(usuario: Usuario){
        firestore.collection("usuarios")
            .document(usuario.id)
            .set(usuario)
            .addOnSuccessListener {
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                )
                exibirMensagem("Usuário Cadastrado")
            }.addOnFailureListener {
                exibirMensagem("Erro ao cadastrar usuário")
            }
    }

    private fun validarCampos(): Boolean {

        binding.textInputNome.error = null
        binding.textInputEmail.error = null
        binding.textInputSenha.error = null

        val nome = binding.editNome.text.toString()
        val email = binding.editEmail.text.toString()
        val senha = binding.editSenha.text.toString()

        var valido = true

        if (nome.isEmpty()) {
            binding.textInputNome.error = "Preencha o nome"
            valido = false
        }

        if (email.isEmpty()) {
            binding.textInputEmail.error = "Preencha o e-mail"
            valido = false
        }

        if (senha.isEmpty()) {
            binding.textInputSenha.error = "Preencha a senha"
            valido = false
        }

        return valido
    }

    private fun inicializarToolbar() {
        val toolbar = binding.includeToobar.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Faça o seu cadastro"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}