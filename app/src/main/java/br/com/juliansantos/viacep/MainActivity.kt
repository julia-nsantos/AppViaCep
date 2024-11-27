package br.com.juliansantos.viacep

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.juliansantos.viacep.network.RetrofitClient
import br.com.juliansantos.viacep.models.Endereco
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Alterar cor da barra de status
        window.statusBarColor = Color.parseColor("#088404")

        // Referenciar os componentes da interface
        val editCep: EditText = findViewById(R.id.edit_cep)
        val editLogradouro: EditText = findViewById(R.id.edit_logradouro)
        val editBairro: EditText = findViewById(R.id.edit_bairro)
        val editCidade: EditText = findViewById(R.id.edit_cidade)
        val editUf: EditText = findViewById(R.id.edit_uf)
        val btnBuscarCep: Button = findViewById(R.id.btn_buscar_cep)

        // Configurar ação do botão
        btnBuscarCep.setOnClickListener {
            val cep = editCep.text.toString().trim()

            // Validar o CEP antes de fazer a requisição
            if (cep.isEmpty()) {
                Toast.makeText(this, "Digite um CEP válido", Toast.LENGTH_SHORT).show()
            } else if (cep.length != 8) {
                Toast.makeText(this, "O CEP deve conter 8 dígitos", Toast.LENGTH_SHORT).show()
            } else {
                consultarCep(cep, editLogradouro, editBairro, editCidade, editUf)
            }
        }
    }

    private fun consultarCep(
        cep: String,
        editLogradouro: EditText,
        editBairro: EditText,
        editCidade: EditText,
        editUf: EditText
    ) {
        // Faz a chamada para o serviço Retrofit configurado
        val call = RetrofitClient.instance.getEndereco(cep)

        // Enfileira a chamada para execução assíncrona
        call.enqueue(object : Callback<Endereco> {
            override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                if (response.isSuccessful) {
                    // Obter os dados do endereço da resposta
                    val endereco = response.body()
                    endereco?.let {
                        // Preencher os campos com os dados do endereço
                        editLogradouro.setText(it.logradouro)
                        editBairro.setText(it.bairro)
                        editCidade.setText(it.localidade)
                        editUf.setText(it.uf)
                    }
                } else {
                    // Tratamento de erro na resposta
                    Toast.makeText(this@MainActivity, "Erro ao consultar o CEP", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Endereco>, t: Throwable) {
                // Tratamento de erro na chamada
                Toast.makeText(this@MainActivity, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
