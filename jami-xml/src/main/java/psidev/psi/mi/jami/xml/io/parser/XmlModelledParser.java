package psidev.psi.mi.jami.xml.io.parser;

import psidev.psi.mi.jami.datasource.DefaultFileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.model.InteractionCategory;
import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.xml.XmlEntryContext;
import psidev.psi.mi.jami.xml.exception.PsiXmlParserException;
import psidev.psi.mi.jami.xml.model.extension.PsiXmlLocator;
import psidev.psi.mi.jami.xml.utils.PsiXmlUtils;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

/**
 * Parser generating modelled interaction objects and ignore experimental details
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>16/10/13</pre>
 */
public class XmlModelledParser extends AbstractPsiXmlParser<ModelledInteraction> {
    /**
     * <p>Constructor for XmlModelledParser.</p>
     *
     * @param file a {@link java.io.File} object.
     */
    public XmlModelledParser(File file) {
        super(file);
    }

    /**
     * <p>Constructor for XmlModelledParser.</p>
     *
     * @param inputStream a {@link java.io.InputStream} object.
     */
    public XmlModelledParser(InputStream inputStream){
        super(inputStream);
    }

    /**
     * <p>Constructor for XmlModelledParser.</p>
     *
     * @param url a {@link java.net.URL} object.
     */
    public XmlModelledParser(URL url) {
        super(url);
    }

    /**
     * <p>Constructor for XmlModelledParser.</p>
     *
     * @param reader a {@link java.io.Reader} object.
     */
    public XmlModelledParser(Reader reader){
        super(reader);
    }

    /** {@inheritDoc} */
    @Override
    protected Unmarshaller createJAXBUnmarshaller() throws JAXBException {
        return JaxbUnmarshallerFactory.getInstance().createUnmarshaller(getVersion(), InteractionCategory.modelled);
    }

    /** {@inheritDoc} */
    @Override
    protected void parseAvailabilityList(XmlEntryContext entryContext) throws PsiXmlParserException {
        // read availabilityList
        Location availabilityList = getStreamReader().getLocation();
        try {
            if (getStreamReader().hasNext()){
                getStreamReader().next();
            }
        } catch (XMLStreamException e) {
            throw createPsiXmlExceptionFrom("Impossible to read the next availability in the availability list", e);
        }

        setCurrentElement(getNextPsiXmlStartElement());
        // process experiments. Each experiment will be loaded in entryContext so no needs to do something else
        if (getCurrentElement() != null){
            String evt = getCurrentElement();
            String name = null;
            // skip experimentDescription up to the end of experiment list
            while (evt != null && (name == null || (name != null && !PsiXmlUtils.AVAILABILITYLIST_TAG.equals(name)))) {
                while (evt != null && !getStreamReader().isEndElement()){
                    skipNextElement();
                    evt = getStreamReader().getLocalName();
                }

                if (evt != null && getStreamReader().isEndElement()){
                    name = getStreamReader().getLocalName();
                    skipNextElement();
                    evt = getStreamReader().getLocalName();
                }
            }
        }
        else{
            if (getListener() != null){
                FileSourceContext context = null;
                if (availabilityList != null){
                    context = new DefaultFileSourceContext(new PsiXmlLocator(availabilityList.getLineNumber(), availabilityList.getColumnNumber(), null));
                }
                getListener().onInvalidSyntax(context, new PsiXmlParserException("AvailabilityList elements does not contains any availability node. PSI-XML is not valid."));
            }
        }
        setCurrentElement(getNextPsiXmlStartElement());
    }
}
