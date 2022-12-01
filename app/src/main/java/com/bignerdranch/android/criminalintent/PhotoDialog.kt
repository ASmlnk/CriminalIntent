package com.bignerdranch.android.criminalintent

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.io.File

private const val ARG_PHOTO = "photo"

class PhotoDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val photoFile = arguments?.getSerializable(ARG_PHOTO) as File
        //val bitmap = getScaledBitmap(photoFile.path, requireActivity())
       val bitmap = BitmapFactory.decodeFile(photoFile.path)
        val matrix: Matrix = Matrix()
        if (bitmap.height < bitmap.width) {
            matrix.postRotate(90F)
        } else {
            matrix.postRotate(0F)
        }

        val b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        val dialog = AlertDialog.Builder(requireActivity()).create()
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.photo_crime, null)
        dialog.setView(dialogLayout)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)

        val photo: ImageView = dialogLayout.findViewById(R.id.photo_crime_dialog) as ImageView
        photo.setImageBitmap(b)

        return dialog

        /*val photo = arguments?.getSerializable(ARG_PHOTO) as File
        photoUri = FileProvider.getUriForFile(requireActivity(), "com.bignerdranch.android.criminalintent.fileprovider", photo)
            val bitmap = getScaledBitmap(photo.path, requireActivity())
            photoCrimeDialog.setImageURI(photoUri)  *//*setImageBitmap(bitmap)*//*

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog*/
    }
    companion object {
        fun newInstance(photo: File): PhotoDialog {
            val args = Bundle().apply {
                putSerializable(ARG_PHOTO, photo)
            }

            return PhotoDialog().apply {
                arguments = args
            }
        }
    }

}
