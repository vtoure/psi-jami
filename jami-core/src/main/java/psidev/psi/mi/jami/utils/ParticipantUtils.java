package psidev.psi.mi.jami.utils;

import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.ModelledParticipant;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.model.ParticipantEvidence;
import psidev.psi.mi.jami.model.impl.DefaultModelledParticipant;
import psidev.psi.mi.jami.model.impl.DefaultParticipant;
import psidev.psi.mi.jami.model.impl.DefaultParticipantEvidence;
import psidev.psi.mi.jami.utils.comparator.cv.DefaultCvTermComparator;

import java.util.Collection;

/**
 * Factory for participants
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>11/02/13</pre>
 */
public class ParticipantUtils {

    /**
     * <p>createUnknownBasicParticipantEvidence</p>
     *
     * @return a {@link psidev.psi.mi.jami.model.ParticipantEvidence} object.
     */
    public static ParticipantEvidence createUnknownBasicParticipantEvidence(){
        return new DefaultParticipantEvidence(InteractionUtils.createEmptyBasicExperimentalInteraction(), InteractorUtils.createUnknownBasicInteractor(), null);
    }

    /**
     * <p>createUnknownBasicParticipant</p>
     *
     * @return a {@link psidev.psi.mi.jami.model.Participant} object.
     */
    public static Participant createUnknownBasicParticipant(){
        return new DefaultParticipant(InteractorUtils.createUnknownBasicInteractor());
    }

    /**
     * <p>createBasicModelledParticipant</p>
     *
     * @return a {@link psidev.psi.mi.jami.model.ModelledParticipant} object.
     */
    public static ModelledParticipant createBasicModelledParticipant(){
        return new DefaultModelledParticipant(InteractorUtils.createUnknownBasicInteractor());
    }

    /**
     * Method to know if a participant evidence has a putative self experimental or biological role
     *
     * @param p a {@link psidev.psi.mi.jami.model.ParticipantEvidence} object.
     * @return a boolean.
     */
    public static boolean isPutativeSelfParticipantEvidence(ParticipantEvidence p){
        if (p == null){
            return false;
        }

        CvTerm experimentalRole = p.getExperimentalRole();
        CvTerm biologicalRole = p.getBiologicalRole();

        return DefaultCvTermComparator.areEquals(CvTermUtils.getPutativeSelf(), experimentalRole)
                || DefaultCvTermComparator.areEquals(CvTermUtils.getPutativeSelf(), biologicalRole) ;
    }

    /**
     * Method to know if a participant evidence has a self experimental or biological role
     *
     * @param p a {@link psidev.psi.mi.jami.model.ParticipantEvidence} object.
     * @return a boolean.
     */
    public static boolean isSelfParticipantEvidence(ParticipantEvidence p){
        if (p == null){
            return false;
        }

        CvTerm experimentalRole = p.getExperimentalRole();
        CvTerm biologicalRole = p.getBiologicalRole();

        return DefaultCvTermComparator.areEquals(CvTermUtils.getSelf(), experimentalRole)
                || DefaultCvTermComparator.areEquals(CvTermUtils.getSelf(), biologicalRole) ;
    }

    /**
     * Method to know if a participant has a putative self biological role.
     * If checkParticipantEvidence is set to true, it will check if the given p is a ParticipantEvidence and look at the experimental role
     *
     * @param p a {@link psidev.psi.mi.jami.model.Participant} object.
     * @param checkParticipantEvidence a boolean.
     * @return a boolean.
     */
    public static boolean isPutativeSelfParticipant(Participant p, boolean checkParticipantEvidence){
        if (p == null){
            return false;
        }

        CvTerm biologicalRole = p.getBiologicalRole();

        if (DefaultCvTermComparator.areEquals(CvTermUtils.getPutativeSelf(), biologicalRole)){
            return true;
        }

        if (checkParticipantEvidence){
            if (p instanceof ParticipantEvidence){
                CvTerm expRole = ((ParticipantEvidence)p).getExperimentalRole();
                return DefaultCvTermComparator.areEquals(CvTermUtils.getPutativeSelf(), expRole) ;
            }
        }

        return false;
    }

