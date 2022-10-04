package org.shirakawatyu.handixikebackend.utils;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {
    public static List<String> arrayToList(Object[] arr) {
        List<String> list = new ArrayList<>();
        if(arr != null) {
            for (int i = 0; i < arr.length; i++) {
                list.add((String) arr[i]);
            }
        }
        return list;
    }
}
