package edu.uw.ischool.kong314.quizdroid

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager

class PreferencesFragment : Fragment(R.layout.fragment_preferences) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = sharedPreferences.edit()
        val savedUrl = sharedPreferences.getString("quiz_url", "")
        val savedDuration = sharedPreferences.getString("duration", "")

        val button = view.findViewById<Button>(R.id.saveButton)
        val editTextUrl = view.findViewById<TextView>(R.id.urlEditText)
        editTextUrl.text = savedUrl
        val urlPattern = """^http[s]?://[www]?\w+.([\w+]+)?.?(edu|com|org|net)/?(.+)?(\w+.json)$"""
        val duration = view.findViewById<TextView>(R.id.frequencyEditText)
        duration.text = savedDuration

        button.setOnClickListener() {
            val url = editTextUrl.text.toString()
            if (Regex(urlPattern).matches(url) && duration.text.toString().isNotEmpty()) {
                editor.putString("quiz_url", url)
                editor.putString("duration", duration.text.toString())
                editor.apply()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please fill both fields with valid inputs.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
