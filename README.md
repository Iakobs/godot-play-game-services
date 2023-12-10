# Godot Play Game Services
Godot 4.2+ plugin for integration of the latest version of [Google Play Game Services SDK](https://developers.google.com/games/services/android/quickstart).

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
  - [Building the plugin](#building-the-plugin)
  - [Testing the plugin](#testing-the-plugin)
  - [Configuring the plugin](#configuring-the-plugin)
- [Technical documentation](#technical-documentation)

## Forewords
The whole project started as a collaboration with [Rafa Laguna](https://github.com/rafalagoon), and this plugin is the updated version of the [old one](https://github.com/Iakobs/godot-google-play-game-services-android-plugin/tree/main) for Godot 3.5+.

With Godot 4.2+, the way android plugins work [has changed](https://docs.godotengine.org/en/stable/tutorials/platform/android/android_plugin.html), so I had to update this plugin to match the new requirements. Now there's only one editor plugin (this one), instead of having two separate plugins like before, one android plugin and one godot plugin.

## How to use the plugin
Using the plugin requires downloading the repository, and generating the sources. I'm working to provide a more user-friendly way of using the plugin, but for now I'm afraid this is the only way of using it.

### Downloading the plugin
To download the plugin you can use `git` to clone it, or just download the repository in a zip file from Github.

![Screenshot of Github's menu to download the repository as a zip file](docs/images/download_repo.png)

### Building the plugin
In a terminal window, navigate to the project's root directory and run the following command:
```
./gradlew assemble
```
The output files can be found in [`plugin/demo/addons`](plugin/demo/addons). The plugin itself, that you can copy to your project to use, is in the [`plugin/demo/addons/GodotPlayGameServices`](plugin/demo/addons/GodotPlayGameServices), just copy the whole `GodotPlayGameServices` folder into the `addons` folder of your Godot project and activate the plugin in `Project` -> `Project Settings...` -> `Plugins`

### Testing the plugin
You can also use the included [Godot demo project](plugin/demo/project.godot) to test the built Android plugin.

- Open the demo in Godot (4.2 or higher)
- Navigate to `Project` -> `Project Settings...` -> `Plugins`, and ensure the plugin is enabled
- Install the Godot Android build template by clicking on `Project` -> `Install Android Build Template...`
- Connect an Android device to your machine and run the demo on it
- Follow the steps on [Configuring the plugin](#configuring-the-plugin)

### Configuring the plugin
Before configuring the plugin, you have to create  a **Google developer account**, which involves a one time payment to google, and then you must create a game. The explanation on how to do all of these is out of the scope of this documentation. Please [refer to Google](https://developer.android.com/distribute/console) for those steps.

Once that's done, you will need to create some achievements and leaderboards in your game, which will generate some IDs. You will need those IDs later in your game, but for the plugin, the one that's important is the **Game ID**, which can be found in your game configuration, in the Google Dev Console:

![Screenshot of the Game ID inside the Google Dev Console](docs/images/game_id.png)

In order to make the configuration easier, the plugin adds a new dock to the bottom panel of your Godot editor, where you can add the Game ID. Once you click on the submit button, the configuration will be saved and loaded every time you reopen the editor.

![Screenshot of the new dock in the bottom panel of the Godot Editor](docs/images/plugin_dock.png)

> :warning: Beware of not changing the Game Id and submitting by mistake, since that will break the integration with Google Game Services.

### Export configuration
To be able to use the plugin, you need to use a **custom gradle build**. The steps to do so, are explained with detail in the [Godot Documentation](https://docs.godotengine.org/en/stable/tutorials/export/android_gradle_build.html).

An additional configuration has to be made in order to connect with Google Game Services. In the Google App, you have to configure an **Android OAuth client**. Google describes the steps [here](https://developers.google.com/games/services/console/enabling#step_3_generate_an_oauth_20_client_id), but at some point you have to introduce the **package name** of your game, as well as the **SHA-1 of your signing key**. Both things need to be introduced also in the Godot Export configuration for Android:

![Screenshot of the relevant section inside the export configuration for Android in the Godot editor](docs/images/android_export.png)

## Technical Documentation
You can find the technical documentation of the kotlin code in the [Github Pages](https://iakobs.github.io/godot-play-game-services/) of the repository.

You can find Google's technical documentation of the SDK [here](https://developers.google.com/android/reference/packages).
