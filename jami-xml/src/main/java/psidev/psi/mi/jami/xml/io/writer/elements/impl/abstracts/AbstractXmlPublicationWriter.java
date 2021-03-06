package psidev.psi.mi.jami.xml.io.writer.elements.impl.abstracts;

import org.apache.commons.lang.StringUtils;
import psidev.psi.mi.jami.exception.MIIOException;
import psidev.psi.mi.jami.model.Annotation;
import psidev.psi.mi.jami.model.CurationDepth;
import psidev.psi.mi.jami.model.Publication;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.AnnotationUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlElementWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlPublicationWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlXrefWriter;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.XmlAnnotationWriter;
import psidev.psi.mi.jami.xml.utils.PsiXmlUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Collection;
import java.util.Iterator;

/**
 * Abstract Xml writer for publications (bibref objects)
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>11/11/13</pre>
 */
public abstract class AbstractXmlPublicationWriter implements PsiXmlPublicationWriter {
    private XMLStreamWriter streamWriter;
    private PsiXmlXrefWriter xrefWriter;
    private PsiXmlElementWriter<Annotation> attributeWriter;

    /**
     * <p>Constructor for AbstractXmlPublicationWriter.</p>
     *
     * @param writer a {@link javax.xml.stream.XMLStreamWriter} object.
     */
    public AbstractXmlPublicationWriter(XMLStreamWriter writer){
        if (writer == null){
            throw new IllegalArgumentException("The XML stream writer is mandatory for the XmlPublicationWriter");
        }
        this.streamWriter = writer;
    }

    /**
     * <p>Getter for the field <code>xrefWriter</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlXrefWriter} object.
     */
    public PsiXmlXrefWriter getXrefWriter() {
        if (this.xrefWriter == null){
            initialiseXrefWriter();
        }
        return xrefWriter;
    }

    /**
     * <p>initialiseXrefWriter.</p>
     */
    protected abstract void initialiseXrefWriter();

    /**
     * <p>Setter for the field <code>xrefWriter</code>.</p>
     *
     * @param xrefWriter a {@link psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlXrefWriter} object.
     */
    public void setXrefWriter(PsiXmlXrefWriter xrefWriter) {
        this.xrefWriter = xrefWriter;
    }

    /**
     * <p>Getter for the field <code>attributeWriter</code>.</p>
     *
     * @return a {@link psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlElementWriter} object.
     */
    public PsiXmlElementWriter<Annotation> getAttributeWriter() {
        if (this.attributeWriter == null){
            this.attributeWriter = new XmlAnnotationWriter(streamWriter);

        }
        return attributeWriter;
    }

    /**
     * <p>Setter for the field <code>attributeWriter</code>.</p>
     *
     * @param attributeWriter a {@link psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlElementWriter} object.
     */
    public void setAttributeWriter(PsiXmlElementWriter<Annotation> attributeWriter) {
        this.attributeWriter = attributeWriter;
    }

    /** {@inheritDoc} */
    @Override
    public void write(Publication object) throws MIIOException {
        try {
            // write start
            this.streamWriter.writeStartElement("bibref");

            // write xref
            writeBibrefContent(object);

            // write end publication
            this.streamWriter.writeEndElement();

        } catch (XMLStreamException e) {
            throw new MIIOException("Impossible to write the publication : "+object.toString(), e);
        }
    }

    /** {@inheritDoc} */
    public void writeAllPublicationAttributes(Publication object){
        try{
            boolean hasTitle = object.getTitle() != null;
            boolean hasJournal = object.getJournal() != null;
            boolean hasPublicationDate = object.getPublicationDate() != null;
            boolean hasCurationDepth = !CurationDepth.undefined.equals(object.getCurationDepth());
            boolean hasAuthors = !object.getAuthors().isEmpty();
            boolean hasAttributes = !object.getAnnotations().isEmpty();
            // write attributes if no identifiers available
            if (hasTitle || hasJournal || hasPublicationDate || hasCurationDepth || hasAuthors || hasAttributes){
                // write start attribute list
                this.streamWriter.writeStartElement("attributeList");
                // write publication properties such as title, journal, etc..
                writePublicationPropertiesAsAttributes(object, hasTitle, hasJournal, hasPublicationDate, hasCurationDepth, hasAuthors);
                // write normal attributes
                if (hasAttributes){
                    Iterator<Annotation> annotIterator = object.getAnnotations().iterator();
                    while (annotIterator.hasNext()){
                        Annotation ann = annotIterator.next();
                        getAttributeWriter().write(ann);
                    }
                }
                // write end attributeList
                this.streamWriter.writeEndElement();
            }
        } catch (XMLStreamException e) {
            throw new MIIOException("Impossible to write the publication attributes for : "+object.toString(), e);
        }
    }

