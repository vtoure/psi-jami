package psidev.psi.mi.jami.crosslink.io.parser;

import com.googlecode.jcsv.reader.CSVEntryParser;
import psidev.psi.mi.jami.crosslink.extension.*;
import psidev.psi.mi.jami.crosslink.listener.CsvParserListener;
import psidev.psi.mi.jami.crosslink.utils.CsvUtils;
import psidev.psi.mi.jami.datasource.DefaultFileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.impl.DefaultExperiment;
import psidev.psi.mi.jami.model.impl.DefaultPosition;
import psidev.psi.mi.jami.utils.CvTermUtils;

import java.util.*;

/**
 * Abstract class for crosslink CSV parser
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>22/08/14</pre>
 */
public abstract class AbstractCsvInteractionEvidenceParser<T extends InteractionEvidence> implements CSVEntryParser<T> {

    private Map<Integer, CrossLinkCSVColumns> columnsIndex=null;
    private int currentLineIndex=0;
    private CsvParserListener parserListener;
    private boolean isStarted = false;

    /**
     * <p>parseEntry.</p>
     *
     * @param data a {@link java.lang.String} object.
     * @return a T object.
     */
    public T parseEntry(String... data) {
        isStarted = true;
        if (data == null){
            return null;
        }

        // increments current line index
        currentLineIndex++;

        // initialise columns
        if (columnsIndex == null) {
            return null;
        }
        // parse data
        else{
            String protein1 = null;
            String protein2 = null;
            String pepPos1 = null;
            String pepPos2 = null;
            String linkPos1 = null;
            String linkPos2 = null;

            int index = 0;
            int protein1Index = -1;
            int protein2Index  = -1;
            int pepPos1Index  = -1;
            int pepPos2Index  = -1;
            int linkPos1Index  = -1;
            int linkPos2Index  = -1;
            String bait=null;

            for (String value : data){
                // the column index is recognized
                if (this.columnsIndex.containsKey(index)){
                    CrossLinkCSVColumns colName = this.columnsIndex.get(index);

                    switch (colName){
                        case protein1:
                            protein1 = value;
                            protein1Index = index;
                            break;
                        case protein2:
                            protein2 = value;
                            protein2Index = index;
                            break;
                        case peppos1:
                            pepPos1 = value;
                            pepPos1Index = index;
                            break;
                        case peppos2:
                            pepPos2 = value;
                            pepPos2Index = index;
                            break;
                        case linkpos1:
                            linkPos1 = value;
                            linkPos1Index = index;
                            break;
                        case linkpos2:
                            linkPos2 = value;
                            linkPos2Index = index;
                            break;
                        case narygroup:
                            bait = value;
                            break;
                    }
                }
                index++;
            }

            T interaction = instantiateInteractionEvidence(currentLineIndex, bait);

            if (protein1 != null){
                ParticipantEvidence participant1 = createParticipantEvidence(protein1, protein1Index, pepPos1, pepPos1Index, linkPos1, linkPos1Index);
                ParticipantEvidence participant2=null;
                if (protein2 != null){
                    participant2 = createParticipantEvidence(protein2, protein2Index, pepPos2, pepPos2Index, linkPos2, linkPos2Index);
                }

                if (participant1 != null){
                    interaction.addParticipant(participant1);

                    if (participant2 != null){
                        interaction.addParticipant(participant2);

                        if (participant1.getFeatures().size() == 1 && participant2.getFeatures().size() == 1){
                            FeatureEvidence f1 = participant1.getFeatures().iterator().next();
                            FeatureEvidence f2 = participant2.getFeatures().iterator().next();
                            f1.getLinkedFeatures().add(f2);
                            f2.getLinkedFeatures().add(f1);
                        }
                    }

                    // set interaction type
                    interaction.setInteractionType(CvTermUtils.createMICvTerm(CsvUtils.PHYSICAL_INTERACTION, CsvUtils.PHYSICAL_INTERACTION_MI));
                    // create experiment
                    Experiment exp = new DefaultExperiment(null);
                    // set interaction detection method
                    exp.setInteractionDetectionMethod(CvTermUtils.createMICvTerm(CsvUtils.CROSS_LINK, CsvUtils.CROSS_LINK_MI));
                    // set experiment
                    interaction.setExperiment(exp);

                    return interaction;
                }
            }
            else{
                processNoProtein1Error(currentLineIndex);
                return interaction;
            }
            return interaction;
        }
    }

