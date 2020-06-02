package ml.asvsharma.bluetooth
import kotlinx.android.synthetic.main.activity_main.*
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_oscilloscope.*
import ml.asvsharma.bluetooth.MainActivity.Companion.m_bluetoothSocket
import ml.asvsharma.bluetooth.MainActivity.Companion.m_isConnected
import java.io.IOException
import java.util.*
import java.util.logging.Logger.global

@Suppress("DEPRECATION")
class OscilloscopeActivity : AppCompatActivity() {
    companion object{
        val entries = ArrayList<Entry>()
        val vl: LineDataSet = LineDataSet(entries, "Oscilloscope 1")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oscilloscope)
        supportActionBar?.hide()
        ReadFromDeviceAndUpdate().execute()
        vl.setDrawCircles(false)
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 2f
        vl.fillColor = Color.BLACK
        vl.setColor(Color.RED)
        lineChart.xAxis.labelRotationAngle = 0f
        lineChart.axisRight.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.highlightValue(null)
        lineChart.setPinchZoom(true)
        lineChart.setNoDataText("Not Getting Signal")
        lineChart.animateX(1800, Easing.EaseInExpo)
        lineChart.data = LineData(vl)
    }
    fun DrawGraph(){
        val vl = LineDataSet(entries, "Oscilloscope 1")
        vl.setDrawCircles(false)
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 2f
        vl.fillColor = Color.BLACK
        vl.setColor(Color.RED)
        lineChart.xAxis.labelRotationAngle = 0f
        lineChart.axisRight.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.highlightValue(null)
        lineChart.setPinchZoom(true)
        lineChart.setNoDataText("Not Getting Signal")
        lineChart.animateX(1800, Easing.EaseInExpo)
        lineChart.data = LineData(vl)
    }
    class  ReadFromDeviceAndUpdate():AsyncTask<Void,Float,Void>(){
        override fun onProgressUpdate(vararg values: Float?) {
            super.onProgressUpdate(*values)
        }
        override fun doInBackground(vararg params: Void?): Void? {
            if(m_bluetoothSocket?.isConnected!!) {
                val inputStream = m_bluetoothSocket?.inputStream
                val buffer = ByteArray(7)
                var bytes: Int
                var x = 0
                while (true) {
                    try {
                        if (inputStream != null) {
                            bytes = inputStream.read(buffer)
                            var value = String(buffer, 0, bytes)
                            var data = value.split('\n')[0]
                            var valuedata = data.toFloatOrNull()
                            if(valuedata!=null){
                                entries.add(Entry((System.currentTimeMillis()/1000).toFloat(),data.toFloat()))
                                vl.notifyDataSetChanged()
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return null
        }


    }


}
