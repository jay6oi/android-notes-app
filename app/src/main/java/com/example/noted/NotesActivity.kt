package com.example.noted

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.noted.databinding.ActivityNotesBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception

class NotesActivity : AppCompatActivity() {
    private lateinit var notesBinding: ActivityNotesBinding
    private val FILE_NAME: String = "Note"
    private val EXT = ".txt"

    private fun getLinesExceptFirst(inputString: String): String {
        val lines = inputString.split("\n")
        if (lines.size > 1) {
            return lines.subList(1, lines.size).joinToString("\n")
        }
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesBinding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(notesBinding.root)

        val editTextNote = notesBinding.etNoteArea
        val editTextTitle = notesBinding.etTitle
        val noteContent = intent.getStringExtra("note_content")
        val titleContent = intent.getStringExtra("title_content")
        val fileName = intent.getStringExtra("file_name")

        if (noteContent != null) {
            if(titleContent.equals("")){
                editTextNote.setText(noteContent)
            }else{
                editTextNote.setText(getLinesExceptFirst(noteContent))
            }

        }

        if(titleContent!=null) {
            editTextTitle.setText(titleContent)
            //notesBinding.etNoteArea.setText(getLinesExceptFirst(notesBinding.etNoteArea.text.toString()))
        }

        notesBinding.button.setOnClickListener {
            saveNote(fileName.toString())
        }
    }

    private fun readTracker(): Int{
        var count = 0

        try {
            val fileInputStream = openFileInput("track.txt")
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            val firstLine = bufferedReader.readLine()

            if (firstLine != null) {
                // Process the first line here
                // For example, you can print it along with the file name
                count=firstLine.toInt()
                //Toast.makeText(this, firstLine,Toast.LENGTH_LONG).show()
            }

            bufferedReader.close()
        } catch (e: IOException) {
            // Handle any exceptions that may occur during file reading for this file
            e.stackTrace
            Toast.makeText(this, e.stackTrace.toString(),Toast.LENGTH_LONG).show()
        }catch (e: FileNotFoundException){
            e.stackTrace
            Toast.makeText(this, "errorFile!",Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Toast.makeText(this, "genException!",Toast.LENGTH_LONG).show()
        }

        return count
    }

    fun saveNote(fileName: String) {
        //getDir("Notes", MODE_PRIVATE)
        if(notesBinding.etNoteArea.text.isNotBlank()){
            val folderName = "Notes"
            val title = notesBinding.etTitle.text.toString()
            val note: String = notesBinding.etNoteArea.text.toString()
            val fos: FileOutputStream
            //val tFile = File("track.txt")
            val folder = File(filesDir,folderName)
            var count = 0

            if (!File(filesDir,"track.txt").exists()){
                try{
                    val tOutput =openFileOutput("track.txt", MODE_PRIVATE)
                    tOutput.write(count.toString().toByteArray())
                    //Toast.makeText(this, "i did this!",Toast.LENGTH_LONG).show()
                    tOutput.close()
                }catch (e: FileNotFoundException){
                    e.stackTrace
                }
                catch (e: IOException){
                    e.stackTrace
                }
            }

            if(!folder.exists()){
                folder.mkdirs()
            }

            val toWrite = if(notesBinding.etTitle.text.isBlank()){
                "noTitle\n"+note
            }else{
                title+"\n"+note
            }

            count = readTracker()


            try{
                val file = if(fileName!="null"){
                    File(folder, fileName)

                }else{
                    File(folder,FILE_NAME+count.toString()+EXT)
                }

                fos =FileOutputStream(file)
                fos.write(toWrite.toByteArray())
                notesBinding.etNoteArea.text.clear()

                Toast.makeText(this,"Noted!",Toast.LENGTH_SHORT).show()
                if(fos!=null){
                    fos.close()
                }
            }catch (e: FileNotFoundException){
                e.stackTrace
            }catch (e: IOException){
                e.stackTrace
            }

            count+=1
            try{
                val tOutput =openFileOutput("track.txt", MODE_PRIVATE)
                tOutput.write(count.toString().toByteArray())
                tOutput.close()
            }catch (e: FileNotFoundException){
                e.stackTrace
            }
            catch (e: IOException){
                e.stackTrace
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0,0)
            startActivity(intent)
            overridePendingTransition(0,0)
        }else{
            Toast.makeText(this, "Cannot save empty note", Toast.LENGTH_LONG).show()
        }

    }
}