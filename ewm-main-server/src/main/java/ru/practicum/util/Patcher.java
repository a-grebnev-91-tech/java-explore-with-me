package ru.practicum.util;

import ru.practicum.entity.Event;
import ru.practicum.model.UpdateEvent;

public class Patcher {
    public static void patchEvent(Event toPatch, UpdateEvent patch) {
        if (patch.hasAnnotation()) {
            toPatch.setAnnotation(patch.getAnnotation());
        }
        if (patch.hasDescription()) {
            toPatch.setDescription(patch.getDescription());
        }
        if (patch.hasCategory()) {
            toPatch.setCategory(patch.getCategory());
        }
        if (patch.hasEventDate()) {
            toPatch.setEventDate(patch.getEventDate());
        }
        if (patch.hasLat()) {
            toPatch.setLat(patch.getLat());
        }
        if (patch.hasLon()) {
            toPatch.setLon(patch.getLon());
        }
        if (patch.hasPaid()) {
            toPatch.setPaid(patch.getPaid());
        }
        if (patch.hasParticipantLimit()) {
            toPatch.setParticipantLimit(patch.getParticipantLimit());
        }
        if (patch.hasRequestModeration()) {
            toPatch.setRequestModeration(patch.getRequestModeration());
        }
        if (patch.hasTitle()) {
            toPatch.setTitle(patch.getTitle());
        }
    }
}
