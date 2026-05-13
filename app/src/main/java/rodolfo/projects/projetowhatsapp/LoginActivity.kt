package rodolfo.projects.projetowhatsapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import rodolfo.projects.projetowhatsapp.databinding.ActivityLoginBinding
import rodolfo.projects.projetowhatsapp.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(binding.root)

        inicializarEventosClique()
    }

    private fun inicializarEventosClique() {
        binding.textCadastro.setOnClickListener {
            startActivity(
                Intent(this, CadastroActivity::class.java)
            )
        }
    }
}