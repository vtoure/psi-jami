package psidev.psi.mi.jami.utils.comparator.interaction;

import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.utils.comparator.cv.UnambiguousCvTermComparator;
import psidev.psi.mi.jami.utils.comparator.participant.UnambiguousModelledParticipantComparator;

/**
 * Unambiguous curated ModelledInteraction comparator.
 *
 * It will use a UnambiguousCuratedInteractionBase to compare basic interaction properties.
 * Then it will compare the modelledParticipants using UnambiguousModelledParticipantComparator.
 * Finally, it will compare the source of the modelledInteraction using UnambiguousCvTermComparator
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>21/01/13</pre>
 */
public class UnambiguousCuratedModelledInteractionComparator extends CuratedModelledInteractionComparator{

    private static UnambiguousCuratedModelledInteractionComparator unambiguousCuratedModelledInteractionComparator;

    /**
     * {@inheritDoc}
     *
     * Creates a new UnambiguousCuratedModelledInteractionComparator. It will use a UnambiguousInteractionBaseComparator to
     * compare basic interaction properties
     */
    public UnambiguousCuratedModelledInteractionComparator() {
        super(new UnambiguousModelledParticipantComparator(), new UnambiguousCuratedInteractionBaseComparator(), new UnambiguousCvTermComparator());
    }

    /** {@inheritDoc} */
    @Override
    public UnambiguousCuratedInteractionBaseComparator getInteractionBaseComparator() {
        return (UnambiguousCuratedInteractionBaseComparator) super.getInteractionBaseComparator();
    }

    public UnambiguousCvTermComparator getSourceComparator() {
        return (UnambiguousCvTermComparator) super.getSourceComparator();
    }

    /**
     * It will use a UnambiguousCuratedInteractionBase to compare basic interaction properties.
     * Then it will compare the modelledParticipants using UnambiguousModelledParticipantComparator.
     * Finally, it will compare the source of the modelledInteraction using UnambiguousCvTermComparator
     * */
    @Override
    public int compare(ModelledInteraction interaction1, ModelledInteraction interaction2) {
        return super.compare(interaction1, interaction2);
    }

    /**
     * Use UnambiguousCuratedModelledInteractionComparator to know if two modelled interactions are equals.
     *
     * @param interaction1 a {@link psidev.psi.mi.jami.model.ModelledInteraction} object.
     * @param interaction2 a {@link psidev.psi.mi.jami.model.ModelledInteraction} object.
     * @return true if the two modelled interactions are equal
     */
    public static boolean areEquals(ModelledInteraction interaction1, ModelledInteraction interaction2){
        if (unambiguousCuratedModelledInteractionComparator == null){
            unambiguousCuratedModelledInteractionComparator = new UnambiguousCuratedModelledInteractionComparator();
        }

        return unambiguousCuratedModelledInteractionComparator.compare(interaction1, interaction2) == 0;
    }
}
