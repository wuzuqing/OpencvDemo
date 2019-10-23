package com.example.administrator.opencvdemo.dialog;

import java.util.List;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2017/12/26 13:23
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2017/12/26$
 * @updateDes ${TODO}
 */

public interface DataIO<T> {

    void add(T elem);

    void addAt(int location, T elem);

    void addAll(List<T> elements);

    void addAllAt(int location, List<T> elements);

    void remove(T elem);

    void removeAll(List<T> elements);

    void removeAt(int index);

    void clear();

    void replace(T oldElem, T newElem);

    void replaceAt(int index, T elem);

    void replaceAll(List<T> elements);

    List<T> getAll();

    T get(int position);

    int getSize();

    boolean contains(T elem);
}
