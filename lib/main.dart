import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';

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
    'com.example.video_wallpaper/wallpaper',
  );

  Future<void> _pickVideo(BuildContext context) async {
    try {
      FilePickerResult? result = await FilePicker.platform.pickFiles(
        type: FileType.video,
      );

      if (result != null && result.files.single.path != null) {
        String? filePath = result.files.single.path;

        final prefs = await SharedPreferences.getInstance();
        await prefs.setString('video_path', filePath!);

        if (context.mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Video Selected: ${filePath
                  .split('/')
                  .last}'),
            ),
          );
        }
      }
    } catch (e) {
      print("Failed to pick video: $e");
    }
  }

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
      appBar: AppBar(title: Text('Video Wallpaper')),
      body: Column(
        // mainAxisAlignment: MainAxisAlignment.start,
        children: [
          ElevatedButton(
            onPressed: () => _pickVideo(context),
            child: Text('Pick Video'),
          ),
          SizedBox(height: 20),
          ElevatedButton(
            onPressed: _applyWallpaper,
            child: Text('Apply Live Wallpaper'),
          ),
        ],
      ),
    );
  }
}
