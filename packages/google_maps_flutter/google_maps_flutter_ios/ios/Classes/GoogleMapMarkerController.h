// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#import <Flutter/Flutter.h>
#import <GoogleMaps/GoogleMaps.h>
#import "GoogleMapController.h"

NS_ASSUME_NONNULL_BEGIN

// Defines marker controllable by Flutter.
@interface FLTGoogleMapMarkerController : NSObject
@property(assign, nonatomic, readonly) BOOL consumeTapEvents;
- (instancetype)initMarkerWithPosition:(CLLocationCoordinate2D)position
                            identifier:(NSString *)identifier
                               mapView:(GMSMapView *)mapView
                             iconImage:(UIImage *)iconImage;
- (void)showInfoWindow;
- (void)hideInfoWindow;
- (BOOL)isInfoWindowShown;
- (void)removeMarker;
- (UIImage *)addClusterMarkerText:(NSString *)label;
- (UIImage *)addPriceMarkerText:(NSString *)label;
@end

@interface FLTMarkersController : NSObject
- (instancetype)initWithMethodChannel:(FlutterMethodChannel *)methodChannel
                              mapView:(GMSMapView *)mapView
                            registrar:(NSObject<FlutterPluginRegistrar> *)registrar;
- (void)addMarkers:(NSArray *)markersToAdd;
- (void)changeMarkers:(NSArray *)markersToChange;
- (void)removeMarkersWithIdentifiers:(NSArray *)identifiers;
- (BOOL)didTapMarkerWithIdentifier:(NSString *)identifier;
- (void)didStartDraggingMarkerWithIdentifier:(NSString *)identifier
                                    location:(CLLocationCoordinate2D)coordinate;
- (void)didEndDraggingMarkerWithIdentifier:(NSString *)identifier
                                  location:(CLLocationCoordinate2D)coordinate;
- (void)didDragMarkerWithIdentifier:(NSString *)identifier
                           location:(CLLocationCoordinate2D)coordinate;
- (void)didTapInfoWindowOfMarkerWithIdentifier:(NSString *)identifier;
- (void)showMarkerInfoWindowWithIdentifier:(NSString *)identifier result:(FlutterResult)result;
- (void)hideMarkerInfoWindowWithIdentifier:(NSString *)identifier result:(FlutterResult)result;
- (void)isInfoWindowShownForMarkerWithIdentifier:(NSString *)identifier
                                          result:(FlutterResult)result;
-(UIImage *)baseClusterMarker;
@end

NS_ASSUME_NONNULL_END
