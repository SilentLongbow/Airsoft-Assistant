package nz.ac.uclive.mjk141.airsoftloadout.ui.equipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import nz.ac.uclive.mjk141.airsoftloadout.R

class EquipmentFragment : Fragment() {

    private lateinit var equipmentViewModel: EquipmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        equipmentViewModel =
            ViewModelProvider(this).get(EquipmentViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_equipment, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        equipmentViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
