import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'main.dart';

class Settings extends StatefulWidget {
  const Settings({super.key});

  @override
  State<Settings> createState() => _SettingsState();
}

class _SettingsState extends State<Settings> {
  double _volume = 0.0;
  bool changes = false;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    _loadVolume();
  }

  Future<void> _loadVolume() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() {
      _volume = prefs.getDouble('video_volume') ?? 0.0; // Default to silent
    });
  }

  Future<void> _saveVolume(double value) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setDouble('video_volume', value);
  }

  Future<void> _removeWallpaper() async {
    try {
      await HomeScreen.platform.invokeMethod('removeWallpaper');
      print('Wallpaper reset to system default');
    } on PlatformException catch (e) {
      print("Failed to remove: '${e.message}'.");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Wallpaper Settings'),),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 8.0),
            child: Row(
              children: [
                Icon(Icons.volume_mute),
                Expanded(
                  child: Slider(value: _volume,
                    onChanged: (value) {
                      setState(() {
                        _volume = value;
                        changes = true;
                      });
                    },
                    divisions: 100,
                    padding: EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                    activeColor: Colors.blue,
                    inactiveColor: Colors.grey,
                    thumbColor: Colors.blueGrey,
                    label: "Volume ${(_volume * 100).toInt()}%",
                  ),
                ),
                Icon(Icons.volume_up),
              ],
            ),
          ),
          SizedBox(height: 20,),
          ElevatedButton(
            onPressed: _removeWallpaper,
            child: Text('Remove Live Wallpaper'),
          ),
        ],
      ),
    );
  }
}
