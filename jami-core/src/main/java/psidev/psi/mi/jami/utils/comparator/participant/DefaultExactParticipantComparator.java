package psidev.psi.mi.jami.utils.comparator.participant;

import psidev.psi.mi.jami.model.*;

/**
 * Generic default exact participant comparator.
 * Modelled participants come first and then experimental participants
 * - It uses DefaultExactComponentComparator to compare components
 * - It uses DefaultExactParticipantEvidenceComparator to compare experimental participants
 * - It uses DefaultExactParticipantBaseComparator to compare basic participant properties
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>04/02/13</pre>
 */
public class DefaultExactParticipantComparator {

    /**
     * Use DefaultExactParticipantComparator to know if two participants are equals.
     *
     * @param participant1 a {@link psidev.psi.mi.jami.model.Participant} object.
     * @param participant2 a {@link psidev.psi.mi.jami.model.Participant} object.
     * @return true if the two participants are equal
     */
    public static boolean areEquals(Participant participant1, Participant participant2){

        if (participant1 == participant2){
            return true;
        }
        else if (participant1 == null || participant2 == null){
            return false;
        }
        else {
            // first check if both participants are from the same interface
            // both are biological participants
            boolean isBiologicalParticipant1 = participant1 instanceof ModelledParticipant;
            boolean isBiologicalParticipant2 = participant2 instanceof ModelledParticipant;
            if (isBiologicalParticipant1 && isBiologicalParticipant2){
                return DefaultExactModelledParticipantComparator.areEquals((ModelledParticipant) participant1, (ModelledParticipant) participant2, true);
            }
            // the biological participant is before
            else if (isBiologicalParticipant1 || isBiologicalParticipant2){
                return false;
            }
            else {
                // both are experimental participants
                boolean isExperimentalParticipant1 = participant1 instanceof ParticipantEvidence;
                boolean isExperimentalParticipant2 = participant2 instanceof ParticipantEvidence;
                if (isExperimentalParticipant1 && isExperimentalParticipant2) {
                    return DefaultExactParticipantEvidenceComparator.areEquals(
                            (ParticipantEvidence) participant1,
                            (ParticipantEvidence) participant2, true);
                }
                // the experimental participant is before
                else if (isExperimentalParticipant1 || isExperimentalParticipant2) {
                    return false;
                } else {
                    // both are participant pools
                    boolean isPool1 = participant1 instanceof ParticipantPool;
                    boolean isPool2 = participant2 instanceof ParticipantPool;
                    if (isPool1 && isPool2) {
                        return DefaultExactParticipantPoolComparator.areEquals(
                                (ParticipantPool) participant1,
                                (ParticipantPool) participant2, true);
                    }
                    // the experimental participant is before
                    else if (isPool1 || isPool2) {
                        return false;
                    } else {
                        return DefaultExactParticipantBaseComparator.areEquals(participant1,
                                participant2, true);
                    }
                }
            }
        }
    }
}
