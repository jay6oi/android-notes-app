package com.example.noted

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.noted.databinding.ActivityMainBinding
import com.example.noted.databinding.ActivityNotesBinding
import com.example.noted.databinding.PreviewBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception

class Adapter(val list: ArrayList<Info>, val context: Context): RecyclerView.Adapter<Adapter.MyView>() {

    inner class MyView(val itemBinding: PreviewBinding): RecyclerView.ViewHolder(itemBinding.root){

    }

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
            // Get the position of the swiped item
            val position = viewHolder.adapterPosition
            // Delete the item from the adapter's data source
            deleteItem(position)
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
        if(list[position].title.equals("")){
            holder.itemBinding.tvItem.text = readFirstLine(list[position].note)
        }else{
            holder.itemBinding.tvItem.text = list[position].title
        }


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