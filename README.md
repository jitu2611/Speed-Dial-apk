# Speed Dial

An Android Java learning project for storing contacts and initiating one-touch calls.

> **Archived learning project.** It uses an older Android project structure and is retained as an early mobile-development example.

## Interaction flow

```text
Contact input → local database → speed-dial list → selected contact → Android dial action
```

## Structure

- `SpeedDial/src/.../MainActivity.java` — application entry point
- `SpeedDial/src/.../Database.java` — local contact storage
- `SpeedDial/src/.../fragment1.java` and `fragment2.java` — UI sections
- `SpeedDial/AndroidManifest.xml` — application manifest

## Running

Open the `SpeedDial/` directory in an Android Studio version compatible with its legacy Gradle/SDK setup. Migration may be needed for modern Android tooling.

## Scope

This project demonstrates Android activities, fragments, and local persistence. It is not maintained as a production dialer.
