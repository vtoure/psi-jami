package psidev.psi.mi.jami.enricher.impl;

import psidev.psi.mi.jami.bridges.fetcher.BioactiveEntityFetcher;
import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.util.EnricherUtils;
import psidev.psi.mi.jami.model.BioactiveEntity;

/**
 * Provides maximum updating of the BioactiveEntity.
 * It extends the functionality of the minimum updater.
 * As an updater, values from the provided BioactiveEntity to enrich may be overwritten.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since 07/08/13
 */
public class FullBioactiveEntityUpdater
        extends MinimalBioactiveEntityUpdater {

    /**
     * A constructor which initiates with a fetcher.
     * @param fetcher   The fetcher to use to gather bioactive entity records.
     */
    public FullBioactiveEntityUpdater(BioactiveEntityFetcher fetcher) {
        super(fetcher);
    }

    @Override
    protected boolean isFullEnrichment() {
        return true;
    }

    @Override
    protected void processChecksums(BioactiveEntity bioactiveEntityToEnrich, BioactiveEntity fetched) throws EnricherException {
        EnricherUtils.mergeChecksums(bioactiveEntityToEnrich, bioactiveEntityToEnrich.getChecksums(), fetched.getChecksums(), true,
                getListener());
    }

    @Override
    protected void processXrefs(BioactiveEntity bioactiveEntityToEnrich, BioactiveEntity fetched) {
        EnricherUtils.mergeXrefs(bioactiveEntityToEnrich, bioactiveEntityToEnrich.getXrefs(), fetched.getXrefs(), true, false,
                getListener(), getListener());    }
}