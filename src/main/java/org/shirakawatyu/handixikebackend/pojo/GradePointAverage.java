package org.shirakawatyu.handixikebackend.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GradePointAverage {
    // 平均学分绩点
    private double all;
    // 必修课平均学分绩点
    private double required;

    public GradePointAverage(double all, double required) {
        this.all = all;
        this.required = required;
    }
}
