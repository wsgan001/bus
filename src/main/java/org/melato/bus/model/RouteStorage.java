package org.melato.bus.model;

import java.util.Collection;
import java.util.List;

import org.melato.gpx.GPX;
import org.melato.gpx.Point;
import org.melato.gpx.Waypoint;

/**
 * Provides low-level database access to routes.
 * @author Alex Athanasopoulos
 *
 */
public interface RouteStorage {
  List<Route> loadRoutes();
  Route loadRoute( String qualifiedName );
  GPX loadGPX(String qualifiedName);
  void iterateAllStops(Collection<Waypoint> collector);
  void iterateNearbyStops(Point point, float distance, Collection<Waypoint> collector);
}
