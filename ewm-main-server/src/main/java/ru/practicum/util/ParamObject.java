package ru.practicum.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import ru.practicum.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Constants.DEFAULT_FROM_VALUE;
import static ru.practicum.util.Constants.DEFAULT_SIZE_VALUE;

@Getter
@Setter
@AllArgsConstructor
public class ParamObject {
    private final String text;
    private final List<Integer> categories;
    private final Boolean paid;
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    private final Boolean onlyAvailable;
    private final Pageable pageable;

    public boolean hasText() {
        return text != null && !text.isBlank();
    }

    public boolean hasCategories() {
        return categories != null && !categories.isEmpty();
    }

    public boolean hasPaid() {
        return paid != null;
    }

    public boolean hasRangeStart() {
        return rangeStart != null;
    }

    public boolean hasRangeEnd() {
        return rangeEnd != null;
    }

    public boolean hasOnlyAvailable() {
        return onlyAvailable != null;
    }

    private ParamObject(ParamBuilder builder) {
        this.text = builder.text;
        this.categories = builder.categories;
        this.paid = builder.paid;
        this.rangeStart = builder.rangeStart;
        this.rangeEnd = builder.rangeEnd;
        this.onlyAvailable = builder.onlyAvailable;
        int offset = builder.from == null ? Integer.parseInt(DEFAULT_FROM_VALUE) : builder.from;
        int size = builder.size == null ? Integer.parseInt(DEFAULT_SIZE_VALUE) : builder.size;
        if (builder.orderBy == null) {
            this.pageable = OffsetPageable.of(offset, size);
        } else {
            String orderBy = builder.orderBy;
            Sort sort;
            if (builder.sortOrder == null) {
                sort = Sort.by(orderBy);
            } else {
                sort = Sort.by(Sort.Direction.valueOf(builder.sortOrder), orderBy);
            }
            this.pageable = OffsetPageable.of(offset, size, sort);
        }
    }

    public static ParamBuilder newBuilder() {
        return new ParamBuilder();
    }

    public static final class ParamBuilder {
        private String text;
        private List<Integer> categories;
        private Boolean paid;
        private LocalDateTime rangeStart;
        private LocalDateTime rangeEnd;
        private Boolean onlyAvailable;
        private String orderBy;
        private String sortOrder;
        private Integer from;
        private Integer size;

        private ParamBuilder() {
        }

        public ParamBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public ParamBuilder withCategories(List<Integer> categories) {
            this.categories = categories == null ? categories : List.copyOf(categories);
            return this;
        }

        public ParamBuilder withPaid(Boolean paid) {
            this.paid = paid;
            return this;
        }

        public ParamBuilder withRangeStart(LocalDateTime rangeStart) {
            this.rangeStart = rangeStart;
            return this;
        }

        public ParamBuilder withRangeEnd(LocalDateTime rangeEnd) {
            this.rangeEnd = rangeEnd;
            return this;
        }

        public ParamBuilder withOnlyAvailable(Boolean onlyAvailable) {
            this.onlyAvailable = onlyAvailable;
            return this;
        }

        public ParamBuilder from(Integer from) {
            this.from = from;
            return this;
        }

        public ParamBuilder size(Integer size) {
            this.size = size;
            return this;
        }

        public ParamBuilder orderBy(String orderBy) {
            if (orderBy == null || orderBy.equals("EVENT_DATE") || orderBy.equals("VIEWS")) {
                this.orderBy = orderBy;
                return this;
            } else {
                throw new ValidationException("Sort by is invalid", "Sort by should be one of {EVENT_DATE, VIEWS}");
            }
        }

        public ParamBuilder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public ParamObject build() {
            return new ParamObject(this);
        }
    }
}
