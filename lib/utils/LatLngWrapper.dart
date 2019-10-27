import 'package:google_maps_flutter/google_maps_flutter.dart';

class LatLngWrapper {
  static LatLng fromAndroidJson(Map<String, dynamic> json) {
    return LatLng(json['latitude'], json['longitude']);
  }
}
