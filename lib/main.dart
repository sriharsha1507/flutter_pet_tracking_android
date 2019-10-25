import 'dart:io';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_android_pet_tracking_background_service/utils/AndroidCall.dart';

const String METHOD_CHANNEL = "DeveloperGundaChannel";

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const methodChannel = const MethodChannel(METHOD_CHANNEL);
  bool isTrackingEnabled = false;

  @override
  void initState() {
    super.initState();
    _setAndroidMethodCallHandler();
    _isPetTrackingEnabled();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Background Service',
      home: Scaffold(
        body: Center(
          heightFactor: 50,
          child: Container(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Text('Google maps'),
                RaisedButton(
                  child: Text('Track my pet'),
                  onPressed: () {
                    _invokeServiceInAndroid();
                  },
                ),
                RaisedButton(
                  child: Text('Stop tracking my pet'),
                  onPressed: () {
                    _stopServiceInAndroid();
                  },
                )
              ],
            ),
          ),
        ),
      ),
      debugShowCheckedModeBanner: false,
    );
  }

  Future _invokeServiceInAndroid() async {
    if (Platform.isAndroid) {
      String data = await methodChannel.invokeMethod("startPetTrackingService");
      debugPrint(data);
    }
  }

  Future _stopServiceInAndroid() async {
    if (Platform.isAndroid) {
      String data = await methodChannel.invokeMethod("stopPetTrackingService");
      debugPrint(data);
    }
  }

  Future _isPetTrackingEnabled() async {
    if (Platform.isAndroid) {
      isTrackingEnabled =
          await methodChannel.invokeMethod("isPetTrackingEnabled");
      debugPrint("Pet Tracking Status - $isTrackingEnabled");
    }
  }

  Future<dynamic> _androidMethodCallHandler(MethodCall call) async {
    switch (call.method) {
      case AndroidCall.PATH_LOCATION:
        var pathLocation = call.arguments;
        debugPrint("Dart Path location - $pathLocation");
        break;
    }
  }

  void _setAndroidMethodCallHandler() {
    methodChannel.setMethodCallHandler(_androidMethodCallHandler);
  }
}
