package psidev.psi.mi.jami.xml.extension;

import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;
import psidev.psi.mi.jami.datasource.FileSourceLocator;
import psidev.psi.mi.jami.model.*;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Xml implementation of ParticipantEvidence
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>08/10/13</pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "participantEvidence", propOrder = {
        "JAXBNames",
        "JAXBXref",
        "JAXBInteractionRef",
        "JAXBInteractor",
        "JAXBInteractorRef",
        "JAXBParticipantIdentificationMethods",
        "JAXBBiologicalRole",
        "JAXBExperimentalRoles",
        "JAXBExperimentalPreparations",
        "JAXBExperimentalInteractors",
        "JAXBFeatures",
        "JAXBHostOrganisms",
        "JAXBConfidences",
        "JAXBParameters",
        "JAXBAttributes"
})
public class XmlParticipantEvidence extends AbstractXmlParticipant<InteractionEvidence, FeatureEvidence> implements ParticipantEvidence{
    private Collection<CvTerm> identificationMethods;
    private Collection<CvTerm> experimentalPreparations;
    private Collection<Confidence> confidences;
    private Collection<Parameter> parameters;

    private ArrayList<CvTerm> experimentalRoles;
    private ArrayList<ExperimentalInteractor> experimentalInteractors;
    private ArrayList<Organism> hostOrganisms;
    private ArrayList<ExperimentalCvTerm> originalIdentificationMethods;
    @XmlLocation
    @XmlTransient
    protected Locator locator;

    private boolean initialisedMethods = false;

    private XmlInteractionEvidence originalInteraction;

    public XmlParticipantEvidence() {
    }

    public XmlParticipantEvidence(Interactor interactor) {
        super(interactor);
    }

    public XmlParticipantEvidence(Interactor interactor, CvTerm bioRole) {
        super(interactor, bioRole);
    }

    public XmlParticipantEvidence(Interactor interactor, Stoichiometry stoichiometry) {
        super(interactor, stoichiometry);
    }

    public XmlParticipantEvidence(Interactor interactor, CvTerm bioRole, Stoichiometry stoichiometry) {
        super(interactor, bioRole, stoichiometry);
    }

    protected void initialiseExperimentalPreparations() {
        this.experimentalPreparations = new ArrayList<CvTerm>();
    }

    protected void initialiseConfidences() {
        this.confidences = new ArrayList<Confidence>();
    }

    protected void initialiseParameters() {
        this.parameters = new ArrayList<Parameter>();
    }

    protected void setOriginalXmlInteraction(XmlInteractionEvidence i){
        this.originalInteraction = i;
        setInteraction(i);
    }

    /**
     * This method should only be called when we have loaded all references because the parsing should not call this method
     */
    protected void initialiseIdentificationMethods(){
        Collection<Experiment> expToIgnore = Collections.EMPTY_LIST;

        if (this.identificationMethods == null){
            this.identificationMethods = new ArrayList<CvTerm>();
        }

        if (originalIdentificationMethods != null && !originalIdentificationMethods.isEmpty()){
            expToIgnore = new ArrayList<Experiment>();
            for (ExperimentalCvTerm part : this.originalIdentificationMethods){
                if (!part.getExperiments().isEmpty()){
                    expToIgnore.addAll(part.getExperiments());
                }
            }
            this.originalIdentificationMethods = null;
        }

        if (originalInteraction != null){
            List<XmlExperiment> originalExperiments = originalInteraction.getOriginalExperiments();
            if (originalExperiments != null && !originalExperiments.isEmpty()){
                for (XmlExperiment exp : originalExperiments){
                    if (exp.getJAXBParticipantIdentificationMethod() != null && !expToIgnore.contains(exp)){
                        this.identificationMethods.add(exp.getJAXBParticipantIdentificationMethod());
                    }
                }
            }
        }

        initialisedMethods = true;
    }

    protected void initialiseIdentificationMethodsWith(Collection<CvTerm> methods){
        if (methods == null){
            this.identificationMethods = Collections.EMPTY_LIST;
        }
        else {
            this.identificationMethods = methods;
        }
    }

