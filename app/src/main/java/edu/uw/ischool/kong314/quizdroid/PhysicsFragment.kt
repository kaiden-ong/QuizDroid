package edu.uw.ischool.kong314.quizdroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class PhysicsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_physics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backBtn = view.findViewById<Button>(R.id.backBtn)
        Log.d("Physics", "btn created")
        backBtn.setOnClickListener {
            Log.d("Physics", "Deleting fragment")
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}