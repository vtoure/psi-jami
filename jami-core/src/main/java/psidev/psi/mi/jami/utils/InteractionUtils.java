package psidev.psi.mi.jami.utils;

import psidev.psi.mi.jami.binary.BinaryInteraction;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.binary.ModelledBinaryInteraction;
import psidev.psi.mi.jami.binary.expansion.ComplexExpansionMethod;
import psidev.psi.mi.jami.binary.impl.*;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.impl.DefaultInteractionEvidence;
import psidev.psi.mi.jami.utils.clone.InteractionCloner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Factory for experimental interactions
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>11/02/13</pre>
 */
public class InteractionUtils {

    /**
     * <p>createEmptyBasicExperimentalInteraction</p>
     *
     * @return a {@link psidev.psi.mi.jami.model.InteractionEvidence} object.
     */
    public static InteractionEvidence createEmptyBasicExperimentalInteraction() {

        return new DefaultInteractionEvidence(ExperimentUtils.createUnknownBasicExperiment());
    }

    /**
     * The method will find the interactionCategory (binary, self, etc.)
     *
     * @param interaction a {@link psidev.psi.mi.jami.model.Interaction} object.
     * @return the ComplexType, null if the given interaction is null
     * @param checkExperimentalRoleIfInteractionEvidence a boolean.
     */
    public static ComplexType findInteractionCategoryOf(Interaction interaction, boolean checkExperimentalRoleIfInteractionEvidence) {
        if (interaction == null) {
            return null;
        }

        // only one participant, check stoichiometry
        if (interaction.getParticipants().size() == 1) {
            Participant p = (Participant) interaction.getParticipants().iterator().next();

            // the stoichiometry is not specified
            if (p.getStoichiometry() == null || p.getStoichiometry().getMaxValue() == 0) {
                // if we have self participants, then it is self_intra_molecular
                if (ParticipantUtils.isSelfParticipant(p, checkExperimentalRoleIfInteractionEvidence) || ParticipantUtils.isPutativeSelfParticipant(p, checkExperimentalRoleIfInteractionEvidence)) {
                    return ComplexType.self_intra_molecular;
                }
                // we can consider that we have self inter molecular
                else {
                    return ComplexType.self_inter_molecular;
                }
            }
            // intra molecular
            else if (p.getStoichiometry().getMaxValue() == 1) {
                return ComplexType.self_intra_molecular;
            } else {
                return ComplexType.self_inter_molecular;
            }
        } else if (interaction.getParticipants().size() == 2) {
            return ComplexType.binary;
        } else if (interaction.getParticipants().size() > 2) {
            return ComplexType.n_ary;
        }

        return null;
    }

    /**
     * The method will find the interactionCategory (binary, self, etc.)
     *
     * @param interaction a {@link psidev.psi.mi.jami.model.InteractionEvidence} object.
     * @return the ComplexType, null if the given interaction is null
     */
    public static ComplexType findInteractionEvidenceCategoryOf(InteractionEvidence interaction) {
        if (interaction == null) {
            return null;
        }

        // only one participant, check stoichiometry
        if (interaction.getParticipants().size() == 1) {
            ParticipantEvidence p = interaction.getParticipants().iterator().next();

            // the stoichiometry is not specified
            if (p.getStoichiometry() == null || p.getStoichiometry().getMaxValue() == 0) {
                // if we have self participants, then it is self_intra_molecular
                if (ParticipantUtils.isSelfParticipantEvidence(p) || ParticipantUtils.isPutativeSelfParticipantEvidence(p)) {
                    return ComplexType.self_intra_molecular;
                }
                // we can consider that we have self inter molecular
                else {
                    return ComplexType.self_inter_molecular;
                }
            }
            // intra molecular
            else if (p.getStoichiometry().getMaxValue() == 1) {
                return ComplexType.self_intra_molecular;
            } else {
                return ComplexType.self_inter_molecular;
            }
        } else if (interaction.getParticipants().size() == 2) {
            return ComplexType.binary;
        } else if (interaction.getParticipants().size() > 2) {
            return ComplexType.n_ary;
        }

        return null;
    }

    /**
     * The method will find the interactionCategory (binary, self, etc.)
     *
     * @param interaction a {@link psidev.psi.mi.jami.model.ModelledInteraction} object.
     * @return the ComplexType, null if the given interaction is null
     */
    public static ComplexType findModelledInteractionCategoryOf(ModelledInteraction interaction) {

        return InteractionUtils.findInteractionCategoryOf(interaction, false);
    }

    /**
     * Create a BinaryInteractionWrapper from the given interaction which should contain not more than two participants.
     *
     * @param interaction a {@link psidev.psi.mi.jami.model.Interaction} object.
     * @return the new BinaryInteractionWrapper for this interaction
     * @throws java.lang.IllegalArgumentException if the interaction contains more than two participants or interaction is null
     */
    public static BinaryInteraction createBinaryInteractionFrom(Interaction interaction) {
        return new BinaryInteractionWrapper(interaction);
    }

    /**
     * Create a BinaryInteractionEvidenceWrapper from the given interaction evidence which should contain not more than two participants
     *
     * @param interaction a {@link psidev.psi.mi.jami.model.InteractionEvidence} object.
     * @return the new BinaryInteractionWrapper for this interaction
     * @throws java.lang.IllegalArgumentException if the interaction contains more than two participants or interaction is null
     */
    public static BinaryInteractionEvidence createBinaryInteractionEvidenceFrom(InteractionEvidence interaction) {
        return new BinaryInteractionEvidenceWrapper(interaction);
    }

