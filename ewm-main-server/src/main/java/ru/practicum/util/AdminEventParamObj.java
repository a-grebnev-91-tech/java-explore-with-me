package ru.practicum.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Constants.DEFAULT_FROM_VALUE;
import static ru.practicum.util.Constants.DEFAULT_SIZE_VALUE;

@Getter
@Setter
@AllArgsConstructor
public class AdminEventParamObj {
    private final List<Long> users;
    private final List<EventState> states;
    private final List<Long> categories;
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    private final int offset;
    private final int size;

    public boolean hasCategories() {
        return categories != null && !categories.isEmpty();
    }

    public boolean hasRangeStart() {
        return rangeStart != null;
    }

    public boolean hasRangeEnd() {
        return rangeEnd != null;
    }

    public boolean hasStates() {
        return states != null && !states.isEmpty();
    }
    public boolean hasUsers() {
        return users != null && !users.isEmpty();
    }

    private AdminEventParamObj(ParamBuilder builder) {
        this.users = builder.users;
        this.states = builder.states;
        this.categories = builder.categories;
        this.rangeStart = builder.rangeStart;
        this.rangeEnd = builder.rangeEnd;
        this.offset = builder.from == null ? Integer.parseInt(DEFAULT_FROM_VALUE) : builder.from;
        this.size = builder.size == null ? Integer.parseInt(DEFAULT_SIZE_VALUE) : builder.size;

    }

    public static ParamBuilder newBuilder() {
        return new ParamBuilder();
    }

    public static final class ParamBuilder {
        private List<Long> users;
        private List<EventState> states;
        private List<Long> categories;
        private LocalDateTime rangeStart;
        private LocalDateTime rangeEnd;
        private Integer from;
        private Integer size;

        private ParamBuilder() {
        }

        public ParamBuilder withUsers(List<Long> users) {
            this.users = users == null ? null : List.copyOf(users);
            return this;
        }

        public ParamBuilder withStates(List<EventState> states) {
            this.states = states == null ? null : List.copyOf(states);
            return this;
        }

        public ParamBuilder withCategories(List<Long> categories) {
            this.categories = categories == null ? null : List.copyOf(categories);
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


        public ParamBuilder from(Integer from) {
            this.from = from;
            return this;
        }

        public ParamBuilder size(Integer size) {
            this.size = size;
            return this;
        }

        public AdminEventParamObj build() {
            return new AdminEventParamObj(this);
        }
    }
}
