# Geocraft - Geoguessr but in Minecraft
Geocraft is a Minecraft plugin that allows you to play Geoguessr in Minecraft.

Of course, it's different from the real Geoguessr but the concept is the same.
After some configurations, you will be teleported to a pseudo-random location 
in your world. You will have guess where you are and put a banner on the guess
map. The closer you are to the real location, the more points you will get.

## Installation
1. Download the latest release from the [releases page](https://github.com/MatteoLanglois/Geocraft) (It will be available soon)
2. Put the jar file in your plugins folder
3. Start your server

## Configuration
The configuration file is located in the `plugins/Geocraft/config.yml` file.

Here is an example of the configuration file:
```yaml
database:
  host: localhost
  port: 3306
  name: geocraft
  user: geocraft
  password: password
mapLocation:
  x: 0
  y: 255
  z: 0
tools:
  selection:
    - PAPER
materials:
  grass:
    - GRASS_BLOCK
    - OAK_LEAVES
  water:
    - WATER
  building:
    - STONE
    - COBBLESTONE
  road:
    - GRAY_CONCRETE
    - WHITE_CONCRETE
  sand:
    - SAND
    - SANDSTONE
```

### Database
The database section is used to configure the database connection. If you use
MySQL, you should also add the MySQL driver to your server.

### Map Location
The map location is the location where the guess map will be generated. The
default location is the world spawn.

### Tools
The tools section is used to configure the tools that the player can use to
save locations. The default tool is the paper.

### Materials
The materials section is used to configure the materials that will be used to
generate the guess map.

## Start a game
### Configuration
To start a game, you will first need to save some data in the database.
Happily there are some commands for that purpose. First you will need to add
a Region.
```
/geocraft-create region <name>
```
Then you will need to add a city to the region.
```
/geocraft-create city <region> <name>
```
Then, you will need to add a district to the city.
```
/geocraft-create district <city> <name>
```
Finally, you will need to add a location to the district.
```
/geocraft-create road <district> <name>
```
Each location will be saved in the database and will be used to teleport you
in your world. I know that it's a bit long but it's the only way to save
locations in the database that I found and it allows to have a lot of
locations and to start a game with some settings.

Now you need to create the map, it's only the limit of your world that will
be used to generate the map. You can use the following command to create the
map.
```
/geocraft map create <minX> <minZ> <maxX> <maxZ> <zoneType> <zoneName>
```
The zone type can be : `region`, `city`, `district` or `road`, `world`. The zone name is
the name of the zone that you want to use to generate the map.

### Start the game
Now that you have saved some locations and created the map, you can start the
game. You can use the following command to start the game.
```
/geocraft start <zoneType> <zoneName> <time>
```
The zone type can be : `region`, `city`, `district` or `road`, `world`. The zone name is
the name of the zone that you want to use to start the game. The time is the
time that the player will have to guess where he is.

Now the other players can join the game with :
```
/geocraft join
```

Once everyone has joined , you could start the game with :
```
/geocraft start
```

Now the guess map will be built. For now, it's very long. I will try to
optimize it in the future. Once the map is built, the players will be
teleported to a pseudo-random location in the world. They will have to guess
where they are and put a banner on the guess map.

To go to the guess map, you can use the following command :
```
/geocraft-guess tp
```
Use the same commands to go back to the game.

Once the timer is over, the game will be over and the players will be
teleported back to their previous location. In the future they will get back
their inventory too.

## Commands
- `/geocraft-create region <name>` : Create a region
- `/geocraft-create city <region> <name>` : Create a city
- `/geocraft-create district <city> <name>` : Create a district
- `/geocraft-create road <district> <name>` : Create a location
- `/geocraft map create <minX> <minZ> <maxX> <maxZ> <zoneType> <zoneName>` : Create a map
- `/geocraft start <zoneType> <zoneName> <time>` : Start a game
- `/geocraft join` : Join a game
- `/geocraft start` : Start a game
- `/geocraft-guess tp` : Teleport to the guess map (and back to the game)
- `/geocraft-reloaddb` : Reload the database