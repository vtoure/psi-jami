package psidev.psi.mi.jami.enricher;

import psidev.psi.mi.jami.bridges.fetcher.FeatureEvidenceFetcher;
import psidev.psi.mi.jami.enricher.impl.featureevidence.listener.FeatureEvidenceEnricherListener;
import psidev.psi.mi.jami.model.FeatureEvidence;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Gabriel Aldam (galdam@ebi.ac.uk)
 * Date: 13/06/13
 * Time: 16:31
 */
public interface FeatureEvidenceEnricher {

    public void enrichFeatureEvidence(FeatureEvidence featureEvidence);

    public void setFeatureEvidenceFetcher(FeatureEvidenceFetcher featureEvidenceFetcher);
    public FeatureEvidenceFetcher getFeatureEvidenceFetcher();

    public void setFeatureEvidenceEnricherListener(FeatureEvidenceEnricherListener featureEvidenceEnricherListener);
    public FeatureEvidenceEnricherListener getFeatureEvidenceEnricherListener();
}
