package psidev.psi.mi.jami.xml.extension;

import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;
import psidev.psi.mi.jami.datasource.FileSourceLocator;
import psidev.psi.mi.jami.model.*;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * XML implementation of ModelledParticipant
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>08/10/13</pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "modelledParticipant", propOrder = {
        "JAXBNames",
        "JAXBXref",
        "JAXBInteractionRef",
        "JAXBInteractor",
        "JAXBInteractorRef",
        "JAXBBiologicalRole",
        "JAXBFeatures",
        "JAXBAttributes"
})
public class XmlModelledParticipant extends AbstractXmlParticipant<ModelledInteraction, ModelledFeature> implements ModelledParticipant{

    @XmlLocation
    @XmlTransient
    protected Locator locator;

    public XmlModelledParticipant() {
    }

    public XmlModelledParticipant(Interactor interactor) {
        super(interactor);
    }

    public XmlModelledParticipant(Interactor interactor, CvTerm bioRole) {
        super(interactor, bioRole);
    }

    public XmlModelledParticipant(Interactor interactor, CvTerm bioRole, Stoichiometry stoichiometry) {
        super(interactor, bioRole, stoichiometry);
    }

    public XmlModelledParticipant(Interactor interactor, Stoichiometry stoichiometry) {
        super(interactor, stoichiometry);
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
    @XmlElement(name = "biologicalRole")
    public CvTerm getJAXBBiologicalRole() {
        return super.getJAXBBiologicalRole();
    }

    @Override
    @XmlElementWrapper(name="featureList")
    @XmlElements({ @XmlElement(type=XmlModelledFeature.class, name="feature", required = true)})
    public ArrayList<ModelledFeature> getJAXBFeatures() {
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

    @Override
    public void setSourceLocator(FileSourceLocator sourceLocator) {
        super.setSourceLocator(sourceLocator);
        this.locator = sourceLocation();
    }

    @Override
    public void setSourceLocation(Locator sourceLocator) {
        super.setSourceLocation(sourceLocator);
        this.locator = sourceLocation();
    }
}
