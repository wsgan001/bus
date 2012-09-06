package org.melato.bus.model;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.melato.bus.model.xml.XmlRouteStorage;
import org.melato.gpx.GPX;
import org.melato.gpx.Point;
import org.melato.gpx.Waypoint;


/**
 * Provides read-access to routes.
 * For use on Java SE or Android
 * @author Alex Athanasopoulos
 *
 */
public class RouteManager {
  private RouteStorage storage;  
  private List<Route> routes;
    
  public RouteManager(RouteStorage storage) {
    super();
    this.storage = storage;
  }
  
  public RouteManager(File dataDir) {
    super();
    storage = new XmlRouteStorage(dataDir);
  }

  public RouteManager(URL dataUrl) {
    super();
    storage = new XmlRouteStorage(dataUrl);
  }

  public List<Route> getRoutes() {
    if ( routes == null ) {
      routes = storage.loadRoutes();
      for( Route route: routes ) {
        route.routeManager = this;
        route.schedule = null;          
      }
    }
    return routes;
  }

  public Route getRoute(RouteId routeId) {
    boolean logged = false;
    for( Route route: getRoutes() ) {
      if ( ! logged ) {
        logged = true;
      }
      if (routeId.equals(route.getRouteId()))
          return route;
    }
    return null;    
  }
  
  public Route loadRoute(RouteId routeId) {
    return storage.loadRoute(routeId);
  }

  Route loadRoute(Route route) {
    return loadRoute(route.getRouteId());
  }

  public GPX loadGPX(RouteId routeId) {
    return storage.loadGPX(routeId);
  }

  public GPX loadGPX(Route route) {
    return loadGPX(route.getRouteId());
  }

  public String getUri( Route route ) {
    return storage.getUri(route.getRouteId());
  }
  /**
   * Load marker information:
   * - waypoint (location, name)
   * - linked routes
   * - 
   * @param symbol
   * @return
   */
  public MarkerInfo loadMarker(String symbol) {
    return storage.loadMarker(symbol);
  }
  
  public List<Waypoint> findNearbyStops(Point point, float distance) {
    List<Waypoint> result = new ArrayList<Waypoint>();
    storage.iterateNearbyStops(point, distance, result);
    return result;
  }
}
