package com.example.todoapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.database.model.Todo
import com.zerobranch.layout.SwipeLayout

class TodosRecyclerAdapter(var items: MutableList<Todo>?) :
    RecyclerView.Adapter<TodosRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.description)
        val leftView: ImageView = itemView.findViewById(R.id.left_view)
        val rightView: ImageView = itemView.findViewById(R.id.right_view)
        val cardView: CardView = itemView.findViewById(R.id.card_view)
        val markASDone: ImageView = itemView.findViewById(R.id.mark_as_done)
        val done: TextView = itemView.findViewById(R.id.done)
        val sideLine: View = itemView.findViewById(R.id.line)
        val swipeLayout: SwipeLayout = itemView.findViewById(R.id.swipe_layout)

    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items!!.get(position)
        holder.title.setText(item.name)
        holder.description.setText(item.details)
        if (item.isDone == true) {
            holder.title.setTextColor(Color.GREEN)
            holder.sideLine.setBackgroundColor(Color.GREEN)
            holder.markASDone.setBackgroundColor(Color.WHITE)
            holder.done.setText("Done!")
        }



        holder.swipeLayout.setOnActionsListener(object : SwipeLayout.SwipeActionsListener {
            override fun onOpen(direction: Int, isContinuous: Boolean) {

                holder.leftView.setOnClickListener(View.OnClickListener {
                    onItemClicked?.onItemClickToBeDeleted(holder.adapterPosition, item)

                })


                holder.rightView.setOnClickListener(View.OnClickListener {
                    onItemClicked?.onItemClickToUpdate(item)
                })

            }

            override fun onClose() {

            }
        })

        holder.markASDone.setOnClickListener(View.OnClickListener {
            onItemClicked?.makeItDone(item)
        })


    }

    fun changeData(newItems: MutableList<Todo>) {
        items = newItems
        notifyDataSetChanged()
    }


    var onItemClicked: OnItemClicked? = null

    interface OnItemClicked {
        fun onItemClickToUpdate(todo: Todo)
        fun onItemClickToBeDeleted(position: Int, todo: Todo)
        fun makeItDone(todo: Todo)
    }

}