package org.shirakawatyu.handixikebackend.controller;

import jakarta.servlet.http.HttpSession;
import org.shirakawatyu.handixikebackend.common.Result;
import org.shirakawatyu.handixikebackend.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LibraryController {
    @Autowired
    LibraryService libraryService;

    /**
     * 获取图书信息
     *
     * @param session 会期
     * @return {@code Result}
     * @throws IOException io异常
     */
    @GetMapping("/api/v2/extension/library")
    public Result getLibraryInfo(HttpSession session) throws IOException{
        return libraryService.getLibrary (session);
    }

    /**
     * 查询书籍
     *
     * @param session  会期
     * @param bookName 书名
     * @param page     页
     * @return {@code Result}
     * @throws IOException io异常
     */
    @GetMapping("/api/v2/extension/queryBooks")
    public Result queryBooks(HttpSession session, @RequestParam("bookName")String bookName, @RequestParam("page")int page) throws IOException {

        return libraryService.queryBooks(session,bookName,page);
    }

    /**
     * 查询位置
     *
     * @param session 会期
     * @param id      同上
     * @return {@code Result}
     * @throws IOException io异常
     */
    @GetMapping("/api/v2/extension/queryLocation")
    public Result queryLocation(HttpSession session, @RequestParam("id")String id) throws IOException {
        return libraryService.queryLocation(session,id);

    }


}
