package psidev.psi.mi.jami.tab.extension.datasource;

import psidev.psi.mi.jami.exception.MIIOException;
import psidev.psi.mi.jami.model.Feature;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.tab.io.iterator.MitabInteractionIterator;
import psidev.psi.mi.jami.tab.io.parser.InteractionLineParser;

import java.io.*;
import java.net.URL;
import java.util.Iterator;

/**
 * A mitab datasource that loads very basic interactions and ignore experimental details, source, confidence and experimental details
 * It will only provide an iterator of interactions.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>21/06/13</pre>
 */
public class LightMitabStreamSource extends AbstractMitabStreamSource<Interaction, Participant, Feature> {

    /**
     * <p>Constructor for LightMitabStreamSource.</p>
     */
    public LightMitabStreamSource() {
        super();
    }

    /**
     * <p>Constructor for LightMitabStreamSource.</p>
     *
     * @param file a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public LightMitabStreamSource(File file) throws IOException {
        super(file);
    }

    /**
     * <p>Constructor for LightMitabStreamSource.</p>
     *
     * @param input a {@link java.io.InputStream} object.
     */
    public LightMitabStreamSource(InputStream input) {
        super(input);
    }

    /**
     * <p>Constructor for LightMitabStreamSource.</p>
     *
     * @param reader a {@link java.io.Reader} object.
     */
    public LightMitabStreamSource(Reader reader) {
        super(reader);
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseMitabLineParser(Reader reader) {
        if (reader == null){
            throw new IllegalArgumentException("The reader cannot be null.");
        }
        setOriginalReader(reader);
        setLineParser(new InteractionLineParser(reader));
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseMitabLineParser(File file) {
        if (file == null){
            throw new IllegalArgumentException("The file cannot be null.");
        }
        else if (!file.canRead()){
            throw new IllegalArgumentException("Does not have the permissions to read the file "+file.getAbsolutePath());
        }
        setOriginalFile(file);
        try {
            InputStream stream = new BufferedInputStream(new FileInputStream(file));
            initialiseMitabLineParser(stream);
        } catch (FileNotFoundException e) {
            throw new MIIOException("Impossible to open the file " + file.getName());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseMitabLineParser(InputStream input) {
        setOriginalStream(input);
        setLineParser(new InteractionLineParser(input));
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseMitabLineParser(URL url) {
        if (url == null){
            throw new IllegalArgumentException("The url cannot be null.");
        }
        setOriginalURL(url);
        try {
            InputStream stream = new BufferedInputStream(url.openStream());
            initialiseMitabLineParser(stream);
        } catch (IOException e) {
            throw new MIIOException("Impossible to open the url " + url.toExternalForm());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected Iterator<Interaction> createMitabIterator() throws MIIOException{
        return new MitabInteractionIterator(getLineParser());
    }
}
