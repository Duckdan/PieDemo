package study.com.piedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import study.com.piedemo.weight.PieChartView;

public class MainActivity extends AppCompatActivity {

    private PieChartView pcv;
    private List<DataBean> list = new ArrayList<>();
    private String[] colors = {"#FFFF00", "#FF7256", "#FF0000", "#BDBDBD", "#98FB98", "#7CFC00", "#080808", "#9400D3"};
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pcv = (PieChartView) findViewById(R.id.pcv);
        random = new Random();
        initData();
    }

    private void initData() {
        for (int i = 0; i < colors.length; i++) {
            int value = random.nextInt(10);
            value = value < 5 ? value + 5 : value;
            list.add(new DataBean(value, colors[i]));
        }
        pcv.setData(list);
    }
}
