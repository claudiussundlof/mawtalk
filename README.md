# MawTalk

Simple, lightweight private-messaging and party chat plugin for Paper/Purpur.

## Features
- Player-to-player private messages (with last-reply tracking)
- Reply to the last player who messaged you
- Ignore/unignore players (blocks both private and public chat)
- View your ignore list
- Create lightweight parties, invite/accept/leave
- Toggle party chat: when enabled, your chat goes only to party members

## Commands
- /msg <player> <message> — send a private message (alias: /w)
- /reply <message> — reply to the last player who messaged you (alias: /r)
- /ignore <player> — ignore a player (blocks their private and public messages)
- /unignore <player> — stop ignoring a player
- /ignorelist — list players you are ignoring
- /mawtalk — show plugin help (alias: /mt)
- /party create — create a new party
- /party invite <player> — invite a player to your party (leader only)
- /party accept — accept a pending party invite
- /party leave — leave your current party
- /party chat — toggle party chat on/off (when on, your chat is sent only to party members)