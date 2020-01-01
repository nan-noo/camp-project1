package com.example.basictabkt

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.provider.Contacts.People.loadContactPhoto
import android.provider.ContactsContract
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class CustomAdapter(private val mList: ArrayList<Tab1Fragment.ContactItem>?, private val mContext: Context) :
    RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

    interface ItemLongClick {
        fun onLongClick(view: View, position: Int)
    }
    var itemLongClick: ItemLongClick? = null

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var photo: ImageView? = null
        var name: TextView
        var phNumber: TextView


        init {
            this.photo = view.findViewById(R.id.photo_listitem) as ImageView
            this.name = view.findViewById(R.id.name_listitem) as TextView
            this.phNumber = view.findViewById(R.id.phNumber_listitem) as TextView
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CustomViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list, viewGroup, false)

        return CustomViewHolder(view)
    }


    override fun onBindViewHolder(viewholder: CustomViewHolder, position: Int) {

        viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        viewholder.phNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)

        viewholder.name.gravity = Gravity.CENTER
        viewholder.phNumber.gravity = Gravity.CENTER


        viewholder.photo?.setImageDrawable(mContext.resources.getDrawable(R.drawable.empty_profile))
        val profile = loadContactPhoto(mContext.contentResolver, mList!![position].person_id, mList[position].photo_id)
        viewholder.name.setText(mList!![position].user_Name)
        viewholder.phNumber.setText(mList?.get(position).user_phNumber)
        if (profile != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                viewholder.photo?.setBackground(ShapeDrawable(OvalShape()))
                viewholder.photo?.setClipToOutline(true)
            }
            viewholder.photo?.setImageBitmap(profile)
        } else {
            //            viewHolder.profile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img_profile_thumnail));
            if (Build.VERSION.SDK_INT >= 21) {
                viewholder.photo?.setClipToOutline(false)
            }
        }

        if(itemLongClick != null) {
            viewholder.itemView.setOnLongClickListener{
                v -> itemLongClick!!.onLongClick(v, position)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    fun loadContactPhoto(cr: ContentResolver, id: Long, photo_id: Long): Bitmap? {
        //        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        //        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        //        if (input != null)
        //            return resizingBitmap(BitmapFactory.decodeStream(input));
        //        else
        //            Log.d("<<CONTACT_PHOTO>>", "first try failed to load photo");

        var photoBytes: ByteArray? = null
        val photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id)
        val c = cr.query(
            photoUri,
            arrayOf(ContactsContract.CommonDataKinds.Photo.PHOTO),
            null,
            null,
            null
        )
        try {
            if (c!!.moveToFirst())
                photoBytes = c.getBlob(0)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            c!!.close()
        }

        if (photoBytes != null) {
            return resizingBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size))
        } else
            Log.d("<<CONTACT_PHOTO>>", "second try also failed")

        return null

    }

    fun resizingBitmap(oBitmap: Bitmap?): Bitmap? {
        if (oBitmap == null) {
            return null
        }

        var width = oBitmap.width.toFloat()
        var height = oBitmap.height.toFloat()
        val resizing_size = 30f

        var rBitmap: Bitmap?
        if (width > resizing_size) {
            val mWidth = width / 100
            val fScale = resizing_size / mWidth
            width *= fScale / 100
            height *= fScale / 100

        } else if (height > resizing_size) {
            val mHeight = height / 100
            val fScale = resizing_size / mHeight

            width *= fScale / 100
            height *= fScale / 100
        }

        //Log.d("rBitmap : " + width + ", " + height);

        rBitmap = Bitmap.createScaledBitmap(oBitmap, width.toInt(), height.toInt(), true)
        return rBitmap
    }

}