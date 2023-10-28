package com.example.firestoreapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firestoreapp.databinding.ItemNoteBinding
import com.example.firestoreapp.domain.entity.Note
import com.example.firestoreapp.utils.toHourMinString

class NoteViewHolder private constructor(
    private val binding: ItemNoteBinding,

    ) : RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {

    constructor(parent: ViewGroup) : this(
        ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(
        data: Note,
        onEditNoteClicked: ((Note) -> Unit)?,
    ) {
        if (layoutPosition != RecyclerView.NO_POSITION) {
            with(binding) {
                pressure1Tv.text = data.pressure1.toString()
                pressure2Tv.text = data.pressure2.toString()
                pulseTv.text = data.pulse.toString()
                timeTv.text = data.dateTime.toHourMinString()

                root.setOnClickListener {
                    onEditNoteClicked?.invoke(data)
                }
            }
        }
    }

    override fun onItemSelected() {

    }

    override fun onItemClear() {

    }
}