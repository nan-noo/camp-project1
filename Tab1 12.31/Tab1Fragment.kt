package com.example.basictabkt

import com.example.basictabkt.R
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import java.io.Serializable
import java.util.ArrayList
import java.util.LinkedHashSet

class Tab1Fragment : Fragment() {

    var activityInstance: Activity? = null

    class ContactItem : Serializable {
        var user_phNumber: String? = null
        var user_Name: String? = null
        //사진 부분
        var photo_id: Long = 0
        var person_id: Long = 0
        var id: Int = 0
//        val phNumberChanged: String
//            get() = user_phNumber!!.replace("-", "")

        //실제 연락처를 ArrayList 형태로 가져오는 함수
        //            if (contactItem.getUser_phNumber().startsWith("01")) {
        //                hashList.add(contactItem);
        //                //contactsList.add(myContact);
        //                Log.d("<<CONTACTS>>", "name=" + contactItem.getUser_Name() + ", phone=" + contactItem.getUser_phNumber());
        //            }

        override fun toString(): String {
            return this.user_phNumber!!
        }

        override fun hashCode(): Int {
            return user_phNumber.hashCode()
//            return phNumberChanged.hashCode()
        }

        fun equals(o: Serializable?): Boolean {
            return if (o is ContactItem) user_phNumber == o.user_phNumber else false

        }

    }
    private val contactList: ArrayList<ContactItem>
        get() {
            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_ID, ContactsContract.Contacts._ID)

            val selectionArgs: Array<String>? = null
            val sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"

            val cursor = context(activityInstance).contentResolver.query(uri, projection, null,
                selectionArgs, sortOrder)



            val hashlist = LinkedHashSet<ContactItem>()
            val contactItems: ArrayList<ContactItem>
            if (cursor!!.moveToFirst()) {
                do {
                    val photo_id = cursor!!.getLong(2)
                    val person_id = cursor!!.getLong(3)

                    val contactItem = ContactItem()
                    contactItem.user_phNumber = cursor.getString(0)
                    contactItem.user_Name = cursor.getString(1)
                    contactItem.photo_id = photo_id
                    contactItem.person_id = person_id

                    hashlist.add(contactItem)

                } while (cursor.moveToNext())
            }

            contactItems = ArrayList(hashlist)
            for (i in contactItems.indices) {
                contactItems[i].id = i
            }

            cursor.close()


            return contactItems
        }

    private var mArrayList: ArrayList<ContactItem>? = null
    private var mAdapter: CustomAdapter? = null
    private var count = -1

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        activityInstance = activity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //권한이 부여되어 있는지 확인
        val permissionCheckR = ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.READ_CONTACTS)
        val permissionCheckW = ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.WRITE_CONTACTS)
        val view = inflater.inflate(R.layout.fragment_tab1, container, false)



        //MainActivity 돌아올때마다 메세지 나옴
        if (permissionCheckR == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(context!!.applicationContext, "연락처 읽기권한 있음", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context!!.applicationContext, "연락처 읽기권한 없음", Toast.LENGTH_SHORT).show()

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if (ActivityCompat.shouldShowRequestPermissionRationale(activityInstance!!, android.Manifest.permission.READ_CONTACTS)) {
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(context!!.applicationContext, "연락처 읽기권한이 필요합니다", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(activityInstance!!, arrayOf(android.Manifest.permission.READ_CONTACTS), READ_CONTACTS_PERMISSION)
            } else {
                ActivityCompat.requestPermissions(activityInstance!!, arrayOf(android.Manifest.permission.READ_CONTACTS), READ_CONTACTS_PERMISSION)
            }

        }

        if (permissionCheckW == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(context!!.applicationContext, "연락처 쓰기권한 있음", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context!!.applicationContext, "연락처 쓰기권한 없음", Toast.LENGTH_SHORT).show()

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if (ActivityCompat.shouldShowRequestPermissionRationale(activityInstance!!, android.Manifest.permission.READ_CONTACTS)) {
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(context!!.applicationContext, "연락처 쓰기권한이 필요합니다", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(activityInstance!!, arrayOf(android.Manifest.permission.READ_CONTACTS), READ_CONTACTS_PERMISSION)
            } else {
                ActivityCompat.requestPermissions(activityInstance!!, arrayOf(android.Manifest.permission.READ_CONTACTS), READ_CONTACTS_PERMISSION)
            }

        }


        val mRecyclerView = view.findViewById(R.id.recyclerview_main_list) as RecyclerView
//        val mListitemView = view.findViewById(R.id.list_item) as androidx.constraintlayout.widget.ConstraintLayout
        val mLinearLayoutManager = LinearLayoutManager(context!!)
        mRecyclerView.layoutManager = mLinearLayoutManager
//        mRecyclerView.addOnItemTouchListener(OnItemTouchListener {
//
//
//        })

        mArrayList = contactList

        mAdapter = CustomAdapter(mArrayList, context!!)
        mRecyclerView.adapter = mAdapter


        val dividerItemDecoration = DividerItemDecoration(
            mRecyclerView.context,
            mLinearLayoutManager.orientation
        )
        mRecyclerView.addItemDecoration(dividerItemDecoration)
//        mListitemView.setOnLongClickListener(View.OnLongClickListener() {
//
//            context(activityInstance).contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, )
//        }
//        )
//        val buttonInsert = view.findViewById(R.id.button_main_insert) as Button
//        buttonInsert.setOnClickListener {
//
////          연락처 추가
//
//
//            mAdapter!!.notifyDataSetChanged()
//        }



        return view

    }

    //필요한건지 모르겠으나 grantResults[]는 요청한 권한의 허용 여부 확인 가능
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            READ_CONTACTS_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context!!.applicationContext, "연락처 열람권한 승인함", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context!!.applicationContext, "연락처 열람권한 거부함", Toast.LENGTH_SHORT).show()
            }
            WRITE_CONTACTS_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context!!.applicationContext, "연락처 쓰기권한 승인함", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context!!.applicationContext, "연락처 쓰기권한 거부함", Toast.LENGTH_SHORT).show()
            }

        }
    }

    companion object {
        internal val READ_CONTACTS_PERMISSION = 1
        internal val WRITE_CONTACTS_PERMISSION = 2

        fun context(activity: Activity?) : Context {
            return activity!!.applicationContext
        }
    }


}// Required empty public constructor


