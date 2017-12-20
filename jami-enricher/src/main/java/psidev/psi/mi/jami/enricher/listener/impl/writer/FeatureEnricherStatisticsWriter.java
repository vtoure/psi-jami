package psidev.psi.mi.jami.enricher.listener.impl.writer;

import psidev.psi.mi.jami.enricher.listener.FeatureEnricherListener;
import psidev.psi.mi.jami.model.*;

import java.io.File;
import java.io.IOException;

/**
 * A statistics logger which records changes made by the enricher.
 * Each addition, removal or update is counted and, upon the completion of the enrichment of the object,
 * is logged in either a file of successes or failures depending on the enrichmentStatus.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 18/07/13

 */
public class FeatureEnricherStatisticsWriter<T extends Feature>
        extends EnricherStatisticsWriter<T>
        implements FeatureEnricherListener<T> {


    private static final String FILE_NAME = "feature";
    /**
     * Uses the known name of the JamiObject type as the seed to generate names for the success an failure log files.
     *
     * @throws java.io.IOException      Thrown if a problem is encountered with file location.
     */
    public FeatureEnricherStatisticsWriter() throws IOException {
        super(FILE_NAME);
    }

    /**
     * Creates the files from the provided seed file name with 'success' and 'failure' appended.
     *
     * @param fileName          The seed to base the names of the files on.
     * @throws java.io.IOException      Thrown if a problem is encountered with file location.
     */
    public FeatureEnricherStatisticsWriter(String fileName) throws IOException {
        super(fileName);
    }

    /**
     * Uses the provided names to create the files for successful and failed enrichment logging.
     *
     * @param successFileName   The exact name for the file to log successful enrichments in
     * @param failureFileName   The exact name for the file to log failed enrichments in
     * @throws java.io.IOException      Thrown if a problem is encountered with file location.
     */
    public FeatureEnricherStatisticsWriter(String successFileName, String failureFileName) throws IOException {
        super(successFileName, failureFileName);
    }

    /**
     * Uses the exact files provided to log successful and failed enrichments.
     *
     * @param successFile       The file to log successful enrichments in
     * @param failureFile       The file to log failed enrichments in.
     * @throws java.io.IOException      Thrown if a problem is encountered with file location.
     */
    public FeatureEnricherStatisticsWriter(File successFile, File failureFile) throws IOException {
        super(successFile, failureFile);
    }


    // ================================================================

    /** {@inheritDoc} */
    public void onShortNameUpdate(T feature, String oldShortName) {
        checkObject(feature);
        incrementUpdateCount();
    }

    /** {@inheritDoc} */
    public void onFullNameUpdate(T feature, String oldFullName) {
        checkObject(feature);
        incrementUpdateCount();
    }

    /** {@inheritDoc} */
    public void onInterproUpdate(T feature, String oldInterpro) {
        checkObject(feature);
        incrementUpdateCount();
    }

    /** {@inheritDoc} */
    public void onTypeUpdate(T feature, CvTerm oldType) {
        checkObject(feature);
        incrementUpdateCount();
    }

    /** {@inheritDoc} */
    public void onAddedIdentifier(T feature, Xref added) {
        checkObject(feature);
        incrementAdditionCount();
    }

    /** {@inheritDoc} */
    public void onRemovedIdentifier(T feature, Xref removed) {
        checkObject(feature);
        incrementRemovedCount();
    }

    /** {@inheritDoc} */
    public void onAddedXref(T feature, Xref added) {
        checkObject(feature);
        incrementAdditionCount();
    }

    /** {@inheritDoc} */
    public void onRemovedXref(T feature, Xref removed) {
        checkObject(feature);
        incrementRemovedCount();
    }

    /** {@inheritDoc} */
    public void onAddedAnnotation(T feature, Annotation added) {
        checkObject(feature);
        incrementAdditionCount();
    }

    /** {@inheritDoc} */
    public void onRemovedAnnotation(T feature, Annotation removed) {
        checkObject(feature);
        incrementRemovedCount();
    }

    /** {@inheritDoc} */
    public void onAddedRange(T feature, Range added) {
        checkObject(feature);
        incrementAdditionCount();
    }

    /** {@inheritDoc} */
    public void onRemovedRange(T feature, Range removed) {
        checkObject(feature);
        incrementRemovedCount();
    }

    /** {@inheritDoc} */
    public void onUpdatedRangePositions(T feature, Range range, Position position, Position position2) {
        checkObject(feature);
        incrementUpdateCount();
    }

    /** {@inheritDoc} */
    public void onRoleUpdate(T feature, CvTerm oldRole) {
        checkObject(feature);
        incrementUpdateCount();
    }

    /**
     * <p>onAddedLinkedFeature.</p>
     *
     * @param feature a T object.
     * @param added a T object.
     */
    public void onAddedLinkedFeature(T feature, T added) {
        checkObject(feature);
        incrementAdditionCount();
    }

    /**
     * <p>onRemovedLinkedFeature.</p>
     *
     * @param feature a T object.
     * @param removed a T object.
     */
    public void onRemovedLinkedFeature(T feature, T removed) {
        checkObject(feature);
        incrementRemovedCount();
    }


    /** {@inheritDoc} */
    public void onAddedAlias(T o, Alias added) {
        checkObject(o);
        incrementAdditionCount();
    }

    /** {@inheritDoc} */
    public void onRemovedAlias(T o, Alias removed) {
        checkObject(o);
        incrementRemovedCount();
    }
}
