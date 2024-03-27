package org.shirakawatyu.handixikebackend.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.shirakawatyu.handixikebackend.api.LibraryApi;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.LibraryService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final LibraryApi libraryApi;
    private final HttpSession session;

    @Override
    public Result getLibrary() {
        BasicCookieStore cookieStore = (BasicCookieStore) session.getAttribute("cookieStore");
        return Result.ok().data(libraryApi.getBorrows(cookieStore));
    }

    @Override
    public Result queryBooks(String bookName, int page) throws IOException {
        return Result.ok().data(libraryApi.queryBooks(bookName, page));
    }

    @Override
    public Result queryLocation(String id) throws IOException {
        return Result.ok().data(libraryApi.getLocationOfBook(id));
    }
}