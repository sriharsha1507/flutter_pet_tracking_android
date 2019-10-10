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
  static const platform = const MethodChannel(METHOD_CHANNEL);

  @override
  void initState() {
    super.initState();
    _setAndroidMethodCallHandler();
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
      var methodChannel = MethodChannel("DeveloperGundaChannel");
      String data = await methodChannel.invokeMethod("startPetTrackingService");
      debugPrint(data);
    }
  }

  Future<dynamic> _androidMethodCallHandler(MethodCall call) async {
    switch (call.method) {
      case AndroidCall.TEST_PET_TRACKING:
        var data = call.arguments;
        debugPrint("Call coming from Android native service listener $data");
        return Future.value(data);
    }
  }

  void _setAndroidMethodCallHandler() {
    platform.setMethodCallHandler(_androidMethodCallHandler);
  }
}
