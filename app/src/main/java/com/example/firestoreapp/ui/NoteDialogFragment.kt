package com.example.firestoreapp.ui

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.example.firestoreapp.NO_ID
import com.example.firestoreapp.databinding.AddNoteDialogBinding
import com.example.firestoreapp.domain.entity.Note
import com.example.firestoreapp.utils.toStdFormatString
import com.example.firestoreapp.utils.toStdLocalDate
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.android.support.AndroidSupportInjection
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class NoteDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModel: MainViewModel

    private var _binding: AddNoteDialogBinding? = null
    private val binding get() = _binding!!

    var currentNote: Note? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = AddNoteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSliders()

        setInitialValue()

        initDialogButtons()
        initButtonChooseDate()
    }

    private fun setInitialValue() {
        currentNote = arguments?.getParcelable<Note>(ADD_NOTE_DIALOG_DATA_KEY)
        if (currentNote == null) {
            currentNote =
                Note(dateTime = LocalDateTime.now(), pressure1 = 120, pressure2 = 80, pulse = 70)
        }

        currentNote?.let {
            binding.sliderPressure1.value = it.pressure1.toFloat()
            binding.sliderPressure2.value = it.pressure2.toFloat()
            binding.sliderPulse.value = it.pulse.toFloat()
            binding.chooseDateBtn.text = it.dateTime.toLocalDate().toStdFormatString()
            binding.idTv.text = it.id

            binding.timeTp.hour = it.dateTime.hour
            binding.timeTp.minute = it.dateTime.minute
        }
    }

    private fun initSliders() {

        binding.sliderPressure1.addOnChangeListener { _, value, _ ->
            binding.labelPressureValue1Tv.text = value.toInt().toString()
        }

        binding.sliderPressure2.addOnChangeListener { _, value, _ ->
            binding.labelPressureValue2Tv.text = value.toInt().toString()
        }

        binding.sliderPulse.addOnChangeListener { _, value, _ ->
            binding.labelPulseValueTv.text = value.toInt().toString()
        }
    }

    private fun initDialogButtons() {
        binding.okBtn.setOnClickListener {
            with(binding) {
                val newNote = Note(
                    id = currentNote?.id ?: NO_ID,
                    dateTime = LocalDateTime.of(
                        chooseDateBtn.text.toString().toStdLocalDate(),
                        LocalTime.of(timeTp.hour, timeTp.minute)
                    ),
                    pressure1 = sliderPressure1.value.toInt(),
                    pressure2 = sliderPressure2.value.toInt(),
                    pulse = sliderPulse.value.toInt()
                )
                viewModel.addNote(newNote)
            }
            this.dismiss()
        }

        binding.cancelBtn.setOnClickListener {
            this.dismiss()
        }
    }

    private fun initButtonChooseDate() {
        binding.chooseDateBtn.setOnClickListener {
            val localDate = binding.chooseDateBtn.text.toString().toStdLocalDate()
            val calendar = Calendar.getInstance()
            calendar.set(localDate.year, localDate.monthValue - 1, localDate.dayOfMonth)

            MaterialDatePicker.Builder
                .datePicker()
                .setTitleText("Выберите дату")
                .setSelection(calendar.timeInMillis)
                .build()
                .apply {
                    addOnPositiveButtonClickListener {
                        val date =
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .toStdFormatString()
                        binding.chooseDateBtn.text = date
                    }
                }
                .show(requireActivity().supportFragmentManager, DATE_PICKER_TAG)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        const val ADD_NOTE_DIALOG_DATA_KEY = "DIALOG_DATA_KEY"
        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
        fun newInstance(
            note: Note?,
        ): NoteDialogFragment =
            NoteDialogFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(ADD_NOTE_DIALOG_DATA_KEY, note)
                arguments = bundle
            }
    }
}