/*-------------------------------------------------------------------------
 * Copyright (c) 2012, Alex Athanasopoulos.  All Rights Reserved.
 * alex@melato.org
 *-------------------------------------------------------------------------
 * This file is part of Athens Next Bus
 *
 * Athens Next Bus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Athens Next Bus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Athens Next Bus.  If not, see <http://www.gnu.org/licenses/>.
 *-------------------------------------------------------------------------
 */
package org.melato.bus.client;

import java.util.ArrayList;
import java.util.List;

import org.melato.geometry.gpx.PointTimeListener;
import org.melato.geometry.gpx.RollingSpeedManager;
import org.melato.geometry.gpx.RollingSpeedManager.RollingSpeed;
import org.melato.gps.Earth;
import org.melato.gps.Metric;
import org.melato.gps.PointTime;
import org.melato.log.Log;

/**
 * Maintains track history.
 * @author Alex Athanasopoulos
 *
 */
public class TrackHistory implements PointTimeListener {
  private Metric metric;
  private PointTime   previousLocation;
  private PointTime   location;
  private List<PointTimeListener> listeners = new ArrayList<PointTimeListener>();
  private RollingSpeedManager speedManager;
  private RollingSpeed speed60;
  private RollingSpeed speed300;

  public TrackHistory(Metric metric) {
    this.metric = metric;
    speedManager = new RollingSpeedManager(metric);
    speed60 = speedManager.getRollingSpeed(60);
    speed300 = speedManager.getRollingSpeed(300);
  }
  
  public Metric getMetric() {
    return metric;    
  }
  
  protected void enableUpdates(boolean enabled) {}
    
  public synchronized void addLocationListener(PointTimeListener listener) {
    listeners.add(listener);
    if ( listeners.size() == 1 )
      enableUpdates(true);
  }
  
  public synchronized void removeLocationListener(PointTimeListener listener) {
    int size = listeners.size();
    for( int i = 0; i < size; i++ ) {
      if ( listener == listeners.get(i)) {
        listeners.remove(i);
        break;
      }
    }
    if ( listeners.size() == 0 )
      enableUpdates(false);
  }

  @Override
  public final void setLocation(PointTime point) {
    Log.info(point);
    if ( point == null )
      return;
    previousLocation = location;
    location = point;
    speedManager.addPoint(point);
    for( PointTimeListener listener: listeners ) {
      listener.setLocation(location);
    }
  }
    
  public PointTime getLocation() {
    return location;
  }
  
  public float getBearing() {
    if ( previousLocation != null && location != null) {
      return Earth.bearing(previousLocation, location);
    }
    return Float.NaN;
  }

  public RollingSpeed getSpeed60() {
    return speed60;
  }

  public RollingSpeed getSpeed300() {
    return speed300;
  }
  
  
  
}