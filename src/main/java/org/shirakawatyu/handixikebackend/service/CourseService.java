package org.shirakawatyu.handixikebackend.service;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface CourseService {
    String course(List<String> cookies);
}
