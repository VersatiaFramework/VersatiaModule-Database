# VersatiaModule-Database

The API of the Database module of the [Versatia framework](https://github.com/VersatiaFramework):
a database per plugin, injected and ready. Without any configuration a plugin gets a SQLite file in
its data folder with the migrations from its JAR applied; the server admin moves it to H2, MySQL,
MariaDB or PostgreSQL by editing `database.yml`, and the module takes care of the pool, the SQL
differences between engines, the threads and the shutdown.

```kotlin
@LocalBean
class Profiles(database: Database) : PlayerStore<Profile> by database.playerStore("profiles", Profile::uuid) { Profile(it) }

database.async { sql("SELECT name, coins FROM {prefix}profiles ORDER BY coins DESC LIMIT 10").list<TopEntry>() }
    .thenSync { top -> show(sender, top) }
```

Documentation: [Database module](https://versatiaframework.github.io/VersatiaDocs/docs/modules/database).

Like VersatiaAPI and VersatiaCore, the module comes in two halves. This repository is the public
half, the library plugins compile against, nothing that runs:

| Package                                         | What it holds                                                                 |
|-------------------------------------------------|-------------------------------------------------------------------------------|
| `com.github.marcoral.versatia.database`         | `Database`, `SqlScope`, `Transaction`, `Statement`, `Row`, `DatabaseFuture`, `Engine`, `Json`, the status and the exceptions |
| `com.github.marcoral.versatia.database.mapping` | `Column` and `ColumnCodec`, for columns whose name or type needs help         |
| `com.github.marcoral.versatia.database.store`   | `KeyedStore`, `PlayerStore`, `StoreSchema`, `StoreTransaction`: write-behind caches of rows |

The API names only JDK, Kotlin and Bukkit types; the pool, the drivers and every engine-specific
rule live in the implementation, the plugin installed on the server as `VersatiaModule-Database`,
in the private repository VersatiaModule-DatabaseImpl. Its JAR carries these classes, so a plugin
never bundles them.

## Using it

```kotlin
dependencies {
    compileOnly(libs.versatia.starter)
    compileOnly(libs.versatia.database)
}
```

```yaml
depend:
  - VersatiaCore
  - VersatiaModule-Database
```

`VersatiaStarter` does not include this API: a plugin using the module adds it next to the starter
(`libs.versatia.database` from the `VersatiaCatalog`, or
`com.github.marcoral.versatia:VersatiaModule-Database:0.0.1`).

## Building

Requires JDK 21. The artifact is not on a Maven repository yet:

```bash
./gradlew publishToMavenLocal
```

publishes `com.github.marcoral.versatia:VersatiaModule-Database` to your local Maven repository.
