# Development

## Build configuration

Use the checked-in Gradle wrapper. The build uses a Java 8 toolchain, Gradle
8.14.5, and the Spigot 1.13.2 API as `compileOnly`. The server provides the API;
`shadowJar` bundles and relocates the plugin's runtime dependencies. The Bukkit
plugin description is generated from `build.gradle.kts`.

Java 8 is the plugin's compilation target, not a promise that every supported
Minecraft server runs on Java 8. Use the Java runtime required by the chosen
server. The README advertises Minecraft 1.13.2–26.2, while the included Docker
Compose setup targets Paper 26.2 on Java 25. No version-by-version results are
recorded here yet.

## Checks and local server

The existing JUnit tests cover selected listeners, language handling, and
update-version comparison. GitHub CI runs `./gradlew build` on JDK 8. The
`testWatchWolf` task is separate from `build` and `check` because it requires
a running WatchWolf control plane.

For developer-run checks, use `gradlew.bat test` or `gradlew.bat build` on Windows
(`./gradlew` on Unix). The shaded plugin is produced under `build/libs/`.
`README.md` documents the separate Docker Compose Paper server and RCON workflow.
It uses a persistent `mc-data` volume, so use a disposable world when checking
block or inventory behavior.

For a manual Paper smoke check in that disposable world, record the server and
Java versions, plugin JAR version, configuration, and observed results:

1. Confirm the plugin enables without errors and `/farmassist info` responds.
2. Break mature wheat with a seed and permission; check seed consumption and
   replanting after the scheduled delay.
3. Repeat without the crop permission, then with `no-seeds: true`, and confirm
   the expected behavior for each configuration.
4. Check wheat planting on tilling, a disabled world, `/farmassist reload`,
   and a clean restart. Inspect server logs for exceptions.

RCON can check commands and logs, but crop interaction and inventory outcomes
need a player client or equivalent real-server test driver.

## WatchWolf smoke test

The separate `testWatchWolf` task stages `shadowJar` as
`build/watchwolf/FarmAssistReboot.jar` and uses a disposable Paper 1.20.6 server
to check plugin startup and `/farmassist info`. Its configuration is in
`src/testWatchWolf/resources/watchwolf.yaml`. The Gradle suite follows
ChunkSpawnerLimiter's `JvmTestSuite` and custom-release Ivy setup. The control
plane follows the
[WatchWolf v0.4.1 example](https://github.com/sarhatabaot/WatchWolf/tree/a61ddaf76ef5cbce5d4ef3100267c6e29aaeb3ae/examples/hello-plugin),
which documents a real-server run. This FarmAssist test has not been run yet.

The source and artifact revisions used for this setup are:

| Component | Revision |
| --- | --- |
| WatchWolf standard and release Compose | [`a61ddaf76ef5cbce5d4ef3100267c6e29aaeb3ae`](https://github.com/sarhatabaot/WatchWolf/commit/a61ddaf76ef5cbce5d4ef3100267c6e29aaeb3ae) (`v0.4.1`) |
| Servers Manager | [`71d041ae1cc01c77ce42824277d09359a0293063`](https://github.com/sarhatabaot/WatchWolf-ServersManager/commit/71d041ae1cc01c77ce42824277d09359a0293063) (`v0.4.1`) |
| WatchWolf Server | [`416f9bbf83e7f13df2b9df65c119fda98cedeeab`](https://github.com/sarhatabaot/WatchWolf-Server/commit/416f9bbf83e7f13df2b9df65c119fda98cedeeab) (`v0.4.1`) |
| Tester | [Custom fork release `watchwolf-tester-0.4.1.jar`](https://github.com/sarhatabaot/WatchWolf-Tester/releases/download/v0.4.1/watchwolf-tester-0.4.1.jar) via artifact-only Ivy repository |

The `v0.4.1` control-plane images were available on 2026-10-23 and are selected
by release tag in `watchwolf-compose.yaml`. On a machine with Docker Linux containers,
the developer can run `docker compose -f watchwolf-compose.yaml up --wait -d`,
then `gradlew.bat testWatchWolf` (or `./gradlew testWatchWolf` on Unix), followed
by `docker compose -f watchwolf-compose.yaml down`. WatchWolf downloads the
requested Paper server on first use. The existing project `docker-compose.yml`
is a separate manual server with persistent data.

The first suite covers only startup and a console command. After it passes,
add real-player tests for seed consumption and delayed replanting, permission
denial, disabled worlds, no-seeds behavior, and tilling. Folia scheduling needs
its own server run. Record each run's plugin commit, server and Java versions,
control-plane image digests, task result, and logs before claiming coverage.
