package psidev.psi.mi.jami.tab.extension.datasource;

import psidev.psi.mi.jami.datasource.InteractionEvidenceStream;
import psidev.psi.mi.jami.exception.MIIOException;
import psidev.psi.mi.jami.model.FeatureEvidence;
import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.jami.model.ParticipantEvidence;
import psidev.psi.mi.jami.tab.io.iterator.MitabInteractionEvidenceIterator;
import psidev.psi.mi.jami.tab.io.parser.InteractionEvidenceLineParser;

import java.io.*;
import java.net.URL;
import java.util.Iterator;

/**
 * A mitab datasource that loads interaction evidences (full experimental details)
 * It only provides an iterator of the interactions
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>25/06/13</pre>
 */
public class MitabEvidenceStreamSource extends AbstractMitabStreamSource<InteractionEvidence, ParticipantEvidence, FeatureEvidence> implements InteractionEvidenceStream<InteractionEvidence> {

    /**
     * <p>Constructor for MitabEvidenceStreamSource.</p>
     */
    public MitabEvidenceStreamSource() {
        super();
    }

    /**
     * <p>Constructor for MitabEvidenceStreamSource.</p>
     *
     * @param file a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public MitabEvidenceStreamSource(File file) throws IOException {
        super(file);
    }

    /**
     * <p>Constructor for MitabEvidenceStreamSource.</p>
     *
     * @param input a {@link java.io.InputStream} object.
     */
    public MitabEvidenceStreamSource(InputStream input) {
        super(input);
    }

    /**
     * <p>Constructor for MitabEvidenceStreamSource.</p>
     *
     * @param reader a {@link java.io.Reader} object.
     */
    public MitabEvidenceStreamSource(Reader reader) {
        super(reader);
    }

    /** {@inheritDoc} */
    @Override
    protected void initialiseMitabLineParser(Reader reader) {
        if (reader == null){
            throw new IllegalArgumentException("The reader cannot be null.");
        }
        setOriginalReader(reader);
        setLineParser(new InteractionEvidenceLineParser(reader));
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
        setLineParser(new InteractionEvidenceLineParser(input));
    }

    /** {@inheritDoc} */
    @Override
    protected Iterator<InteractionEvidence> createMitabIterator() throws MIIOException {
        return new MitabInteractionEvidenceIterator(getLineParser());
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
}
