package psidev.psi.mi.jami.model.impl;

import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import psidev.psi.mi.jami.utils.collection.AbstractListHavingProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Default implementation for InteractionEvidence
 *
 * Notes: The equals and hashcode methods have NOT been overridden because the InteractionEvidence object is a complex object.
 * To compare InteractionEvidence objects, you can use some comparators provided by default:
 * - DefaultInteractionEvidenceComparator
 * - UnambiguousInteractionEvidenceComparator
 * - DefaultCuratedInteractionEvidenceComparator
 * - UnambiguousCuratedInteractionEvidenceComparator
 * - DefaultExactInteractionEvidenceComparator
 * - UnambiguousExactInteractionEvidenceComparator
 * - DefaultCuratedExactInteractionEvidenceComparator
 * - UnambiguousCuratedExactInteractionEvidenceComparator
 * - AbstractInteractionBaseComparator
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>05/02/13</pre>
 */
public class DefaultInteractionEvidence extends AbstractInteraction<ParticipantEvidence> implements InteractionEvidence {

    private Xref imexId;
    private Experiment experiment;
    private String availability;
    private Collection<Parameter> parameters;
    private boolean isInferred = false;
    private Collection<Confidence> confidences;
    private boolean isNegative;

    private Collection<VariableParameterValueSet> variableParameterValueSets;

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param experiment a {@link psidev.psi.mi.jami.model.Experiment} object.
     */
    public DefaultInteractionEvidence(Experiment experiment) {
        super();

        this.experiment = experiment;
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param experiment a {@link psidev.psi.mi.jami.model.Experiment} object.
     * @param shortName a {@link java.lang.String} object.
     */
    public DefaultInteractionEvidence(Experiment experiment, String shortName) {
        super(shortName);
        this.experiment = experiment;
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param experiment a {@link psidev.psi.mi.jami.model.Experiment} object.
     * @param shortName a {@link java.lang.String} object.
     * @param source a {@link psidev.psi.mi.jami.model.Source} object.
     */
    public DefaultInteractionEvidence(Experiment experiment, String shortName, Source source) {
        super(shortName, source);
        this.experiment = experiment;
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param experiment a {@link psidev.psi.mi.jami.model.Experiment} object.
     * @param shortName a {@link java.lang.String} object.
     * @param type a {@link psidev.psi.mi.jami.model.CvTerm} object.
     */
    public DefaultInteractionEvidence(Experiment experiment, String shortName, CvTerm type) {
        super(shortName, type);
        this.experiment = experiment;
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param experiment a {@link psidev.psi.mi.jami.model.Experiment} object.
     * @param imexId a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    public DefaultInteractionEvidence(Experiment experiment, Xref imexId) {
        super();
        this.experiment = experiment;
        getXrefs().add(imexId);
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param experiment a {@link psidev.psi.mi.jami.model.Experiment} object.
     * @param shortName a {@link java.lang.String} object.
     * @param imexId a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    public DefaultInteractionEvidence(Experiment experiment, String shortName, Xref imexId) {
        super(shortName);
        this.experiment = experiment;
        getXrefs().add(imexId);
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param experiment a {@link psidev.psi.mi.jami.model.Experiment} object.
     * @param shortName a {@link java.lang.String} object.
     * @param source a {@link psidev.psi.mi.jami.model.Source} object.
     * @param imexId a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    public DefaultInteractionEvidence(Experiment experiment, String shortName, Source source, Xref imexId) {
        super(shortName, source);
        this.experiment = experiment;
        getXrefs().add(imexId);
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param experiment a {@link psidev.psi.mi.jami.model.Experiment} object.
     * @param shortName a {@link java.lang.String} object.
     * @param type a {@link psidev.psi.mi.jami.model.CvTerm} object.
     * @param imexId a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    public DefaultInteractionEvidence(Experiment experiment, String shortName, CvTerm type, Xref imexId) {
        super(shortName, type);
        this.experiment = experiment;
        getXrefs().add(imexId);
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param imexId a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    public DefaultInteractionEvidence(Xref imexId) {
        super();
        getXrefs().add(imexId);
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param shortName a {@link java.lang.String} object.
     * @param imexId a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    public DefaultInteractionEvidence(String shortName, Xref imexId) {
        super(shortName);
        getXrefs().add(imexId);
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param shortName a {@link java.lang.String} object.
     * @param source a {@link psidev.psi.mi.jami.model.Source} object.
     * @param imexId a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    public DefaultInteractionEvidence(String shortName, Source source, Xref imexId) {
        super(shortName, source);
        getXrefs().add(imexId);
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param shortName a {@link java.lang.String} object.
     * @param type a {@link psidev.psi.mi.jami.model.CvTerm} object.
     * @param imexId a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    public DefaultInteractionEvidence(String shortName, CvTerm type, Xref imexId) {
        super(shortName, type);
        getXrefs().add(imexId);
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     */
    public DefaultInteractionEvidence() {
        super();
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param shortName a {@link java.lang.String} object.
     */
    public DefaultInteractionEvidence(String shortName) {
        super(shortName);
    }

    /**
     * <p>Constructor for DefaultInteractionEvidence.</p>
     *
     * @param shortName a {@link java.lang.String} object.
     * @param type a {@link psidev.psi.mi.jami.model.CvTerm} object.
     */
    public DefaultInteractionEvidence(String shortName, CvTerm type) {
        super(shortName, type);
    }

    /**
     * <p>initialiseExperimentalConfidences</p>
     */
    protected void initialiseExperimentalConfidences(){
        this.confidences = new ArrayList<Confidence>();
    }

    /**
     * <p>initialiseExperimentalConfidencesWith</p>
     *
     * @param confidences a {@link java.util.Collection} object.
     */
    protected void initialiseExperimentalConfidencesWith(Collection<Confidence> confidences){
        if (confidences == null){
            this.confidences = Collections.EMPTY_LIST;
        }
        else {
            this.confidences = confidences;
        }
    }

    /**
     * <p>initialiseVariableParameterValueSets</p>
     */
    protected void initialiseVariableParameterValueSets(){
        this.variableParameterValueSets = new ArrayList<VariableParameterValueSet>();
    }

    /**
     * <p>initialiseVariableParameterValueSetsWith</p>
     *
     * @param variableValues a {@link java.util.Collection} object.
     */
    protected void initialiseVariableParameterValueSetsWith(Collection<VariableParameterValueSet> variableValues){
        if (variableValues == null){
            this.variableParameterValueSets = Collections.EMPTY_LIST;
        }
        else {
            this.variableParameterValueSets = variableValues;
        }
    }

    /**
     * <p>initialiseExperimentalParameters</p>
     */
    protected void initialiseExperimentalParameters(){
        this.parameters = new ArrayList<Parameter>();
    }

    /**
     * <p>initialiseExperimentalParametersWith</p>
     *
     * @param parameters a {@link java.util.Collection} object.
     */
    protected void initialiseExperimentalParametersWith(Collection<Parameter> parameters){
        if (parameters == null){
            this.parameters = Collections.EMPTY_LIST;
        }
        else {
            this.parameters = parameters;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseXrefs() {
        initialiseXrefsWith(new ExperimentalInteractionXrefList());
    }

    /**
     * <p>Getter for the field <code>imexId</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getImexId() {
        return this.imexId != null ? this.imexId.getId() : null;
    }

    /** {@inheritDoc} */
    public void assignImexId(String identifier) {
        // add new imex if not null
        if (identifier != null){
            ExperimentalInteractionXrefList interactionXrefs = (ExperimentalInteractionXrefList) getXrefs();
            CvTerm imexDatabase = CvTermUtils.createImexDatabase();
            CvTerm imexPrimaryQualifier = CvTermUtils.createImexPrimaryQualifier();
            // first remove old doi if not null
            if (this.imexId != null){
                interactionXrefs.removeOnly(this.imexId);
            }
            this.imexId = new DefaultXref(imexDatabase, identifier, imexPrimaryQualifier);
            interactionXrefs.addOnly(this.imexId);
        }
        else {
            throw new IllegalArgumentException("The imex id has to be non null.");
        }
    }

    /**
     * <p>Getter for the field <code>experiment</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.model.Experiment} object.
     */
    public Experiment getExperiment() {
        return this.experiment;
    }

    /** {@inheritDoc} */
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    /** {@inheritDoc} */
    public void setExperimentAndAddInteractionEvidence(Experiment experiment) {
        if (this.experiment != null){
            this.experiment.removeInteractionEvidence(this);
        }

        if (experiment != null){
            experiment.addInteractionEvidence(this);
        }
    }

    /**
     * <p>getVariableParameterValues</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    public Collection<VariableParameterValueSet> getVariableParameterValues() {

        if (variableParameterValueSets == null){
            initialiseVariableParameterValueSets();
        }
        return this.variableParameterValueSets;
    }

    /**
     * <p>Getter for the field <code>confidences</code>.</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    public Collection<Confidence> getConfidences() {
        if (confidences == null){
            initialiseExperimentalConfidences();
        }
        return this.confidences;
    }

    /**
     * <p>Getter for the field <code>availability</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAvailability() {
        return this.availability;
    }

    /** {@inheritDoc} */
    public void setAvailability(String availability) {
        this.availability = availability;
    }

    /**
     * <p>isNegative</p>
     *
     * @return a boolean.
     */
    public boolean isNegative() {
        return this.isNegative;
    }

    /** {@inheritDoc} */
    public void setNegative(boolean negative) {
        this.isNegative = negative;
    }

    /**
     * <p>Getter for the field <code>parameters</code>.</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    public Collection<Parameter> getParameters() {
        if (parameters == null){
            initialiseExperimentalParameters();
        }
        return this.parameters;
    }

    /**
     * <p>isInferred</p>
     *
     * @return a boolean.
     */
    public boolean isInferred() {
        return this.isInferred;
    }

    /** {@inheritDoc} */
    public void setInferred(boolean inferred) {
        this.isInferred = inferred;
    }

    /**
     * <p>processAddedXrefEvent</p>
     *
     * @param added a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    protected void processAddedXrefEvent(Xref added) {

        // the added identifier is imex and the current imex is not set
        if (imexId == null && XrefUtils.isXrefFromDatabase(added, Xref.IMEX_MI, Xref.IMEX)){
            // the added xref is imex-primary
            if (XrefUtils.doesXrefHaveQualifier(added, Xref.IMEX_PRIMARY_MI, Xref.IMEX_PRIMARY)){
                imexId = added;
            }
        }
    }

    /**
     * <p>processRemovedXrefEvent</p>
     *
     * @param removed a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    protected void processRemovedXrefEvent(Xref removed) {
        // the removed identifier is pubmed
        if (imexId != null && imexId.equals(removed)){
            Collection<Xref> existingImex = XrefUtils.collectAllXrefsHavingDatabaseAndQualifier(getXrefs(), Xref.IMEX_MI, Xref.IMEX, Xref.IMEX_PRIMARY_MI, Xref.IMEX_PRIMARY);
            if (!existingImex.isEmpty()){
                imexId = existingImex.iterator().next();
            }
        }
    }

    /**
     * <p>clearPropertiesLinkedToXrefs</p>
     */
    protected void clearPropertiesLinkedToXrefs() {
        imexId = null;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Interaction evidence: "+(getImexId() != null ? getImexId() :
                (getShortName() != null ? getShortName()+", " : "") + (getInteractionType() != null ? getInteractionType().toString() : ""));
    }

    /**
     * Experimental interaction Xref list
     */
    private class ExperimentalInteractionXrefList extends AbstractListHavingProperties<Xref> {
        public ExperimentalInteractionXrefList(){
            super();
        }

        @Override
        protected void processAddedObjectEvent(Xref added) {

            processAddedXrefEvent(added);
        }

        @Override
        protected void processRemovedObjectEvent(Xref removed) {
            processRemovedXrefEvent(removed);
        }

        @Override
        protected void clearProperties() {
            clearPropertiesLinkedToXrefs();
        }
    }
}
