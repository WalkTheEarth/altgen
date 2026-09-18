# AltGen

A [Meteor Client](https://meteorclient.com) addon that adds a **New Altening Account** button to Minecraft's kick/ban (disconnect) screen. One click: it generates a fresh [TheAltening](https://thealtening.com) account, logs into it, saves it to your alt manager, and reconnects you to the server.

## Setup

1. Install [Meteor Client](https://meteorclient.com) for the matching Minecraft version.
2. Put the AltGen jar in your `mods` folder.
3. Launch the game. On first use of the button, AltGen asks for your TheAltening **API key** (find it at [thealtening.com/account](https://thealtening.com/account)). It is saved in `.minecraft/meteor-client/altgen.nbt`.

## Usage

- When you get kicked or banned, the disconnect screen shows a **New Altening Account** button below the vanilla buttons.
- Clicking it opens a small Meteor-style window: click **Generate & Login**.
- If you were connected to a server, AltGen reconnects automatically after logging in.
- You can also reach the login screen any time with `.altgen` — wait, there is no command. Future idea.

## Notes

- Requires a paid TheAltening plan with API access.
- If the API key is missing, the setup screen opens first.
- Daily limit and invalid-key errors are shown as messages in the login window.
- If no previous server connection is known, you're dropped at the multiplayer screen instead of auto-reconnecting.

## Building

```bash
./gradlew build
```

Requires Java 25 (auto-provisioned by Gradle via the Foojay resolver).
Output: `build/libs/altgen-0.1.0.jar`.

The addon icon is generated programmatically: `python3 tools/make_icon.py` (requires Pillow).
