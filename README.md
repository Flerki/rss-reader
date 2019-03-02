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

## Example
```create http://static.feed.rbc.ru/rbc/logical/footer/news.rss``` - creates feed for http://static.feed.rbc.ru/rbc/logical/footer/news.rss , asks the user if they want to hide any feed's entries'properties. 

```list``` - shows only one entry.

```describe 1``` - describes created feed.

```set filename rbc 1``` - sets rbc as a file for writing in for the feed.

```poll on 1``` - begins polling.

```stop``` - stops the program.
