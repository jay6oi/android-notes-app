package com.example.noted

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.noted.databinding.ActivityMainBinding
import com.example.noted.databinding.PreviewBinding
import java.io.File

class Adapter(private val list: ArrayList<Info>, private val context: Context): RecyclerView.Adapter<Adapter.MyView>() {
    inner class MyView(val itemBinding: PreviewBinding): RecyclerView.ViewHolder(itemBinding.root)



    inner class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            // Handle drag and drop if needed (not used in this case)
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val builder = AlertDialog.Builder(context)
            // Get the position of the swiped item
            val position = viewHolder.adapterPosition
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Delete") { dialog, id ->
                    // Delete selected note from database
                    deleteItem(position)
                    val intent = Intent(context, MainActivity::class.java)
                    (context as Activity).finish()
                    context.overridePendingTransition(0,0)
                    context.startActivity(intent)
                    context.overridePendingTransition(0,0)
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                    val intent = Intent(context, MainActivity::class.java)
                    (context as Activity).finish()
                    context.overridePendingTransition(0,0)
                    context.startActivity(intent)
                    context.overridePendingTransition(0,0)
                }
            val alert = builder.create()
            alert.show()


            // Delete the item from the adapter's data source
            //deleteItem(position)
        }
    }

    fun deleteItem(position: Int) {
        val file = File("/data/data/com.example.noted/files/Notes/"+list[position].fileName)
        file.delete()
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(PreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false) )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun readFirstLine(inputString: String): String? {
        val lines = inputString.split("\n")
        if (lines.isNotEmpty()) {
            return lines[0]
        }
        return null
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        if(list[position].title ==""){
            holder.itemBinding.tvItem.text = readFirstLine(list[position].note)
        }else{
            holder.itemBinding.tvItem.text = list[position].title
        }

        val tv1 = holder.itemView.findViewById<TextView>(R.id.NOTED)
        val tv2 = holder.itemView.findViewById<TextView>(R.id.textView2)

//        if(list.isEmpty()){
//            tv1.visibility = View.VISIBLE
//            tv2.visibility = View.VISIBLE
//        }else{
//            tv1.visibility = View.GONE
//            tv2.visibility = View.GONE
//        }


        holder.itemView.setOnClickListener {
            val noteContent = list[position].note
            val titleContent = list[position].title
            val fileName = list[position].fileName
            val intent = Intent(context, NotesActivity::class.java)
            intent.putExtra("note_content", noteContent)
            intent.putExtra("title_content", titleContent)
            intent.putExtra("file_name", fileName)
            context.startActivity(intent)
        }
    }
}