package org.shirakawatyu.handixikebackend.controller;

import org.shirakawatyu.handixikebackend.service.CourseService;
import org.shirakawatyu.handixikebackend.utils.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

@Controller
public class CourseController {

    @Autowired
    CourseService courseService;

    @GetMapping("/course")
    @ResponseBody
    public String course(HttpSession session) {
        if(session.getAttribute("cookies") == null) {
            return "3401 LOGOUT";
        }
        return courseService.course(ArrayUtils.arrayToList((Object[]) session.getAttribute("cookies")));
    }
}
