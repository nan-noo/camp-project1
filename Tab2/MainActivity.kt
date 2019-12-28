package com.example.imagetab

import androidx.appcompat.app.AppCompatActivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 1. 다량의 데이터
        // 2. Adapter
        // 3. AdapterView - GridView
        val img = intArrayOf(
            R.drawable.c,
            R.drawable.e,
            R.drawable.j,
            R.drawable.q,
            R.drawable.c,
            R.drawable.e,
            R.drawable.j,
            R.drawable.q,
            R.drawable.c,
            R.drawable.e,
            R.drawable.j,
            R.drawable.q,
            R.drawable.c,
            R.drawable.e,
            R.drawable.j,
            R.drawable.q,
            R.drawable.c,
            R.drawable.e,
            R.drawable.j,
            R.drawable.q
        ) // drawable 폴더에서 가져온 이미지

        // 커스텀 아답타 생성
        val adapter = MyAdapter(
            applicationContext,
            R.layout.row,   // GridView 항목의 레이아웃 row.xml
            img             // 데이터
        )

        val gv = findViewById<GridView>(R.id.gridView1)
        gv.adapter = adapter  // 커스텀 아답타를 GridView 에 적용

        val tv = findViewById<TextView>(R.id.textView1)

        // GridView 아이템을 클릭하면 상단 텍스트뷰에 position 출력
        // JAVA8 에 등장한 lambda expression 으로 구현했습니다. 코드가 많이 간결해지네요
        gv.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val positiontext = position + 1 // position 1 부터 시작!!
                tv.text = "position : $positiontext"
            }
    } // end of onCreate
} // end of class

internal class MyAdapter(var context: Context, var layout: Int, var img: IntArray) : BaseAdapter() {
    var inf: LayoutInflater

    init {
        inf = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return img.size
    }

    override fun getItem(position: Int): Any {
        return img[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null)
            convertView = inf.inflate(layout, null)
        val iv = convertView!!.findViewById(R.id.imageView1) as ImageView
        iv.setImageResource(img[position])

        return convertView
    }
}