package psidev.psi.mi.jami.utils.comparator.interaction;

import psidev.psi.mi.jami.model.Interaction;

/**
 * Unambiguous curated Generic interaction comparator.
 * Modelled interactions come first and then experimental interactions
 * - It uses UnambiguousCuratedInteractionEvidenceComparator to compare experimental interactions
 * - It uses UnambiguousCuratedModelledInteractionComparator to compare modelled interactions
 * - It uses UnambiguousCuratedInteractionBaseComparator to compare basic interaction properties
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>05/02/13</pre>
 */
public class UnambiguousCuratedInteractionComparator extends InteractionComparator {
    private static UnambiguousCuratedInteractionComparator unambiguousCuratedInteractionComparator;

    /**
     * {@inheritDoc}
     *
     * Creates a new UnambiguousCuratedInteractionComparator.
     */
    public UnambiguousCuratedInteractionComparator() {
        super(new UnambiguousCuratedInteractionBaseComparator(), new UnambiguousCuratedModelledInteractionComparator(), new UnambiguousCuratedInteractionEvidenceComparator());
    }

    /** {@inheritDoc} */
    @Override
    public UnambiguousCuratedInteractionBaseComparator getInteractionBaseComparator() {
        return (UnambiguousCuratedInteractionBaseComparator) super.getInteractionBaseComparator();
    }

    /** {@inheritDoc} */
    @Override
    public UnambiguousCuratedInteractionEvidenceComparator getExperimentalInteractionComparator() {
        return (UnambiguousCuratedInteractionEvidenceComparator) super.getExperimentalInteractionComparator();
    }

    @Override
    public UnambiguousCuratedModelledInteractionComparator getModelledInteractionComparator() {
        return (UnambiguousCuratedModelledInteractionComparator) super.getModelledInteractionComparator();
    }

    /**
     * Experimental interactions come first, then allosteric interactions, then cooperative interactions, then modelled interactions.
     * Modelled interactions come first and then experimental interactions
     * - It uses UnambiguousCuratedInteractionEvidenceComparator to compare experimental interactions
     * - It uses UnambiguousCuratedModelledInteractionComparator to compare modelled interactions
     * - It uses UnambiguousCuratedInteractionBaseComparator to compare basic interaction properties
     */
    @Override
    public int compare(Interaction interaction1, Interaction interaction2) {
        return super.compare(interaction1, interaction2);
    }

    /**
     * Use UnambiguousCuratedInteractionComparator to know if two interactions are equals.
     *
     * @param interaction1 a {@link psidev.psi.mi.jami.model.Interaction} object.
     * @param interaction2 a {@link psidev.psi.mi.jami.model.Interaction} object.
     * @return true if the two interactions are equal
     */
    public static boolean areEquals(Interaction interaction1, Interaction interaction2){
        if (unambiguousCuratedInteractionComparator == null){
            unambiguousCuratedInteractionComparator = new UnambiguousCuratedInteractionComparator();
        }

        return unambiguousCuratedInteractionComparator.compare(interaction1, interaction2) == 0;
    }
}
