package psidev.psi.mi.jami.enricher.impl;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import psidev.psi.mi.jami.bridges.fetcher.mock.MockCvTermFetcher;
import psidev.psi.mi.jami.enricher.ParticipantEnricher;
import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.impl.minimal.MinimalCvTermEnricher;
import psidev.psi.mi.jami.enricher.listener.EnrichmentStatus;
import psidev.psi.mi.jami.enricher.listener.ParticipantEnricherListener;
import psidev.psi.mi.jami.enricher.listener.impl.ParticipantEnricherListenerManager;
import psidev.psi.mi.jami.enricher.listener.impl.ParticipantEnricherLogger;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.impl.DefaultCvTerm;
import psidev.psi.mi.jami.model.impl.DefaultParticipant;
import psidev.psi.mi.jami.model.impl.DefaultProtein;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 26/07/13
 */
public class BasicParticipantEnricherTest {

    private ParticipantEnricher participantEnricher;

    private Participant persistentParticipant;
    private int persistentInt = 0;

    @Before
    public void setup(){
        participantEnricher = new MinimalParticipantEnricher();
        Protein protein = new DefaultProtein("ShortName");
        protein.setUniprotkb("P1234");
        persistentParticipant = new DefaultParticipant(protein);
        persistentInt = 0;
    }

    @Test
    public void test_enrichment_without_enrichers_succeeds() throws EnricherException {
        assertNull(participantEnricher.getFeatureEnricher());
        assertNull(participantEnricher.getCvTermEnricher());

        participantEnricher.setParticipantListener(new ParticipantEnricherListenerManager(
                new ParticipantEnricherLogger() ,
                new ParticipantEnricherListener() {

                    public void onEnrichmentError(Participant object, String message, Exception e) {
                        Assert.fail();
                    }

                    public void onBiologicalRoleUpdate(Entity participant, CvTerm oldType) {
                        Assert.fail();
                    }

                    public void onStoichiometryUpdate(Entity participant, Stoichiometry oldStoichiometry) {
                        Assert.fail();
                    }

                    public void onAddedCausalRelationship(Entity participant, CausalRelationship added) {
                        Assert.fail();
                    }

                    public void onRemovedCausalRelationship(Entity participant, CausalRelationship removed) {
                        Assert.fail();
                    }

                    public void onAddedFeature(Entity participant, Feature added) {
                        Assert.fail();
                    }

                    public void onRemovedFeature(Entity participant, Feature removed) {
                        Assert.fail();
                    }

                    public void onAddedAlias(Object o, Alias added) {
                        Assert.fail();
                    }

                    public void onRemovedAlias(Object o, Alias removed) {
                        Assert.fail();
                    }

                    public void onAddedAnnotation(Object o, Annotation added) {
                        Assert.fail();
                    }

                    public void onRemovedAnnotation(Object o, Annotation removed) {
                        Assert.fail();
                    }

                    public void onEnrichmentComplete(Object object, EnrichmentStatus status, String message) {
                        assertTrue(object == persistentParticipant);
                        assertEquals(EnrichmentStatus.SUCCESS , status);
                        persistentInt ++ ;
                    }

                    public void onEnrichmentError(Object object, String message, Exception e) {
                        Assert.fail();
                    }

                    public void onInteractorUpdate(Entity entity, Interactor oldInteractor) {
                        Assert.fail();
                    }

                    public void onAddedXref(Object o, Xref added) {
                        Assert.fail();
                    }

                    public void onRemovedXref(Object o, Xref removed) {
                        Assert.fail();
                    }
                }
        ));
        participantEnricher.enrich(persistentParticipant);
        assertEquals(1 , persistentInt);
    }


    // == CVTERM ENRICHER TESTS =========================================================================

