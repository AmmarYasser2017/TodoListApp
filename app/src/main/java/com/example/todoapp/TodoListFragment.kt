package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.database.MyDataBase
import com.example.todoapp.database.model.Todo
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.zerobranch.layout.SwipeLayout
import java.util.*

class TodoListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    lateinit var recyclerView: RecyclerView
    lateinit var calenderView: MaterialCalendarView
    lateinit var todosList: MutableList<Todo>
    lateinit var swipeLayout: SwipeLayout

    val adapter = TodosRecyclerAdapter(null)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    override fun onResume() {
        super.onResume()
        getTodosListFromDB()
        onItemClick()
        refreshTodoList()
    }

    var calendar = Calendar.getInstance()


    fun getTodosListFromDB() {
        todosList = MyDataBase.getInstance(requireContext()).todoDao()
            .getTodosByDate(calendar.clearTime().time)
        adapter.changeData(todosList.toMutableList())
        adapter.notifyDataSetChanged()
    }

    private fun initViews() {
        recyclerView = requireView().findViewById(R.id.todos_recyclerview)
        calenderView = requireView().findViewById(R.id.calendarView)
        calenderView.selectedDate = CalendarDay.today()
        recyclerView.adapter = adapter

        calenderView.setOnDateChangedListener(fun(
            widget: MaterialCalendarView,
            date: CalendarDay,
            selected: Boolean
        ) {
            calendar.set(Calendar.DAY_OF_MONTH, date.day)
            calendar.set(Calendar.MONTH, date.month - 1)
            calendar.set(Calendar.YEAR, date.year)
            getTodosListFromDB()
            adapter.notifyDataSetChanged()
        })
        onItemClick()
    }


    private fun onItemClick() {
        adapter.onItemClicked = object : TodosRecyclerAdapter.OnItemClicked {
            override fun onItemClickToUpdate(todo: Todo) {
                var intent: Intent = Intent(requireContext(), UpdateTodoActivity::class.java)
                intent.putExtra("Todo", todo)
                startActivity(intent)
                adapter.notifyDataSetChanged()
            }

            override fun onItemClickToBeDeleted(position: Int, todo: Todo) {

                MyDataBase.getInstance(requireContext())
                    .todoDao()
                    .deleteTodo(todo)
                todosList.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyDataSetChanged()
                getTodosListFromDB()

                Toast.makeText(requireContext(), "Note Deleted", Toast.LENGTH_LONG).show()
            }

            override fun makeItDone(todo: Todo) {
                var newTodo = todo
                newTodo.isDone = true
                MyDataBase.getInstance(requireContext()).todoDao().updateTodo(newTodo)
                refreshTodoList()
                Toast.makeText(requireContext(), "Done", Toast.LENGTH_LONG).show()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun refreshTodoList() {
        val list = MyDataBase.getInstance(requireContext()).todoDao()
            .getTodosByDate(calendar.clearTime().time)
        adapter.changeData(list)
        adapter.notifyDataSetChanged()
    }


}