    protected void initialiseExperimentalPreparationsWith(Collection<CvTerm> expPreparations) {
        if (expPreparations == null){
            this.experimentalPreparations = Collections.EMPTY_LIST;
        }
        else {
            this.experimentalPreparations = expPreparations;
        }
    }

    protected void initialiseConfidencesWith(Collection<Confidence> confidences) {
        if (confidences == null){
            this.confidences = Collections.EMPTY_LIST;
        }
        else {
            this.confidences = confidences;
        }
    }

    protected void initialiseParametersWith(Collection<Parameter> parameters) {
        if (parameters == null){
            this.parameters = Collections.EMPTY_LIST;
        }
        else {
            this.parameters = parameters;
        }
    }

    public CvTerm getExperimentalRole() {
        if (this.experimentalRoles == null){
            this.experimentalRoles = new ArrayList<CvTerm>();
        }
        if (this.experimentalRoles.isEmpty()){
            this.experimentalRoles.add(new XmlCvTerm(Participant.UNSPECIFIED_ROLE, Participant.UNSPECIFIED_ROLE_MI));
        }
        return this.experimentalRoles.get(0);
    }

    public void setExperimentalRole(CvTerm expRole) {
        if (this.experimentalRoles == null && expRole != null){
            this.experimentalRoles = new ArrayList<CvTerm>();
            this.experimentalRoles.add(expRole);
        }
        else if (this.experimentalRoles != null){
            if (!this.experimentalRoles.isEmpty() && expRole == null){
                this.experimentalRoles.remove(0);
            }
            else if (expRole != null){
                this.experimentalRoles.remove(0);
                this.experimentalRoles.add(0, expRole);
            }
        }
    }

    public Collection<CvTerm> getIdentificationMethods() {
        if (!initialisedMethods){
            initialiseIdentificationMethods();
        }
        return this.identificationMethods;
    }

    public Collection<CvTerm> getExperimentalPreparations() {
        if (experimentalPreparations == null){
            initialiseExperimentalPreparations();
        }
        return this.experimentalPreparations;
    }

    public Organism getExpressedInOrganism() {
        return (this.hostOrganisms != null && !this.hostOrganisms.isEmpty())? this.hostOrganisms.iterator().next() : null;
    }

    public void setExpressedInOrganism(Organism organism) {
        if (this.hostOrganisms == null && organism != null){
            this.hostOrganisms = new ArrayList<Organism>();
            this.hostOrganisms.add(organism);
        }
        else if (this.hostOrganisms != null){
            if (!this.hostOrganisms.isEmpty() && organism == null){
                this.hostOrganisms.remove(0);
            }
            else if (organism != null){
                this.hostOrganisms.remove(0);
                this.hostOrganisms.add(0, organism);
            }
        }
    }

    public Collection<Confidence> getConfidences() {
        if (confidences == null){
            initialiseConfidences();
        }
        return this.confidences;
    }

    public Collection<Parameter> getParameters() {
        if (parameters == null){
            initialiseParameters();
        }
        return this.parameters;
    }

    @Override
    public String toString() {
        return super.toString() + (getExperimentalRole() != null ? ", " + getExperimentalRole().toString() : "") + (getExpressedInOrganism() != null ? ", " + getExpressedInOrganism().toString() : "");
    }

    @Override
    @XmlElement(name = "names")
    public NamesContainer getJAXBNames() {
        return super.getJAXBNames();
    }

    @Override
    @XmlElement(name = "xref")
    public XrefContainer getJAXBXref() {
        return super.getJAXBXref();
    }

    @Override
    @XmlElement(name = "interactionRef")
    public Integer getJAXBInteractionRef() {
        return super.getJAXBInteractionRef();
    }

    @Override
    @XmlElement(name = "interactorRef")
    public Integer getJAXBInteractorRef() {
        return super.getJAXBInteractorRef();
    }

    @Override
    @XmlElement(name = "biologicalRole", type = XmlCvTerm.class)
    public CvTerm getJAXBBiologicalRole() {
        return super.getJAXBBiologicalRole();
    }

