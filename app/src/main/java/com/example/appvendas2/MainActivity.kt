package com.example.appvendas2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appvendas2.data.AppDatabase
import com.example.appvendas2.ui.adapters.UserAdapter
import com.example.appvendas2.ui.view.CadastreActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        findViewById<Button>(R.id.btn_add_main).setOnClickListener {
            val intent = Intent(this@MainActivity, CadastreActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val userList = withContext(Dispatchers.IO) {
                AppDatabase(this@MainActivity).clientDao().getAllClient()
            }

            findViewById<RecyclerView>(R.id.recycler_view).apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = UserAdapter().apply {
                    setData(userList)

                    setOnItemClickListener {
                        val intent = Intent(this@MainActivity, CadastreActivity::class.java)
                        intent.putExtra("orderId", it)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}