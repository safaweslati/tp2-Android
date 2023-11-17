package com.tp.tp2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class StudentAdapter(private val data: ArrayList<Student>) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>(), Filterable {

    var dataFilterList = ArrayList<Student>()
    var presentStudents : ArrayList<Student> = data.filter { s -> s.present == true } as ArrayList<Student>
    var absentStudents : ArrayList<Student> = data.filter { s -> s.present == false } as ArrayList<Student>

    private var _filterType: FilterType = FilterType.ALL

    init {
        filterByPresence()
    }

    enum class FilterType {
        ALL,
        PRESENT,
        ABSENT
    }

    var filterType: FilterType
        get() = _filterType
        set(value) {
            _filterType = value
            filterByPresence()
        }

    private fun filterByPresence() {
        when (filterType) {
            FilterType.ALL -> dataFilterList = data
            FilterType.PRESENT -> dataFilterList = data.filter { it.present } as ArrayList<Student>
            FilterType.ABSENT -> dataFilterList = data.filter { !it.present } as ArrayList<Student>
        }
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var imageView: ImageView
        var checkbox: CheckBox

        init {
            textView = itemView.findViewById(R.id.textView)
            imageView = itemView.findViewById(R.id.imageView)
            checkbox = itemView.findViewById(R.id.checkbox)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataFilterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = dataFilterList[position]

        holder.textView.text = student.nom+" "+student.prenom

        // Set the image based on gender
        val imageRes = if (student.genre == "Male") {
            R.drawable.man
        } else {
            R.drawable.women
        }

        holder.imageView.setImageResource(imageRes)
        holder.checkbox.isChecked = student.present

        holder.checkbox.setOnCheckedChangeListener { view, value ->
            if(position < dataFilterList.size) {
                val positionInList = data.indexOf(dataFilterList[position])
                if (positionInList != -1) {
                    data[positionInList].present = value
                }
            }
        }


    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    dataFilterList = data
                } else {
                    val resultList = ArrayList<Student>()
                    for (student in data) {
                        if (student.prenom.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(student)
                        }
                    }
                    dataFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = dataFilterList
                return filterResults
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataFilterList = results?.values as ArrayList<Student>
                notifyDataSetChanged()
            }

        }
    }

}