    /**
     * Method to know if a participant has a self biological role
     * If checkParticipantEvidence is set to true, it will check if the given p is a ParticipantEvidence and look at the experimental role
     *
     * @param p a {@link psidev.psi.mi.jami.model.Participant} object.
     * @param checkParticipantEvidence a boolean.
     * @return a boolean.
     */
    public static boolean isSelfParticipant(Participant p, boolean checkParticipantEvidence){
        if (p == null){
            return false;
        }

        CvTerm biologicalRole = p.getBiologicalRole();

        if (DefaultCvTermComparator.areEquals(CvTermUtils.getSelf(), biologicalRole)){
            return true;
        }

        if (checkParticipantEvidence){
            if (p instanceof ParticipantEvidence){
                CvTerm expRole = ((ParticipantEvidence)p).getExperimentalRole();
                return DefaultCvTermComparator.areEquals(CvTermUtils.getSelf(), expRole) ;
            }
        }

        return false;
    }

    /**
     * To know if the participant does have a specific biological role
     *
     * @param participant a {@link psidev.psi.mi.jami.model.Participant} object.
     * @param roleId a {@link java.lang.String} object.
     * @param roleName a {@link java.lang.String} object.
     * @return true if the participant has the biological role with given name/identifier
     */
    public static boolean doesParticipantHaveBiologicalRole(Participant participant, String roleId, String roleName){

        if (participant == null || (roleName == null && roleId == null)){
            return false;
        }

        CvTerm role = participant.getBiologicalRole();
        // we can compare identifiers
        if (roleId != null && role.getMIIdentifier() != null){
            // we have the same topic id
            return role.getMIIdentifier().equals(roleId);
        }
        // we need to compare topic names
        else if (roleName != null) {
            return roleName.equalsIgnoreCase(role.getShortName());
        }

        return false;
    }

    /**
     * To know if the participant evidence does have a specific experimental role
     *
     * @param participant a {@link psidev.psi.mi.jami.model.ParticipantEvidence} object.
     * @param roleId a {@link java.lang.String} object.
     * @param roleName a {@link java.lang.String} object.
     * @return true if the participant has the experimental role with given name/identifier
     */
    public static boolean doesParticipantHaveExperimentalRole(ParticipantEvidence participant, String roleId, String roleName){

        if (participant == null || (roleName == null && roleId == null)){
            return false;
        }

        CvTerm role = participant.getExperimentalRole();
        // we can compare identifiers
        if (roleId != null && role.getMIIdentifier() != null){
            // we have the same topic id
            return role.getMIIdentifier().equals(roleId);
        }
        // we need to compare topic names
        else if (roleName != null) {
            return roleName.equalsIgnoreCase(role.getShortName());
        }

        return false;
    }

    /**
     * Check if the experimental role of this participant is an alternative bait for spoke expansion in case there are no baits:
     * fluorescence donor or suppressor gene.
     *
     * @param participant a {@link psidev.psi.mi.jami.model.ParticipantEvidence} object.
     * @return true if the participant can be used as an alternative bait for spoke expansion
     */
    public static boolean isParticipantEvidenceAnAlternativeBaitForSpokeExpansion(ParticipantEvidence participant){

        if (doesParticipantHaveExperimentalRole(participant, Participant.FLUORESCENCE_DONOR_ROLE_MI, Participant.FLUORESCENCE_DONOR_ROLE)
                || doesParticipantHaveExperimentalRole(participant, Participant.SUPPRESSOR_GENE_ROLE_MI, Participant.SUPPRESSOR_GENE_ROLE) ){
            return true;
        }

        return false;
    }

    /**
     * Check if the biological role of this participant is an alternative bait for spoke expansion in case there are no baits:
     * donor or enzyme.
     *
     * @param participant a {@link psidev.psi.mi.jami.model.Participant} object.
     * @return true if the participant can be used as an alternative bait for spoke expansion
     */
    public static boolean isParticipantAnAlternativeBaitForSpokeExpansion(Participant participant){

        if (doesParticipantHaveBiologicalRole(participant, Participant.ENZYME_ROLE_MI, Participant.ENZYME_ROLE)
                || doesParticipantHaveBiologicalRole(participant, Participant.DONOR_ROLE_MI, Participant.DONOR_ROLE)
                || doesParticipantHaveBiologicalRole(participant, Participant.PHOSPHATE_DONOR_ROLE_MI, Participant.PHOSPHATE_DONOR_ROLE)
                || doesParticipantHaveBiologicalRole(participant, Participant.ELECTRON_DONOR_ROLE_MI, Participant.ELECTRON_DONOR_ROLE)
                || doesParticipantHaveBiologicalRole(participant, Participant.PHOTON_DONOR_ROLE_MI, Participant.PHOTON_DONOR_ROLE)){
            return true;
        }

        return false;
    }

