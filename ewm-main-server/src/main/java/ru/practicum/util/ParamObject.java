package ru.practicum.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import ru.practicum.model.EventOrderBy;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Constants.DEFAULT_FROM_VALUE;
import static ru.practicum.util.Constants.DEFAULT_SIZE_VALUE;

@Getter
@Setter
@AllArgsConstructor
//TODO rename
public class ParamObject {
    private final String text;
    private final List<Long> categories;
    private final Boolean paid;
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    private final Boolean onlyAvailable;
    private final EventOrderBy orderBy;
    private final int offset;
    private final int size;

    public boolean hasCategories() {
        return categories != null && !categories.isEmpty();
    }

    public boolean hasOnlyAvailable() {
        return onlyAvailable != null;
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

    public boolean hasOrderBy() {
        return orderBy != null;
    }

    public boolean hasText() {
        return text != null && !text.isBlank();
    }

    private ParamObject(ParamBuilder builder) {
        this.text = builder.text;
        this.categories = builder.categories;
        this.paid = builder.paid;
        this.rangeStart = builder.rangeStart;
        this.rangeEnd = builder.rangeEnd;
        this.onlyAvailable = builder.onlyAvailable;
        this.orderBy = builder.orderBy;
        this.offset = builder.from == null ? Integer.parseInt(DEFAULT_FROM_VALUE) : builder.from;
        this.size = builder.size == null ? Integer.parseInt(DEFAULT_SIZE_VALUE) : builder.size;

    }

    public static ParamBuilder newBuilder() {
        return new ParamBuilder();
    }

    public static final class ParamBuilder {
        private String text;
        private List<Long> categories;
        private Boolean paid;
        private LocalDateTime rangeStart;
        private LocalDateTime rangeEnd;
        private Boolean onlyAvailable;
        private EventOrderBy orderBy;
        private Integer from;
        private Integer size;

        private ParamBuilder() {
        }

        public ParamBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public ParamBuilder withCategories(List<Long> categories) {
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

        public ParamBuilder orderBy(EventOrderBy orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        public ParamObject build() {
            return new ParamObject(this);
        }
    }
}
