package com.tp.tp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Présence des Etudiants"
        val spinner : Spinner by lazy { findViewById(R.id.spinner) }
        val recycler : RecyclerView by lazy { findViewById(R.id.recycler) }
        val filter : EditText by lazy { findViewById(R.id.filterEditText) }

        val spinnerPresence: Spinner by lazy { findViewById(R.id.spinnerPresence) }

        val filterOptions = arrayOf("Tous", "Absents", "Presents")
        spinnerPresence.adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, filterOptions)


        var matieres = listOf<String>("Cours","TP")
        var students = arrayListOf<Student>()
        var studentAdapter = StudentAdapter(students)
        spinner.adapter = ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,matieres)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var toast = Toast.makeText(applicationContext, matieres[position], Toast.LENGTH_SHORT)
              toast.show()
                var studentsCours = arrayListOf<Student>(
                    Student("Salma", "Badri", "Female",true),
                    Student("Ahmed", "Ben Attia", "Male",true),
                    Student("Soumaya", "Besrour", "Female",true),
                    Student("Aziz", "Iheb", "Male",true),
                )
                var studentsTp = arrayListOf<Student>(
                    Student("Adem", "Jlassi", "Male",true),
                    Student("Maryem", "Ben Smida", "Female",false),
                    Student("Alma", "Torki", "Female",true),
                    Student("Mohamed", "Hammami", "Male",false),
                    Student("Isra", "Oueslati", "Female",true),
                    Student("Salma", "Rgaya", "Female",false)
                )

                if(matieres[position] == "Cours"){
                     studentAdapter = StudentAdapter(studentsCours)

                    filter.text.clear()
                }
                else {
                    studentAdapter = StudentAdapter(studentsTp)
                    filter.text.clear()
                }

                spinnerPresence.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        var toast1 = Toast.makeText(applicationContext, filterOptions[position], Toast.LENGTH_SHORT)
                        toast1.show()
                        when (position) {
                            0 -> studentAdapter.filterType = StudentAdapter.FilterType.ALL
                            1 -> studentAdapter.filterType = StudentAdapter.FilterType.ABSENT
                            2 -> studentAdapter.filterType = StudentAdapter.FilterType.PRESENT
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    }
                }

                recycler.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = studentAdapter
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }


        filter.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                studentAdapter.filter.filter(p0)

            }
            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }
}