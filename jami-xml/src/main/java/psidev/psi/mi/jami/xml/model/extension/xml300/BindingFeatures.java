package psidev.psi.mi.jami.xml.model.extension.xml300;

import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceLocator;
import psidev.psi.mi.jami.model.Feature;
import psidev.psi.mi.jami.model.ModelledEntity;
import psidev.psi.mi.jami.model.ModelledFeature;
import psidev.psi.mi.jami.xml.XmlEntryContext;
import psidev.psi.mi.jami.xml.cache.PsiXmlIdCache;
import psidev.psi.mi.jami.xml.model.extension.PsiXmlLocator;
import psidev.psi.mi.jami.xml.model.extension.XmlModelledFeature;
import psidev.psi.mi.jami.xml.model.reference.AbstractFeatureRef;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class that wraps a list of linkedFeatures which all bind together
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>23/05/14</pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(namespace = "http://psi.hupo.org/mi/mif300")
public class BindingFeatures implements FileSourceContext, Locatable {

    private PsiXmlLocator sourceLocator;
    @XmlLocation
    @XmlTransient
    private Locator locator;

    private JAXBFeatureRefList jaxbFeatureRefs;
    private List<ModelledFeature> linkedFeatures;

    /**
     * <p>Constructor for BindingFeatures.</p>
     */
    public BindingFeatures() {
        XmlEntryContext.getInstance().registerBindingFeature(this);
    }

    /** {@inheritDoc} */
    @Override
    public Locator sourceLocation() {
        return (Locator)getSourceLocator();
    }

    /**
     * <p>Getter for the field <code>sourceLocator</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.datasource.FileSourceLocator} object.
     */
    public FileSourceLocator getSourceLocator() {
        if (sourceLocator == null && locator != null){
            sourceLocator = new PsiXmlLocator(locator.getLineNumber(), locator.getColumnNumber(), null);
        }
        return sourceLocator;
    }

    /** {@inheritDoc} */
    public void setSourceLocator(FileSourceLocator sourceLocator) {
        if (sourceLocator == null){
            this.sourceLocator = null;
        }
        else if (sourceLocator instanceof PsiXmlLocator){
            this.sourceLocator = (PsiXmlLocator)sourceLocator;
        }
        else {
            this.sourceLocator = new PsiXmlLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
        }
    }

    /**
     * <p>Getter for the field <code>linkedFeatures</code>.</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    public Collection<ModelledFeature> getLinkedFeatures() {
        if (linkedFeatures == null){
            linkedFeatures = new ArrayList<ModelledFeature>();
        }
        return this.linkedFeatures;
    }

    /**
     * <p>getJAXBFeatureRefs.</p>
     *
     * @return a {@link java.util.List} object.
     */
    @XmlElement(name = "participantFeatureRef", type = Integer.class, required = true, namespace = "http://psi.hupo.org/mi/mif300")
    public List<Integer> getJAXBFeatureRefs() {
        if (this.jaxbFeatureRefs == null){
            this.jaxbFeatureRefs = new JAXBFeatureRefList();
        }
        return jaxbFeatureRefs;
    }

    ////////////////////////////////////////////////////////////////// classes

    /**
     * The participant feature ref list used by JAXB to populate feature refs
     */
    private class JAXBFeatureRefList extends ArrayList<Integer>{

        public JAXBFeatureRefList(){
            super();
        }

        @Override
        public boolean add(Integer val) {
            if (val == null){
                return false;
            }
            return getLinkedFeatures().add(new ParticipantFeatureRef(val));
        }

        @Override
        public boolean addAll(Collection<? extends Integer> c) {
            if (c == null){
                return false;
            }
            boolean added = false;

            for (Integer a : c){
                if (add(a)){
                    added = true;
                }
            }
            return added;
        }

        @Override
        public void add(int index, Integer element) {
            addToSpecificIndex(index, element);
        }

        @Override
        public boolean addAll(int index, Collection<? extends Integer> c) {
            int newIndex = index;
            if (c == null){
                return false;
            }
            boolean add = false;
            for (Integer a : c){
                if (addToSpecificIndex(newIndex, a)){
                    newIndex++;
                    add = true;
                }
            }
            return add;
        }

        private boolean addToSpecificIndex(int index, Integer val) {
            if (val == null){
                return false;
            }
            ((ArrayList<ModelledFeature>) getLinkedFeatures()).add(index, new ParticipantFeatureRef(val));
            return true;
        }

        @Override
        public String toString() {
            return "Inferred Interaction Experiment List: "+(getSourceLocator() != null ? getSourceLocator().toString():super.toString());
        }
    }

    ///////////////////////////////////////////
    /**
     * Experiment ref for experimental interactor
     */
    private class ParticipantFeatureRef extends AbstractFeatureRef<ModelledEntity, ModelledFeature> implements ModelledFeature{
        public ParticipantFeatureRef(int ref) {
            super(ref);
        }

        public boolean resolve(PsiXmlIdCache parsedObjects) {
            if (parsedObjects.containsFeature(this.ref)){
                Feature obj = parsedObjects.getFeature(this.ref);
                if (obj != null){
                    BindingFeatures.this.getLinkedFeatures().remove(this);
                    BindingFeatures.this.getLinkedFeatures().add((ModelledFeature)obj);
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "Binding feature Reference: "+ref+(getSourceLocator() != null ? ", "+getSourceLocator().toString():super.toString());
        }

        @Override
        protected void initialiseFeatureDelegate() {
            XmlModelledFeature modelled = new XmlModelledFeature();
            modelled.setId(this.ref);
            setDelegate(modelled);
        }

        public FileSourceLocator getSourceLocator() {
            return sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator locator) {
            throw new UnsupportedOperationException("Cannot set the source locator of a binding feature ref");
        }
    }
}
