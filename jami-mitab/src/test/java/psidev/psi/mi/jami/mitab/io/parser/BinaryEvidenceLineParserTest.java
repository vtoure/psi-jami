package psidev.psi.mi.jami.mitab.io.parser;

import junit.framework.Assert;
import org.junit.Test;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.impl.*;
import psidev.psi.mi.jami.tab.extension.MitabFeatureEvidence;
import psidev.psi.mi.jami.tab.io.parser.BinaryEvidenceLineParser;
import psidev.psi.mi.jami.tab.io.parser.ParseException;
import psidev.psi.mi.jami.tab.utils.MitabUtils;
import psidev.psi.mi.jami.utils.AnnotationUtils;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.PositionUtils;
import psidev.psi.mi.jami.utils.XrefUtils;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * Unit tester for InteractionEvidenceLineParser
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>26/06/13</pre>
 */

public class BinaryEvidenceLineParserTest {

    @Test
    public void test_read_valid_mitab25() throws ParseException, java.text.ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/mitab25_line.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // read first interaction
        BinaryInteractionEvidence binary = (BinaryInteractionEvidence)parser.MitabLine();
        Assert.assertNotNull(binary);
        Assert.assertFalse(parser.hasFinished());

        Iterator<ParticipantEvidence> iterator = binary.getParticipants().iterator();
        ParticipantEvidence A = iterator.next();
        Assert.assertEquals("LY96", A.getInteractor().getShortName());
        Assert.assertEquals(2, A.getInteractor().getIdentifiers().size());
        Iterator<Xref> xrefIterator = A.getInteractor().getIdentifiers().iterator();
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("innatedb"), "IDBG-82738", CvTermUtils.createIdentityQualifier()), xrefIterator.next());
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("ensembl"), "ENSG00000136869", CvTermUtils.createSecondaryXrefQualifier()), xrefIterator.next());
        Assert.assertEquals(5, A.getInteractor().getAliases().size());
        Iterator<Alias> aliasIterator = A.getInteractor().getAliases().iterator();
        Assert.assertEquals(new DefaultAlias(new DefaultCvTerm("gene name"), "bla"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("O00206"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("TLR4_HUMAN"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NM_138554"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NP_612564"), aliasIterator.next());
        Assert.assertEquals(9606, A.getInteractor().getOrganism().getTaxId());
        Assert.assertEquals("Human", A.getInteractor().getOrganism().getCommonName());
        Assert.assertEquals("Homo Sapiens", A.getInteractor().getOrganism().getScientificName());
        Assert.assertEquals(CvTermUtils.createUnspecifiedRole(), A.getBiologicalRole());
        Assert.assertEquals(CvTermUtils.createUnspecifiedRole(), A.getExperimentalRole());
        Assert.assertEquals(CvTermUtils.createProteinInteractorType(), A.getInteractor().getInteractorType());
        Assert.assertEquals(0, A.getInteractor().getXrefs().size());
        Assert.assertEquals(0, A.getAnnotations().size());
        Assert.assertEquals(1, A.getInteractor().getChecksums().size());
        Assert.assertEquals("xxx4", A.getInteractor().getChecksums().iterator().next().getValue());
        Assert.assertEquals(0, A.getFeatures().size());
        Assert.assertNull(A.getStoichiometry());
        Assert.assertEquals(0, A.getIdentificationMethods().size());

        ParticipantEvidence B = iterator.next();
        Assert.assertEquals("LY96", B.getInteractor().getShortName());
        xrefIterator = B.getInteractor().getIdentifiers().iterator();
        Assert.assertEquals(2, B.getInteractor().getIdentifiers().size());
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("innatedb"), "IDBG-25842", CvTermUtils.createIdentityQualifier()), xrefIterator.next());
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("ensembl"), "ENSG00000154589", CvTermUtils.createSecondaryXrefQualifier()), xrefIterator.next());
        Assert.assertEquals(6, B.getInteractor().getAliases().size());
        aliasIterator = B.getInteractor().getAliases().iterator();
        Assert.assertEquals(new DefaultAlias("Q9Y6Y9"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("LY96_HUMAN"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NM_015364"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NP_056179"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NM_001195797"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NP_001182726"), aliasIterator.next());
        Assert.assertEquals(9606, B.getInteractor().getOrganism().getTaxId());
        Assert.assertEquals("Human", B.getInteractor().getOrganism().getCommonName());
        Assert.assertNull(B.getInteractor().getOrganism().getScientificName());
        Assert.assertEquals(CvTermUtils.createUnspecifiedRole(), B.getBiologicalRole());
        Assert.assertEquals(CvTermUtils.createUnspecifiedRole(), B.getExperimentalRole());
        Assert.assertEquals(CvTermUtils.createUnknownInteractorType(), B.getInteractor().getInteractorType());
        Assert.assertEquals(0, B.getInteractor().getXrefs().size());
        Assert.assertEquals(0, B.getAnnotations().size());
        Assert.assertEquals(0, B.getInteractor().getChecksums().size());
        Assert.assertTrue(B.getFeatures().isEmpty());
        Assert.assertNull(B.getStoichiometry());
        Assert.assertEquals(0, B.getIdentificationMethods().size());

        Experiment experiment = binary.getExperiment();
        Assert.assertNotNull(experiment);
        Assert.assertEquals(CvTermUtils.createMICvTerm("anti tag coimmunoprecipitation", "MI:0007"), experiment.getInteractionDetectionMethod());
        Publication publication = experiment.getPublication();
        Assert.assertNotNull(publication);
        Assert.assertEquals(1, publication.getAuthors().size());
        Assert.assertEquals("Shimazu", publication.getAuthors().iterator().next());
        Assert.assertEquals(MitabUtils.PUBLICATION_YEAR_FORMAT.parse("1999"), publication.getPublicationDate());
        Assert.assertEquals("10359581", publication.getPubmedId());
        Assert.assertEquals(new DefaultSource("innatedb", "MI:0974"), publication.getSource());
        Assert.assertNull(experiment.getHostOrganism());

        Assert.assertEquals(CvTermUtils.createMICvTerm("physical association","MI:0915"), binary.getInteractionType());
        Assert.assertEquals(1, binary.getIdentifiers().size());
        Assert.assertEquals(XrefUtils.createIdentityXref("innatedb", "IDB-113240"), binary.getIdentifiers().iterator().next());
        Assert.assertEquals("IM-1-1", binary.getImexId());
        Assert.assertEquals(3, binary.getConfidences().size());
        Iterator<Confidence> confIterator = binary.getConfidences().iterator();
        Assert.assertEquals(new DefaultConfidence(new DefaultCvTerm("lpr"), "2"), confIterator.next());
        Assert.assertEquals(new DefaultConfidence(new DefaultCvTerm("hpr"), "2"), confIterator.next());
        Assert.assertEquals(new DefaultConfidence(new DefaultCvTerm("np"), "1"), confIterator.next());
        Assert.assertEquals(1, binary.getXrefs().size());
        Assert.assertEquals(0, binary.getAnnotations().size());
        Assert.assertEquals(0, binary.getParameters().size());
        Assert.assertFalse(binary.isNegative());
        Assert.assertEquals(0, binary.getChecksums().size());

        InteractionEvidence binary2 = parser.MitabLine();
        Assert.assertNotNull(binary2);
        Assert.assertTrue(parser.hasFinished());
    }

    @Test
    public void test_read_valid_mitab26() throws ParseException, java.text.ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/mitab26_line.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // read first interaction
        BinaryInteractionEvidence binary = (BinaryInteractionEvidence)parser.MitabLine();
        Assert.assertNotNull(binary);
        Assert.assertFalse(parser.hasFinished());

        Iterator<ParticipantEvidence> iterator = binary.getParticipants().iterator();
        ParticipantEvidence A = iterator.next();
        Assert.assertEquals("LY96", A.getInteractor().getShortName());
        Assert.assertEquals(2, A.getInteractor().getIdentifiers().size());
        Iterator<Xref> xrefIterator = A.getInteractor().getIdentifiers().iterator();
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("innatedb"), "IDBG-82738", CvTermUtils.createIdentityQualifier()), xrefIterator.next());
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("ensembl"), "ENSG00000136869", CvTermUtils.createSecondaryXrefQualifier()), xrefIterator.next());
        Assert.assertEquals(5, A.getInteractor().getAliases().size());
        Iterator<Alias> aliasIterator = A.getInteractor().getAliases().iterator();
        Assert.assertEquals(new DefaultAlias(new DefaultCvTerm("gene name"), "bla"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("O00206"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("TLR4_HUMAN"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NM_138554"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NP_612564"), aliasIterator.next());
        Assert.assertEquals(9606, A.getInteractor().getOrganism().getTaxId());
        Assert.assertEquals("Human", A.getInteractor().getOrganism().getCommonName());
        Assert.assertEquals("Homo Sapiens", A.getInteractor().getOrganism().getScientificName());
        Assert.assertEquals(CvTermUtils.createUnspecifiedRole(), A.getBiologicalRole());
        Assert.assertEquals(CvTermUtils.createMICvTerm("bait", "MI:0496"), A.getExperimentalRole());
        Assert.assertEquals(CvTermUtils.createProteinInteractorType(), A.getInteractor().getInteractorType());
        Assert.assertEquals(1, A.getInteractor().getXrefs().size());
        Assert.assertEquals(XrefUtils.createXref("go", "GO:xxxx"), A.getInteractor().getXrefs().iterator().next());
        Assert.assertEquals(1, A.getAnnotations().size());
        Assert.assertEquals(AnnotationUtils.createAnnotation("caution","test caution"), A.getAnnotations().iterator().next());
        Assert.assertEquals(1, A.getInteractor().getChecksums().size());
        Assert.assertEquals("xxx4", A.getInteractor().getChecksums().iterator().next().getValue());
        Assert.assertEquals(0, A.getFeatures().size());
        Assert.assertNull(A.getStoichiometry());
        Assert.assertEquals(0, A.getIdentificationMethods().size());

        ParticipantEvidence B = iterator.next();
        Assert.assertEquals("LY96", B.getInteractor().getShortName());
        xrefIterator = B.getInteractor().getIdentifiers().iterator();
        Assert.assertEquals(2, B.getInteractor().getIdentifiers().size());
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("innatedb"), "IDBG-25842", CvTermUtils.createIdentityQualifier()), xrefIterator.next());
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("ensembl"), "ENSG00000154589", CvTermUtils.createSecondaryXrefQualifier()), xrefIterator.next());
        Assert.assertEquals(6, B.getInteractor().getAliases().size());
        aliasIterator = B.getInteractor().getAliases().iterator();
        Assert.assertEquals(new DefaultAlias("Q9Y6Y9"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("LY96_HUMAN"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NM_015364"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NP_056179"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NM_001195797"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NP_001182726"), aliasIterator.next());
        Assert.assertEquals(9606, B.getInteractor().getOrganism().getTaxId());
        Assert.assertEquals("Human", B.getInteractor().getOrganism().getCommonName());
        Assert.assertNull(B.getInteractor().getOrganism().getScientificName());
        Assert.assertEquals(CvTermUtils.createUnspecifiedRole(), B.getBiologicalRole());
        Assert.assertEquals(CvTermUtils.createMICvTerm("prey", "MI:0498"), B.getExperimentalRole());
        Assert.assertEquals(CvTermUtils.createProteinInteractorType(), B.getInteractor().getInteractorType());
        Assert.assertEquals(1, B.getInteractor().getXrefs().size());
        Assert.assertEquals(XrefUtils.createXref("interpro", "interpro:xxx"), B.getInteractor().getXrefs().iterator().next());
        Assert.assertEquals(0, B.getAnnotations().size());
        Assert.assertEquals(1, B.getInteractor().getChecksums().size());
        Assert.assertEquals("xxxx2", B.getInteractor().getChecksums().iterator().next().getValue());
        Assert.assertTrue(B.getFeatures().isEmpty());
        Assert.assertNull(B.getStoichiometry());
        Assert.assertEquals(0, B.getIdentificationMethods().size());

        Experiment experiment = binary.getExperiment();
        Assert.assertNotNull(experiment);
        Assert.assertEquals(CvTermUtils.createMICvTerm("anti tag coimmunoprecipitation", "MI:0007"), experiment.getInteractionDetectionMethod());
        Publication publication = experiment.getPublication();
        Assert.assertNotNull(publication);
        Assert.assertEquals(1, publication.getAuthors().size());
        Assert.assertEquals("Shimazu", publication.getAuthors().iterator().next());
        Assert.assertEquals(MitabUtils.PUBLICATION_YEAR_FORMAT.parse("1999"), publication.getPublicationDate());
        Assert.assertEquals("10359581", publication.getPubmedId());
        Assert.assertEquals(new DefaultSource("innatedb", "MI:0974"), publication.getSource());
        Assert.assertEquals(10090, experiment.getHostOrganism().getTaxId());
        Assert.assertEquals("mouse", experiment.getHostOrganism().getCommonName());

        Assert.assertEquals(CvTermUtils.createMICvTerm("physical association","MI:0915"), binary.getInteractionType());
        Assert.assertEquals(1, binary.getIdentifiers().size());
        Assert.assertEquals(XrefUtils.createIdentityXref("innatedb", "IDB-113240"), binary.getIdentifiers().iterator().next());
        Assert.assertEquals("IM-1-1", binary.getImexId());
        Assert.assertEquals(3, binary.getConfidences().size());
        Iterator<Confidence> confIterator = binary.getConfidences().iterator();
        Assert.assertEquals(new DefaultConfidence(new DefaultCvTerm("lpr"), "2"), confIterator.next());
        Assert.assertEquals(new DefaultConfidence(new DefaultCvTerm("hpr"), "2"), confIterator.next());
        Assert.assertEquals(new DefaultConfidence(new DefaultCvTerm("np"), "1"), confIterator.next());
        Assert.assertEquals(2, binary.getXrefs().size());
        xrefIterator = binary.getXrefs().iterator();
        xrefIterator.next();
        Assert.assertEquals(XrefUtils.createXrefWithQualifier("go", "GO:xxx1", "process"), xrefIterator.next());
        Assert.assertEquals(1, binary.getAnnotations().size());
        Assert.assertEquals(AnnotationUtils.createAnnotation("figure-legend", "Fig1."), binary.getAnnotations().iterator().next());
        Assert.assertEquals(1, binary.getParameters().size());
        Assert.assertEquals("2x10^(-1)", binary.getParameters().iterator().next().getValue().toString());
        Assert.assertEquals(MitabUtils.DATE_FORMAT.parse("2008/03/30"), binary.getCreatedDate());
        Assert.assertEquals(MitabUtils.DATE_FORMAT.parse("2008/03/30"), binary.getUpdatedDate());
        Assert.assertFalse(binary.isNegative());
        Assert.assertEquals(1, binary.getChecksums().size());
        Assert.assertEquals("xxxx3", ((Checksum)binary.getChecksums().iterator().next()).getValue());

        InteractionEvidence binary2 = parser.MitabLine();
        Assert.assertNotNull(binary2);
        Assert.assertTrue(parser.hasFinished());
    }

    @Test
    public void test_read_valid_mitab27() throws ParseException, java.text.ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/mitab27_line.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // read first interaction
        BinaryInteractionEvidence binary = (BinaryInteractionEvidence)parser.MitabLine();
        Assert.assertNotNull(binary);
        Assert.assertFalse(parser.hasFinished());

        Iterator<ParticipantEvidence> iterator = binary.getParticipants().iterator();
        ParticipantEvidence A = iterator.next();
        Assert.assertEquals("LY96", A.getInteractor().getShortName());
        Assert.assertEquals(2, A.getInteractor().getIdentifiers().size());
        Iterator<Xref> xrefIterator = A.getInteractor().getIdentifiers().iterator();
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("innatedb"), "IDBG-82738", CvTermUtils.createIdentityQualifier()), xrefIterator.next());
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("ensembl"), "ENSG00000136869", CvTermUtils.createSecondaryXrefQualifier()), xrefIterator.next());
        Assert.assertEquals(5, A.getInteractor().getAliases().size());
        Iterator<Alias> aliasIterator = A.getInteractor().getAliases().iterator();
        Assert.assertEquals(new DefaultAlias(new DefaultCvTerm("gene name"), "bla"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("O00206"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("TLR4_HUMAN"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NM_138554"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NP_612564"), aliasIterator.next());
        Assert.assertEquals(9606, A.getInteractor().getOrganism().getTaxId());
        Assert.assertEquals("Human", A.getInteractor().getOrganism().getCommonName());
        Assert.assertEquals("Homo Sapiens", A.getInteractor().getOrganism().getScientificName());
        Assert.assertEquals(CvTermUtils.createUnspecifiedRole(), A.getBiologicalRole());
        Assert.assertEquals(CvTermUtils.createMICvTerm("bait", "MI:0496"), A.getExperimentalRole());
        Assert.assertEquals(CvTermUtils.createProteinInteractorType(), A.getInteractor().getInteractorType());
        Assert.assertEquals(1, A.getInteractor().getXrefs().size());
        Assert.assertEquals(XrefUtils.createXref("go", "GO:xxxx"), A.getInteractor().getXrefs().iterator().next());
        Assert.assertEquals(1, A.getAnnotations().size());
        Assert.assertEquals(AnnotationUtils.createAnnotation("caution","test caution"), A.getAnnotations().iterator().next());
        Assert.assertEquals(1, A.getInteractor().getChecksums().size());
        Assert.assertEquals("xxx4", A.getInteractor().getChecksums().iterator().next().getValue());
        Assert.assertEquals(1, A.getFeatures().size());
        MitabFeatureEvidence f = (MitabFeatureEvidence)A.getFeatures().iterator().next();
        Assert.assertEquals(new DefaultCvTerm("binding site"), f.getType());
        Assert.assertEquals(2, f.getRanges().size());
        Iterator<Range> rangeIterator = f.getRanges().iterator();
        Range r1 = rangeIterator.next();
        Assert.assertEquals(PositionUtils.createCertainPosition(3), r1.getStart());
        Assert.assertEquals(PositionUtils.createCertainPosition(3), r1.getEnd());
        Range r2 = rangeIterator.next();
        Assert.assertEquals(PositionUtils.createFuzzyPosition(4, 5), r2.getStart());
        Assert.assertEquals(PositionUtils.createFuzzyPosition(6, 7), r2.getEnd());
        Assert.assertEquals("interpro:xxxx", f.getText());
        Assert.assertEquals(new DefaultStoichiometry(2), A.getStoichiometry());
        Assert.assertEquals(1, A.getIdentificationMethods().size());
        Assert.assertEquals(CvTermUtils.createMICvTerm("inferred by author", "MI:0363"), A.getIdentificationMethods().iterator().next());

        ParticipantEvidence B = iterator.next();
        Assert.assertEquals("LY96", B.getInteractor().getShortName());
        xrefIterator = B.getInteractor().getIdentifiers().iterator();
        Assert.assertEquals(2, B.getInteractor().getIdentifiers().size());
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("innatedb"), "IDBG-25842", CvTermUtils.createIdentityQualifier()), xrefIterator.next());
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("ensembl"), "ENSG00000154589", CvTermUtils.createSecondaryXrefQualifier()), xrefIterator.next());
        Assert.assertEquals(6, B.getInteractor().getAliases().size());
        aliasIterator = B.getInteractor().getAliases().iterator();
        Assert.assertEquals(new DefaultAlias("Q9Y6Y9"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("LY96_HUMAN"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NM_015364"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NP_056179"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NM_001195797"), aliasIterator.next());
        Assert.assertEquals(new DefaultAlias("NP_001182726"), aliasIterator.next());
        Assert.assertEquals(9606, B.getInteractor().getOrganism().getTaxId());
        Assert.assertEquals("Human", B.getInteractor().getOrganism().getCommonName());
        Assert.assertNull(B.getInteractor().getOrganism().getScientificName());
        Assert.assertEquals(CvTermUtils.createUnspecifiedRole(), B.getBiologicalRole());
        Assert.assertEquals(CvTermUtils.createMICvTerm("prey","MI:0498"), B.getExperimentalRole());
        Assert.assertEquals(CvTermUtils.createProteinInteractorType(), B.getInteractor().getInteractorType());
        Assert.assertEquals(1, B.getInteractor().getXrefs().size());
        Assert.assertEquals(XrefUtils.createXref("interpro", "interpro:xxx"), B.getInteractor().getXrefs().iterator().next());
        Assert.assertEquals(0, B.getAnnotations().size());
        Assert.assertEquals(1, B.getInteractor().getChecksums().size());
        Assert.assertEquals("xxxx2", B.getInteractor().getChecksums().iterator().next().getValue());
        Assert.assertTrue(B.getFeatures().isEmpty());
        Assert.assertEquals(new DefaultStoichiometry(5), B.getStoichiometry());
        Assert.assertEquals(1, B.getIdentificationMethods().size());
        Assert.assertEquals(CvTermUtils.createMICvTerm("inferred by author", "MI:0363"), B.getIdentificationMethods().iterator().next());

        Experiment experiment = binary.getExperiment();
        Assert.assertNotNull(experiment);
        Assert.assertEquals(CvTermUtils.createMICvTerm("anti tag coimmunoprecipitation", "MI:0007"), experiment.getInteractionDetectionMethod());
        Publication publication = experiment.getPublication();
        Assert.assertNotNull(publication);
        Assert.assertEquals(1, publication.getAuthors().size());
        Assert.assertEquals("Shimazu", publication.getAuthors().iterator().next());
        Assert.assertEquals(MitabUtils.PUBLICATION_YEAR_FORMAT.parse("1999"), publication.getPublicationDate());
        Assert.assertEquals("10359581", publication.getPubmedId());
        Assert.assertEquals(new DefaultSource("innatedb", "MI:0974"), publication.getSource());
        Assert.assertEquals(10090, experiment.getHostOrganism().getTaxId());
        Assert.assertEquals("mouse", experiment.getHostOrganism().getCommonName());

        Assert.assertEquals(CvTermUtils.createMICvTerm("physical association","MI:0915"), binary.getInteractionType());
        Assert.assertEquals(1, binary.getIdentifiers().size());
        Assert.assertEquals(XrefUtils.createIdentityXref("innatedb", "IDB-113240"), binary.getIdentifiers().iterator().next());
        Assert.assertEquals("IM-1-1", binary.getImexId());
        Assert.assertEquals(3, binary.getConfidences().size());
        Iterator<Confidence> confIterator = binary.getConfidences().iterator();
        Assert.assertEquals(new DefaultConfidence(new DefaultCvTerm("lpr"), "2"), confIterator.next());
        Assert.assertEquals(new DefaultConfidence(new DefaultCvTerm("hpr"), "2"), confIterator.next());
        Assert.assertEquals(new DefaultConfidence(new DefaultCvTerm("np"), "1"), confIterator.next());
        Assert.assertEquals(2, binary.getXrefs().size());
        xrefIterator = binary.getXrefs().iterator();
        xrefIterator.next();
        Assert.assertEquals(XrefUtils.createXrefWithQualifier("go", "GO:xxx1", "process"), xrefIterator.next());
        Assert.assertEquals(1, binary.getAnnotations().size());
        Assert.assertEquals(AnnotationUtils.createAnnotation("figure-legend", "Fig1."), binary.getAnnotations().iterator().next());
        Assert.assertEquals(1, binary.getParameters().size());
        Assert.assertEquals("2x10^(-1)", binary.getParameters().iterator().next().getValue().toString());
        Assert.assertEquals(MitabUtils.DATE_FORMAT.parse("2008/03/30"), binary.getCreatedDate());
        Assert.assertEquals(MitabUtils.DATE_FORMAT.parse("2008/03/30"), binary.getUpdatedDate());
        Assert.assertFalse(binary.isNegative());
        Assert.assertEquals(1, binary.getChecksums().size());
        Assert.assertEquals("xxxx3", ((Checksum)binary.getChecksums().iterator().next()).getValue());

        InteractionEvidence binary2 = parser.MitabLine();
        Assert.assertNotNull(binary2);
        Participant A2 = binary2.getParticipants().iterator().next();
        Feature f2 = (Feature)A2.getFeatures().iterator().next();
        Assert.assertEquals("IPR020405", f2.getInterpro());
        Assert.assertTrue(parser.hasFinished());
    }

    @Test
    public void test_read_valid_mitab28() throws ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/mitab28.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // 1st interaction
        BinaryInteractionEvidence binary1 = parser.MitabLine();
        Assert.assertNotNull(binary1);
        Assert.assertFalse(parser.hasFinished());

        Collection<CvTerm> methodsA = binary1.getParticipantA().getIdentificationMethods();
        Assert.assertEquals(1, methodsA.size());
        Assert.assertEquals(CvTermUtils.createMICvTerm("predetermined participant", "MI:0396"), methodsA.iterator().next());

        // check biological effect of A participant
        CvTerm bioeffectA = binary1.getParticipantA().getBiologicalEffect();
        Xref identifierA = bioeffectA.getIdentifiers().iterator().next();

        Assert.assertEquals(bioeffectA.getShortName(), "kinase activity");
        Assert.assertEquals(identifierA.getId(),"GO:0016301");
        Assert.assertEquals(identifierA.getDatabase().getShortName(),"go");

        // check biological effect of B participant
        CvTerm bioeffectB = binary1.getParticipantB().getBiologicalEffect();
        Xref identifierB = bioeffectB.getIdentifiers().iterator().next();

        Assert.assertEquals(bioeffectB.getShortName(), "kinase activity");
        Assert.assertEquals(identifierB.getId(),"GO:0016301");
        Assert.assertEquals(identifierB.getDatabase().getShortName(),"go");

        // check the causal regulatory mechanism
        CvTerm causalRegMechanism = binary1.getCausalRegulatoryMechanism();
        Xref causalMechanismIdentifier = causalRegMechanism.getIdentifiers().iterator().next();

        Assert.assertEquals(causalMechanismIdentifier.getId(), "MI:2249");
        Assert.assertEquals(causalMechanismIdentifier.getDatabase().getShortName(), "psi-mi");
        Assert.assertEquals(causalRegMechanism.getShortName(), "post transcriptional regulation");

        // check the causal statement (relationType)
        Iterator<CausalRelationship> it = binary1.getParticipantA().getCausalRelationships().iterator();
        CausalRelationship causalRelationship = it.next();
        CvTerm causalStatement = causalRelationship.getRelationType();
        Xref causalStatementIdentifier = causalStatement.getIdentifiers().iterator().next();
        Assert.assertEquals(causalStatementIdentifier.getId(),"MI:2235");
        Assert.assertEquals(causalStatementIdentifier.getDatabase().getShortName(),"psi-mi");
        Assert.assertEquals(causalStatement.getShortName(), "up regulates");

        // check the target of causality
        Entity target = causalRelationship.getTarget();
        Assert.assertEquals(target, binary1.getParticipantB());

        // 2nd interaction
        BinaryInteractionEvidence binary2 = parser.MitabLine();
        Assert.assertNotNull(binary2);
        Assert.assertFalse(parser.hasFinished());

        Collection<CvTerm> methodsB = binary2.getParticipantB().getIdentificationMethods();
        Assert.assertEquals(1, methodsB.size());
        Assert.assertEquals(CvTermUtils.createMICvTerm("western blot", "MI:0113"), methodsB.iterator().next());

        // check biological effect of A participant
        bioeffectA = binary2.getParticipantA().getBiologicalEffect();
        identifierA = bioeffectA.getIdentifiers().iterator().next();

        Assert.assertEquals(bioeffectA.getShortName(), "kinase activity");
        Assert.assertEquals(identifierA.getId(),"GO:0016301");
        Assert.assertEquals(identifierA.getDatabase().getShortName(),"go");

        // check biological effect of B participant
        bioeffectB = binary2.getParticipantB().getBiologicalEffect();
        identifierB = bioeffectB.getIdentifiers().iterator().next();

        Assert.assertEquals(bioeffectB.getShortName(), "kinase activity");
        Assert.assertEquals(identifierB.getId(),"GO:0016301");
        Assert.assertEquals(identifierB.getDatabase().getShortName(),"go");

        // check the causal regulatory mechanism
        causalRegMechanism = binary2.getCausalRegulatoryMechanism();
        causalMechanismIdentifier = causalRegMechanism.getIdentifiers().iterator().next();

        Assert.assertEquals(causalMechanismIdentifier.getId(), "MI:2249");
        Assert.assertEquals(causalMechanismIdentifier.getDatabase().getShortName(), "psi-mi");
        Assert.assertEquals(causalRegMechanism.getShortName(), "post transcriptional regulation");

        // check the causal statement (relationType)
        it = binary2.getParticipantA().getCausalRelationships().iterator();
        causalRelationship = it.next();
        causalStatement = causalRelationship.getRelationType();
        causalStatementIdentifier = causalStatement.getIdentifiers().iterator().next();
        Assert.assertEquals(causalStatementIdentifier.getId(),"MI:2240");
        Assert.assertEquals(causalStatementIdentifier.getDatabase().getShortName(),"psi-mi");
        Assert.assertEquals(causalStatement.getShortName(), "down regulates");

        // check the target of causality
        target = causalRelationship.getTarget();
        Assert.assertEquals(target, binary2.getParticipantB());

        // 3rd interaction
        BinaryInteractionEvidence binary3 = parser.MitabLine();
        Assert.assertNotNull(binary3);
        Assert.assertFalse(parser.hasFinished());

        methodsB = binary3.getParticipantB().getIdentificationMethods();
        Assert.assertEquals(1, methodsB.size());
        Assert.assertEquals(CvTermUtils.createMICvTerm("western blot", "MI:0113"), methodsB.iterator().next());

        // check biological effect of A participant
        bioeffectA = binary3.getParticipantA().getBiologicalEffect();
        identifierA = bioeffectA.getIdentifiers().iterator().next();

        Assert.assertEquals(bioeffectA.getShortName(), "kinase activity");
        Assert.assertEquals(identifierA.getId(),"GO:0016301");
        Assert.assertEquals(identifierA.getDatabase().getShortName(),"go");

        // check biological effect of B participant
        bioeffectB = binary3.getParticipantB().getBiologicalEffect();
        identifierB = bioeffectB.getIdentifiers().iterator().next();

        Assert.assertEquals(bioeffectB.getShortName(), "kinase activity");
        Assert.assertEquals(identifierB.getId(),"GO:0016301");
        Assert.assertEquals(identifierB.getDatabase().getShortName(),"go");

        // check the causal regulatory mechanism
        causalRegMechanism = binary3.getCausalRegulatoryMechanism();
        causalMechanismIdentifier = causalRegMechanism.getIdentifiers().iterator().next();

        Assert.assertEquals(causalMechanismIdentifier.getId(), "MI:2249");
        Assert.assertEquals(causalMechanismIdentifier.getDatabase().getShortName(), "psi-mi");
        Assert.assertEquals(causalRegMechanism.getShortName(), "post transcriptional regulation");

        // check the causal statement (relationType)
        it = binary3.getParticipantA().getCausalRelationships().iterator();
        causalRelationship = it.next();
        causalStatement = causalRelationship.getRelationType();
        causalStatementIdentifier = causalStatement.getIdentifiers().iterator().next();
        Assert.assertEquals(causalStatementIdentifier.getId(),"MI:2240");
        Assert.assertEquals(causalStatementIdentifier.getDatabase().getShortName(),"psi-mi");
        Assert.assertEquals(causalStatement.getShortName(), "down regulates");

        // check the target of causality
        target = causalRelationship.getTarget();
        Assert.assertEquals(target, binary3.getParticipantB());

        // 4th interaction
        BinaryInteractionEvidence binary4 = parser.MitabLine();
        Assert.assertNotNull(binary4);
        Assert.assertTrue(parser.hasFinished());

        methodsA = binary4.getParticipantA().getIdentificationMethods();
        Assert.assertEquals(1, methodsA.size());
        Assert.assertEquals(CvTermUtils.createMICvTerm("tag visualisation by peroxidase activity", "MI:0981"), methodsA.iterator().next());

        // check biological effect of A participant
        bioeffectA = binary4.getParticipantA().getBiologicalEffect();
        identifierA = bioeffectA.getIdentifiers().iterator().next();

        Assert.assertEquals(bioeffectA.getShortName(), "kinase activity");
        Assert.assertEquals(identifierA.getId(),"GO:0016301");
        Assert.assertEquals(identifierA.getDatabase().getShortName(),"go");

        // check biological effect of B participant
        bioeffectB = binary4.getParticipantB().getBiologicalEffect();
        identifierB = bioeffectB.getIdentifiers().iterator().next();

        Assert.assertEquals(bioeffectB.getShortName(), "antioxidant activity");
        Assert.assertEquals(identifierB.getId(),"GO:0016209");
        Assert.assertEquals(identifierB.getDatabase().getShortName(),"go");

        // check the causal regulatory mechanism
        causalRegMechanism = binary4.getCausalRegulatoryMechanism();
        causalMechanismIdentifier = causalRegMechanism.getIdentifiers().iterator().next();

        Assert.assertEquals(causalMechanismIdentifier.getId(), "MI:2249");
        Assert.assertEquals(causalMechanismIdentifier.getDatabase().getShortName(), "psi-mi");
        Assert.assertEquals(causalRegMechanism.getShortName(), "post transcriptional regulation");

        // check the causal statement (relationType)
        it = binary4.getParticipantA().getCausalRelationships().iterator();
        causalRelationship = it.next();
        causalStatement = causalRelationship.getRelationType();
        causalStatementIdentifier = causalStatement.getIdentifiers().iterator().next();
        Assert.assertEquals(causalStatementIdentifier.getId(),"MI:2235");
        Assert.assertEquals(causalStatementIdentifier.getDatabase().getShortName(),"psi-mi");
        Assert.assertEquals(causalStatement.getShortName(), "up regulates");

        // check the target of causality
        target = causalRelationship.getTarget();
        Assert.assertEquals(target, binary4.getParticipantB());
    }

    @Test
    public void test_read_empty_columns_mitab28() throws ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/mitab28_empty_columns.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // 1st interaction
        BinaryInteractionEvidence binary1 = parser.MitabLine();
        Assert.assertNotNull(binary1);
        Assert.assertFalse(parser.hasFinished());

        // All MITAB 2.8 columns are empty
        CvTerm bioeffectA = binary1.getParticipantA().getBiologicalEffect();
        CvTerm bioeffectB = binary1.getParticipantB().getBiologicalEffect();
        CvTerm causalRegMechanism = binary1.getCausalRegulatoryMechanism();
        Collection<CausalRelationship> causalRelationships = binary1.getParticipantA().getCausalRelationships();

        Assert.assertNull(bioeffectA);
        Assert.assertNull(bioeffectB);
        Assert.assertNull(causalRegMechanism);
        Assert.assertTrue(causalRelationships.isEmpty());

        // 2nd interaction
        BinaryInteractionEvidence binary2 = parser.MitabLine();
        Assert.assertNotNull(binary2);
        Assert.assertFalse(parser.hasFinished());

        // check biological effect of A participant
        bioeffectA = binary2.getParticipantA().getBiologicalEffect();
        Xref identifierA = bioeffectA.getIdentifiers().iterator().next();

        Assert.assertEquals(bioeffectA.getShortName(), "kinase activity");
        Assert.assertEquals(identifierA.getId(),"GO:0016301");
        Assert.assertEquals(identifierA.getDatabase().getShortName(),"go");

        // biological effect of B participant is empty
        bioeffectB = binary2.getParticipantB().getBiologicalEffect();
        Assert.assertNull(bioeffectB);

        // causal regulatory mechanism is empty
        causalRegMechanism = binary2.getCausalRegulatoryMechanism();
        Assert.assertNull(causalRegMechanism);

        // check the causal statement (relationType)
        CvTerm causalStatement = binary2.getParticipantA().getCausalRelationships().iterator().next().getRelationType();
        Xref causalStatementIdentifier = causalStatement.getIdentifiers().iterator().next();
        Assert.assertEquals(causalStatementIdentifier.getId(),"MI:2235");
        Assert.assertEquals(causalStatementIdentifier.getDatabase().getShortName(),"psi-mi");
        Assert.assertEquals(causalStatement.getShortName(), "up regulates");

        // check the target of causality
        Entity target = binary2.getParticipantA().getCausalRelationships().iterator().next().getTarget();
        Assert.assertEquals(target, binary2.getParticipantB());
    }

    @Test
    public void test_empty_file() throws ParseException, java.text.ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/empty_file.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // read first interaction
        BinaryInteractionEvidence binary = (BinaryInteractionEvidence)parser.MitabLine();
        Assert.assertNull(binary);
        Assert.assertTrue(parser.hasFinished());
    }

    @Test
    public void test_header_and_empty_lines() throws ParseException, java.text.ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/mitab27_line_header.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // read first interaction
        BinaryInteractionEvidence binary = (BinaryInteractionEvidence)parser.MitabLine();
        Assert.assertNull(binary);
        Assert.assertFalse(parser.hasFinished());

        // read title
        InteractionEvidence empty_line1 = parser.MitabLine();
        Assert.assertNull(empty_line1);
        Assert.assertFalse(parser.hasFinished());

        // read title
        InteractionEvidence line1 = parser.MitabLine();
        Assert.assertNotNull(line1);
        Assert.assertFalse(parser.hasFinished());

        // read title
        InteractionEvidence empty_line2 = parser.MitabLine();
        Assert.assertNull(empty_line2);
        Assert.assertFalse(parser.hasFinished());

        // read title
        InteractionEvidence line2 = parser.MitabLine();
        Assert.assertNotNull(line2);
        Assert.assertTrue(parser.hasFinished());
    }

    @Test
    public void test_syntax_error_unique_identifier() throws ParseException, java.text.ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/mitab27_unique_identifier_error.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // read first interaction
        BinaryInteractionEvidence line1 = (BinaryInteractionEvidence)parser.MitabLine();
        Assert.assertNotNull(line1);
        Assert.assertEquals(2, line1.getParticipants().iterator().next().getInteractor().getIdentifiers().size());
        Iterator<Xref> identifierIterator = line1.getParticipants().iterator().next().getInteractor().getIdentifiers().iterator();
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("innatedb"), "IDBG-827", CvTermUtils.createIdentityQualifier()), identifierIterator.next());
        Assert.assertEquals(new DefaultXref(new DefaultCvTerm("ensembl"), "ENSG00000136869", CvTermUtils.createSecondaryXrefQualifier()), identifierIterator.next());
        Assert.assertFalse(parser.hasFinished());

        // read title
        InteractionEvidence line2 = parser.MitabLine();
        Assert.assertNotNull(line2);
        Assert.assertTrue(parser.hasFinished());
    }

    @Test
    public void test_read_clustered_mitab27() throws ParseException, java.text.ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/mitab27_clustered_line.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // read first interaction
        BinaryInteractionEvidence binary = (BinaryInteractionEvidence)parser.MitabLine();
        Assert.assertNotNull(binary);
        Assert.assertFalse(parser.hasFinished());

        Iterator<ParticipantEvidence> iterator = binary.getParticipants().iterator();
        ParticipantEvidence A = iterator.next();
        Assert.assertEquals(9606, A.getInteractor().getOrganism().getTaxId());
        Assert.assertEquals("Human", A.getInteractor().getOrganism().getCommonName());
        Assert.assertEquals("Homo Sapiens", A.getInteractor().getOrganism().getScientificName());
        Assert.assertEquals(CvTermUtils.createUnspecifiedRole(), A.getBiologicalRole());
        Assert.assertEquals(CvTermUtils.createMICvTerm("bait", "MI:0496"), A.getExperimentalRole());
        Assert.assertEquals(CvTermUtils.createProteinInteractorType(), A.getInteractor().getInteractorType());
        Assert.assertEquals(new DefaultStoichiometry(2), A.getStoichiometry());
        Assert.assertEquals(2, A.getIdentificationMethods().size());
        Assert.assertEquals(CvTermUtils.createMICvTerm("inferred by author", "MI:0363"), A.getIdentificationMethods().iterator().next());

        ParticipantEvidence B = iterator.next();
        Assert.assertEquals(9606, B.getInteractor().getOrganism().getTaxId());
        Assert.assertEquals("Human", B.getInteractor().getOrganism().getCommonName());
        Assert.assertNull(B.getInteractor().getOrganism().getScientificName());
        Assert.assertEquals(CvTermUtils.createUnspecifiedRole(), B.getBiologicalRole());
        Assert.assertEquals(CvTermUtils.createMICvTerm("prey","MI:0498"), B.getExperimentalRole());
        Assert.assertEquals(CvTermUtils.createProteinInteractorType(), B.getInteractor().getInteractorType());
        Assert.assertEquals(new DefaultStoichiometry(5), B.getStoichiometry());
        Assert.assertEquals(2, B.getIdentificationMethods().size());
        Assert.assertEquals(CvTermUtils.createMICvTerm("inferred by author", "MI:0363"), B.getIdentificationMethods().iterator().next());

        Experiment experiment = binary.getExperiment();
        Assert.assertNotNull(experiment);
        Assert.assertEquals(CvTermUtils.createMICvTerm("anti tag coimmunoprecipitation", "MI:0007"), experiment.getInteractionDetectionMethod());
        Publication publication = experiment.getPublication();
        Assert.assertNotNull(publication);
        Assert.assertEquals(1, publication.getAuthors().size());
        Assert.assertEquals("Shimazu", publication.getAuthors().iterator().next());
        Assert.assertEquals(MitabUtils.PUBLICATION_YEAR_FORMAT.parse("1999"), publication.getPublicationDate());
        Assert.assertEquals(new DefaultSource("innatedb", "MI:0974"), publication.getSource());
        Assert.assertEquals(10090, experiment.getHostOrganism().getTaxId());
        Assert.assertEquals("mouse", experiment.getHostOrganism().getCommonName());

        Assert.assertEquals(CvTermUtils.createMICvTerm("physical association","MI:0915"), binary.getInteractionType());
        Assert.assertEquals(MitabUtils.DATE_FORMAT.parse("2008/03/30"), binary.getCreatedDate());
        Assert.assertEquals(MitabUtils.DATE_FORMAT.parse("2008/03/30"), binary.getUpdatedDate());
        Assert.assertFalse(binary.isNegative());

        InteractionEvidence binary2 = parser.MitabLine();
        Assert.assertNotNull(binary2);
        Assert.assertTrue(parser.hasFinished());
    }

    @Test
    public void test_read_no_interactor_details() throws ParseException, java.text.ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/mitab27_no_interactor_details.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // read first interaction
        BinaryInteractionEvidence binary = (BinaryInteractionEvidence)parser.MitabLine();
        Assert.assertNotNull(binary);
        Assert.assertFalse(parser.hasFinished());

        Iterator<ParticipantEvidence> iterator = binary.getParticipants().iterator();
        ParticipantEvidence A = iterator.next();
        Assert.assertEquals(MitabUtils.UNKNOWN_NAME, A.getInteractor().getShortName());
        Assert.assertEquals(CvTermUtils.createUnknownInteractorType(), A.getInteractor().getInteractorType());
        Assert.assertNull(A.getInteractor().getOrganism());

        InteractionEvidence binary2 = parser.MitabLine();
        Assert.assertNotNull(binary2);
        Assert.assertTrue(parser.hasFinished());
    }

    @Test
    public void test_read_no_participants() throws ParseException, java.text.ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/mitab27_no_participants.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // read first interaction
        BinaryInteractionEvidence binary = (BinaryInteractionEvidence)parser.MitabLine();
        Assert.assertNotNull(binary);
        Assert.assertFalse(parser.hasFinished());

        Assert.assertTrue(binary.getParticipants().isEmpty());

        InteractionEvidence binary2 = parser.MitabLine();
        Assert.assertNotNull(binary2);
        Assert.assertTrue(parser.hasFinished());
    }

    @Test
    public void test_read_too_many_columns() throws ParseException, java.text.ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/mitab27_line_too_many_columns.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // read first interaction
        BinaryInteractionEvidence binary = (BinaryInteractionEvidence)parser.MitabLine();
        Assert.assertNotNull(binary);
        Assert.assertFalse(parser.hasFinished());
        Assert.assertEquals(2, binary.getParticipants().size());

        InteractionEvidence binary2 = parser.MitabLine();
        Assert.assertNotNull(binary2);
        Assert.assertTrue(parser.hasFinished());
    }

    @Test
    public void test_parse() throws ParseException, java.text.ParseException {
        InputStream stream = BinaryEvidenceLineParserTest.class.getResourceAsStream("/samples/10075675.txt");
        BinaryEvidenceLineParser parser = new BinaryEvidenceLineParser(stream);

        // read first interaction
        BinaryInteractionEvidence binary = (BinaryInteractionEvidence)parser.MitabLine();
        Assert.assertNotNull(binary);
        Assert.assertFalse(parser.hasFinished());
        Assert.assertEquals(2, binary.getParticipants().size());

        InteractionEvidence binary2 = parser.MitabLine();
        Assert.assertNotNull(binary2);
        Assert.assertFalse(parser.hasFinished());

        InteractionEvidence binary4 = parser.MitabLine();
        Assert.assertNotNull(binary4);
        Assert.assertTrue(parser.hasFinished())
;
    }
}
