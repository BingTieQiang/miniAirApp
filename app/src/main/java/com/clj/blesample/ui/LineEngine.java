package com.clj.blesample.ui;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.Random;

/**
 * 这是个动态折线图的工具类
 */

public class LineEngine {
    private String title;
    private int max;
    private LineChart mChart;

    public LineEngine(String title) {
        this.title = title;
    }
    public void setMax(int val){
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMaxValue(val);
    }
    public void setView(Context context,int max,LineChart mChart) {
        this.max = max;
        this.mChart = mChart;

        mChart.setDescription("描述");
        mChart.setScaleEnabled(false);

        LineData lineData = new LineData();
        lineData.setValueTextColor(0xFF000000);
        mChart.setData(lineData);

        Legend legend = mChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(15f);
        legend.setTextColor(Color.BLACK);
        legend.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15f);
        xAxis.setGridLineWidth(1f);


        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMaxValue(max);
        yAxis.setAxisMinValue(0);
        yAxis.setTextSize(15f);

        YAxis axisRight = mChart.getAxisRight();
        axisRight.setDrawGridLines(false);
        axisRight.setXOffset(15f);
        mChart.setDrawGridBackground(true);


    }

    public void update(float values,int dele) {
        if (values <= 0) {
            values = new Random().nextInt(max);
        }
        setMax(  (int)  Math.ceil(values*(dele)));
        LineData data = mChart.getData();

//        LineDataSet set = data.getDataSetByIndex(0);
        LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
        if (set == null) {
            set = createDataSet();
            data.addDataSet(set);
        }

        data.addXValue(String.valueOf(data.getXValCount()));

        Entry entry = new Entry(values, set.getEntryCount());
        data.addEntry(entry,0);
        mChart.setVisibleXRangeMaximum(15);//可见X范围最大
        mChart.notifyDataSetChanged();//通知更新速度

        mChart.moveViewToX(mChart.getXValCount()-2);//对X移动视图
//        mChart.moveViewToX(mChart.-2);//对X移动视图
    }

    private LineDataSet createDataSet() {
        LineDataSet dataSet = new LineDataSet(null,title);
        dataSet.setCircleSize(2f);
        dataSet.setLineWidth(1f);
        dataSet.setCircleColor(Color.RED);
        dataSet.setDrawCubic(true);

        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(Color.GREEN);
        return dataSet;
    }

}
