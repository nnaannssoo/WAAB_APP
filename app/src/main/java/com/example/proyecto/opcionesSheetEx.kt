 package com.example.proyecto

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_opciones_sheet_ex.*
import kotlinx.android.synthetic.main.fragment_opciones_sheet_ex.view.*
import java.lang.ClassCastException


 // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        v.registroTextView.setOnClickListener {

            Toast.makeText(v.context, "Posicion a redireccionar: "+posicion.toString(), Toast.LENGTH_SHORT).show();
            startActivity(intentReg)
            dismiss()
        }
        v.mapaTextView.setOnClickListener {
            startActivity(intentMap)
            //intent.putExtra("ruta",posicion.toString())
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
