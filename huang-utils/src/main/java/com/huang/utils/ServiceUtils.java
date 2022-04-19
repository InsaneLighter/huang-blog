package com.huang.utils;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utilities for service.
 *
 * @author johnniang
 */
public class ServiceUtils {

    private ServiceUtils() {
    }

    /**
     * Fetches id to set.
     *
     * @param datas           data collection
     * @param mappingFunction calculate the id in data list
     * @param <I>             id type
     * @param <T>             data type
     * @return a set of id
     */
    @NonNull
    public static <I, T> Set<I> fetchProperty(final Collection<T> datas,
                                              Function<T, I> mappingFunction) {
        return CollectionUtils.isEmpty(datas)
                ? Collections.emptySet() :
                datas.stream().map(mappingFunction).collect(Collectors.toSet());
    }

    /**
     * Converts a list to a list map where list contains id in ids.
     *
     * @param ids             id collection
     * @param list            data list
     * @param mappingFunction calculate the id in data list
     * @param <I>             id type
     * @param <D>             data type
     * @return a map which key is in ids and value containing in list
     */
    @NonNull
    public static <I, D> Map<I, List<D>> convertToListMap(Collection<I> ids, Collection<D> list,
                                                          Function<D, I> mappingFunction) {
        Assert.notNull(mappingFunction, "mapping function must not be null");

        if (CollectionUtils.isEmpty(ids) || CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Map<I, List<D>> resultMap = new HashMap<>();

        list.forEach(
                data -> resultMap.computeIfAbsent(mappingFunction.apply(data), id -> new LinkedList<>())
                        .add(data));

        ids.forEach(id -> resultMap.putIfAbsent(id, Collections.emptyList()));

        return resultMap;
    }

    /**
     * Converts to map (key from the list data)
     *
     * @param list            data list
     * @param mappingFunction calclulate the id from list data
     * @param <I>             id type
     * @param <D>             data type
     * @return a map which key from list data and value is data
     */
    @NonNull
    public static <I, D> Map<I, D> convertToMap(Collection<D> list,
                                                Function<D, I> mappingFunction) {
        Assert.notNull(mappingFunction, "mapping function must not be null");

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Map<I, D> resultMap = new HashMap<>();

        list.forEach(data -> resultMap.putIfAbsent(mappingFunction.apply(data), data));

        return resultMap;
    }

    /**
     * Converts to map (key from the list data)
     *
     * @param list          data list
     * @param keyFunction   key mapping function
     * @param valueFunction value mapping function
     * @param <I>           id type
     * @param <D>           data type
     * @param <V>           value type
     * @return a map which key from list data and value is data
     */
    @NonNull
    public static <I, D, V> Map<I, V> convertToMap(@Nullable Collection<D> list,
                                                   @NonNull Function<D, I> keyFunction, @NonNull Function<D, V> valueFunction) {
        Assert.notNull(keyFunction, "Key function must not be null");
        Assert.notNull(valueFunction, "Value function must not be null");

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Map<I, V> resultMap = new HashMap<>();

        list.forEach(
                data -> resultMap.putIfAbsent(keyFunction.apply(data), valueFunction.apply(data)));

        return resultMap;
    }

    /**
     * Checks if the given number id is empty id.
     *
     * @param id the given number id
     * @return true if the given number id is empty id; false otherwise
     */
    public static boolean isEmptyId(@Nullable Number id) {
        return id == null || id.longValue() <= 0;
    }

}
