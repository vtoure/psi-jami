package psidev.psi.mi.jami.utils.comparator.interaction;

import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.utils.comparator.participant.UnambiguousModelledParticipantComparator;

/**
 * Unambiguous ModelledInteraction comparator.
 *
 * It will use a UnambiguousInteractionBaseComparator to compare basic interaction properties.
 * Then it will compare the modelledParticipants using UnambiguousModelledParticipantComparator.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>21/01/13</pre>
 */
public class UnambiguousModelledInteractionComparator extends ModelledInteractionComparator{

    private static UnambiguousModelledInteractionComparator unambiguousModelledInteractionComparator;

    /**
     * {@inheritDoc}
     *
     * Creates a new UnambiguousModelledInteractionComparator. It will use a UnambiguousInteractionBaseComparator to
     * compare basic interaction properties
     */
    public UnambiguousModelledInteractionComparator() {
        super(new UnambiguousModelledParticipantComparator(), new UnambiguousInteractionBaseComparator());
    }
    @Override
    /**
     * It will use a UnambiguousInteractionBaseComparator to compare basic interaction properties.
     * Then it will compare the modelledParticipants using UnambiguousModelledParticipantComparator.
     * */
    public int compare(ModelledInteraction interaction1, ModelledInteraction interaction2) {
        return super.compare(interaction1, interaction2);
    }

    /**
     * Use UnambiguousModelledInteractionComparator to know if two modelled interactions are equals.
     *
     * @param interaction1 a {@link psidev.psi.mi.jami.model.ModelledInteraction} object.
     * @param interaction2 a {@link psidev.psi.mi.jami.model.ModelledInteraction} object.
     * @return true if the two modelled interactions are equal
     */
    public static boolean areEquals(ModelledInteraction interaction1, ModelledInteraction interaction2){
        if (unambiguousModelledInteractionComparator == null){
            unambiguousModelledInteractionComparator = new UnambiguousModelledInteractionComparator();
        }

        return unambiguousModelledInteractionComparator.compare(interaction1, interaction2) == 0;
    }
}
