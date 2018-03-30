package main;

public class Model {
    private String metric;
    private Integer value;
    private String counterType="GAUGE";
    private String tags="";

    public String getMetric() {
        return metric;
    }

    /**
     * 设置监测项名称
     * @param metric
     */
    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Integer getValue() {
        return value;
    }

    /**
     * 设置监测值
     * @param value
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    public String getCounterType() {
        return counterType;
    }

    /**
     * 设置监测值更新方式，有GAUGE和COUNTER两种类型，分别表示原始值和累加
     * @param counterType
     * 默认为GAUGE
     */
    public void setCounterType(String counterType) {
        this.counterType = counterType;
    }

    public String getTags() {
        return tags;
    }

    /**
     * 设置数据标签，格式为 "key1=value1 key2=value2..."
     * 默认为空
     * @param tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }
}
