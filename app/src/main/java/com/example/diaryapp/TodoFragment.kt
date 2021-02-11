package com.example.diaryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

//To-do 리스트 작성창
class TodoFragment : Fragment() {
    lateinit var recyclerView : RecyclerView
    lateinit var btnTodo : ImageButton
    lateinit var edtTodo : EditText

    val list = ArrayList<TodoList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        btnTodo = view.findViewById(R.id.btnTodo)
        edtTodo = view.findViewById(R.id.edtTodo)


        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerAdapter(list)
        }

        btnTodo.setOnClickListener {
            if(edtTodo.text.toString() != ""){
                list.add(TodoList(edtTodo.text.toString()))
                recyclerView.adapter?.notifyDataSetChanged()
                edtTodo.setText(null)
            }
            else {
                Toast.makeText(activity, "칸을 채워주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}