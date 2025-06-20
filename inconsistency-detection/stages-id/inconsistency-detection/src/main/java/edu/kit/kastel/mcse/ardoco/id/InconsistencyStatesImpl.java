/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.id;

import java.io.Serial;
import java.util.EnumMap;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.InconsistencyState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.InconsistencyStates;

public class InconsistencyStatesImpl implements InconsistencyStates {
    @Serial
    private static final long serialVersionUID = 5683120566268132355L;
    private EnumMap<Metamodel, InconsistencyStateImpl> inconsistencyStates;

    private InconsistencyStatesImpl() {
        inconsistencyStates = new EnumMap<>(Metamodel.class);
    }

    public static InconsistencyStatesImpl build() {
        var recStates = new InconsistencyStatesImpl();
        for (Metamodel mm : Metamodel.values()) {
            recStates.inconsistencyStates.put(mm, new InconsistencyStateImpl());
        }
        return recStates;
    }

    @Override
    public InconsistencyState getInconsistencyState(Metamodel metamodel) {
        return inconsistencyStates.get(metamodel);
    }
}
