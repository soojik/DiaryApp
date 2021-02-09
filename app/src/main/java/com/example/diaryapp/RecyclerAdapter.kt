package com.example.diaryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(val items: ArrayList<TodoList>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(item: TodoList) {
            view.findViewById<TextView>(R.id.todoItem).text = item.todoName

            view.findViewById<ImageButton>(R.id.btnDelete).setOnClickListener {
                var position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    Toast.makeText(view.context, "삭제 버튼", Toast.LENGTH_SHORT).show()
                    items.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            bind(item)
        }
    }
}