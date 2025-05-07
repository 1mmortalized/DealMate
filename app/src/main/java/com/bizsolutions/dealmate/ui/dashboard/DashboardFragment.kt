package com.bizsolutions.dealmate.ui.dashboard

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.util.TypedValueCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.data.currency.CurrencyManager
import com.bizsolutions.dealmate.databinding.FragmentDashboardBinding
import com.bizsolutions.dealmate.db.DealWithClient
import com.bizsolutions.dealmate.ext.getThemeColor
import com.bizsolutions.dealmate.ui.dashboard.charts.AmountAxisFormatter
import com.bizsolutions.dealmate.ui.dashboard.charts.CurrencyDealCount.Companion.countDealsByCurrency
import com.bizsolutions.dealmate.ui.dashboard.charts.IncomeChartMarker
import com.bizsolutions.dealmate.ui.dashboard.charts.MonthAxisFormatter
import com.bizsolutions.dealmate.ui.dashboard.charts.MonthlyIncome.Companion.getMonthlyIncome
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.roundToInt

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val anim = MaterialFadeThrough().apply {
            addTarget(R.id.fragment_dashboard)
        }

        enterTransition = anim
        exitTransition = anim
        reenterTransition = anim
        returnTransition = anim
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        viewModel.allDeals.observe(viewLifecycleOwner) { dealsList ->
            val avgIncome = dealsList.map {
                val currencyCode = it.deal.currency.uppercase()
                val currencyRate = CurrencyManager.currencyRates?.get(currencyCode)?.toFloat() ?: 1f
                it.deal.amount / currencyRate
            }
                .average()
                .roundToInt()

            @SuppressLint("SetTextI18n")
            binding.fragmentDashboardAvgIncomeTxt.text = "%d USD".format(avgIncome)

            val topCurrency = dealsList.groupingBy { it.deal.currency }
                .eachCount()
                .maxByOrNull { it.value }
                ?.key

            binding.fragmentDashboardTopCurrencyTxt.text = topCurrency

            binding.fragmentDashboardTotalDealsTxt.text = "%d".format(dealsList.size)

            val bestMonth = dealsList.groupBy { YearMonth.from(it.deal.date) }
                .mapValues { entry -> entry.value.sumOf { it.deal.amount } }
                .maxByOrNull { it.value }
                ?.key

            binding.fragmentDashboardBestMonthTxt.text =
                bestMonth?.format(DateTimeFormatter.ofPattern("LLLL yyyy"))
                    ?.replaceFirstChar { it.uppercase() }

            setupIncomeChart(dealsList)
            setupCurrencyChart(dealsList)
        }

        return binding.root
    }

    private fun setupIncomeChart(dealsList: List<DealWithClient>) {
        val colorOnSurface = requireContext().getThemeColor(com.google.android.material.R.attr.colorOnSurface)
        val colorPrimary = requireContext().getThemeColor(androidx.appcompat.R.attr.colorPrimary)

        binding.fragmentDashboardIncomeChart.apply {
            setScaleEnabled(true)

            isScaleYEnabled = false
            isDoubleTapToZoomEnabled = true

            isHighlightPerTapEnabled = true
            legend.isEnabled = false
            axisRight.isEnabled = false
            description.isEnabled = false
            isDragDecelerationEnabled = false
            isAutoScaleMinMaxEnabled = true

            setDrawGridBackground(false)
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)

            axisLeft.axisMinimum = 0f

            xAxis.granularity = 1f
            axisLeft.granularity = 1f

            xAxis.setLabelCount(6, false)
            axisLeft.setLabelCount(5, false)

            xAxis.axisLineColor = colorOnSurface
            axisLeft.axisLineColor = colorOnSurface

            xAxis.textColor = colorOnSurface
            axisLeft.textColor = colorOnSurface

            xAxis.position = XAxis.XAxisPosition.BOTTOM

            val fontSizePx =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11f, resources.displayMetrics)
            val fontSizeDp = TypedValueCompat.pxToDp(fontSizePx, resources.displayMetrics)

            axisLeft.textSize = fontSizeDp
            xAxis.textSize = fontSizeDp

            extraBottomOffset = 16f

            xAxis.yOffset = 10f
            axisLeft.xOffset = 10f

            setNoDataTextColor(colorOnSurface)
            setNoDataText(getString(R.string.chart_no_data))

            xAxis.labelRotationAngle = -15f
        }

        val monthlyIncome = getMonthlyIncome(dealsList)
        val incomeDataEntries: ArrayList<BarEntry> = ArrayList()

        monthlyIncome.indices.forEach { i ->
            incomeDataEntries.add(BarEntry(
                i.toFloat(), monthlyIncome[i].total.toFloat()
            ))
        }
        val incomeDataSet = BarDataSet(incomeDataEntries, null).apply {
            axisDependency = YAxis.AxisDependency.LEFT

            color = colorPrimary

            highLightColor = colorOnSurface
            setDrawValues(false)
        }

        binding.fragmentDashboardIncomeChart.apply {
            axisLeft.valueFormatter = AmountAxisFormatter()
            xAxis.valueFormatter = MonthAxisFormatter(monthlyIncome)

            marker = IncomeChartMarker(requireContext(), R.layout.item_marker, monthlyIncome)

            data = BarData(incomeDataSet)

            fitScreen()
            zoom(max(incomeDataEntries.size / 10f, 1f), 1f, incomeDataEntries.lastIndex.toFloat(), 0f,
                YAxis.AxisDependency.RIGHT)

            animateY(1000, Easing.EaseOutCubic)
        }
    }

    private fun setupCurrencyChart(dealsList: List<DealWithClient>) {
        val colorSurface = requireContext().getThemeColor(com.google.android.material.R.attr.colorSurface)
        val colorOnSurface = requireContext().getThemeColor(com.google.android.material.R.attr.colorOnSurface)

        binding.fragmentDashboardCurrencyChart.apply {
            setHoleColor(Color.TRANSPARENT)
            setTransparentCircleColor(
                colorSurface
            )

            description.isEnabled = false
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            legend.orientation = Legend.LegendOrientation.VERTICAL

            legend.textColor = colorOnSurface
            val legendTextSizePx =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics)
            val legendTextSizeDp = TypedValueCompat.pxToDp(legendTextSizePx, resources.displayMetrics)
            legend.textSize = legendTextSizeDp
            legend.formSize = legendTextSizeDp

            extraRightOffset = 32f

            setDrawEntryLabels(false)

            setCenterTextColor(colorOnSurface)

            val centerTextSizePx =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11f, resources.displayMetrics)
            val centerTextSizeDp = TypedValueCompat.pxToDp(centerTextSizePx, resources.displayMetrics)
            setCenterTextSize(centerTextSizeDp)

            setNoDataTextColor(colorOnSurface)
            setNoDataText(getString(R.string.chart_no_data))
        }

        val currencyDeals = countDealsByCurrency(dealsList)
        val currencyDataEntries: ArrayList<PieEntry> = ArrayList()

        currencyDeals.indices.forEach { i ->
            currencyDataEntries.add(PieEntry(
                currencyDeals[i].count.toFloat(),
                currencyDeals[i].currencyCode
            ))

        }

        val currencyDataSet = PieDataSet(currencyDataEntries, null).apply {
            colors = generateHarmonizedColors(currencyDataEntries.size)
            setDrawValues(false)
        }

        binding.fragmentDashboardCurrencyChart.setOnChartValueSelectedListener(object: OnChartValueSelectedListener {
            override fun onValueSelected(
                e: Entry?,
                h: Highlight?
            ) {
                val index = h?.x?.toInt() ?: 0
                (e as? PieEntry)?.let { entry ->
                    val currencyDeal = currencyDeals[index]

                    val decimalFormat = DecimalFormat("#,###")
                    val formattedAmount = decimalFormat.format(currencyDeal.amount)

                    binding.fragmentDashboardCurrencyChart.centerText =
                        "%d deal(s)\n%s %s".format(
                            currencyDeal.count,
                            formattedAmount,
                            currencyDeal.currencyCode.uppercase()
                        )
                }
            }

            override fun onNothingSelected() {
                binding.fragmentDashboardCurrencyChart.centerText = null
            }
        })

        binding.fragmentDashboardCurrencyChart.data = PieData(currencyDataSet)
        binding.fragmentDashboardCurrencyChart.animateY(1000, Easing.EaseOutCubic)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generateHarmonizedColors(
        size: Int
    ): List<Int> {
        return List(size) { index ->
            val baseHSL = FloatArray(3)
            ColorUtils.colorToHSL(
                requireContext().getThemeColor(androidx.appcompat.R.attr.colorPrimary),
                baseHSL
            )

            val hue = (360f / size) * index
            val hsl = floatArrayOf(hue, baseHSL[1], baseHSL[2])

            val resultHSL = FloatArray(3)
            ColorUtils.blendHSL(hsl, baseHSL, 0.2f, resultHSL)
            ColorUtils.HSLToColor(resultHSL)
        }
    }
}