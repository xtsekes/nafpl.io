package dev.nafplio.service.model;

public record PageResult<T>(int pageNumber, int pageSize, int totalPages, long totalElements, T data) {
    public PageResult {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        if (totalPages < 0) {
            throw new IllegalArgumentException("Total pages cannot be negative");
        }
        if (totalElements < 0) {
            throw new IllegalArgumentException("Total elements cannot be negative");
        }
    }

    public static <T> PageResult<T> empty(int pageNumber, int pageSize) {
        return new PageResult<>(pageNumber, pageSize, 0, 0, null);
    }

    public static <T> PageResult<T> of(int pageNumber, int pageSize, long totalElements, T data) {
        if (totalElements == 0) {
            return empty(pageNumber, pageSize);
        }

        return new PageResult<>(pageNumber, pageSize, (int) Math.ceil((double) totalElements / pageSize), totalElements, data);
    }
}