    /**
     * <p>Getter for the field <code>parserListener</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.crosslink.listener.CsvParserListener} object.
     */
    public CsvParserListener getParserListener() {
        return parserListener;
    }

    /**
     * <p>Setter for the field <code>parserListener</code>.</p>
     *
     * @param parserListener a {@link psidev.psi.mi.jami.crosslink.listener.CsvParserListener} object.
     */
    public void setParserListener(CsvParserListener parserListener) {
        this.parserListener = parserListener;
    }

    /**
     * <p>isStarted.</p>
     *
     * @return a boolean.
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * <p>createParticipantEvidence.</p>
     *
     * @param protein1 a {@link java.lang.String} object.
     * @param protein1Index a int.
     * @param pepPos a {@link java.lang.String} object.
     * @param pepPos1Index a int.
     * @param linkedPos a {@link java.lang.String} object.
     * @param linkedPosIndex a int.
     * @return a {@link psidev.psi.mi.jami.model.ParticipantEvidence} object.
     */
    protected ParticipantEvidence createParticipantEvidence(String protein1, int protein1Index, String pepPos, int pepPos1Index, String linkedPos,
                                                            int linkedPosIndex) {
        // parse proteins
        List<CsvProtein> csvProteins1 = createProteinsFromString(protein1, currentLineIndex, protein1Index);

        List<CsvRange> positions = parseCrossLinkingFeatures(pepPos, linkedPos, currentLineIndex, pepPos1Index, linkedPosIndex);

        // check if we have more than one position and we need adjusting
        if (!positions.isEmpty()){
            // the same protein has different ranges and need to be duplicated in a participant set
            if (positions.size() > 1 && csvProteins1.size() == 1){
                CsvProtein firstProt = csvProteins1.iterator().next();
                // we duplicate the protein
                for (int i=1; i < positions.size(); i++){
                    csvProteins1.add(firstProt);
                }
            }
        }

        // parse participantEvidence
        ParticipantEvidence participantEvidence1 = null;
        // simple participant
        if (csvProteins1.size() == 1 && positions.size() == 1){
            participantEvidence1 = createParticipantEvidence(csvProteins1.iterator().next(), currentLineIndex, protein1Index, positions.iterator().next());
        }
        // participant no linked features
        else if (csvProteins1.size() == 1 && positions.isEmpty()){
            participantEvidence1 = createParticipantEvidence(csvProteins1.iterator().next(), currentLineIndex, protein1Index, null);
        }
        // we have an error, the number of positions does not match the number of proteins and we have ambiguous results
        else if (positions.size() > 0 && positions.size() != csvProteins1.size()){
            processMismatchProteinPositions(positions, csvProteins1);
        }
        // participant pool
        else {
            participantEvidence1 = createExperimentalParticipantPool(csvProteins1, currentLineIndex, protein1Index, positions);
        }

        return participantEvidence1;
    }

    /**
     * <p>parseCrossLinkingFeatures.</p>
     *
     * @param pepPos a {@link java.lang.String} object.
     * @param linkedPos a {@link java.lang.String} object.
     * @param lineNumber a int.
     * @param pepColumnNumber a int.
     * @param linkedColumnNumber a int.
     * @return a {@link java.util.List} object.
     */
    protected List<CsvRange> parseCrossLinkingFeatures(String pepPos, String linkedPos, int lineNumber, int pepColumnNumber, int linkedColumnNumber) {
        List<CsvRange> positions = Collections.EMPTY_LIST;

        // parse positions
        // the position is relative to the peptide
        if (pepPos != null && linkedPos != null){
            List<CsvRange> peptidePositions = parsePositions(pepPos, lineNumber, pepColumnNumber);
            List<CsvRange> relativePositions = parsePositions(linkedPos, lineNumber, linkedColumnNumber);

            if (!peptidePositions.isEmpty() && !relativePositions.isEmpty()){
                // the same protein has different ranges and need to be duplicated in a participant set
                if (relativePositions.size() > 1 && peptidePositions.size() == 1){
                    CsvRange firstPepPosition = peptidePositions.iterator().next();
                    // we duplicate the protein
                    for (int i=1; i < relativePositions.size(); i++){
                        relativePositions.add(firstPepPosition);
                    }
                }
                // we have an error, the number of positions does not match the number of peptide positions and we have ambiguous results
                else if (relativePositions.size() > 0 && relativePositions.size() != peptidePositions.size()){
                    processMismatchPeptidePositions(peptidePositions, relativePositions);
                }

                // compute final positions
                positions = new ArrayList<CsvRange>(relativePositions.size());
                for (int i=0; i < relativePositions.size(); i++){
                    CsvRange finalRange = new CsvRange(
                            new DefaultPosition(relativePositions.get(i).getStart().getStart() + peptidePositions.get(i).getStart().getStart()),
                            new DefaultPosition(relativePositions.get(i).getEnd().getEnd() + peptidePositions.get(i).getEnd().getEnd()));
                    finalRange.setSourceLocator(relativePositions.get(i).getSourceLocator());
                    positions.add(finalRange);
                }
            }
            else{
                processMismatchPeptidePositions(peptidePositions, relativePositions);
            }
        }
        // the position is absolute
        else if (pepPos == null && linkedPos != null){
            positions = parsePositions(linkedPos, lineNumber, linkedColumnNumber);
        }
        return positions;
    }

