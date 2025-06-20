/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.connectiongenerator;

import java.io.Serial;
import java.util.EnumMap;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.stage.connectiongenerator.ConnectionStates;

public class ConnectionStatesImpl implements ConnectionStates {
    @Serial
    private static final long serialVersionUID = 2578919199522815549L;
    private final EnumMap<Metamodel, ConnectionStateImpl> connectionStates;

    private ConnectionStatesImpl() {
        connectionStates = new EnumMap<>(Metamodel.class);
    }

    public static ConnectionStatesImpl build() {
        var recStates = new ConnectionStatesImpl();
        for (Metamodel mm : Metamodel.values()) {
            recStates.connectionStates.put(mm, new ConnectionStateImpl());
        }
        return recStates;
    }

    @Override
    public ConnectionStateImpl getConnectionState(Metamodel mm) {
        return connectionStates.get(mm);
    }
}
