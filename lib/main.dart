import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
      ),
      home: const HomeScreen(),
    );
  }
}

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  static const platform = MethodChannel(
      'com.example.video_wallpaper/wallpaper');

  Future<void> _applyWallpaper() async {
    try {
      await platform.invokeMethod('applyWallpaper');
    } on PlatformException catch (e) {
      print("Failed to apply the wallpaper: '${e.message}'.");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Video Wallpaper'),
      ),
      body: Center(
        child: ElevatedButton(
          onPressed: _applyWallpaper,
          child: Text('Apply Live Wallpaper'),
        ),
      ),
    );
  }
}
