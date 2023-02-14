package com.example.todoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.database.MyDataBase
import com.example.todoapp.database.model.Todo
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class UpdateTodoActivity : AppCompatActivity() {

    lateinit var title: EditText
    lateinit var details: EditText
    lateinit var titleLayout: TextInputLayout
    lateinit var detailsLayout: TextInputLayout
    lateinit var date: TextView
    lateinit var update_btn: Button
    lateinit var todo: Todo
    lateinit var todoListFragment: TodoListFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_todo)
        initViews()
        receiveData()
    }

    private fun receiveData() {
        todo = (intent.getSerializableExtra("Todo") as? Todo)!!
        title.setText(todo?.name)
        details.setText(todo?.details)
        date.setText(todo?.date.toString())

    }

    private fun validateForm(): Boolean {
        var isValid = true
        if (titleLayout.editText?.text.toString().isBlank()) {
            titleLayout.error = "please enter todo title"
            isValid = false
        } else {
            titleLayout.error = null
        }
        if (detailsLayout.editText?.text.toString().isBlank()) {
            detailsLayout.error = "please enter todo details"
            isValid = false
        } else {
            detailsLayout.error = null
        }
        return isValid
    }

    private fun initViews() {
        title = findViewById(R.id.update_todo_title)
        details = findViewById(R.id.update_todo_details)
        titleLayout = findViewById(R.id.update_title_layout)
        detailsLayout = findViewById(R.id.update_details_layout)
        date = findViewById(R.id.update_choose_date)
        update_btn = findViewById(R.id.update_btn)

        update_btn.setOnClickListener(View.OnClickListener {
            if (validateForm()) {
                var title: String = title.text.toString()
                var details: String = details.text.toString()
                var date: Date = Calendar.getInstance().clearTime().time

                var updatedTodo = Todo(todo.id, title, details, date, false)
                updateTodo(updatedTodo)
            }
        })

        date.setOnClickListener(View.OnClickListener {
            showDatePicker()
        })

    }

    override fun onResume() {
        super.onResume()
        update_btn

    }

    private fun updateTodo(todo: Todo) {

        MyDataBase.getInstance(this).todoDao().updateTodo(todo)
        Toast.makeText(this, "Note Updated", Toast.LENGTH_LONG).show()
        onBackPressedDispatcher.onBackPressed()

    }

    var calendar = Calendar.getInstance()
    fun showDatePicker() {

        val datePicker = DatePickerDialog(
            this,
            object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.YEAR, year)

                    date.setText("" + day + "/" + (month + 1) + "/" + year)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}