    /**
     * Create a ModelledBinaryInteractionWrapper from the given modelled interaction which should contain not more than two participants
     *
     * @param interaction a {@link psidev.psi.mi.jami.model.ModelledInteraction} object.
     * @return the new BinaryInteractionWrapper for this interaction
     * @throws java.lang.IllegalArgumentException if the interaction contains more than two participants
     */
    public static ModelledBinaryInteraction createModelledBinaryInteractionFrom(ModelledInteraction interaction) {
        return new ModelledBinaryInteractionWrapper(interaction);
    }

    /**
     * Creates a new BinaryInteraction from the given interaction which should only contain one participant with a stoichiometry &gt;= 2
     * or participant stoichiometry null and participant is not self/putative self.
     * The new Binary interaction will have a participantB which is a copy of participantA with stoichiometry 0
     *
     * @param interaction a {@link psidev.psi.mi.jami.model.Interaction} object.
     * @return a {@link psidev.psi.mi.jami.binary.BinaryInteraction} object.
     */
    public static BinaryInteraction createNewSelfBinaryInteractionFrom(Interaction interaction) {
        BinaryInteraction<Participant> binary = new DefaultBinaryInteraction();
        InteractionCloner.copyAndOverrideBasicInteractionProperties(interaction, binary, false, true);
        InteractionCloner.copyAndOverrideBasicParticipantsToBinary(interaction, binary, false, true);
        return binary;
    }

    /**
     * Creates a new BinaryInteractionEvidence from the given interactionEvidence which should only contain one participant with a stoichiometry &gt;= 2
     * or participant stoichiometry null and participant is not self/putative self.
     * The new Binary interaction evidence will have a participantB which is a copy of participantA with stoichiometry 0
     *
     * @param interaction a {@link psidev.psi.mi.jami.model.InteractionEvidence} object.
     * @return a {@link psidev.psi.mi.jami.binary.BinaryInteractionEvidence} object.
     */
    public static BinaryInteractionEvidence createAndAddNewSelfBinaryInteractionEvidence(InteractionEvidence interaction) {
        BinaryInteractionEvidence binary = new DefaultBinaryInteractionEvidence();
        InteractionCloner.copyAndOverrideInteractionEvidenceProperties(interaction, binary, false, true);
        InteractionCloner.copyAndOverrideParticipantsEvidencesToBinary(interaction, binary, false, true);

        return binary;
    }

    /**
     * Creates a new ModelledBinaryInteraction from the given interaction which should only contain one participant with a stoichiometry &gt;= 2
     * or participant stoichiometry null and participant is not self/putative self.
     * The new Binary interaction evidence will have a participantB which is a copy of participantA with stoichiometry 0
     *
     * @param interaction a {@link psidev.psi.mi.jami.model.ModelledInteraction} object.
     * @return a {@link psidev.psi.mi.jami.binary.ModelledBinaryInteraction} object.
     */
    public static ModelledBinaryInteraction createAndAddNewSelfModelledBinaryInteraction(ModelledInteraction interaction) {
        ModelledBinaryInteraction binary = new DefaultModelledBinaryInteraction();
        InteractionCloner.copyAndOverrideModelledInteractionProperties(interaction, binary, false, true);
        InteractionCloner.copyAndOverrideModelledParticipantsToBinary(interaction, binary, false, true);

        return binary;
    }

    /**
     * <p>collectComplexExpansionMethodFromAnnotations</p>
     *
     * @param annotations a {@link java.util.Collection} object.
     * @return a {@link psidev.psi.mi.jami.model.Annotation} object.
     */
    public static Annotation collectComplexExpansionMethodFromAnnotations(Collection<? extends Annotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            return null;
        }

        for (Annotation annot : annotations) {
            if (AnnotationUtils.doesAnnotationHaveTopic(annot, ComplexExpansionMethod.SPOKE_EXPANSION_MI, ComplexExpansionMethod.SPOKE_EXPANSION)
                    || AnnotationUtils.doesAnnotationHaveTopic(annot, null, ComplexExpansionMethod.SPOKE)
                    || AnnotationUtils.doesAnnotationHaveTopic(annot, ComplexExpansionMethod.MATRIX_EXPANSION_MI, ComplexExpansionMethod.MATRIX_EXPANSION)
                    || AnnotationUtils.doesAnnotationHaveTopic(annot, null, ComplexExpansionMethod.MATRIX)
                    || AnnotationUtils.doesAnnotationHaveTopic(annot, ComplexExpansionMethod.BIPARTITE_EXPANSION_MI, ComplexExpansionMethod.BIPARTITE_EXPANSION)
                    || AnnotationUtils.doesAnnotationHaveTopic(annot, null, ComplexExpansionMethod.BIPARTITE)) {
                return annot;
            }
        }
        return null;
    }

    /**
     * This method can return all participants having causal relationships in an interaction
     *
     * @param interaction a {@link psidev.psi.mi.jami.model.Interaction} object.
     * @return the collection of participants having causal relationships, empty collection if no participants with causal relationships
     */
    public static Collection<Participant> extractParticipantWithCausalRelationships(Interaction interaction) {

        if (interaction != null && !interaction.getParticipants().isEmpty()) {
            Collection<Participant> participants = new ArrayList<Participant>(interaction.getParticipants().size());
            for (Object o : interaction.getParticipants()) {
                Participant participant = (Participant) o;
                if (!participant.getCausalRelationships().isEmpty()) {
                    participants.add(participant);
                }
            }
            return participants;
        }
        return Collections.EMPTY_LIST;
    }
}
