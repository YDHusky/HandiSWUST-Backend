package org.shirakawatyu.handixikebackend.service;

import org.shirakawatyu.handixikebackend.common.Result;

import java.io.IOException;

public interface LibraryService {
    Result getLibrary();

    Result queryBooks(String bookName, int page) throws IOException;

    Result queryLocation(String id) throws IOException;
}
