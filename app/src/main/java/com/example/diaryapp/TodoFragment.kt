package com.example.diaryapp

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

//To-do 리스트 작성창
class TodoFragment : Fragment() {
    val TAG = "TodoFragment"
    lateinit var recyclerView: RecyclerView
    lateinit var btnTodo: ImageButton
    lateinit var edtTodo: EditText
    lateinit var name: String

    lateinit var db: FirebaseFirestore

    lateinit var myHelper: myDBHelper
    lateinit var sqlDB: SQLiteDatabase

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

        val user = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()

        user?.let {
            db.collection("users")
                    .whereEqualTo("email", user.email)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents != null) {
                            for (document in documents) {
                                var get_user = document.data
                                var get_name = get_user["name"].toString()
                                name = get_name
                            }
                        }
                    }
        }

        user?.let{
            myHelper = myDBHelper(view.context, user.email + "_TodoDB")
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerAdapter(list)
        }

        sqlDB = myHelper.readableDatabase
        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT * FROM TodoTBL;", null)

        while (cursor.moveToNext()) {
            list.add(TodoList(cursor.getString(0)))
        }

        recyclerView.adapter?.notifyDataSetChanged()

        cursor.close()
        sqlDB.close()

        btnTodo.setOnClickListener {
            val TodoContent = edtTodo.text.toString()

            if (TodoContent != "") {
                if(valid(TodoContent)){
                    sqlDB = myHelper.writableDatabase
                    sqlDB.execSQL("INSERT INTO TodoTBL VALUES ('" + TodoContent + "');")
                    sqlDB.close()

                    list.clear()    //먼저 리스트 초기화하고 DB에서 불러옴
                    sqlDB = myHelper.readableDatabase
                    var cursor: Cursor
                    cursor = sqlDB.rawQuery("SELECT * FROM TodoTBL;", null)

                    while (cursor.moveToNext()) {
                        list.add(TodoList(cursor.getString(0)))
                    }

                    recyclerView.adapter?.notifyDataSetChanged()
                    edtTodo.setText(null)

                    cursor.close()
                    sqlDB.close()

                    Toast.makeText(activity, "입력됨", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "칸을 채워주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun valid(TodoContent: String) : Boolean {
        var valid = true

        sqlDB = myHelper.readableDatabase
        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT * FROM TodoTBL;", null)

        while (cursor.moveToNext()) {
            if (cursor.getString(0) == TodoContent){
                Toast.makeText(activity, "중복되는 할일은 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                valid = false
            }
        }

        cursor.close()
        sqlDB.close()

        return valid
    }
}