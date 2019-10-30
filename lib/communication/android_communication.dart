import 'dart:io';

import 'package:flutter/services.dart';

const String METHOD_CHANNEL = "DeveloperGundaChannel";

class AndroidCommunication {
  MethodChannel _methodChannel;
  AndroidCommunication() {
    _methodChannel = MethodChannel(METHOD_CHANNEL);
  }

  Future invokeServiceInAndroid() async {
    if (Platform.isAndroid) {
      await _methodChannel.invokeMethod("startPetTrackingService");
    }
  }

  Future stopServiceInAndroid() async {
    if (Platform.isAndroid) {
      await _methodChannel.invokeMethod("stopPetTrackingService");
    }
  }

  Future isPetTrackingEnabled() async {
    if (Platform.isAndroid) {
      bool result = await _methodChannel.invokeMethod("isPetTrackingEnabled");
      return result;
    }
  }

  Future isServiceBound() async {
    if (Platform.isAndroid) {
      bool result = await _methodChannel.invokeMethod("serviceBound");
      if (result) {
        isPetTrackingEnabled();
      }
    }
  }
}
