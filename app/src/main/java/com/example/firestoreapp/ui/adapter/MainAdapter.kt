package com.example.firestoreapp.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firestoreapp.domain.entity.Note


class MainAdapter(
    private var onEditNoteClicked: ((Note) -> Unit)?,
    private var onDeleteNoteClicked: ((Note) -> Unit)?,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {

    private var data = mutableListOf<Note>()

    fun setData(data: List<Note>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position].isHeader) {
            true -> TITLE
            false -> NOTE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TITLE -> NoteTitleViewHolder(parent)
            NOTE -> NoteViewHolder(parent)
            else -> throw IllegalStateException()
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NoteViewHolder) {
            holder.bind(data[position], onEditNoteClicked)
        }
        if (holder is NoteTitleViewHolder) {
            holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
//        data.removeAt(fromPosition).apply {
//            data.add(
//                if (toPosition > fromPosition) toPosition - 1 else toPosition,
//                this
//            )
//        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        if (data[position].isHeader) {
            notifyItemChanged(position)
        } else {
            val note = data[position]
            data.removeAt(position)
            notifyItemRemoved(position)
            onDeleteNoteClicked?.invoke(note)
        }
    }


    companion object {
        const val NOTE = 1
        const val TITLE = 2
    }
}