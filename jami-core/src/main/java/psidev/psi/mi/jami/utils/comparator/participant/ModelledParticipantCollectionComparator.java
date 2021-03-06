package psidev.psi.mi.jami.utils.comparator.participant;

import psidev.psi.mi.jami.model.ModelledParticipant;
import psidev.psi.mi.jami.utils.comparator.CollectionComparator;

/**
 * Comparator for collection of biological participants
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>13/02/13</pre>
 */
public class ModelledParticipantCollectionComparator extends CollectionComparator<ModelledParticipant> {

    /**
     * Creates a new component CollectionComparator. It requires a Comparator for the biological participants in the Collection
     *
     * @param biologicalParticipantComparator a {@link psidev.psi.mi.jami.utils.comparator.participant.CustomizableModelledParticipantComparator} object.
     */
    public ModelledParticipantCollectionComparator(CustomizableModelledParticipantComparator<ModelledParticipant> biologicalParticipantComparator) {
        super(biologicalParticipantComparator);
    }


    /** {@inheritDoc} */
    @Override
    public CustomizableModelledParticipantComparator getObjectComparator() {
        return (CustomizableModelledParticipantComparator) super.getObjectComparator();
    }
}
