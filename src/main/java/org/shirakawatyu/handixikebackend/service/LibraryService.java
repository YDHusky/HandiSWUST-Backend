package org.shirakawatyu.handixikebackend.service;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface LibraryService {
    public String getLibrary(List<String> cookie);
}
