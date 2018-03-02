package study.com.piedemo;

/**
 * Created by Administrator on 2018/3/2.
 */

public class DataBean {
    private int value;
    private String color;

    public DataBean(int value, String color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
