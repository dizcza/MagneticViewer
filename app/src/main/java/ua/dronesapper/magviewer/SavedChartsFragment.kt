package ua.dronesapper.magviewer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

internal class ChartDataAdapter(context: Context?, objects: List<LineDataLabeled>?) :
    ArrayAdapter<LineDataLabeled?>(
        context!!, 0, objects!!
    ) {
    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertViewArg: View?, parent: ViewGroup): View {
        var convertView = convertViewArg
        val data = getItem(position)
        val chart: LineChart
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                R.layout.saved_chart_item, null
            )
            chart = convertView.findViewById(R.id.chart)
            convertView.tag = chart
        } else {
            chart = convertView.tag as LineChart
        }
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.data = data
        val description = Description()
        description.text = data!!.label
        chart.description = description
        chart.animateY(700)
        return convertView!!
    }
}

internal class LineDataLabeled(dataSet: ILineDataSet?, val label: String) : LineData(dataSet)
class SavedChartsFragment : Fragment() {
    private lateinit var mAdapter: ChartDataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestReadExternal =
            registerForActivityResult<String, Boolean>(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    mAdapter.addAll(loadCharts())
                } else {
                    parentFragmentManager.popBackStack()
                }
            }
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestReadExternal.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.saved_chart_list, container, false)
    }

    private fun loadCharts(): List<LineDataLabeled> {
        val charts: MutableList<LineDataLabeled> = ArrayList()
        val root = Environment.getExternalStorageDirectory()
        val folder = File(root.absolutePath, Constants.RECORDS_FOLDER)
        val files = folder.listFiles()
        if (!folder.exists() || files == null) {
            return charts
        }
        for (file in files) {
            val chartEntries: MutableList<Entry> = ArrayList()
            var descriptionText = ""
            try {
                BufferedReader(FileReader(file)).use { br ->
                    descriptionText = br.readLine()
                    var line: String
                    while (br.readLine().also { line = it } != null) {
                        val xy = line.split(",").toTypedArray()
                        val time = xy[0].toFloat()
                        val dp = xy[1].toFloat()
                        chartEntries.add(Entry(time, dp))
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val dataset = LineDataSet(chartEntries, file.name.replace(".txt", ""))
            charts.add(LineDataLabeled(dataset, descriptionText))
        }
        return charts
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listView = view.findViewById<ListView>(R.id.charts_list)
        val charts = loadCharts()
        mAdapter = ChartDataAdapter(context, charts)
        listView.adapter = mAdapter
    }
}