    /**
     * <p>parsePositions.</p>
     *
     * @param pos a {@link java.lang.String} object.
     * @param lineNumber a int.
     * @param colNumber a int.
     * @return a {@link java.util.List} object.
     */
    protected List<CsvRange> parsePositions(String pos, int lineNumber, int colNumber){
        // several ranges are present
        if (pos.contains(CsvUtils.PROTEIN_SEPARATOR)){
            String[] ranges = pos.split(CsvUtils.PROTEIN_SEPARATOR);

            List<CsvRange> positions = new ArrayList<CsvRange>(ranges.length);
            for (String p : ranges){
                try {
                    Long posValue = Long.parseLong(p.trim());
                    CsvRange range = new CsvRange(new DefaultPosition(posValue), new DefaultPosition(posValue));
                    range.setSourceLocator(new CsvSourceLocator(lineNumber, -1, colNumber));
                    positions.add(range);
                }
                catch (NumberFormatException e){
                    processInvalidPosition("Invalid range positions: "+
                            e.getMessage(), new DefaultFileSourceContext(new CsvSourceLocator(lineNumber, -1, colNumber)));
                    positions.add(null);
                    return Collections.EMPTY_LIST;
                }
            }
            return positions;
        }
        // only one range
        else{
            try {
                Long posValue = Long.parseLong(pos.trim());
                CsvRange range = new CsvRange(new DefaultPosition(posValue), new DefaultPosition(posValue));
                range.setSourceLocator(new CsvSourceLocator(lineNumber, -1, colNumber));
                return Arrays.asList(range);
            }
            catch (NumberFormatException e){
                processInvalidPosition("Invalid range positions: "+
                        e.getMessage(), new DefaultFileSourceContext(new CsvSourceLocator(lineNumber, -1, colNumber)));
                return Collections.EMPTY_LIST;
            }
        }
    }

    /**
     * <p>createParticipantEvidence.</p>
     *
     * @param csvProtein a {@link psidev.psi.mi.jami.crosslink.extension.CsvProtein} object.
     * @param lineNumber a int.
     * @param columnNumber a int.
     * @param range a {@link psidev.psi.mi.jami.crosslink.extension.CsvRange} object.
     * @return a {@link psidev.psi.mi.jami.crosslink.extension.CsvParticipantEvidence} object.
     */
    protected CsvParticipantEvidence createParticipantEvidence(CsvProtein csvProtein, int lineNumber, int columnNumber, CsvRange range) {
        CsvParticipantEvidence participant = new CsvParticipantEvidence(csvProtein);

        // inti bio role
        participant.setBiologicalRole(CvTermUtils.createUnspecifiedRole());
        // inti exp role
        participant.setExperimentalRole(CvTermUtils.createMICvTerm(Participant.NEUTRAL, Participant.NEUTRAL_MI));
        // init identification method
        participant.getIdentificationMethods().add(CvTermUtils.createMICvTerm(CsvUtils.SEQUENCE_TAG, CsvUtils.SEQUENCE_TAG_MI));

        participant.setSourceLocator(new CsvSourceLocator(lineNumber, -1, columnNumber));

        // range not null, create feature
        if (range != null){
            CsvFeatureEvidence featureEvidence = createCrossLinkFeatureEvidence(range);

            // add feature to participant
            participant.addFeature(featureEvidence);
        }

        return participant;
    }

