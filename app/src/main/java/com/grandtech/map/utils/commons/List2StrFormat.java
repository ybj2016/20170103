package com.grandtech.map.utils.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zy on 2016/11/24.
 * 可以按照你的想法 toString() 的ArrayList
 */

public class List2StrFormat<T> extends ArrayList<T> {

    private String s;

    public List2StrFormat(String s){
        this.s=s;
    }

    @Override
    public String toString() {

        StringBuffer buf = new StringBuffer();
        Iterator<T> i = iterator();
        boolean hasNext = i.hasNext();
        while (hasNext) {
            T o = i.next();
            buf.append(o == this ? "(this Collection)" : String.valueOf(o));
            hasNext = i.hasNext();
            if (hasNext)
                buf.append(s);
        }
        return buf.toString();
    }
}
