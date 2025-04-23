package com.bizsolutions.dealmate.ui

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentDialogBaseBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.textfield.TextInputEditText

open class FullscreenDialogFragment : DialogFragment() {
    protected var _baseBinding: FragmentDialogBaseBinding? = null
    protected val baseBinding get() = _baseBinding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        val colorSurfaceContainerHigh = MaterialColors.getColor(
            requireContext(),
            com.google.android.material.R.attr.colorSurfaceContainerHigh,
            Color.BLACK
        )
        dialog.window!!.setBackgroundDrawable(colorSurfaceContainerHigh.toDrawable())
        return dialog
    }

    override fun onStart() {
        super.onStart()

        if (dialog != null)
            dialog!!.window!!.setWindowAnimations(
                R.style.Theme_DealMate_FullscreenDialog_FadeAnimations
            )
    }

    override fun onPause() {
        super.onPause()

        if (requireDialog().currentFocus != null) {
            requireArguments().putInt("currentFocus", requireDialog().currentFocus!!.id)
        }
    }

    override fun onResume() {
        super.onResume()
        val currentFocus = requireArguments().getInt("currentFocus")
        if (currentFocus != 0) {
            try {
                baseBinding.root.findViewById<TextInputEditText>(currentFocus)?.requestFocus()
            } catch (_: ClassCastException) {
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.Theme_DealMate_FullscreenDialog
    }
}