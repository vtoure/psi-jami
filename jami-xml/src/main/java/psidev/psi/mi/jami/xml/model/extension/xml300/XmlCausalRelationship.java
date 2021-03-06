package psidev.psi.mi.jami.xml.model.extension.xml300;

import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceLocator;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Entity;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.xml.cache.PsiXmlIdCache;
import psidev.psi.mi.jami.xml.model.extension.PsiXmlLocator;
import psidev.psi.mi.jami.xml.model.extension.XmlCvTerm;
import psidev.psi.mi.jami.xml.model.extension.XmlOpenCvTerm;
import psidev.psi.mi.jami.xml.model.extension.XmlParticipant;
import psidev.psi.mi.jami.xml.model.reference.AbstractParticipantRef;
import psidev.psi.mi.jami.xml.utils.PsiXmlUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * XML 3.0 implementation of causal relationship
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>30/05/14</pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
public class XmlCausalRelationship implements ExtendedPsiXmlCausalRelationship,FileSourceContext,Locatable {

    private PsiXmlLocator sourceLocator;

    @XmlLocation
    @XmlTransient
    private Locator locator;

    private CvTerm relationType;
    private Entity target;

    private Entity source;

    /**
     * <p>Constructor for XmlCausalRelationship.</p>
     */
    public XmlCausalRelationship(){

    }

    /**
     * <p>Constructor for XmlCausalRelationship.</p>
     *
     * @param relationType a {@link psidev.psi.mi.jami.model.CvTerm} object.
     * @param target a {@link psidev.psi.mi.jami.model.Participant} object.
     */
    public XmlCausalRelationship(CvTerm relationType, Participant target){
        if (relationType == null){
            throw new IllegalArgumentException("The relationType in a CausalRelationship cannot be null");
        }
        this.relationType = relationType;

        if (target == null){
            throw new IllegalArgumentException("The participat target in a CausalRelationship cannot be null");
        }
        this.target = target;
    }

    /**
     * <p>Getter for the field <code>relationType</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.model.CvTerm} object.
     */
    public CvTerm getRelationType() {
        if (this.relationType == null){
            this.relationType = new XmlCvTerm(PsiXmlUtils.UNSPECIFIED);
        }
        return relationType;
    }

    /**
     * <p>Getter for the field <code>target</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.model.Entity} object.
     */
    public Entity getTarget() {
        if (this.target == null){
            this.target = new XmlParticipant();
        }
        return target;
    }

    /** {@inheritDoc} */
    @Override
    public Entity getSource() {
        if (this.source == null){
            this.source = new XmlParticipant();
            this.source.getCausalRelationships().add(this);
        }
        return source;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return relationType.toString() + ": " + target.toString();
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
     * <p>setSourceLocation.</p>
     *
     * @param sourceLocator a {@link psidev.psi.mi.jami.xml.model.extension.PsiXmlLocator} object.
     */
    public void setSourceLocation(PsiXmlLocator sourceLocator) {
        this.sourceLocator = sourceLocator;
    }

    /**
     * <p>setJAXBSourceParticipant.</p>
     *
     * @param ref a int.
     */
    @XmlElement(name = "sourceParticipantRef", required = true)
    public void setJAXBSourceParticipant(int ref){
        this.source = new SourceParticipantRef(ref);
    }

    /**
     * <p>setJAXBRelationType.</p>
     *
     * @param value a {@link psidev.psi.mi.jami.xml.model.extension.XmlOpenCvTerm} object.
     */
    @XmlElement(name = "causalityStatement", required = true)
    public void setJAXBRelationType(XmlOpenCvTerm value){
        this.relationType = value;
    }

    /**
     * <p>setJAXBTargetParticipant.</p>
     *
     * @param ref a int.
     */
    @XmlElement(name = "targetParticipantRef", required = true)
    public void setJAXBTargetParticipant(int ref){
        this.target = new TargetParticipantRef(ref);
    }

    ///////////////////////////////////////////////////////////////

    /**
     * participant ref for target of relationship
     */
    private class TargetParticipantRef extends AbstractParticipantRef{
        private PsiXmlLocator sourceLocator;

        public TargetParticipantRef(int ref) {
            super(ref);
        }

        public boolean resolve(PsiXmlIdCache parsedObjects) {
            if (parsedObjects.containsParticipant(this.ref)){
                Entity object = parsedObjects.getParticipant(this.ref);
                if (object != null){
                    target = object;
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "Target participant ref: "+ref+(getSourceLocator() != null ? ", "+getSourceLocator().toString():super.toString());
        }

        @Override
        protected void initialiseParticipantDelegate() {
            XmlParticipant modelled = new XmlParticipant();
            modelled.setId(this.ref);
            setDelegate(modelled);
        }

        public FileSourceLocator getSourceLocator() {
            return this.sourceLocator;
        }

        public void setSourceLocator(FileSourceLocator sourceLocator) {
            if (sourceLocator == null){
                this.sourceLocator = null;
            }
            else{
                this.sourceLocator = new PsiXmlLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null);
            }
        }

        public void setSourceLocator(PsiXmlLocator sourceLocator) {
            this.sourceLocator = sourceLocator;
        }
    }

    /**
     * participant ref for source of relationship
     */
    private class SourceParticipantRef extends AbstractParticipantRef{
        private PsiXmlLocator sourceLocator;

        public SourceParticipantRef(int ref) {
            super(ref);
        }

        public boolean resolve(PsiXmlIdCache parsedObjects) {
            if (parsedObjects.containsParticipant(this.ref)){
                Entity object = parsedObjects.getParticipant(this.ref);
                if (object != null){
                    object.getCausalRelationships().add(XmlCausalRelationship.this);
                    source = object;
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "Source participant ref: "+ref+(getSourceLocator() != null ? ", "+getSourceLocator().toString():super.toString());
        }

        @Override
        protected void initialiseParticipantDelegate() {
            XmlParticipant modelled = new XmlParticipant();
            modelled.setId(this.ref);
            setDelegate(modelled);
        }

        public FileSourceLocator getSourceLocator() {
            return this.sourceLocator;
        }

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

        public void setSourceLocator(PsiXmlLocator sourceLocator) {
            this.sourceLocator = sourceLocator;
        }
    }
}