    @Override
    @XmlElementWrapper(name="featureList")
    @XmlElements({ @XmlElement(type=XmlFeatureEvidence.class, name="feature", required = true)})
    public ArrayList<FeatureEvidence> getJAXBFeatures() {
        return super.getJAXBFeatures();
    }

    @Override
    @XmlElement(name = "interactor", type = XmlInteractor.class)
    public Interactor getJAXBInteractor() {
        return super.getJAXBInteractor();
    }

    @Override
    @XmlAttribute(name = "id", required = true)
    public int getJAXBId() {
        return super.getJAXBId();
    }

    @Override
    @XmlElementWrapper(name="attributeList")
    @XmlElements({ @XmlElement(type=XmlAnnotation.class, name="attribute", required = true)})
    public ArrayList<Annotation> getJAXBAttributes() {
        return super.getJAXBAttributes();
    }

    /**
     * Gets the value of the participantIdentificationMethodList property.
     *
     * @return
     *     possible object is
     *     {@link XmlCvTerm }
     *
     */
    @XmlElementWrapper(name="participantIdentificationMethodList")
    @XmlElements({ @XmlElement(type=ExperimentalCvTerm.class, name="participantIdentificationMethod", required = true)})
    public ArrayList<CvTerm> getJAXBParticipantIdentificationMethods() {
        if (this.identificationMethods != null && this.identificationMethods.isEmpty()){
            this.identificationMethods = null;
            return null;
        }
        return new ArrayList<CvTerm>(this.identificationMethods);
    }

    /**
     * Sets the value of the participantIdentificationMethodList property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlCvTerm }
     *
     */
    public void setJAXBParticipantIdentificationMethods(ArrayList<ExperimentalCvTerm> value) {
        this.originalIdentificationMethods = value;
        if (value != null && !value.isEmpty()){
            if (this.identificationMethods == null){
                this.identificationMethods = new ArrayList<CvTerm>();
            }
            this.identificationMethods.addAll(value);
        }
    }

    /**
     * Gets the value of the experimentalRoleList property.
     *
     * @return
     *     possible object is
     *     {@link XmlCvTerm }
     *
     */
    @XmlElementWrapper(name="experimentalRoleList")
    @XmlElements({ @XmlElement(type=ExperimentalCvTerm.class, name="experimentalRole", required = true)})
    public ArrayList<CvTerm> getJAXBExperimentalRoles() {
        if (this.experimentalRoles != null && this.experimentalRoles.isEmpty()){
            this.experimentalRoles = null;
        }
        return this.experimentalRoles;
    }

    /**
     * Sets the value of the experimentalRoleList property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlCvTerm }
     *
     */
    public void setJAXBExperimentalRoles(ArrayList<CvTerm> value) {
        this.experimentalRoles = value;
    }

    /**
     * Gets the value of the experimentalRoleList property.
     *
     * @return
     *     possible object is
     *     {@link XmlCvTerm }
     *
     */
    @XmlElementWrapper(name="experimentalPreparationList")
    @XmlElements({ @XmlElement(type=ExperimentalCvTerm.class, name="experimentalPreparation", required = true)})
    public ArrayList<CvTerm> getJAXBExperimentalPreparations() {
        if (this.experimentalPreparations != null && this.experimentalPreparations.isEmpty()){
            return null;
        }
        return new ArrayList<CvTerm>(this.experimentalPreparations);
    }

    /**
     * Sets the value of the experimentalRoleList property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlCvTerm }
     *
     */
    public void setJAXBExperimentalPreparations(ArrayList<ExperimentalCvTerm> value) {
        getExperimentalPreparations().clear();
        if (value != null && !value.isEmpty()){
            this.experimentalRoles.addAll(value);

        }
    }

    /**
     * Gets the value of the experimentalInteractorList property.
     *
     * @return
     *     possible object is
     *     {@link ExperimentalInteractor }
     *
     */
    @XmlElementWrapper(name="experimentalInteractorList")
    @XmlElements({ @XmlElement(type=ExperimentalInteractor.class, name="experimentalInteractor", required = true)})
    public ArrayList<ExperimentalInteractor> getJAXBExperimentalInteractors() {
        return this.experimentalInteractors;
    }

