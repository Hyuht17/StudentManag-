package com.example.studentmanager

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private val studentList = mutableListOf<Pair<String, String>>(
        "Nguyen Van A" to "1",
        "Tran Thi B" to "2",
        "Le Van C" to "3",
        "Pham Thi D" to "4",
        "Hoang Van E" to "5",
        "Dang Thi F" to "6",
        "Vu Van G" to "7",
        "Nguyen Thi H" to "8",
        "Tran Van I" to "9",
        "Le Thi J" to "`10",
        "Pham Van K" to "11",
        "Hoang Thi L" to "12",
        "Dang Van M" to "13",
        "Vu Thi N" to "14",
        "Nguyen Van O" to "15"
    )
    // Lưu danh sách (Họ tên, MSSV)
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listViewStudents)

        // Adapter hiển thị danh sách sinh viên
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList.map { "${it.first} - ${it.second}" })
        listView.adapter = adapter

        // Đăng ký context menu
        registerForContextMenu(listView)
    }

    // Tạo OptionMenu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Xử lý OptionMenu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_new -> {
                // Mở Activity thêm sinh viên
                val intent = Intent(this, AddStudentActivity::class.java)
                startActivityForResult(intent, REQUEST_ADD)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Tạo ContextMenu
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    // Xử lý ContextMenu
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            R.id.menu_edit-> {
                // Mở Activity chỉnh sửa
                val intent = Intent(this, AddStudentActivity::class.java)
                intent.putExtra("name", studentList[info.position].first)
                intent.putExtra("mssv", studentList[info.position].second)
                intent.putExtra("position", info.position)
                startActivityForResult(intent, REQUEST_EDIT)
            }
            R.id.menu_remove -> {
                // Xóa sinh viên
                studentList.removeAt(info.position)
                updateList()
            }
        }
        return super.onContextItemSelected(item)
    }

    // Nhận kết quả từ Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val name = data.getStringExtra("name") ?: return
            val mssv = data.getStringExtra("mssv") ?: return

            when (requestCode) {
                REQUEST_ADD -> {
                    studentList.add(Pair(name, mssv))
                }
                REQUEST_EDIT -> {
                    val position = data.getIntExtra("position", -1)
                    if (position >= 0) {
                        studentList[position] = Pair(name, mssv)
                    }
                }
            }
            updateList()
        }
    }

    private fun updateList() {
        adapter.clear()
        adapter.addAll(studentList.map { "${it.first} - ${it.second}" })
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val REQUEST_ADD = 1
        const val REQUEST_EDIT = 2
    }
}