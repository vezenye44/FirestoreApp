package com.example.firestoreapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firestoreapp.databinding.ItemNoteTitleBinding
import com.example.firestoreapp.domain.entity.Note

class NoteTitleViewHolder private constructor(
    private val binding: ItemNoteTitleBinding,
) : RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {

    constructor(parent: ViewGroup) : this(
        ItemNoteTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(data: Note) {
        if (layoutPosition != RecyclerView.NO_POSITION) {
            binding.tvDate.text =
                "${data.dateTime.dayOfMonth} ${data.dateTime.month} ${data.dateTime.year}"
        }
    }

    override fun onItemSelected() {

    }

    override fun onItemClear() {

    }
}