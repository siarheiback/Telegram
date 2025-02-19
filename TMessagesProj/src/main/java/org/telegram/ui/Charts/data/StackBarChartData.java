package org.telegram.ui.Charts.data;


import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.bautrukevich.SegmentTree;

public class StackBarChartData extends ChartData {

    public int[] ySum;
    public SegmentTree ySumSegmentTree;

    public StackBarChartData(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        init();
    }

    public void init() {
        int n = lines.get(0).y.length;
        int k = lines.size();

        ySum = new int[n];
        for (int i = 0; i < n; i++) {
            ySum[i] = 0;
            for (int j = 0; j < k; j++) {
                ySum[i] += lines.get(j).y[i];
            }
        }

        ySumSegmentTree = new SegmentTree(ySum);
    }

    public int findMax(int start, int end) {
        return ySumSegmentTree.rMaxQ(start, end);
    }

}
