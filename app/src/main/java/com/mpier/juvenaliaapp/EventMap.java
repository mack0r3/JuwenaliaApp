package com.mpier.juvenaliaapp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Selve on 16.02.2016.
 */
public class EventMap {

    Map<String, Event[]> eventMap = new HashMap<String, Event[]>();

    // Add events for specified day
    public void addEvents(String day, Event[] events) {
        eventMap.put(day, events);
    }

    // Get all events for specified day
    public Event[] getEvents(String day) {
        Event events[] = eventMap.get(day);
        return events;
    }

    public int countEvents(String day) {
        return getEvents(day).length;
    }

    public Event getEvent(String day, int position) {
        return getEvents(day)[position];
    }

}
