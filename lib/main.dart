import 'dart:io';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  Future _invokeServiceInAndroid() async {
    if (Platform.isAndroid) {
      var methodChannel = MethodChannel("DeveloperGundaChannel");
      String data = await methodChannel.invokeMethod("startPetTrackingService");
      debugPrint(data);
    }
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
}
