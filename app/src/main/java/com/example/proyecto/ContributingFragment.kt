package com.example.proyecto


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_contribution.*

class ContributingFragment : Fragment() {
    private lateinit var cancel : CancelListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.fragment_contribution,container,false)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cancel = activity as CancelListener
    }

    override fun onStart() {
        super.onStart()
        button_cancel.setOnClickListener {
            cancel.onCancelListener()
        }
    }
}

