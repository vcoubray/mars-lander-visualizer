package fr.vco.genetic.algorithm.visualizer.persistence

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database
import org.ktorm.support.sqlite.SQLiteDialect

private val SCHEMA = """
    PRAGMA foreign_keys = ON;
    PRAGMA journal_mode = WAL;

    CREATE TABLE IF NOT EXISTS simulation (
        id               INTEGER PRIMARY KEY AUTOINCREMENT,
        settings_json    TEXT    NOT NULL,
        status           TEXT    NOT NULL,
        duration_ms      INTEGER NOT NULL DEFAULT 0,
        best_score       REAL    NOT NULL DEFAULT 0,
        generation_count INTEGER NOT NULL DEFAULT 0,
        created_at       INTEGER NOT NULL
    );

    CREATE TABLE IF NOT EXISTS generation (
        id             INTEGER PRIMARY KEY AUTOINCREMENT,
        simulation_id  INTEGER NOT NULL REFERENCES simulation(id) ON DELETE CASCADE,
        generation_idx INTEGER NOT NULL,
        best_score     REAL    NOT NULL,
        mean_score     REAL    NOT NULL,
        population_size INTEGER NOT NULL,
        UNIQUE(simulation_id, generation_idx)
    );

    CREATE INDEX IF NOT EXISTS idx_generation_simulation ON generation(simulation_id);

    CREATE TABLE IF NOT EXISTS chromosome (
        id               INTEGER PRIMARY KEY AUTOINCREMENT,
        generation_id    INTEGER NOT NULL REFERENCES generation(id) ON DELETE CASCADE,
        chromosome_idx   INTEGER NOT NULL,
        score            REAL    NOT NULL,
        normalized_score REAL    NOT NULL,
        cumulative_score REAL    NOT NULL,
        payload_json     TEXT    NOT NULL
    );

    CREATE INDEX IF NOT EXISTS idx_chromosome_generation ON chromosome(generation_id);

    CREATE TABLE IF NOT EXISTS benchmark (
        id            INTEGER PRIMARY KEY AUTOINCREMENT,
        settings_json TEXT    NOT NULL,
        status        TEXT    NOT NULL,
        created_at    INTEGER NOT NULL
    );

    CREATE TABLE IF NOT EXISTS benchmark_run (
        id               INTEGER PRIMARY KEY AUTOINCREMENT,
        benchmark_id     INTEGER NOT NULL REFERENCES benchmark(id) ON DELETE CASCADE,
        puzzle_id        INTEGER NOT NULL,
        run_idx          INTEGER NOT NULL,
        status           TEXT    NOT NULL,
        duration_ms      INTEGER NOT NULL DEFAULT 0,
        best_score       REAL    NOT NULL DEFAULT 0,
        generation_count INTEGER NOT NULL DEFAULT 0,
        settings_json    TEXT    NOT NULL,
        UNIQUE(benchmark_id, puzzle_id, run_idx)
    );

    CREATE INDEX IF NOT EXISTS idx_run_benchmark ON benchmark_run(benchmark_id);
""".trimIndent()

fun buildDatabase(): Database {
    val dbPath = System.getenv("DB_PATH") ?: "ga-visualizer.db"
    val cfg = HikariConfig().apply {
        jdbcUrl = "jdbc:sqlite:$dbPath"
        driverClassName = "org.sqlite.JDBC"
        maximumPoolSize = 1
        addDataSourceProperty("busy_timeout", "5000")
    }
    val ds = HikariDataSource(cfg)
    val db = Database.connect(ds, dialect = SQLiteDialect())
    runMigrations(db)
    return db
}

fun buildInMemoryDatabase(): Database {
    val db = Database.connect("jdbc:sqlite::memory:", dialect = SQLiteDialect())
    runMigrations(db)
    return db
}

private fun runMigrations(db: Database) {
    db.useConnection { conn ->
        conn.createStatement().use { stmt ->
            SCHEMA.split(";")
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .forEach { stmt.execute(it) }
        }
    }
}
