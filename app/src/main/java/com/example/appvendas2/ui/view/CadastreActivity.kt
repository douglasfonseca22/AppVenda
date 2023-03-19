package com.example.appvendas2.ui.view

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.appvendas2.R
import com.example.appvendas2.data.AppDatabase
import com.example.appvendas2.data.models.Client
import com.example.appvendas2.data.models.Item
import com.example.appvendas2.data.models.OrderEntity
import com.example.appvendas2.ui.adapters.ItemListAdapter
import com.example.appvendas2.ui.viewmodels.CadastreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CadastreActivity : AppCompatActivity() {

    private lateinit var editClient: EditText
    private lateinit var editDescription: EditText
    private lateinit var editQuantity: EditText
    private lateinit var editUnitPrice: EditText
    private lateinit var buttonAddItem: Button
    private lateinit var textViewTotal: TextView
    private lateinit var buttonSave: Button
    private lateinit var listViewItems: ListView
    private lateinit var editQuantityTextTotal: TextView
    private lateinit var editTextTotal: TextView
    private lateinit var tvOrderNumber: TextView
    private lateinit var buttonCancel: Button
    private val items = mutableListOf<Item>()
    private var itemList: MutableList<Item> = mutableListOf()
    private lateinit var adapter: ItemListAdapter
    private var itemsRecupera: Client? = null
    private lateinit var viewModel: CadastreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        viewModel = ViewModelProvider(this).get(CadastreViewModel::class.java)

        setupViews()
        setupListeners()
        setupAdapter()
        setupData()
        setupOrderNumber()

        buttonAddItem.setOnClickListener {
            viewModel.addItem(
                editDescription.text.toString(),
                editQuantity.text.toString().toIntOrNull(),
                editUnitPrice.text.toString().toDoubleOrNull()
            )

            adapter.notifyDataSetChanged()
            editTextTotal.text = viewModel.totalValue.toString()
            editQuantityTextTotal.text = viewModel.totalQuantity.toString()
            editDescription.text.clear()
            editQuantity.text.clear()
            editUnitPrice.text.clear()
        }

        buttonCancel.setOnClickListener {
            showCancelConfirmationDialog()
        }

        adapter = ItemListAdapter(this@CadastreActivity, viewModel.items as MutableList<Item>)
        listViewItems.adapter = adapter

    }

    private fun setupViews() {
        editClient = findViewById(R.id.edit_text_client)
        editDescription = findViewById(R.id.edit_text_item_description)
        editQuantity = findViewById(R.id.edit_text_item_quantity)
        editUnitPrice = findViewById(R.id.edit_text_item_unit_price)
        buttonAddItem = findViewById(R.id.button_add_item)
        textViewTotal = findViewById(R.id.text_view_total)
        buttonSave = findViewById(R.id.button_save)
        listViewItems = findViewById(R.id.list_view_items)
        editQuantityTextTotal = findViewById(R.id.edit_text_item_quantity_total)
        editTextTotal = findViewById(R.id.edit_text_item_total_value)
        buttonCancel = findViewById(R.id.button_cancel)
        tvOrderNumber = findViewById(R.id.tv_number_pedido)
    }

    private fun setupListeners() {
        buttonSave.setOnClickListener { registerOrder() }
        buttonCancel.setOnClickListener { showCancelConfirmationDialog() }
    }

    private fun setupAdapter() {
        adapter = ItemListAdapter(this, items)
        listViewItems.adapter = adapter
    }

    private fun setupData() {
        itemsRecupera = intent.getSerializableExtra("orderId") as Client?

        if (itemsRecupera == null || itemsRecupera?.id == null) {
            Toast.makeText(this, "Não há itens lançados.", Toast.LENGTH_SHORT).show()
        } else {
            recoverlistAdapter()
        }
    }

    private fun setupOrderNumber() {
        if (itemsRecupera?.id == 0 || itemsRecupera?.id == null) {
            lifecycleScope.launch {
                val lastOrderId = withContext(Dispatchers.IO) {
                    AppDatabase(this@CadastreActivity).clientDao().getLastOrderId() ?: 0
                }

                val newOrderId = lastOrderId + 1
                tvOrderNumber.text = newOrderId.toString()
            }
        } else {
            tvOrderNumber.text = itemsRecupera?.id.toString()
        }
    }

    private fun registerOrder() {
        if (viewModel.items.isEmpty()) {
            Toast.makeText(
                this,
                "Adicione pelo menos um item antes de registrar o pedido",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val newItems = mutableListOf<Item>()
        for (item in viewModel.items) {
            val newItem = Item(
                description = item.description,
                quantity = item.quantity,
                unitPrice = item.unitPrice
            )
            newItems.add(newItem)
        }

        val totalValue = viewModel.calculateTotalValue()
        val order = OrderEntity(newItems, totalValue)
        val client = editClient.text.toString()

        lifecycleScope.launch {
            AppDatabase(this@CadastreActivity).orderDao().addOrder(order)
            val item = Client(client)
            AppDatabase(this@CadastreActivity).clientDao().addClient(item)
            Toast.makeText(
                this@CadastreActivity,
                "Pedido registrado com sucesso!",
                Toast.LENGTH_SHORT
            )
                .show()
            finish()
        }

        items.addAll(newItems)
    }

    private fun recoverlistAdapter() {
        val orderId = itemsRecupera?.id ?: -1
        if (orderId == -1) {
            finish()
            return
        }

        lifecycleScope.launch {
            val order = withContext(Dispatchers.IO) {
                AppDatabase(this@CadastreActivity).orderDao().getOrderById(orderId)
            }
            if (order == null) {
                finish()
                return@launch
            }

            val adapter = ItemListAdapter(this@CadastreActivity, order.items as MutableList<Item>)

            listViewItems.adapter = adapter

            buttonAddItem.isEnabled = false
            buttonSave.isEnabled = false

            buttonCancel.text = "Voltar"

            val totalQuantity = viewModel.calculateTotalQuantity(order.items)
            editQuantityTextTotal.text = totalQuantity.toString()
            editQuantityTextTotal.isEnabled = true

            val totalValue = viewModel.calculateTotalValueList(order.items)
            editTextTotal.text = totalValue.toString()
        }
    }

    private fun showCancelConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Tem certeza que deseja sair?")
        builder.setPositiveButton("Sim") { _, _ ->
            finish()
        }
        builder.setNegativeButton("Não") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}