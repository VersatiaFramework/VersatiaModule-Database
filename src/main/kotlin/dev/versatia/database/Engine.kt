package dev.versatia.database

/** The engines the module speaks, as `engine:` names them (case-insensitive); [embedded] ones are a file in the plugin's data folder. */
enum class Engine(val embedded: Boolean) {
    SQLITE(true), H2(true), MYSQL(false), MARIADB(false), POSTGRESQL(false)
}
