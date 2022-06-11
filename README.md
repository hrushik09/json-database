# JSON Database

This is a multithreaded server application to store JSON objects. It uses sockets for communication between clients and
server. All the data is stored in `src\main\java\server\data\db.json`.

- Google Gson library is used for JSON serialization/deserialization.
- jCommander framework is used to parse command line parameters passed to the client.
- `ReentrantReadWriteLock` is used to allow multiple readers of the `db.json`
  file but only one writer at any time. Thus, concurrency related errors are avoided.

## Commands

Users can use the following commands: `get`, `set`, `delete`.

Some examples of commands after parsing:

`{ "type": "get", "key": "Secret key" }`

`{ "type": "set", "key": "Secret key", "value": "Secret value" }`

`{ "type": "delete", "key": "Key to be deleted" }`

`{"type": "exit"}` is used to shut down the server.

## How to Run

- First run `src\main\java\server\Main.java` to start server
- Then, run `src\main\java\client\Main.java` as many times as needed to spin up new clients.

The command arguments will be passed to the client via command line arguments in the following format:

`java <path/to/client/Main> -t set -k "Some key" -v "Here is some text to store on the server"`

Here,

- `-t` is the type of the request
- `-k` is the key
- `-v` is the value to save in the database: this is needed only for a set request

User can also pass in a file containing these arguments

- `java <path/to/client/Main> -in testSet.json`

Here, `-in` is the file name containing command arguments in valid format.

## What type of data can be saved?

Primitive data types and JSON objects are supported. The following is a valid command.

```
{
  "type": "set",
  "key": "person",
  "value": {
    "name": "This is person name",
    "car": {
      "model": "This is car model",
      "year": 2000
    },
    "mobile": {
      "brand": "This is mobile brand"
    },
    "cities": [
      "city1",
      "city2",
      "city3"
    ]
  }
}
```

In the code snippet below, if the user wants to get only the surname of the person:

```
{
  ...,
  "person": {
    "name": "Adam",
    "surname": "Smith"
  }
  ...
}
```

then, key should be the full path to this field in the form of a JSON array: `["person", "surname"]`. If the user wants
to get the full person object, then key should be `["person"]`.

It is possible to set only the surname using a key `["person", "surname"]` and give it any value including another JSON.
User can also set new values inside other JSON values; using a key `["person", "age"]` and a value 25, the person
object should look like this:

```
{
  ...,
  "person": {
    "name": "Adam",
    "surname": "Smith",
    "age": 25
  }
  ...
}
```

If there are no root objects, they will be created. For example, if the database does not have a "person1"
key but the user set the value `{"id1": 12, "id2": 14}` for the key `["person1", "inside1", "inside2"]`, then
the database will have the following structure:

```
{
  ...,
  "person1": {
    "inside1": {
      "inside2": {
        "id1": 12,
        "id2": 14
      }
    }
  },
  ...
}
```

The deletion of objects follows same rules. If user deletes the object above by the
key `["person1", "inside1", "inside2"]`, then only "inside2" will be deleted, not "inside1" or "person1".

```
{
  ...,
  "person1": {
    "inside1": {}
  },
  ...
}
```

---

This program is based on the JSON Database project on [JetBrains Academy](https://hyperskill.org).