    /**
     * Sets the value of the experimentalRoleList property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlCvTerm }
     *
     */
    public void setJAXBExperimentalInteractors(ArrayList<ExperimentalInteractor> value) {
        this.experimentalInteractors = value;
    }

    /**
     * Gets the value of the hostOrganismList property.
     *
     * @return
     *     possible object is
     *     {@link HostOrganism }
     *
     */
    @XmlElementWrapper(name="hostOrganismList")
    @XmlElements({ @XmlElement(type=HostOrganism.class, name="hostOrganism", required = true)})
    public ArrayList<Organism> getJAXBHostOrganisms() {
        if (this.hostOrganisms != null && this.hostOrganisms.isEmpty()){
            this.hostOrganisms = null;
        }
        return this.hostOrganisms;
    }

    /**
     * Sets the value of the hostOrganismList property.
     *
     * @param value
     *     allowed object is
     *     {@link HostOrganism }
     *
     */
    public void setJAXBHostOrganisms(ArrayList<Organism> value) {
        this.hostOrganisms = value;
    }

    /**
     * Gets the value of the parameterList property.
     *
     * @return
     *     possible object is
     *     {@link XmlParameter }
     *
     */
    @XmlElementWrapper(name="parameterList")
    @XmlElements({ @XmlElement(type=XmlParameter.class, name="parameter", required = true)})
    public ArrayList<Parameter> getJAXBParameters() {
        if (this.parameters != null && this.parameters.isEmpty()){
            this.parameters = null;
        }
        return new ArrayList<Parameter>(this.parameters);
    }

    /**
     * Sets the value of the parameterList property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlParameter }
     *
     */
    public void setJAXBParameters(ArrayList<XmlParameter> value) {
        getParameters().clear();
        if (value != null && !value.isEmpty()){
            getParameters().addAll(value);
        }
    }

    /**
     * Gets the value of the confidenceList property.
     *
     * @return
     *     possible object is
     *     {@link XmlConfidence }
     *
     */
    @XmlElementWrapper(name="confidenceList")
    @XmlElements({ @XmlElement(type=XmlConfidence.class, name="confidence", required = true)})
    public ArrayList<Confidence> getJAXBConfidences() {
        if (this.confidences != null && this.confidences.isEmpty()){
            this.confidences = null;
        }
        return new ArrayList<Confidence>(this.confidences);
    }

    /**
     * Sets the value of the confidenceList property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlConfidence }
     *
     */
    public void setJAXBConfidences(ArrayList<XmlConfidence> value) {
        getConfidences().clear();
        if (value != null && !value.isEmpty()){
            getConfidences().addAll(value);
        }
    }

    @Override
    public FileSourceLocator getSourceLocator() {
        if (super.getSourceLocator() == null && locator != null){
            super.setSourceLocator(new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), getJAXBId()));
        }
        return super.getSourceLocator();
    }

    @Override
    public void setSourceLocator(FileSourceLocator sourceLocator) {
        if (sourceLocator == null){
            super.setSourceLocator(null);
        }
        else{
            super.setSourceLocator(new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), getJAXBId()));
        }
    }

    @Override
    public void setJAXBFeatures(ArrayList<FeatureEvidence> value) {
        removeAllFeatures(getFeatures());
        if (value != null && !value.isEmpty()){
            addAllJAXBFeatures(value);
        }
    }

    protected XmlInteractionEvidence getOriginalInteraction() {
        return originalInteraction;
    }

    private void addAllJAXBFeatures(Collection<? extends FeatureEvidence> features) {
        if (features != null){
            for (FeatureEvidence feature : features){
                addJAXBFeature((XmlFeatureEvidence) feature);
            }
        }
    }

    private void addJAXBFeature(XmlFeatureEvidence feature) {

        if (feature != null){
            if (getFeatures().add(feature)){
                feature.setOriginalParticipant(this);
            }
        }
    }
}