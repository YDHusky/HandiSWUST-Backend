package org.shirakawatyu.handixikebackend.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.shirakawatyu.handixikebackend.service.LibraryService;
import org.shirakawatyu.handixikebackend.utils.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class LibraryController {
    @Autowired
    LibraryService libraryService;

    @GetMapping("/library")
    public String getLibraryInfo(HttpSession session){
        return libraryService.getLibrary (ArrayUtils.arrayToList((Object[]) session.getAttribute("cookies")));


    }
}
