package com.example.noted

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noted.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: Adapter
    private lateinit var list: ArrayList<Info>
    private var isAllFabsVisible: Boolean = true

    private fun getLinesExceptFirst(inputString: String): String {
        val lines = inputString.split("\n")
        if (lines.size > 1) {
            return lines.subList(1, lines.size).joinToString("\n")
        }
        return ""
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val files =File(filesDir,"Notes").listFiles()
        //val folder = File(filesDir, "Notes")
        list = ArrayList()

        if(files!=null){
            for(file in files){
                if(file.isFile){
                    try {
                        val fileInputStream = FileInputStream(file)
                        val inputStreamReader = InputStreamReader(fileInputStream)
                        val bufferedReader = BufferedReader(inputStreamReader)
                        var line : String?
                        val lines= ArrayList<String>()
                        val stringBuilder = StringBuilder()

                        while(bufferedReader.readLine().also { line = it } != null){
                            lines.add(line!!)
                            stringBuilder.append(line).append("\n")
                        }
                        val firstLine = lines[0]

                        if (firstLine == "noTitle") {
                            // Process the first line here
                            // For example, you can print it along with the file name
                            list.add(Info("",getLinesExceptFirst(stringBuilder.toString()),file.name, file.lastModified()))
                            //Toast.makeText(this,file.lastModified().toString(),Toast.LENGTH_SHORT).show()
                        }else{
                            //Toast.makeText(this, "there is a title",Toast.LENGTH_LONG).show()
                            list.add(Info(firstLine,stringBuilder.toString(),file.name, file.lastModified()))
                        }

                        bufferedReader.close()
                    } catch (e: IOException) {
                        // Handle any exceptions that may occur during file reading for this file
                        e.printStackTrace()
                    }
                }
//                list.add(Info(files[i].name))
//                i++
            }
        }

        list = ArrayList(list.sortedByDescending { it.lastMod })

        adapter = Adapter(list, this)


        val itemTouchHelper = ItemTouchHelper(adapter.SwipeToDeleteCallback())
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.adapter=adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

//        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                //filterList(newText)
//                return true
//            }
//
//        })

        if (adapter.itemCount == 0) {
            binding.apply{
                NOTED.visibility = View.VISIBLE
                textView2.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        } else {
            binding.apply{
                NOTED.visibility = View.GONE
                textView2.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }

        }

        binding.apply {
            addNote.isVisible=false
            addVoice.isVisible=false
            voice.isVisible=false
            note.isVisible=false
        }

        isAllFabsVisible = false

        binding.addFab.setOnClickListener {
            if(!isAllFabsVisible){
                binding.apply{
                    addNote.isVisible=true
                    addVoice.isVisible=true
                    voice.isVisible=true
                    note.isVisible=true
                }
                isAllFabsVisible=true
            }else{
                binding.apply {
                    addNote.isVisible=false
                    addVoice.isVisible=false
                    voice.isVisible=false
                    note.isVisible=false
                }
                isAllFabsVisible=false
            }
        }

        binding.addNote.setOnClickListener {
            //Toast.makeText(this,"You'll be able to use this soon",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, NotesActivity::class.java)

            startActivity(intent)
        }
    }

//    private fun filterList(query: String?){
//        if(query!=null){
//            val filteredList = ArrayList<Info>()
//            for(i in list){
//                if(i.){
//
//                }
//            }
//        }
//    }
}