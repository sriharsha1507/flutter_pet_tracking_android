import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_android_pet_tracking_background_service/utils/AndroidCall.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

const String METHOD_CHANNEL = "DeveloperGundaChannel";

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const methodChannel = const MethodChannel(METHOD_CHANNEL);
  bool isTrackingEnabled = false;
  bool isServiceBounded = false;

  GoogleMapController googleMapController;

  final LatLng _center = const LatLng(45.521563, -122.677433);

  @override
  void initState() {
    super.initState();
    _setAndroidMethodCallHandler();
    _isServiceBound();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Background Service',
      home: Scaffold(
          body: !isServiceBounded
              ? CircularProgressIndicator()
              : getInitialWidget(context)),
      debugShowCheckedModeBanner: false,
    );
  }

  Center getInitialWidget(BuildContext context) {
    return Center(
      heightFactor: 50,
      child: Container(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Container(
              height: 500,
              child: GoogleMap(
                onMapCreated: _onMapCreated,
                myLocationEnabled: true,
                initialCameraPosition:
                    CameraPosition(target: _center, zoom: 11.0),
              ),
            ),
            !isTrackingEnabled
                ? RaisedButton(
                    child: Text('Track my pet'),
                    onPressed: () {
                      _invokeServiceInAndroid();
                    },
                  )
                : RaisedButton(
                    child: Text('Stop tracking my pet'),
                    onPressed: () {
                      _stopServiceInAndroid();
                    },
                  )
          ],
        ),
      ),
    );
  }

  void _onMapCreated(GoogleMapController googleMapController) {
    this.googleMapController = googleMapController;
  }

  Future _invokeServiceInAndroid() async {
    if (Platform.isAndroid) {
      String data = await methodChannel.invokeMethod("startPetTrackingService");
      setState(() {
        isTrackingEnabled = true;
      });
      debugPrint(data);
    }
  }

  Future _stopServiceInAndroid() async {
    if (Platform.isAndroid) {
      String data = await methodChannel.invokeMethod("stopPetTrackingService");
      setState(() {
        isTrackingEnabled = false;
      });
      debugPrint(data);
    }
  }

  Future _isPetTrackingEnabled() async {
    if (Platform.isAndroid) {
      bool result = await methodChannel.invokeMethod("isPetTrackingEnabled");
      setState(() {
        isTrackingEnabled = result;
      });
      debugPrint("Pet Tracking Status - $isTrackingEnabled");
    }
  }

  Future _isServiceBound() async {
    if (Platform.isAndroid) {
      bool result = await methodChannel.invokeMethod("serviceBound");
      setState(() {
        isServiceBounded = result;
        if (isServiceBounded) {
          _isPetTrackingEnabled();
        }
      });
      debugPrint("Pet Tracking Status - $isTrackingEnabled");
    }
  }

  Future<dynamic> _androidMethodCallHandler(MethodCall call) async {
    switch (call.method) {
      case AndroidCall.PATH_LOCATION:
        var pathLocation = call.arguments;
        debugPrint("Dart Path location - $pathLocation");
        break;

      case AndroidCall.IS_PET_TRACKING_ENABLED:
        bool enabled = call.arguments;
        debugPrint("Tracking service bounded and status - $enabled");
        setState(() {
          isTrackingEnabled = enabled;
          debugPrint("From Android it is invoked");
        });
        break;

      case AndroidCall.SERVICE_BOUND:
        bool result = call.arguments;
        debugPrint("Service bounded on dart side - $result");
        setState(() {
          isServiceBounded = result;
          if (isServiceBounded) {
            _isPetTrackingEnabled();
          }
        });
        break;
    }
  }

  void _setAndroidMethodCallHandler() {
    methodChannel.setMethodCallHandler(_androidMethodCallHandler);
  }
}
