package org.shirakawatyu.handixikebackend.api;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.shirakawatyu.handixikebackend.pojo.Library;

import java.io.IOException;
import java.util.ArrayList;

public interface LibraryApi {

    ArrayList<Library> getBorrows(BasicCookieStore cookieStore);


    String queryBooks(String bookName, int page) throws IOException;

    String getLocationOfBook(String id) throws IOException;
}
