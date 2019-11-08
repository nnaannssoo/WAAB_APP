package com.example.proyecto

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.item_ruta.view.*

class RutasAdapter(private val mContext: Context, private val listaRutas: List<Ruta>): BaseAdapter()
{
    override fun getItem(position: Int): Any {
        return listaRutas.get(position)
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getCount(): Int {
        return listaRutas.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout: View
        if(convertView==null)
        {
            layout = LayoutInflater.from(mContext).inflate(R.layout.item_ruta, parent, false )
            val nombreRutaTextView= layout.findViewById<TextView>(R.id.nombreRuta)
           // val nameTextView= layout.nombreRuta //-->Acceso Rápido
            val sentidoRutaTextView= layout.findViewById<TextView>(R.id.sentido)
            val imagenRuta= layout.findViewById<ImageView>(R.id.imagenRuta)
            val viewHolder= ViewHolder(nombreRutaTextView,sentidoRutaTextView,imagenRuta)
            layout.tag= viewHolder
        }
        else
        {
            layout= convertView
        }
        val ruta= listaRutas.get(position)
        val viewHolder= layout.tag as ViewHolder
        viewHolder.nombreTextView.text= ruta.nombre
        viewHolder.sentidoTextView.text=ruta.sentido
        viewHolder.nombreTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        viewHolder.sentidoTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        viewHolder.sentidoTextView.setSelected(true);
        viewHolder.nombreTextView.setSelected(true);
        viewHolder.imagenView.setImageResource(ruta.id);
        return layout
    }
    private class ViewHolder(val nombreTextView: TextView, val sentidoTextView: TextView, val imagenView: ImageView)
    {

    }
}