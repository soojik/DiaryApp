package com.example.diaryapp

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

//이 Activity 위에서 Diary(캘린더), QuestionDiary(다이어리 작성)와 같은 Fragment들이 움직임
class DiaryActivity : AppCompatActivity() {
    lateinit var toolbar : Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var headerView: View

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

        headerView = navView.getHeaderView(0)
        user?.let {
            headerView.findViewById<TextView>(R.id.userEmail).text = user.email
        }
    }

    //이건 오른쪽 메뉴 뜨게 하는건데 이거 사실 지금은 안써서 지워도 되고.. 어떻게 하져
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    //이것도 네비게이터에 필요한거
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
