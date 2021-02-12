package com.example.diaryapp

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


//이 Activity 위에서 Diary(캘린더), QuestionDiary(다이어리 작성)와 같은 Fragment들이 움직임
class DiaryActivity : AppCompatActivity() {
    lateinit var toolbar : Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var headerView: View
    lateinit var db : FirebaseFirestore

    var mBackWait : Long = 0

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        //toolbar 관련된 거는 navigation drawer라고 바에 왼쪽 메뉴 누르면 나오는 메뉴 창 구현할 때 필요
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        appBarConfiguration = AppBarConfiguration(
            setOf(
                    //드로어를 이용해 이동할 Fragment 지정
                    //Diary->QuestionDiary는 드로어에서 메뉴 선택을 통해 이동하는 것이 아니기에 QuestionDiary는 여기 없음, 아직 설정에 대한 Fragment는 안만들어서 없음
                R.id.navDiary, R.id.navTodo
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //user랑 headerview는 드로어 부분에 보면 이름이랑 이메일 띄우는 TextView가 있는데 firebase 연결해서 사용자정보 불러와서 띄우려고 구현해놓은거
        val user = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()

        headerView = navView.getHeaderView(0)
        user?.let {
            headerView.findViewById<TextView>(R.id.userEmail).text = user.email
            db.collection("users")
                .whereEqualTo("email", user.email)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents != null) {
                        for (document in documents) {
                            var get_user = document.data
                            var name = get_user["name"].toString()
                            headerView.findViewById<TextView>(R.id.userName).text = name
                        }
                    }
                }
        }
    }

    //이것도 네비게이터에 필요한거
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        if(System.currentTimeMillis() - mBackWait > 2000){
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else{
            ActivityCompat.finishAffinity(this)
            System.exit(0)
        }
    }
}
