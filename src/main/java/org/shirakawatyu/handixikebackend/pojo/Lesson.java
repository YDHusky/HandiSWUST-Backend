package org.shirakawatyu.handixikebackend.pojo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Lesson implements Serializable {
    @Serial
    private static final long serialVersionUID = -4978423418111694244L;
    private String jw_course_code;
    private String base_teacher_name;
    private String base_room_name;
    private String week;
    private String jw_task_book_no;
    private String jw_course_name;
    private String section_end;
    private String week_day;
    private String section;
    private String base_teacher_no;
    private String section_start;

    public Lesson() {
    }

    public Lesson(String jw_course_code, String base_teacher_name, String base_room_name, String week, String jw_task_book_no, String jw_course_name, String section_end, String week_day, String section, String base_teacher_no, String section_start) {
        this.jw_course_code = jw_course_code;
        this.base_teacher_name = base_teacher_name;
        this.base_room_name = base_room_name;
        this.week = week;
        this.jw_task_book_no = jw_task_book_no;
        this.jw_course_name = jw_course_name;
        this.section_end = section_end;
        this.week_day = week_day;
        this.section = section;
        this.base_teacher_no = base_teacher_no;
        this.section_start = section_start;
    }


}
