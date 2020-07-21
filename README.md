# Share Damage Minecraft plugin
### [Latest release](https://github.com/morozoffnor/Share-Damage-Minecraft-plugin/releases/tag/v1.2)
### Features
* If any player gets damage, all the other players get the same amount of it.
* You can start and stop the game whenever you want.
* Statistics for this run will be sent in chat when the run ends.
* Database integration made for the freaks who want to *store everything*.
### Setup
Put plugin in your `plugins` folder and reload the server.

Send `/sharedamage start` in chat to start the run and `/sharedamage stop` to stop one.

#### Database
The plugin will work perfectly fine without this but if you want a db for some reason, do this:

Create ([or download if you're lazy](https://github.com/morozoffnor/Share-Damage-Minecraft-plugin/blob/master/database.properties)) a file `database.properties` in `plugins/sharedamage` folder. 
Put your db properties in it. The file must look like this:
```
host=your-domain-or-ip.com
port=3306
database=the-name-of-the-database
username=db-username
password=db-user-password
```
Save it and reload your server.

> Don't ask questions about db. I just wanted to test my skillz and created this for future projects.
