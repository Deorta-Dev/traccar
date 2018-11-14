/*
 * Copyright 2016 - 2018 Anton Tananaev (anton@traccar.org)
 * Copyright 2016 - 2018 Andrey Kunitsyn (andrey@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.events;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandler;
import org.traccar.BaseEventHandler;
import org.traccar.Context;
import org.traccar.model.Event;
import org.traccar.model.Maintenance;
import org.traccar.model.Position;

@ChannelHandler.Sharable
public class MaintenanceEventHandler extends BaseEventHandler {

    @Override
    protected Map<Event, Position> analyzePosition(Position position) {
        if (Context.getIdentityManager().getById(position.getDeviceId()) == null
                || !Context.getIdentityManager().isLatestPosition(position)) {
            return null;
        }

        Position lastPosition = Context.getIdentityManager().getLastPosition(position.getDeviceId());
        if (lastPosition == null) {
            return null;
        }

        Map<Event, Position> events = new HashMap<>();
        for (long maintenanceId : Context.getMaintenancesManager().getAllDeviceItems(position.getDeviceId())) {
            Maintenance maintenance = Context.getMaintenancesManager().getById(maintenanceId);
            if (maintenance.getPeriod() != 0) {
                double oldValue = lastPosition.getDouble(maintenance.getType());
                double newValue = position.getDouble(maintenance.getType());
                if (oldValue != 0.0 && newValue != 0.0
                        && (long) ((oldValue - maintenance.getStart()) / maintenance.getPeriod())
                        < (long) ((newValue - maintenance.getStart()) / maintenance.getPeriod())) {
                    Event event = new Event(Event.TYPE_MAINTENANCE, position);
                    event.setMaintenanceId(maintenanceId);
                    event.set(maintenance.getType(), newValue);
                    events.put(event, position);
                }
            }
        }

        return events;
    }

}
