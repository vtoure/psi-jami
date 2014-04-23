package psidev.psi.mi.jami.xml.model;

import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceLocator;
import psidev.psi.mi.jami.model.Experiment;
import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.jami.xml.model.extension.*;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ExperimentalEntry implementation for JAXB read only
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>07/11/13</pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ExperimentalEntry extends AbstractEntry<InteractionEvidence>{
    private JAXBExperimentsWrapper experimentsWrapper;
    private JAXBAvailabilitiesWrapper availabilitiesWrapper;

    @XmlLocation
    @XmlTransient
    private Locator locator;

    public List<Experiment> getExperiments(){
        return this.experimentsWrapper != null ? this.experimentsWrapper.experiments : Collections.EMPTY_LIST;
    }
    @XmlElement(name = "source", type = XmlSource.class)
    public void setJAXBSource(ExtendedPsiXmlSource source) {
        super.setSource(source);
    }

    @XmlElement(name = "availabilityList")
    public void setJAXBAvailabilityWrapper(JAXBAvailabilitiesWrapper wrapper) {
        this.availabilitiesWrapper = wrapper;
    }

    @XmlElement(name = "experimentList")
    public void setJAXBExperimentWrapper(JAXBExperimentsWrapper wrapper){
        this.experimentsWrapper = wrapper;
    }

    @XmlElement(name = "interactorList")
    public void setJAXBInteractorsWrapper(JAXBInteractorsWrapper wrapper){
        super.setInteractorsWrapper(wrapper);
    }

    @XmlElement(name = "interactionList", required = true)
    public void setJAXBInteractionsWrapper(JAXBInteractionsWrapper wrapper){
        super.setInteractionsWrapper(wrapper);
    }

    @XmlElement(name = "annotationList")
    public void setJAXBAnnotationWrapper(JAXBAnnotationsWrapper wrapper) {
        super.setAnnotationsWrapper(wrapper);
    }

    @Override
    public FileSourceLocator getSourceLocator() {
        if (super.getSourceLocator() == null && locator != null){
            super.setSourceLocator(new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), null));
        }
        return super.getSourceLocator();
    }

    @Override
    public void setSourceLocator(FileSourceLocator sourceLocator) {
        if (sourceLocator == null){
            super.setSourceLocator(null);
        }
        else{
            super.setSourceLocator(new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null));
        }
    }

    @Override
    protected void initialiseAvailabilities() {
        super.initialiseAvailabilitiesWith(this.availabilitiesWrapper != null ? this.availabilitiesWrapper.availabilities : null);
    }

    //////////////////////////////// class wrapper

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="entryAvailabilitiesWrapper")
    public static class JAXBAvailabilitiesWrapper implements Locatable, FileSourceContext {
        private List<AbstractAvailability> availabilities;
        private PsiXmLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;

        public JAXBAvailabilitiesWrapper(){
            initialiseAvailabilities();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else{
                this.sourceLocator = new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        protected void initialiseAvailabilities(){
            availabilities = new ArrayList<AbstractAvailability>();
        }

        @XmlElement(type=Availability.class, name="availability", required = true)
        public List<AbstractAvailability> getJAXBAvailabilities() {
            return availabilities;
        }

        @Override
        public String toString() {
            return "Entry availability List: "+(getSourceLocator() != null ? getSourceLocator().toString():super.toString());
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="experimentsWrapper")
    public static class JAXBExperimentsWrapper implements Locatable, FileSourceContext {
        private List<Experiment> experiments;
        private PsiXmLocator sourceLocator;
        @XmlLocation
        @XmlTransient
        private Locator locator;

        public JAXBExperimentsWrapper(){
            initialiseExperiments();
        }

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        public FileSourceLocator getSourceLocator() {
            if (sourceLocator == null && locator != null){
                sourceLocator = new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
            }
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else{
                this.sourceLocator = new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        protected void initialiseExperiments(){
            experiments = new ArrayList<Experiment>();
        }

        @XmlElement(type=XmlExperiment.class, name="experimentDescription", required = true)
        public List<Experiment> getJAXBExperiments() {
            return experiments;
        }

        @Override
        public String toString() {
            return "Entry experiment List: "+(getSourceLocator() != null ? getSourceLocator().toString():super.toString());
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="interactionEvidencesWrapper")
    public static class JAXBInteractionsWrapper extends AbstractEntry.JAXBInteractionsWrapper<InteractionEvidence>{

        public JAXBInteractionsWrapper(){
            super();
        }

        @XmlElement(type=XmlInteractionEvidence.class, name="interaction", required = true)
        public List<InteractionEvidence> getJAXBInteractions() {
            return super.getJAXBInteractions();
        }
    }
}
