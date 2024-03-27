package org.shirakawatyu.handixikebackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Score {
    private String course;
    private String credit;
    private String catalog;
    private String scroll;
}
