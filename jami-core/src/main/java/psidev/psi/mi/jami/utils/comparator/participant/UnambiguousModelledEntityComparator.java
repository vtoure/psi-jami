package psidev.psi.mi.jami.utils.comparator.participant;

import psidev.psi.mi.jami.model.ModelledEntity;
import psidev.psi.mi.jami.utils.comparator.feature.UnambiguousModelledFeaturecomparator;

/**
 * unambiguous biological entity comparator.
 * It will compare the basic properties of a biological participant using UnambiguousParticipantBaseComparator.
 *
 * This comparator will ignore all the other properties of a biological participant.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>13/02/13</pre>
 */
public class UnambiguousModelledEntityComparator extends ModelledEntityComparator {

    private static UnambiguousModelledEntityComparator defaultParticipantComparator;

    /**
     * {@inheritDoc}
     *
     * Creates a new UnambiguousModelledParticipantComparator. It will use a UnambiguousParticipantBaseComparator to compare
     * the basic properties of a participant.
     */
    public UnambiguousModelledEntityComparator() {
        super(new UnambiguousEntityBaseComparator(), new UnambiguousModelledFeaturecomparator());
    }

    /**
     * <p>Constructor for UnambiguousModelledEntityComparator.</p>
     *
     * @param entityBaseComparator a {@link psidev.psi.mi.jami.utils.comparator.participant.UnambiguousEntityBaseComparator} object.
     */
    public UnambiguousModelledEntityComparator(UnambiguousEntityBaseComparator entityBaseComparator) {
        super(entityBaseComparator != null ? entityBaseComparator :
                new UnambiguousEntityBaseComparator(), new UnambiguousModelledFeaturecomparator());
    }

    /** {@inheritDoc} */
    @Override
    public UnambiguousEntityBaseComparator getEntityBaseComparator() {
        return (UnambiguousEntityBaseComparator) super.getEntityBaseComparator();
    }
    /**
     * {@inheritDoc}
     *
     * It will compare the basic properties of a component using UnambiguousParticipantBaseComparator.
     *
     * This comparator will ignore all the other properties of a component.
     */
    @Override
    public int compare(ModelledEntity component1, ModelledEntity component2) {
        return super.compare(component1, component2);
    }

    /**
     * Use UnambiguousModelledParticipantComparator to know if two biological participant are equals.
     *
     * @param component1 a {@link psidev.psi.mi.jami.model.ModelledEntity} object.
     * @param component2 a {@link psidev.psi.mi.jami.model.ModelledEntity} object.
     * @return true if the two biological participant are equal
     */
    public static boolean areEquals(ModelledEntity component1, ModelledEntity component2){
        if (defaultParticipantComparator == null){
            defaultParticipantComparator = new UnambiguousModelledEntityComparator();
        }

        return defaultParticipantComparator.compare(component1, component2) == 0;
    }
}
