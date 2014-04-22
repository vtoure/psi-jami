package psidev.psi.mi.jami.xml.io.writer.elements.impl.extended;

import psidev.psi.mi.jami.binary.BinaryInteraction;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.xml.cache.PsiXmlObjectCache;
import psidev.psi.mi.jami.xml.model.extension.InferredInteraction;
import psidev.psi.mi.jami.xml.io.writer.elements.*;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.compact.CompactXmlParticipantWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Compact XML 2.5 writer for a basic binary interaction (ignore experimental details) which has a fullname
 * and aliases in addition to a shortname
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>15/11/13</pre>
 */

public class CompactXml25BasicBinaryInteractionWriter extends AbstractXml25InteractionWriter<BinaryInteraction,Participant> implements CompactPsiXmlElementWriter<BinaryInteraction> {

    public CompactXml25BasicBinaryInteractionWriter(XMLStreamWriter writer, PsiXmlObjectCache objectIndex) {
        super(writer, objectIndex, new CompactXmlParticipantWriter(writer, objectIndex));
    }

    public CompactXml25BasicBinaryInteractionWriter(XMLStreamWriter writer, PsiXmlObjectCache objectIndex,
                                                    PsiXmlElementWriter<Alias> aliasWriter, PsiXmlXrefWriter primaryRefWriter,
                                                    PsiXmlXrefWriter secondaryRefWriter, PsiXmlExperimentWriter experimentWriter,
                                                    PsiXmlParticipantWriter<Participant> participantWriter, PsiXmlElementWriter<InferredInteraction> inferredInteractionWriter1,
                                                    PsiXmlElementWriter<CvTerm> interactionTypeWriter, PsiXmlElementWriter<Annotation> attributeWriter,
                                                    PsiXmlElementWriter<Checksum> checksumWriter) {
        super(writer, objectIndex, aliasWriter, primaryRefWriter, secondaryRefWriter, experimentWriter,
                participantWriter != null ? participantWriter : new CompactXmlParticipantWriter(writer, objectIndex), inferredInteractionWriter1, interactionTypeWriter, attributeWriter, checksumWriter);
    }

    @Override
    protected void writeAvailability(BinaryInteraction object) {
        // nothing to do
    }

    @Override
    protected void writeExperiments(BinaryInteraction object) throws XMLStreamException {
        writeExperimentRef();
    }

    @Override
    protected void writeOtherAttributes(BinaryInteraction object) {
        // nothing to do
    }

    @Override
    protected void writeModelled(BinaryInteraction object) {
        // do nothing
    }

    @Override
    protected void writeParameters(BinaryInteraction object) {
        // nothing to do
    }

    @Override
    protected void writeConfidences(BinaryInteraction object) {
        // nothing to do
    }

    @Override
    protected void writeNegative(BinaryInteraction object) {
        // nothing to do
    }

    @Override
    protected void writeAttributes(BinaryInteraction object) throws XMLStreamException {
        // write attributes
        if (!object.getAnnotations().isEmpty()){
            // write start attribute list
            getStreamWriter().writeStartElement("attributeList");
            for (Object ann : object.getAnnotations()){
                getAttributeWriter().write((Annotation)ann);
            }
            for (Object c : object.getChecksums()){
                getChecksumWriter().write((Checksum)c);
            }
            // write complex expansion if any
            if (object.getComplexExpansion() != null){
                super.writeAttribute(object.getComplexExpansion().getShortName(), object.getComplexExpansion().getMIIdentifier());
            }
            // write end attributeList
            getStreamWriter().writeEndElement();
        }
        // write checksum
        else if (!object.getChecksums().isEmpty()){
            // write start attribute list
            getStreamWriter().writeStartElement("attributeList");
            for (Object c : object.getChecksums()){
                getChecksumWriter().write((Checksum)c);
            }
            // write complex expansion if any
            if (object.getComplexExpansion() != null){
                super.writeAttribute(object.getComplexExpansion().getShortName(), object.getComplexExpansion().getMIIdentifier());
            }
            // write end attributeList
            getStreamWriter().writeEndElement();
        }
        // write complex expansion if any
        else if (object.getComplexExpansion() != null){
            // write start attribute list
            getStreamWriter().writeStartElement("attributeList");
            super.writeAttribute(object.getComplexExpansion().getShortName(), object.getComplexExpansion().getMIIdentifier());
            // write end attributeList
            getStreamWriter().writeEndElement();
        }
    }
}