    /**
     * <p>createCrossLinkFeatureEvidence.</p>
     *
     * @param range a {@link psidev.psi.mi.jami.crosslink.extension.CsvRange} object.
     * @return a {@link psidev.psi.mi.jami.crosslink.extension.CsvFeatureEvidence} object.
     */
    protected CsvFeatureEvidence createCrossLinkFeatureEvidence(CsvRange range) {
        CsvFeatureEvidence featureEvidence = new CsvFeatureEvidence();
        // set type to crosslinker
        featureEvidence.setType(CvTermUtils.createMICvTerm(CsvUtils.CROSS_LINKER, CsvUtils.CROSS_LINKER_MI));
        // set source locator
        featureEvidence.setSourceLocator(range.getSourceLocator());
        // add range
        featureEvidence.getRanges().add(range);
        return featureEvidence;
    }

    /**
     * <p>createExperimentalParticipantPool.</p>
     *
     * @param csvProteins a {@link java.util.List} object.
     * @param lineNumber a int.
     * @param columnNumber a int.
     * @param csvRanges a {@link java.util.List} object.
     * @return a {@link psidev.psi.mi.jami.crosslink.extension.CsvExperimentalParticipantPool} object.
     */
    protected CsvExperimentalParticipantPool createExperimentalParticipantPool(List<CsvProtein> csvProteins, int lineNumber, int columnNumber,
                                                                               List<CsvRange> csvRanges) {
        CsvExperimentalParticipantPool participant = new CsvExperimentalParticipantPool("interactor set "+lineNumber+"-"+columnNumber);

        // inti bio role
        participant.setBiologicalRole(CvTermUtils.createUnspecifiedRole());
        // inti exp role
        participant.setExperimentalRole(CvTermUtils.createMICvTerm(Participant.NEUTRAL, Participant.NEUTRAL_MI));
        // init identification method
        participant.getIdentificationMethods().add(CvTermUtils.createMICvTerm(CsvUtils.SEQUENCE_TAG, CsvUtils.SEQUENCE_TAG_MI));

        participant.setSourceLocator(new CsvSourceLocator(lineNumber, -1, columnNumber));

        for (int i = 0; i < csvProteins.size(); i++){
            CsvProtein prot = csvProteins.get(i);
            CsvRange range = csvRanges.isEmpty() ? null : csvRanges.get(i);
            CsvExperimentalParticipantCandidate candidate = new CsvExperimentalParticipantCandidate(prot);
            candidate.setSourceLocator(prot.getSourceLocator());
            participant.add(candidate);

            // range not null, create feature
            if (range != null){
                CsvFeatureEvidence featureEvidence = createCrossLinkFeatureEvidence(range);

                // add feature to participant
                candidate.addFeature(featureEvidence);
            }
        }

        return participant;
    }

    /**
     * <p>createProteinsFromString.</p>
     *
     * @param protein1 a {@link java.lang.String} object.
     * @param lineNumber a int.
     * @param columnNumber a int.
     * @return a {@link java.util.List} object.
     */
    protected List<CsvProtein> createProteinsFromString(String protein1, int lineNumber, int columnNumber){
        List<CsvProtein> proteins = new ArrayList<CsvProtein>();

        // several proteins are present
        if (protein1.contains(CsvUtils.PROTEIN_SEPARATOR)){
            String[] proteinIds = protein1.split(CsvUtils.PROTEIN_SEPARATOR);

            for (String proteinId : proteinIds){
                // we have identifier and name
                CsvProtein csvProtein = createProteinFromNameAndIdentifier(proteinId, lineNumber, columnNumber);
                if (csvProtein != null){
                    proteins.add(csvProtein);
                }
            }
        }
        // only one entry
        else{
            // we have identifier and name
            CsvProtein csvProtein = createProteinFromNameAndIdentifier(protein1, lineNumber, columnNumber);
            if (csvProtein != null){
                proteins.add(csvProtein);
            }
        }

        return proteins;
    }

