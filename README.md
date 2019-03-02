# rss-reader

## Requirements

- Java (1.8)

## Building process

Windows:
1) Go to folder with sources
2) Run ```gradlew.bat shadowJar```
3) Go to build/libs

Linux/macOS:
1) Go to folder with sources
2) Run ```./gradlew shadowJar```
3) Go to build/libs

## Running

Run created on the previous stage jar using ```java -jar```

The following lines will appear:
```
Rss-reader is ready to serve
Please, type in a command or run 'help'
```

'help' command will provide short description of available commands.

'help''s output:
```
create <url>
delete <feed-id>
describe <feed-id>
help [<command-name>]
hide <property-name> <feed-id>
list
poll [on | off] <feed-id>
set [poll-period | filename | item-amount | name] <poll-period-in-sec | filename | item-amount | name> <feed-id>
show <property-name> <name-id>
stop
```
