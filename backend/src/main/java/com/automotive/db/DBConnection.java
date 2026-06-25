package com.automotive.db;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBConnection — centralised data-source factory backed by Oracle UCP.
 *
 * <p><b>Migration note (Java 8 → Java 21):</b>
 * <ul>
 *   <li>Legacy: used raw {@code DriverManager.getConnection()} with hard-coded
 *       credentials, no connection pooling, and returned {@code null} on error
 *       (causing downstream NullPointerExceptions).</li>
 *   <li>Modern: Oracle Universal Connection Pool (UCP) manages a pool of
 *       pre-authenticated connections. Credentials are read from environment
 *       variables — no secrets in source control. A connection failure now
 *       throws {@link SQLException} so callers handle it explicitly.</li>
 * </ul>
 *
 * <p>Environment variables consumed (set these in the container or OS):
 * <ul>
 *   <li>{@code DB_URL}      — JDBC URL, e.g. {@code jdbc:oracle:thin:@localhost:1521/XEPDB1}</li>
 *   <li>{@code DB_USER}     — Oracle username, e.g. {@code SYSTEM}</li>
 *   <li>{@code DB_PASSWORD} — Oracle password</li>
 * </ul>
 *
 * <p>Default pool sizing: min=2, max=10, initial=2. Adjust via the
 * {@code DB_POOL_MIN_SIZE} / {@code DB_POOL_MAX_SIZE} environment variables
 * if needed.
 */
public final class DBConnection {

    private static final Logger LOG = Logger.getLogger(DBConnection.class.getName());

    // -----------------------------------------------------------------------
    // Environment variable keys
    // -----------------------------------------------------------------------
    private static final String ENV_URL      = "DB_URL";
    private static final String ENV_USER     = "DB_USER";
    private static final String ENV_PASSWORD = "DB_PASSWORD";
    private static final String ENV_MIN_POOL = "DB_POOL_MIN_SIZE";
    private static final String ENV_MAX_POOL = "DB_POOL_MAX_SIZE";

    // -----------------------------------------------------------------------
    // Pool defaults
    // -----------------------------------------------------------------------
    private static final int DEFAULT_MIN_POOL = 2;
    private static final int DEFAULT_MAX_POOL = 10;

    // -----------------------------------------------------------------------
    // Singleton pool data source — initialised once at class-load time
    // -----------------------------------------------------------------------
    private static final PoolDataSource DATA_SOURCE;

    static {
        PoolDataSource pds;
        try {
            pds = PoolDataSourceFactory.getPoolDataSource();
            pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");

            String url  = requireEnv(ENV_URL);
            String user = requireEnv(ENV_USER);
            String pass = requireEnv(ENV_PASSWORD);

            pds.setURL(url);
            pds.setUser(user);
            pds.setPassword(pass);

            int minPool = parseIntEnv(ENV_MIN_POOL, DEFAULT_MIN_POOL);
            int maxPool = parseIntEnv(ENV_MAX_POOL, DEFAULT_MAX_POOL);
            pds.setMinPoolSize(minPool);
            pds.setMaxPoolSize(maxPool);
            pds.setInitialPoolSize(minPool);

            LOG.info(() -> "UCP pool initialised: url=%s user=%s minPool=%d maxPool=%d"
                    .formatted(url, user, minPool, maxPool));

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Failed to initialise UCP connection pool", e);
            throw new ExceptionInInitializerError(e);
        }
        DATA_SOURCE = pds;
    }

    private DBConnection() {
        // Utility class — no instances
    }

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    /**
     * Borrows a {@link Connection} from the UCP pool.
     *
     * <p>Callers <em>must</em> close the connection in a
     * {@code try-with-resources} block to return it to the pool:
     * <pre>{@code
     * try (Connection con = DBConnection.getConnection()) {
     *     // use con
     * }
     * }</pre>
     *
     * @return a live JDBC connection
     * @throws SQLException if the pool cannot provide a connection
     */
    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private static String requireEnv(String key) {
        String val = System.getenv(key);
        if (val == null || val.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable '%s' is not set".formatted(key));
        }
        return val;
    }

    private static int parseIntEnv(String key, int defaultValue) {
        String val = System.getenv(key);
        if (val == null || val.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(val.strip());
        } catch (NumberFormatException e) {
            LOG.warning(() -> "Invalid integer value for env var '%s': '%s'. Using default %d."
                    .formatted(key, val, defaultValue));
            return defaultValue;
        }
    }
}