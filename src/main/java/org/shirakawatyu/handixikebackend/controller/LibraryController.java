package org.shirakawatyu.handixikebackend.controller;

import lombok.RequiredArgsConstructor;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.LibraryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 图书馆信息
 *
 * @author HuYuanYang
 * @date 2024/03/25
 */
@RestController
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;


    /**
     * 获取图书信息
     *
     * @return {@code Result}
     * @throws IOException io异常
     */
    @GetMapping("/api/v2/extension/library")
    public Result getLibraryInfo() throws IOException {
        return libraryService.getLibrary();
    }

    /**
     * 查询书籍
     *
     * @param bookName 书名
     * @param page     页
     * @return {@code Result}
     * @throws IOException io异常
     */
    @GetMapping("/api/v2/extension/queryBooks")
    public Result queryBooks(@RequestParam("bookName") String bookName, @RequestParam("page") int page) throws IOException {
        return libraryService.queryBooks(bookName, page);
    }

    /**
     * 查询位置
     *
     * @param id 同上
     * @return {@code Result}
     * @throws IOException io异常
     */
    @GetMapping("/api/v2/extension/queryLocation")
    public Result queryLocation(@RequestParam("id") String id) throws IOException {
        return libraryService.queryLocation(id);

    }


}
