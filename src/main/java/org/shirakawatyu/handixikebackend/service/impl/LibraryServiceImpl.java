package org.shirakawatyu.handixikebackend.service.impl;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.shirakawatyu.handixikebackend.api.LibraryApi;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.LibraryService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LibraryServiceImpl implements LibraryService {
    @Resource(name = "libraryApi")
    LibraryApi libraryApi;

    @Override
    public Result getLibrary(HttpSession session) {
        BasicCookieStore cookieStore = (BasicCookieStore) session.getAttribute("cookieStore");
        return Result.ok().data(libraryApi.getBorrows(cookieStore, session));
    }

    @Override
    public Result queryBooks(HttpSession session, String bookName, int page) throws IOException {
        return Result.ok().data(libraryApi.queryBooks(session, bookName, page));
    }

    @Override
    public Result queryLocation(HttpSession session, String id) throws IOException {
        return Result.ok().data(libraryApi.getLocationOfBook(session, id));
    }
}