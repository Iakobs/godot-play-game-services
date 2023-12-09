# Godot Play Game Services
Godot 4.2+ Android plugin for integration of the latest version of [Google Play Game Services SDK](https://developers.google.com/games/services/android/quickstart).

---

![Plugin Header](github-social-preview.png)

[![Android version and API level 33](https://img.shields.io/badge/Android-API%20Level%2033-darkgreen.svg)](https://developer.android.com)
[![Godot version 4.2](https://img.shields.io/badge/Godot%20Engine-4.2-blue.svg)](https://github.com/godotengine/godot/)
[![Google Play Game Services version 19.0.0](https://img.shields.io/badge/Play%20Services%20Games%20v2-19.0.0-green.svg)](https://developers.google.com/games/services/android/quickstart)

---

## Table of contents

- [Forewords](#forewords)
- [How to use the plugin](#how-to-use-the-plugin)
  - [Downloading the plugin](#downloading-the-plugin)
  - [Configuring the plugin](#configuring-the-plugin)
  - [Building the plugin](#building-the-plugin)
  - [Testing the plugin](#testing-the-plugin)
- [Technical documentation](#plugin-technical-documentation)

## Forewords

This plugin is the updated version of the [old one](https://github.com/Iakobs/godot-google-play-game-services-android-plugin/tree/main) for Godot 3.5+.

With Godot 4.2+, the way android plugins work [has changed](https://docs.godotengine.org/en/stable/tutorials/platform/android/android_plugin.html), so I had to update this plugin to match the new requirements. Now there's only one editor plugin (this one), instead of having two separate plugins like before, one android plugin and one godot plugin.

## How to use the plugin
Using the plugin requires some configuration, downloading the repository, and generating the sources. I'm working to provide a more user-friendly way of using the plugin, but for now I'm afraid this is the only way of using it.

### Downloading the plugin
To download the plugin you can use `git` or just download the repository in a zip file from Github.

![Screenshot of Github's menu to download the repository as a zip file](docs/images/download_repo.png)

You will need to open a command line tool and navigate to the root of the project to run the appropriate `gradle` commands in later steps.

### Configuring the plugin
Before configuring the plugin, you have to create  a **Google developer account**, which involves a one time payment to google, and then you must create a game. The explanation on how to do all of these is out of the scope of this documentation. Please [refer to Google](https://developer.android.com/distribute/console) for those steps.

Once that's done, you will need to create some achievements and leaderboards in your game, that will generate some IDs. You will need those IDs later, the one that's important right now is the **Game ID**, which you can found in your game configuration, in the Google Dev Console

![Screenshot of the Game ID inside the Google Dev Console](docs/images/game_id.png)

This ID should be copied in the [string resources file](plugin/src/main/res/values/strings.xml), you will see there's a placeholder there to copy it:

```xml
<?xml version="1.0" encoding="utf-8"?>

<resources>
    <string translatable="false" name="game_services_project_id">your Game ID goes here!</string>
</resources>
```

### Building the plugin
- In a terminal window, navigate to the project's root directory and run the following command:
```
./gradlew assemble
```
- On successful completion of the build, the output files can be found in
  [`plugin/demo/addons`](plugin/demo/addons)

### Testing the plugin
You can use the included [Godot demo project](plugin/demo/project.godot) to test the built Android plugin

- Open the demo in Godot (4.2 or higher)
- Navigate to `Project` -> `Project Settings...` -> `Plugins`, and ensure the plugin is enabled
- Install the Godot Android build template by clicking on `Project` -> `Install Android Build Template...`
- Connect an Android device to your machine and run the demo on it

Also, you can copy the whole generated [addon directory](plugin/demo/addons) to your existing project, as any other plugin.

## Plugin Technical Documentation

This is still a work in progress!
