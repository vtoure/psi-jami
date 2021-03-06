package psidev.psi.mi.jami.enricher.impl.minimal;

import psidev.psi.mi.jami.enricher.OrganismEnricher;
import psidev.psi.mi.jami.enricher.ParticipantEvidenceEnricher;
import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.listener.ParticipantEvidenceEnricherListener;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.FeatureEvidence;
import psidev.psi.mi.jami.model.ParticipantEvidence;
import psidev.psi.mi.jami.utils.comparator.cv.DefaultCvTermComparator;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 28/06/13

 */
public class MinimalParticipantEvidenceEnricher<P extends ParticipantEvidence> extends MinimalParticipantEnricher<P, FeatureEvidence>
implements ParticipantEvidenceEnricher<P>{
    private OrganismEnricher organismEnricher;


    /**
     * <p>Getter for the field <code>organismEnricher</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.enricher.OrganismEnricher} object.
     */
    public OrganismEnricher getOrganismEnricher() {
        return organismEnricher;
    }

    /** {@inheritDoc} */
    public void setOrganismEnricher(OrganismEnricher organismEnricher) {
        this.organismEnricher = organismEnricher;
    }

    /** {@inheritDoc} */
    @Override
    public void processOtherProperties(P participantEvidenceToEnrich)
            throws EnricherException {

        if(getCvTermEnricher() != null){
            processExperimentalRole(participantEvidenceToEnrich);
            processIdentificationMethods(participantEvidenceToEnrich);
        }

        processExpressedInOrganism(participantEvidenceToEnrich);
    }

    /**
     * <p>processExpressedInOrganism.</p>
     *
     * @param participantEvidenceToEnrich a P object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processExpressedInOrganism(P participantEvidenceToEnrich) throws EnricherException {
        if (getOrganismEnricher() != null && participantEvidenceToEnrich.getExpressedInOrganism() != null){
            getOrganismEnricher().enrich(participantEvidenceToEnrich.getExpressedInOrganism());
        }
    }

    /**
     * <p>processIdentificationMethods.</p>
     *
     * @param participantEvidenceToEnrich a P object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processIdentificationMethods(P participantEvidenceToEnrich) throws EnricherException {
        if (!participantEvidenceToEnrich.getIdentificationMethods().isEmpty()){
            getCvTermEnricher().enrich(participantEvidenceToEnrich.getIdentificationMethods());
        }
    }

    /**
     * <p>processExperimentalRole.</p>
     *
     * @param participantEvidenceToEnrich a P object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processExperimentalRole(P participantEvidenceToEnrich) throws EnricherException {
        if (participantEvidenceToEnrich.getExperimentalRole() != null){
            getCvTermEnricher().enrich(participantEvidenceToEnrich.getExperimentalRole());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void processOtherProperties(P participantEvidenceToEnrich, P objectSource)
            throws EnricherException {

        // expressed in
        processExpressedInOrganism(participantEvidenceToEnrich, objectSource);
        // exp roles
        processExperimentalRole(participantEvidenceToEnrich, objectSource);
        // exp identifications
        processIdentificationMethods(participantEvidenceToEnrich, objectSource);

        processOtherProperties(participantEvidenceToEnrich);
    }

    /**
     * <p>processExpressedInOrganism.</p>
     *
     * @param participantEvidenceToEnrich a P object.
     * @param objectSource a P object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void processExpressedInOrganism(P participantEvidenceToEnrich, P objectSource) throws EnricherException {
        if (participantEvidenceToEnrich.getExpressedInOrganism() == null && objectSource.getExpressedInOrganism() != null){
            participantEvidenceToEnrich.setExpressedInOrganism(objectSource.getExpressedInOrganism());
            if (getParticipantEnricherListener() instanceof ParticipantEvidenceEnricherListener){
                ((ParticipantEvidenceEnricherListener)getParticipantEnricherListener()).onExpressedInUpdate(participantEvidenceToEnrich, null);
            }
        }
    }

    /**
     * <p>processIdentificationMethods.</p>
     *
     * @param participantEvidenceToEnrich a P object.
     * @param objectSource a P object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    public void processIdentificationMethods(P participantEvidenceToEnrich, P objectSource) throws EnricherException {
        mergeIdentificationMethods(participantEvidenceToEnrich, participantEvidenceToEnrich.getIdentificationMethods(), objectSource.getIdentificationMethods(), false);

        processIdentificationMethods(participantEvidenceToEnrich);
    }

    /**
     * <p>processExperimentalRole.</p>
     *
     * @param participantEvidenceToEnrich a P object.
     * @param objectSource a P object.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    public void processExperimentalRole(P participantEvidenceToEnrich, P objectSource) throws EnricherException {
        // nothing to do

        processExperimentalRole(participantEvidenceToEnrich);
    }

    /**
     * <p>mergeIdentificationMethods.</p>
     *
     * @param termToEnrich a P object.
     * @param toEnrichTerms a {@link java.util.Collection} object.
     * @param fetchedTerms a {@link java.util.Collection} object.
     * @param remove a boolean.
     * @throws psidev.psi.mi.jami.enricher.exception.EnricherException if any.
     */
    protected void mergeIdentificationMethods(P termToEnrich, Collection<CvTerm> toEnrichTerms, Collection<CvTerm> fetchedTerms , boolean remove) throws EnricherException {

        Iterator<CvTerm> termIterator = toEnrichTerms.iterator();
        // remove xrefs in toEnrichXrefs that are not in fetchedXrefs
        while(termIterator.hasNext()){
            CvTerm term = termIterator.next();
            boolean containsTerm = false;
            for (CvTerm term2 : fetchedTerms){
                // identical terms
                if (DefaultCvTermComparator.areEquals(term, term2)){
                    containsTerm = true;
                    // enrich terms that are here
                    getCvTermEnricher().enrich(term, term2);
                    break;
                }
            }
            // remove term not in second list
            if (remove && !containsTerm){
                termIterator.remove();
                if (getParticipantEnricherListener() instanceof ParticipantEvidenceEnricherListener){
                    ((ParticipantEvidenceEnricherListener)getParticipantEnricherListener()).onRemovedIdentificationMethod(termToEnrich, term);
                }
            }
        }

        // add terms from fetchedTerms that are not in toEnrichTerm
        termIterator = fetchedTerms.iterator();
        while(termIterator.hasNext()){
            CvTerm term = termIterator.next();
            boolean containsTerm = false;
            for (CvTerm term2 : toEnrichTerms){
                // identical terms
                if (DefaultCvTermComparator.areEquals(term, term2)){
                    if (getCvTermEnricher() != null){
                        getCvTermEnricher().enrich(term2, term);
                    }
                    containsTerm = true;
                    break;
                }
            }
            // add missing xref not in second list
            if (!containsTerm){
                toEnrichTerms.add(term);
                if (getParticipantEnricherListener() instanceof ParticipantEvidenceEnricherListener){
                    ((ParticipantEvidenceEnricherListener)getParticipantEnricherListener()).onAddedIdentificationMethod(termToEnrich, term);
                }
            }
        }
    }
}
