package dev.nafplio.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResultTest {
    @Test
    void testPageResult() {
        var page = 1;
        var pageSize = 10;
        var total = 100;
        var data = List.of(new Object());

        var pageResult = new PageResult<>(page, pageSize, total / pageSize, total, data);

        assertEquals(page, pageResult.page());
        assertEquals(pageSize, pageResult.pageSize());
        assertEquals(total / pageSize, pageResult.totalPages());
        assertEquals(total, pageResult.totalElements());
        assertNotNull(pageResult.data());
        assertEquals(data, pageResult.data());
    }

    @Test
    void testNegativePageNumber() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PageResult<>(-1, 10, 10, 100, List.of(new Object())));
    }

    @Test
    void testNegativePageSizeNumber() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PageResult<>(1, -10, 10, 100, List.of(new Object())));
    }

    @Test
    void testZeroPageSize() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PageResult<>(1, 0, 10, 100, List.of(new Object())));
    }

    @Test
    void testNegativeTotalPages() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PageResult<>(1, 10, -1, 100, List.of(new Object())));
    }

    @Test
    void testNegativeTotalElements() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PageResult<>(1, 10, 10, -1, List.of(new Object())));
    }
}