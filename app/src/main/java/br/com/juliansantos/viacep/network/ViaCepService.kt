package br.com.juliansantos.viacep.network

import br.com.juliansantos.viacep.models.Endereco
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("{cep}/json/")
    fun getEndereco(@Path("cep") cep: String): Call<Endereco>
}
