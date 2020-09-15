## Daytime / Night-time Vote plugin for Minecraft

### Daytime
Players can vote during the night to set it to daytime.
Voting starts when the first player activates the command **Dayplease** (_or Daypls_) and will reset when it next becomes daytime.
#### To start a vote simply type
> _/Dayplease **or** _/Dayplease Yes_

Players can vote from any world and can change their vote if they change their mind.
> _/Dayplease No_

### Night-time
Players can vote during the day to set it to night-time.
Voting starts when the first player activates the command **Nightplease** (_or Nightpls_) and will reset when it next becomes night-time.
#### To start a vote simply type
> _/Nightplease **or** _/Nightplease Yes_

Just like the Daytime vote, players can vote from any world and can change their vote if they change their mind.
> _/Nightplease No_

#### Daytime and Night-time voting
Both votes requires a Yes from at-least half the players currently on the **Overworld** to change the time.
Players can query whether there is an on-going vote and how many votes it has by typing the following:
> _/Dayplease Query_
<br/>_/Nightplease Query_

### Debugging
Players with the _daytimevote.canDebug_ permission can enable / disable the plugins debug mode with the command **DebugDaytimevote** (_or /ddv_).
When debug mode is enabled debug messages will be sent to players with the _daytimevote.canDebug_ permission as well as the console.
#### To enable / disable debug mode type
> _/DebugDaytimevote Enable_ **or** _/DebugDaytimevote Disable_

Players with permission can query whether debugging is enabled by typing the following:
> _/DebugDaytimevote Query_

### Modifying the config
Players with the _daytimevote.canChangeConfig_ permission can modify the plugin's config file with the command **changeDaytimeVoteConfig** (_or /cdvc_).
Players can change the calculation used to determine how many players are required to pass a vote or explicitly set the number.
#### To modify the number of required votes for daytime / night-time
> _/changeDaytimeVoteConfig Day_ **or** _/changeDaytimeVoteConfig Night_ **or** _/DebugDaytimevote All_

Players with permission can query the currently saved settings by typing the following:
> _/changeDaytimeVoteConfig Query_

### Permissions
Below is a list of permissions and what they are required for:
- _daytimevote.*_
  - Gives access to all DaytimeVote commands
  - Defaults to players with operator status (_op_)
- _daytimevote.canVote_
  - Allows players to start and participate in votes for daytime / night-time
  - Defaults to all players
- _daytimevote.canDebug_
  - Allows players to enable / disable DaytimeVote debugging mode
  - Defaults to players with operator status (_op_)
- _daytimevote.canChangeConfig_
  - Allows players to modify the DaytimeVote config
  - Defaults to players with operator status (_op_)

### Current Release
The current release is [Version 3.0](https://github.com/Scorpio1331/MC.DaytimeVote/releases/latest)

View other releases [here](https://github.com/Scorpio1331/MC.DaytimeVote/releases)

### Install
To install, put the DaytimeVote.jar into your plugin's folder.