    /** {@inheritDoc} */
    public void writeAllPublicationAttributes(Publication object, Collection<Annotation> attributesToFilter){
        try{
            boolean hasTitle = object.getTitle() != null;
            boolean hasJournal = object.getJournal() != null;
            boolean hasPublicationDate = object.getPublicationDate() != null;
            boolean hasCurationDepth = !CurationDepth.undefined.equals(object.getCurationDepth());
            boolean hasAuthors = !object.getAuthors().isEmpty();
            boolean hasAttributes = !object.getAnnotations().isEmpty();
            for (Annotation filter : attributesToFilter){
                // Filter title
                if (AnnotationUtils.doesAnnotationHaveTopic(filter, Annotation.PUBLICATION_TITLE_MI, Annotation.PUBLICATION_TITLE)){
                    hasTitle = false;
                }
                // Filter journal
                else if (AnnotationUtils.doesAnnotationHaveTopic(filter, Annotation.PUBLICATION_JOURNAL_MI, Annotation.PUBLICATION_JOURNAL)){
                    hasJournal = false;
                }
                // Filter publication date
                else if (AnnotationUtils.doesAnnotationHaveTopic(filter, Annotation.PUBLICATION_YEAR_MI, Annotation.PUBLICATION_YEAR)){
                    hasPublicationDate = false;
                }
                // Filtercuration depth
                else if (AnnotationUtils.doesAnnotationHaveTopic(filter, Annotation.CURATION_DEPTH_MI, Annotation.CURATION_DEPTH)){
                    hasCurationDepth = false;
                }
                // Filter authors
                else if (AnnotationUtils.doesAnnotationHaveTopic(filter, Annotation.AUTHOR_MI, Annotation.AUTHOR)){
                    hasAuthors = false;
                }
            }
            // write attributes if no identifiers available
            if (hasTitle || hasJournal || hasPublicationDate || hasCurationDepth || hasAuthors){
                // write publication properties such as title, journal, etc..
                writePublicationPropertiesAsAttributes(object, hasTitle, hasJournal, hasPublicationDate, hasCurationDepth, hasAuthors);
                // write normal attributes
                if (hasAttributes){
                    Iterator<Annotation> annotIterator = object.getAnnotations().iterator();
                    while (annotIterator.hasNext()){
                        Annotation ann = annotIterator.next();
                        if (!attributesToFilter.contains(ann)){
                            getAttributeWriter().write(ann);
                        }
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new MIIOException("Impossible to write the publication attributes for : "+object.toString(), e);
        }
    }

    /**
     * <p>writePublicationPropertiesAsAttributes.</p>
     *
     * @param object a {@link psidev.psi.mi.jami.model.Publication} object.
     * @param hasTitle a boolean.
     * @param hasJournal a boolean.
     * @param hasPublicationDate a boolean.
     * @param hasCurationDepth a boolean.
     * @param hasAuthors a boolean.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    protected void writePublicationPropertiesAsAttributes(Publication object, boolean hasTitle, boolean hasJournal, boolean hasPublicationDate, boolean hasCurationDepth, boolean hasAuthors) throws XMLStreamException {
        if (hasTitle){
            writeAnnotation(Annotation.PUBLICATION_TITLE, Annotation.PUBLICATION_TITLE_MI, object.getTitle());
        }
        if (hasJournal){
            writeAnnotation(Annotation.PUBLICATION_JOURNAL, Annotation.PUBLICATION_JOURNAL_MI, object.getJournal());
        }
        if (hasPublicationDate){
            writeAnnotation(Annotation.PUBLICATION_YEAR, Annotation.PUBLICATION_YEAR_MI, PsiXmlUtils.YEAR_FORMAT.format(object.getPublicationDate()));
        }
        if (hasCurationDepth){
            switch (object.getCurationDepth()){
                case rapid_curation:
                    writeAnnotation(Annotation.CURATION_DEPTH, Annotation.CURATION_DEPTH_MI, Annotation.RAPID_CURATION);
                    writeAnnotation(Annotation.RAPID_CURATION, Annotation.RAPID_CURATION_MI, null);
                    break;
                case IMEx:
                    writeAnnotation(Annotation.CURATION_DEPTH, Annotation.CURATION_DEPTH_MI, Annotation.IMEX_CURATION);
                    writeAnnotation(Annotation.IMEX_CURATION, Annotation.IMEX_CURATION_MI, null);
                    break;
                case MIMIx:
                    writeAnnotation(Annotation.CURATION_DEPTH, Annotation.CURATION_DEPTH_MI, Annotation.MIMIX_CURATION);
                    writeAnnotation(Annotation.MIMIX_CURATION, Annotation.MIMIX_CURATION_MI, null);
                    break;
                default:
                    break;
            }
        }
        if (hasAuthors){
            writeAnnotation(Annotation.AUTHOR, Annotation.AUTHOR_MI, StringUtils.join(object.getAuthors(), ", "));
        }
    }

    /**
     * <p>writeAnnotation.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param nameAc a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    protected void writeAnnotation(String name, String nameAc, String value) throws XMLStreamException {
        // write start
        this.streamWriter.writeStartElement("attribute");
        // write topic (not null)
        this.streamWriter.writeAttribute("name", name);
        if (nameAc != null){
            this.streamWriter.writeAttribute("nameAc", nameAc);
        }
        // write description (not null)
        if (value != null){
            this.streamWriter.writeCharacters(value);
        }

        // write end attribute
        this.streamWriter.writeEndElement();
    }

    /**
     * <p>writeXrefFromPublicationXrefs.</p>
     *
     * @param object a {@link psidev.psi.mi.jami.model.Publication} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    protected void writeXrefFromPublicationXrefs(Publication object) throws XMLStreamException {
        Iterator<Xref> refIterator = object.getXrefs().iterator();
        // default qualifier is null as we are not processing identifiers
        getXrefWriter().setDefaultRefType(null);
        getXrefWriter().setDefaultRefTypeAc(null);
        // write start xref
        this.streamWriter.writeStartElement("xref");

        int index = 0;
        while (refIterator.hasNext()){
            Xref ref = refIterator.next();
            // write primaryRef
            if (index == 0){
                getXrefWriter().write(ref,"primaryRef");
                index++;
            }
            // write secondaryref
            else{
                getXrefWriter().write(ref,"secondaryRef");
                index++;
            }
        }

        // write end xref
        this.streamWriter.writeEndElement();
    }

    /**
     * <p>writeXrefFromPublicationIdentifiers.</p>
     *
     * @param object a {@link psidev.psi.mi.jami.model.Publication} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    protected void writeXrefFromPublicationIdentifiers(Publication object) throws XMLStreamException {
        // write start xref
        this.streamWriter.writeStartElement("xref");

        String pubmed = object.getPubmedId();
        String doi = object.getDoi();
        Xref pubmedXref = null;
        Xref doiXref = null;
        if (pubmed != null || doi != null){
            for (Xref ref : object.getIdentifiers()){
                // ignore pubmed that is already written
                if (pubmed != null && pubmed.equals(ref.getId())
                        && XrefUtils.isXrefFromDatabase(ref, Xref.PUBMED_MI, Xref.PUBMED)){
                    pubmed = null;
                    pubmedXref = ref;
                }
                // ignore doi that is already writter
                else if (doi != null && doi.equals(ref.getId())
                        && XrefUtils.isXrefFromDatabase(ref, Xref.DOI_MI, Xref.DOI)){
                    doi = null;
                    doiXref = ref;
                }
            }
        }

        boolean hasWrittenPrimaryRef = false;
        // write primaryRef
        if (pubmedXref != null){
            writePrimaryRef(getXrefWriter(), pubmedXref,"primaryRef");
            hasWrittenPrimaryRef = true;
            if (doiXref != null){
                writePrimaryRef(getXrefWriter(), doiXref,"secondaryRef");
            }
        }
        else if (doiXref != null){
            writePrimaryRef(getXrefWriter(), doiXref,"primaryRef");
            hasWrittenPrimaryRef = true;
        }

        // write secondaryRefs (and primary ref if not done already)
        Iterator<Xref> refIterator = object.getIdentifiers().iterator();
        // default qualifier is identity
        getXrefWriter().setDefaultRefType(Xref.IDENTITY);
        getXrefWriter().setDefaultRefTypeAc(Xref.IDENTITY_MI);
        while (refIterator.hasNext()){
            Xref ref = refIterator.next();
            // ignore pubmed that is already written
            // ignore doi that is already written
            if (ref != pubmedXref && ref != doiXref){
                if (!hasWrittenPrimaryRef){
                    hasWrittenPrimaryRef = true;
                    getXrefWriter().write(ref,"primaryRef");
                }
                else{
                    getXrefWriter().write(ref,"secondaryRef");
                }
            }
        }

        // write other xrefs
        if (!object.getXrefs().isEmpty()){
            // default qualifier is null
            getXrefWriter().setDefaultRefType(null);
            getXrefWriter().setDefaultRefTypeAc(null);
            for (Xref ref : object.getXrefs()){
                getXrefWriter().write(ref,"secondaryRef");
            }
        }

        // write end xref
        this.streamWriter.writeEndElement();
    }

    /**
     * <p>writePrimaryRef.</p>
     *
     * @param writer a {@link psidev.psi.mi.jami.xml.io.writer.elements.PsiXmlXrefWriter} object.
     * @param ref a {@link psidev.psi.mi.jami.model.Xref} object.
     * @param name a {@link java.lang.String} object.
     * @throws javax.xml.stream.XMLStreamException if any.
     */
    protected void writePrimaryRef(PsiXmlXrefWriter writer, Xref ref, String name) throws XMLStreamException {
        writer.setDefaultRefType(Xref.PRIMARY);
        writer.setDefaultRefTypeAc(Xref.PRIMARY_MI);
        writer.write(ref, name);
    }

    /**
     * <p>writeBibrefContent.</p>
     *
     * @param object a {@link psidev.psi.mi.jami.model.Publication} object.
     */
    protected abstract void writeBibrefContent(Publication object);

    /**
     * <p>Getter for the field <code>streamWriter</code>.</p>
     *
     * @return a {@link javax.xml.stream.XMLStreamWriter} object.
     */
    protected XMLStreamWriter getStreamWriter() {
        return streamWriter;
    }
}
