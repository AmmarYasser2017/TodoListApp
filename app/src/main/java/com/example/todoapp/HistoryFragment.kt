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
import com.zerobranch.layout.SwipeLayout
import java.util.*

class HistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    lateinit var recyclerView: RecyclerView
    lateinit var todosList: MutableList<Todo>
    lateinit var swipeLayout: SwipeLayout

    val adapter = TodosRecyclerAdapter(null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        onItemClick()
        refreshTodoList()
        getTodosListFromDB()
        adapter.notifyDataSetChanged()
    }

    var calendar = Calendar.getInstance()


    fun getTodosListFromDB() {
        todosList = MyDataBase.getInstance(requireContext()).todoDao().getAllTodos().toMutableList()
        adapter.changeData(todosList.toMutableList())
        adapter.notifyDataSetChanged()
    }

    private fun initViews() {
        recyclerView = requireView().findViewById(R.id.todos_recyclerview)

        recyclerView.adapter = adapter

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
        val list = MyDataBase.getInstance(requireContext()).todoDao().getAllTodos()
        adapter.changeData(list as MutableList<Todo>)

    }


}