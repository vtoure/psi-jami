package psidev.psi.mi.jami.enricher.listener.impl.writer;


import psidev.psi.mi.jami.enricher.listener.InteractorPoolEnricherListener;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.InteractorPool;

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
public class InteractorPoolEnricherStatisticsWriter
        extends InteractorEnricherStatisticsWriter<InteractorPool>
        implements InteractorPoolEnricherListener {


    private static final String FILE_NAME = "Interactor_pool";

    /**
     * Uses the known name of the JamiObject type as the seed to generate names for the success an failure log files.
     *
     * @throws java.io.IOException      Thrown if a problem is encountered with file location.
     */
    public InteractorPoolEnricherStatisticsWriter() throws IOException {
        super(FILE_NAME);
    }

    /**
     * Creates the files from the provided seed file name with 'success' and 'failure' appended.
     *
     * @param fileName          The seed to base the names of the files on.
     * @throws java.io.IOException      Thrown if a problem is encountered with file location.
     */
    public InteractorPoolEnricherStatisticsWriter(String fileName) throws IOException {
        super(fileName);
    }

    /**
     * Uses the provided names to create the files for successful and failed enrichment logging.
     *
     * @param successFileName   The exact name for the file to log successful enrichments in
     * @param failureFileName   The exact name for the file to log failed enrichments in
     * @throws java.io.IOException      Thrown if a problem is encountered with file location.
     */
    public InteractorPoolEnricherStatisticsWriter(String successFileName, String failureFileName) throws IOException {
        super(successFileName, failureFileName);
    }

    /**
     * Uses the exact files provided to log successful and failed enrichments.
     *
     * @param successFile       The file to log successful enrichments in
     * @param failureFile       The file to log failed enrichments in.
     * @throws java.io.IOException      Thrown if a problem is encountered with file location.
     */
    public InteractorPoolEnricherStatisticsWriter(File successFile, File failureFile) throws IOException {
        super(successFile, failureFile);
    }

    /** {@inheritDoc} */
    public void onAddedInteractor(InteractorPool interactor, Interactor added) {
        checkObject(interactor);
        incrementAdditionCount();
    }

    /** {@inheritDoc} */
    public void onRemovedInteractor(InteractorPool interactor, Interactor removed) {
        checkObject(interactor);
        incrementRemovedCount();
    }


    // ================================================================
}
