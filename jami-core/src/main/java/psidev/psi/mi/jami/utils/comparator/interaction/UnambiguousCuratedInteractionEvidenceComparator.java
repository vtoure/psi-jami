package psidev.psi.mi.jami.utils.comparator.interaction;

import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.jami.utils.comparator.experiment.UnambiguousCuratedExperimentComparator;
import psidev.psi.mi.jami.utils.comparator.parameter.UnambiguousParameterComparator;
import psidev.psi.mi.jami.utils.comparator.participant.UnambiguousParticipantEvidenceComparator;

/**
 * Unambiguous curated InteractionEvidenceComparator.
 *
 * It will first compare the basic interaction properties using UnambiguousCuratedInteractionBaseComparator.
 * It will then compares the IMEx identifiers if both IMEx ids are set. If at least one IMEx id is not set, it will compare the negative properties.
 * A negative interaction will come after a positive interaction. it will compare
 * the experiment using UnambiguousCuratedExperimentComparator. If the experiments are the same, it will compare the participants using UnambiguousParticipantEvidenceComparator. Then it will compare the parameters using UnambiguousParameterComparator.
 * If the parameters are the same, it will first compare the experimental variableParameters using VariableParameterValueSetComparator and then it will compare the inferred boolean value (Inferred interactions will always come after).
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>31/05/13</pre>
 */
public class UnambiguousCuratedInteractionEvidenceComparator extends InteractionEvidenceComparator {

    private static UnambiguousCuratedInteractionEvidenceComparator unambiguousCuratedExperimentalInteractionComparator;

    /**
     * {@inheritDoc}
     *
     * Creates a new UnambiguousCuratedInteractionEvidenceComparator. It will use a UnambiguousCuratedInteractionBaseComparator to
     * compare basic interaction properties, UnambiguousParameterComparator to compare parameters, UnambiguousCuratedExperimentComparator to compare experiments
     */
    public UnambiguousCuratedInteractionEvidenceComparator() {
        super(new UnambiguousParticipantEvidenceComparator(), new UnambiguousCuratedInteractionBaseComparator(),
                new UnambiguousCuratedExperimentComparator(), new UnambiguousParameterComparator());
    }

    /** {@inheritDoc} */
    @Override
    public UnambiguousCuratedExperimentComparator getExperimentComparator() {
        return (UnambiguousCuratedExperimentComparator) super.getExperimentComparator();
    }

    @Override
    public UnambiguousCuratedInteractionBaseComparator getInteractionBaseComparator() {
        return (UnambiguousCuratedInteractionBaseComparator) super.getInteractionBaseComparator();
    }

    /**
     * It will first compare the basic interaction properties using UnambiguousCuratedInteractionBaseComparator.
     * It will then compares the IMEx identifiers if both IMEx ids are set. If at least one IMEx id is not set, it will compare the negative properties.
     * A negative interaction will come after a positive interaction. it will compare
     * the experiment using UnambiguousCuratedExperimentComparator. If the experiments are the same, it will compare the participants using UnambiguousParticipantEvidenceComparator. Then it will compare the parameters using UnambiguousParameterComparator.
     * If the parameters are the same, it will first compare the experimental variableParameters using VariableParameterValueSetComparator and then it will compare the inferred boolean value (Inferred interactions will always come after).
     *
     *
     **/
    @Override
    public int compare(InteractionEvidence interaction1, InteractionEvidence interaction2) {
        return super.compare(interaction1, interaction2);
    }

    /**
     * Use UnambiguousCuratedInteractionEvidenceComparator to know if two experimental interactions are equals.
     *
     * @param interaction1 a {@link psidev.psi.mi.jami.model.InteractionEvidence} object.
     * @param interaction2 a {@link psidev.psi.mi.jami.model.InteractionEvidence} object.
     * @return true if the two experimental interactions are equal
     */
    public static boolean areEquals(InteractionEvidence interaction1, InteractionEvidence interaction2){
        if (unambiguousCuratedExperimentalInteractionComparator == null){
            unambiguousCuratedExperimentalInteractionComparator = new UnambiguousCuratedInteractionEvidenceComparator();
        }

        return unambiguousCuratedExperimentalInteractionComparator.compare(interaction1, interaction2) == 0;
    }
}
