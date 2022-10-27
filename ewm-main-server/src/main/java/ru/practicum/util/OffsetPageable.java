package ru.practicum.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

//TODO remove
public class OffsetPageable extends PageRequest {
    private final int offset;

    private OffsetPageable(int offset, int size, Sort sort) {
        super(offset, size, sort);
        this.offset = offset;
    }

    public static OffsetPageable of(int offset, int size) {
        return of(offset, size, Sort.unsorted());
    }

    public static OffsetPageable of(int offset, int size, Sort sort) {
        validateInputOrThrow(offset, size);
        return new OffsetPageable(offset, size, sort);
    }

    @Override
    public long getOffset() {
        return offset;
    }

    private static void validateInputOrThrow(int offset, int size) {
        if (offset < 0 || size < 1) {
            throw new IllegalArgumentException("Offset must be greater than 0 and size must be greater than 1");
        }
    }
}
