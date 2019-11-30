 package com.example.proyecto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_opciones_sheet_ex.view.*
import java.lang.ClassCastException

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [opcionesSheetEx.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [opcionesSheetEx.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class opcionesSheetEx : BottomSheetDialogFragment() {
    private var mBottomSheetListener:BottomListener?=null

    private var posicion: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_opciones_sheet_ex, container, false)
        val bundle : Bundle= Bundle()

        val intentMap: Intent = Intent(this.context,mapasVista::class.java)
        val intentReg: Intent =  Intent(this.context,RegistroActivity::class.java)
        val intentTime: Intent =  Intent(this.context,TimeRequestActivity::class.java)
        v.registroTextView.setOnClickListener {
            startActivity(intentReg)
            dismiss()
        }
        v.mapaTextView.setOnClickListener {
            startActivity(intentMap)
            dismiss()
        }
        v.timeTextView.setOnClickListener {
            startActivity(intentTime)
            dismiss()
        }
        return v
    }
        interface BottomListener
        {
        fun redirigir(ruta: Int)

        }
    public fun setPosition(position:Int)
    {
         this.posicion= position
    }

    public fun getPosition(position:Int): Int {
        return this.posicion;
    }
    override fun onAttach(context:Context?)
    {
        super.onAttach(context)
        try {

             mBottomSheetListener= context as BottomListener?
        }
        catch(e: ClassCastException)
        {
            throw ClassCastException(context!!.toString())
        }
    }

}
