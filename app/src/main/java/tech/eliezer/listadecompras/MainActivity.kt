package tech.eliezer.listadecompras

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ProdutoAdapter(application)
        btn_adicionar.setOnClickListener {

            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)


        }

//definição do ouvinte da lista para clicks longos
        list_view_produtos.setOnItemLongClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            //buscando o item clicado
            val item = adapter.getItem(i)
//removendo o item clicado da lista
            adapter.remove(item)//deletando do banco de dados
            if (item != null) {
                deletarProduto(item.id)
            }
            true
        }

    }

    override fun onResume() {
        super.onResume()


        database.use {
            //efetuando uma cnsulta no banco de dados

            select("Produtos").exec {
                //criando o oarser que montara o objecto roduto

                //Criando o parser que montará o objeto produto
                val parser = rowParser { id: Int,
                                         nome: String,
                                         quantidade: Int,
                                         valor: Double,
                                         foto: ByteArray? ->
                    //Colunas do banco de dados

                    //Montagem do objeto Produto com as colunas do banco
                    Produto(id, nome, quantidade, valor, foto?.toBitmap())
                }

                //criando a lista de orodts com o banco de dados

                var listaProdutos = parseList(parser)

                //implementacao do adaptador

                val produtosAdapter = ProdutoAdapter(application)

                produtosAdapter.addAll(listaProdutos)

                list_view_produtos.adapter = produtosAdapter

                val soma = listaProdutos.sumByDouble { it.valor * it.quantidade }
                val f = NumberFormat.getCurrencyInstance(Locale("pt", "ao"))
                txt_total.text = "TOTAL: ${f.format(soma)}"

            }


        }


    }

    fun deletarProduto(idProduto: Int) {
        database.use {
            delete("Produtos", "id = {id}", "id"  to idProduto)
        }
    }

}
