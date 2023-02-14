package com.example.todoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.example.todoapp.database.MyDataBase
import com.example.todoapp.database.model.Todo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class AddTodoBottomSheet : BottomSheetDialogFragment() {

    lateinit var titleLayout: TextInputLayout
    lateinit var detailsLayout: TextInputLayout
    lateinit var chooseDate: TextView
    lateinit var addTodo: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        titleLayout = requireView().findViewById(R.id.title_layout)
        detailsLayout = requireView().findViewById(R.id.details_layout)
        chooseDate = requireView().findViewById(R.id.choose_date)
        addTodo = requireView().findViewById(R.id.add)
        chooseDate.setText(
            "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(
                Calendar.YEAR
            )
        )

        chooseDate.setOnClickListener {
            showDatePicker()
        }
        addTodo.setOnClickListener {
            if (validateForm()) {
                val title = titleLayout.editText?.text.toString()
                val details = detailsLayout.editText?.text.toString()
                insertTodo(title, details)
            }
        }
    }

    private fun insertTodo(title: String, details: String) {
        val todo = Todo(
            name = title,
            details = details,
            date = calendar.clearTime().time
        )
        MyDataBase.getInstance(requireContext().applicationContext).todoDao().addTodo(todo)
        Toast.makeText(requireContext(), "Todo Added Successfully", Toast.LENGTH_LONG).show()
        onTodoAddedListener?.onTodoAdded()
        dismiss()
    }

    var onTodoAddedListener: OnTodoAddedListener? = null

    interface OnTodoAddedListener {
        fun onTodoAdded()
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

    val calendar = Calendar.getInstance()

    fun showDatePicker() {

        val datePicker = DatePickerDialog(
            requireContext(),
            object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.YEAR, year)

                    chooseDate.setText("" + day + "/" + (month + 1) + "/" + year)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}