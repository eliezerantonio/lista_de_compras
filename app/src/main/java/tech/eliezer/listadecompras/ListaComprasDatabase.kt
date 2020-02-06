package tech.eliezer.listadecompras

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class ListaComprasDatabase(context: Context) : ManagedSQLiteOpenHelper(
    ctx = context,
    name = "listaCompras.db", version = 1
) {

    //singleton classe

    companion object {
        private var intance: ListaComprasDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): ListaComprasDatabase {
            if (intance == null) {
                intance = ListaComprasDatabase(ctx.getApplicationContext())
            }
            return intance!!
        }

    }


    override fun onCreate(db: SQLiteDatabase?) {

        // Criação de tabelas
        if (db != null) {
            db.createTable(
                "Produtos", true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "nome" to TEXT,
                "quantidade" to INTEGER,
                "preco" to REAL,
                "foto" to BLOB
            )
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}

// acesso a proprieade pelo COntexto
val Context.database: ListaComprasDatabase
    get() = ListaComprasDatabase.getInstance(getApplicationContext())

