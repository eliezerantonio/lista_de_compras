package tech.eliezer.listadecompras

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cadastro.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream

class CadastroActivity : AppCompatActivity() {

    val COD_IMAGE = 101;
    var imageBitMap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        img_foto_produto.setOnClickListener {
            abrirGaleria()
        }
        btn_inserir.setOnClickListener {

            val produto = txt_produto.text.toString()
            val qtd = txt_quantidade.text.toString()
            val valor = txt_valor.text.toString()


            if (produto.isNotEmpty() && qtd.isNotEmpty() && valor.isNotEmpty()) {
                //enviando o item para a lista
                database.use {
                    val idProduto = insert(
                        "Produtos",
                        "nome" to produto,
                        "quantidade" to qtd,
                        "preco" to valor.toDouble(),
                        "foto" to imageBitMap?.toByteArray()
                    //acrescentamos a chamada a funcao de extensao
                    )

                    if (idProduto != -1L) {

                        toast("Item inserido com sucesso")

                        //limpando a caixa de texto
                        img_foto_produto.setImageBitmap(imageBitMap);
                        txt_produto.text.clear()
                        txt_quantidade.text.clear()
                        txt_valor.text.clear()
                    } else {
                        toast("Erro ao inserir o banco de dados")
                    }
                }
            } else {
                txt_produto.error =
                    if (txt_produto.text.isEmpty()) "Preencha o nomedo produto " else null
                txt_quantidade.error =
                    if (txt_quantidade.text.isEmpty()) "Preencha a quantidade" else null
                txt_valor.error = if (txt_valor.text.isEmpty()) "Preencha o valor" else null


            }
        }

        btn_voltar.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == COD_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
//Neste ponto podemos acessar a imagem escolhida através da variável "data"

                //lendo a uri com a imagem
                val inputStream = data.getData()?.let { contentResolver.openInputStream(it) };
//transformando o resultado em bitmap
                imageBitMap = BitmapFactory.decodeStream(inputStream)
//Exibir a imagem no aplicativo
                img_foto_produto.setImageBitmap(imageBitMap)
            }
        }
    }

    fun abrirGaleria() {
//definindo a ação de conteúdo
        val intent = Intent(Intent.ACTION_GET_CONTENT)
//definindo filtro para imagens
        intent.type = "image/*"
//inicializando a activity com resultado

        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), COD_IMAGE)
    }


}