    /**
     * <p>createProteinFromNameAndIdentifier.</p>
     *
     * @param protein a {@link java.lang.String} object.
     * @param lineNumber a int.
     * @param columnNumber a int.
     * @return a {@link psidev.psi.mi.jami.crosslink.extension.CsvProtein} object.
     */
    protected CsvProtein createProteinFromNameAndIdentifier(String protein, int lineNumber, int columnNumber){
        // we have identifier and name
        if (protein.contains(CsvUtils.XREF_SEPARATOR)){
            String[] identifiers = protein.split("\\"+CsvUtils.XREF_SEPARATOR);
            if (identifiers.length != 3){
                processProteinIdentifiersError(identifiers, lineNumber, columnNumber);
                return null;
            }
            else{
                CsvProtein prot = new CsvProtein(identifiers[2], new CsvXref(CvTermUtils.createUniprotkbDatabase(), identifiers[1].trim(),
                        CvTermUtils.createIdentityQualifier()));
                prot.setSourceLocator(new CsvSourceLocator(lineNumber, -1, columnNumber));
                return prot;
            }
        }
        // the name and identifier will be the same
        else {
            CsvProtein prot = new CsvProtein(protein, new CsvXref(CvTermUtils.createUniprotkbDatabase(), protein.trim(),
                    CvTermUtils.createIdentityQualifier()));
            prot.setSourceLocator(new CsvSourceLocator(lineNumber, -1, columnNumber));
            return prot;
        }
    }

    /**
     * <p>instantiateInteractionEvidence.</p>
     *
     * @param linePosition a int.
     * @param bait a {@link java.lang.String} object.
     * @return a T object.
     */
    protected abstract T instantiateInteractionEvidence(int linePosition, String bait);

    /**
     * <p>processMismatchPeptidePositions.</p>
     *
     * @param peptidePositions a {@link java.util.List} object.
     * @param linkedPositions a {@link java.util.List} object.
     */
    protected void processMismatchPeptidePositions(List<CsvRange> peptidePositions, List<CsvRange> linkedPositions){
        if (this.parserListener != null){
            this.parserListener.onMismatchBetweenPeptideAndLinkedPositions(peptidePositions, linkedPositions);
        }
    }

    /**
     * <p>processMismatchProteinPositions.</p>
     *
     * @param rangePositions a {@link java.util.List} object.
     * @param proteins a {@link java.util.List} object.
     */
    protected void processMismatchProteinPositions(List<CsvRange> rangePositions, List<CsvProtein> proteins){
        if (this.parserListener != null){
            this.parserListener.onMismatchBetweenRangePositionsAndProteins(rangePositions, proteins);
        }
    }

    /**
     * <p>processInvalidPosition.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param context a {@link psidev.psi.mi.jami.datasource.FileSourceContext} object.
     */
    protected void processInvalidPosition(String message, FileSourceContext context){
        if (this.parserListener != null){
            this.parserListener.onInvalidPosition(message, context);
        }
    }

    /**
     * <p>processProteinIdentifiersError.</p>
     *
     * @param identifiers an array of {@link java.lang.String} objects.
     * @param lineNumber a int.
     * @param columnNumber a int.
     */
    protected void processProteinIdentifiersError(String[] identifiers, int lineNumber, int columnNumber){
        if (this.parserListener != null){
            this.parserListener.onInvalidProteinIdentifierSyntax(identifiers, lineNumber, columnNumber);
        }
    }

    /**
     * <p>processNoProtein1Error.</p>
     *
     * @param lineNumber a int.
     */
    protected void processNoProtein1Error(int lineNumber){
        if (this.parserListener != null){
            this.parserListener.onMissingProtein1Column(lineNumber);
        }
    }

    /**
     * <p>initialiseColumnNames.</p>
     *
     * @param data a {@link java.util.List} object.
     */
    public void initialiseColumnNames(List<String> data){
        currentLineIndex++;
        columnsIndex = new HashMap<Integer, CrossLinkCSVColumns>(CrossLinkCSVColumns.values().length);
        int index = 0;
        for (String name : data){
            CrossLinkCSVColumns colName = CrossLinkCSVColumns.convertFromString(name.trim());
            if (colName != null){
                columnsIndex.put(index, colName);
            }
            index++;
        }
    }

}