    /**
     * Assert that if a CvTerm enricher is included,
     * but the cvTerms to enrich are null, that no problems are encountered.
     * @throws EnricherException
     */
    @Test
    public void test_enrichment_with_cvTermEnricher_but_no_cvTerms() throws EnricherException {

        participantEnricher.setCvTermEnricher(new MinimalCvTermEnricher(new MockCvTermFetcher()));

        participantEnricher.setParticipantListener(new ParticipantEnricherListenerManager(
                new ParticipantEnricherLogger() ,
                new ParticipantEnricherListener() {
                    public void onEnrichmentError(Participant object, String message, Exception e) {
                        Assert.fail();
                    }

                    public void onBiologicalRoleUpdate(Entity participant, CvTerm oldType) {
                        Assert.fail();
                    }

                    public void onStoichiometryUpdate(Entity participant, Stoichiometry oldStoichiometry) {
                        Assert.fail();
                    }

                    public void onAddedCausalRelationship(Entity participant, CausalRelationship added) {
                        Assert.fail();
                    }

                    public void onRemovedCausalRelationship(Entity participant, CausalRelationship removed) {
                        Assert.fail();
                    }

                    public void onAddedFeature(Entity participant, Feature added) {
                        Assert.fail();
                    }

                    public void onRemovedFeature(Entity participant, Feature removed) {
                        Assert.fail();
                    }

                    public void onAddedAlias(Object o, Alias added) {
                        Assert.fail();
                    }

                    public void onRemovedAlias(Object o, Alias removed) {
                        Assert.fail();
                    }

                    public void onAddedAnnotation(Object o, Annotation added) {
                        Assert.fail();
                    }

                    public void onRemovedAnnotation(Object o, Annotation removed) {
                        Assert.fail();
                    }

                    public void onEnrichmentComplete(Object object, EnrichmentStatus status, String message) {
                        assertTrue(object == persistentParticipant);
                        assertEquals(EnrichmentStatus.SUCCESS , status);
                        persistentInt ++;
                    }

                    public void onEnrichmentError(Object object, String message, Exception e) {
                        Assert.fail();
                    }

                    public void onInteractorUpdate(Entity entity, Interactor oldInteractor) {
                        Assert.fail();
                    }

                    public void onAddedXref(Object o, Xref added) {
                        Assert.fail();
                    }

                    public void onRemovedXref(Object o, Xref removed) {
                        Assert.fail();
                    }
                }
        ));

        participantEnricher.enrich(persistentParticipant);

        assertEquals(1 , persistentInt);
    }

    /**
     * Assert that if a CvTerm enricher is included,
     * and the cvTerms are present, the CvTerms are enriched.
     * @throws EnricherException
     */
    @Test
    public void test_enrichment_with_CvTermEnricher_enriches_CvTerms() throws EnricherException {
        MockCvTermFetcher mockCvTermFetcher = new MockCvTermFetcher();
        participantEnricher.setCvTermEnricher(new MinimalCvTermEnricher(mockCvTermFetcher));
        mockCvTermFetcher.addEntry("MI:0001" , new DefaultCvTerm("ShortName" , "FullName" , "MI:0001"));

        persistentParticipant.setBiologicalRole(new DefaultCvTerm("ShortName", "MI:0001"));
        assertNotNull(persistentParticipant.getBiologicalRole());
        assertNull(persistentParticipant.getBiologicalRole().getFullName());

        participantEnricher.setParticipantListener(new ParticipantEnricherListenerManager(
                new ParticipantEnricherLogger() ,
                new ParticipantEnricherListener() {

                    public void onEnrichmentError(Participant object, String message, Exception e) {
                        Assert.fail();
                    }

                    public void onAddedFeature(Entity participant, Feature added) {
                        Assert.fail();
                    }

                    public void onRemovedFeature(Entity participant, Feature removed) {
                        Assert.fail();
                    }
                    public void onBiologicalRoleUpdate(Entity participant, CvTerm oldType) {
                        Assert.fail();
                    }

                    public void onStoichiometryUpdate(Entity participant, Stoichiometry oldStoichiometry) {
                        Assert.fail();
                    }

                    public void onAddedCausalRelationship(Entity participant, CausalRelationship added) {
                        Assert.fail();
                    }

                    public void onRemovedCausalRelationship(Entity participant, CausalRelationship removed) {
                        Assert.fail();
                    }

                    public void onAddedAlias(Object o, Alias added) {
                        Assert.fail();
                    }

                    public void onRemovedAlias(Object o, Alias removed) {
                        Assert.fail();
                    }

                    public void onAddedAnnotation(Object o, Annotation added) {
                        Assert.fail();
                    }

                    public void onRemovedAnnotation(Object o, Annotation removed) {
                        Assert.fail();
                    }

                    public void onEnrichmentComplete(Object object, EnrichmentStatus status, String message) {
                        assertTrue(object == persistentParticipant);
                        assertEquals(EnrichmentStatus.SUCCESS , status);
                        persistentInt ++;
                    }

                    public void onEnrichmentError(Object object, String message, Exception e) {
                        Assert.fail();
                    }

                    public void onInteractorUpdate(Entity entity, Interactor oldInteractor) {
                        Assert.fail();
                    }

                    public void onAddedXref(Object o, Xref added) {
                        Assert.fail();
                    }

                    public void onRemovedXref(Object o, Xref removed) {
                        Assert.fail();
                    }
                }
        ));

        participantEnricher.enrich(persistentParticipant);

        assertNotNull(persistentParticipant.getBiologicalRole());
        assertNotNull(persistentParticipant.getBiologicalRole().getFullName());
        assertEquals(1 , persistentInt);
    }
}
