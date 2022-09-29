package healthcare.severance.parkinson

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner

class DiarySettingActivity01 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting01)

        val professors = arrayOf("Dr.KIM", "Dr.LEE", "Dr.PARK")
        val patient = arrayOf("LEE", "KIM", "PARK")

        initSpinner(findViewById(R.id.dProfessorSpinner), professors)
        initSpinner(findViewById(R.id.dPatientSpinner), patient)
    }

    fun initSpinner(spinner: Spinner, spinnerItem: Array<String>){
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItem)
        spinner.adapter = adapter
    }

    fun backButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun nextButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, DiarySettingActivity02::class.java)
        val patientName: String = findViewById<Spinner?>(R.id.dPatientSpinner).selectedItem.toString()
        val professorName: String = findViewById<Spinner?>(R.id.dProfessorSpinner).selectedItem.toString()
        intent.putExtra("patientName", patientName)
        intent.putExtra("professorName", professorName)

        startActivity(intent)
    }
}