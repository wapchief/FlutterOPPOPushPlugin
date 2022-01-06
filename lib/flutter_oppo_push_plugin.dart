import 'dart:async';

import 'package:flutter/services.dart';
export 'package:flutter_oppo_push_plugin/flutter_oppo_push_plugin.dart';

class FlutterOppoPushPlugin {
  static const MethodChannel _channel = MethodChannel('flutter_oppo_push_plugin');

  static initSDK({bool isDebug = true, required String key, required String secret}) {
    _channel.invokeMethod('initSDK', {'isDebug': isDebug, 'key': key, 'secret': secret});
  }
}
