/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.vector;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.sqlite.SQLiteConfig;

import edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.WordSimUtils;

/**
 * Manages a connection to a SQLite database containing vector word embeddings.
 * The table {@code words} must have columns {@code word} (TEXT, unique) and {@code vec} (BLOB, non-null).
 * Vector blobs are stored as consecutive floats.
 */
public class VectorSqliteDatabase implements WordVectorDataSource, AutoCloseable {

    private static final int BYTES_PER_FLOAT = 4;
    private static final String SELECT_QUERY = "SELECT `vec` FROM `words` WHERE `word` = ?";

    private final Connection connection;
    private final PreparedStatement selectStatement;

    /**
     * Instantiates the database and opens a connection until {@link #close()} is called.
     *
     * @param sqliteFile the path to the SQLite file
     * @throws SQLException if connecting to the database fails
     */
    public VectorSqliteDatabase(Path sqliteFile) throws SQLException {
        if (!Files.exists(sqliteFile)) {
            throw new IllegalArgumentException("sqliteFile does not exist: " + sqliteFile);
        }

        SQLiteConfig cfg = WordSimUtils.getSqLiteConfig();

        this.connection = cfg.createConnection("jdbc:sqlite:" + sqliteFile);
        this.selectStatement = this.connection.prepareStatement(SELECT_QUERY);
    }

    /**
     * Attempts to retrieve the vector representation of the given word.
     *
     * @param word the word
     * @return the vector representation, or {@link Optional#empty()} if no representation exists in the database.
     * @throws RetrieveVectorException if a database access error occurs
     */
    @Override
    public Optional<float[]> getWordVector(String word) throws RetrieveVectorException {
        try {
            this.selectStatement.setString(1, word);
        } catch (SQLException e) {
            throw new RetrieveVectorException("unable to pass word to the database: " + word, e);
        }

        ByteBuffer bytes = null;

        try (ResultSet result = this.selectStatement.executeQuery()) {
            if (result.next()) {
                bytes = ByteBuffer.wrap(result.getBytes("vec"));
            }
        } catch (SQLException e) {
            throw new RetrieveVectorException("unable to execute query for word:" + word, e);
        }

        if (bytes == null) {
            return Optional.empty();
        }

        float[] vec = new float[bytes.capacity() / BYTES_PER_FLOAT];

        for (int i = 0; i < vec.length; i++) {
            vec[i] = bytes.getFloat();
        }

        return Optional.of(vec);
    }

    @Override
    public void close() throws Exception {
        this.selectStatement.close();
        this.connection.close();
    }

}
