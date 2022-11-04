package healthcare.severance.parkinson.adapter

import android.app.TimePickerDialog
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import healthcare.severance.parkinson.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter(val items: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    /**
     * 화면에 표시된 아이템 뷰의 종류 저장
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val medicineTimeButton: Button

        init {
            // 뷰홀더를 위한 클릭 리스너 정의
            medicineTimeButton = view.findViewById(R.id.dMedicineTimeButton)
        }
    }

    // viewType 형태의 아이템 뷰를 위한 ViewHolder를 생성(레이아웃 매니저로부터 호출)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list, viewGroup, false)

        return ViewHolder(view)
    }

    // 생성된 ViewHolder에 데이터를 바인딩(커스텀 데이터로 교체)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.medicineTimeButton.setOnClickListener { v ->

            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                //사용자가 선택한 시간 세팅
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                //선택한 시간 Display
                val userInput = SimpleDateFormat("HH:mm").format(cal.time)
                items[position] = userInput
                //선택한 버튼에 대해서만 적용
                val medicineTimeSelectButton = v.findViewById<Button>(v.id)
                medicineTimeSelectButton.text = userInput
                medicineTimeSelectButton.setTextColor(Color.WHITE)
                medicineTimeSelectButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
            }
            TimePickerDialog(v.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), true).show()
        }
        viewHolder.itemView.layoutParams.height = 110
    }

    // 전체 아이템 개수
    override fun getItemCount(): Int {
        return items.size
    }
}