    /**
     * Collect the 'best bait' candidate to be used among this participantEvidences.
     * - The first choice is the first participant with bait as experimental role
     * - The second choice is the first participant having fluorescence donor or suppressor gene as experimental role
     * - The third choice is the first participant having enzyme or donor as biological role
     * - the last choice is the participant coming first in the alphabetical order (compare interactor shortname case insensitive)
     *
     * @param participantEvidences a {@link java.util.Collection} object.
     * @return the best participantEvidence to use a s a bait in spoke expansion
     */
    public static ParticipantEvidence collectBestParticipantEvidenceAsBaitForSpokeExpansion(Collection<? extends ParticipantEvidence> participantEvidences){
        if (participantEvidences == null || participantEvidences.isEmpty()){
            return null;
        }

        ParticipantEvidence firstBait=null;
        ParticipantEvidence alternativeExperimentalBait=null;
        ParticipantEvidence alternativeBiologicalBait=null;
        ParticipantEvidence firstShortNameAlphabeticalOrder=null;

        for ( ParticipantEvidence p : participantEvidences ) {

            // check bait role first. If found, stop looking for alternative baits
            if (doesParticipantHaveExperimentalRole(p, Participant.BAIT_ROLE_MI, Participant.BAIT_ROLE)){
                firstBait = p;
                break;
            }
            // check alternativeExperimentalBait second.
            else if (alternativeExperimentalBait == null && isParticipantEvidenceAnAlternativeBaitForSpokeExpansion(p)){
                alternativeExperimentalBait = p;
            }
            // check alternativeBiologicalBait third.
            else if (alternativeBiologicalBait == null && isParticipantAnAlternativeBaitForSpokeExpansion(p)){
                alternativeBiologicalBait = p;
            }
            // check for the participant coming first in alphabetical order
            else if (firstShortNameAlphabeticalOrder == null) {
                firstShortNameAlphabeticalOrder = p;
            }
            else {
                String firstName = firstShortNameAlphabeticalOrder.getInteractor().getShortName().trim().toLowerCase();
                String secondName = p.getInteractor().getShortName().trim().toLowerCase();

                // current p is before firstShortNameAlphabeticalOrder in alphabetical order
                if (secondName.compareTo(firstName) < 0){
                    firstShortNameAlphabeticalOrder = p;
                }
            }
        }

        if (firstBait != null){
            return firstBait;
        }
        else if (alternativeExperimentalBait != null){
            return alternativeExperimentalBait;
        }
        else if (alternativeBiologicalBait != null){
            return alternativeBiologicalBait;
        }
        else {
            return firstShortNameAlphabeticalOrder;
        }
    }

    /**
     * Collect the 'best bait' candidate to be used among this participants.
     * - The first choice is the first participant having enzyme or donor as biological role
     * - the last choice is the participant coming first in the alphabetical order (compare interactor shortname case insensitive)
     *
     * @param participants a {@link java.util.Collection} object.
     * @return the best participant to use a s a bait in spoke expansion
     */
    public static Participant collectBestBaitParticipantForSpokeExpansion(Collection<? extends Participant> participants){
        if (participants == null || participants.isEmpty()){
            return null;
        }

        Participant alternativeBiologicalBait=null;
        Participant firstShortNameAlphabeticalOrder=null;

        for ( Participant p : participants ) {

            // check alternativeBiologicalBait third.
            if (isParticipantAnAlternativeBaitForSpokeExpansion(p)){
                alternativeBiologicalBait = p;
                break;
            }
            // check for the participant coming first in alphabetical order
            else if (firstShortNameAlphabeticalOrder == null) {
                firstShortNameAlphabeticalOrder = p;
            }
            else {
                String firstName = firstShortNameAlphabeticalOrder.getInteractor().getShortName().trim().toLowerCase();
                String secondName = p.getInteractor().getShortName().trim().toLowerCase();

                // current p is before firstShortNameAlphabeticalOrder in alphabetical order
                if (secondName.compareTo(firstName) < 0){
                    firstShortNameAlphabeticalOrder = p;
                }
            }
        }

        if (alternativeBiologicalBait != null){
            return alternativeBiologicalBait;
        }
        else {
            return firstShortNameAlphabeticalOrder;
        }
    }
}
