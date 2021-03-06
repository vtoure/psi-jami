package psidev.psi.mi.jami.utils.comparator.participant;

import psidev.psi.mi.jami.model.Complex;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.ModelledEntity;
import psidev.psi.mi.jami.model.ModelledFeature;
import psidev.psi.mi.jami.utils.comparator.feature.DefaultModelledFeatureComparator;

import java.util.*;

/**
 * Default biological entity comparator.
 * It will compare the basic properties of a biological entity using DefaultEntityBaseComparator.
 *
 * This comparator will ignore all the other properties of a biological entity.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>13/02/13</pre>
 */
public class DefaultModelledEntityComparator {

    /**
     * Use DefaultModelledEntityComparator to know if two biological entities are equals.
     *
     * @param bioParticipant1 a {@link psidev.psi.mi.jami.model.ModelledEntity} object.
     * @param bioParticipant2 a {@link psidev.psi.mi.jami.model.ModelledEntity} object.
     * @return true if the two biological entities are equal
     * @param checkComplexesAsInteractors a boolean.
     */
    public static boolean areEquals(ModelledEntity bioParticipant1, ModelledEntity bioParticipant2, boolean checkComplexesAsInteractors){
        Map<Complex, Set<Interactor>> processedComplexes = new IdentityHashMap<Complex, Set<Interactor>>();

        if (bioParticipant1 == bioParticipant2){
            return true;
        }
        else if (bioParticipant1 == null || bioParticipant2 == null){
            return false;
        }
        else {
            boolean ignoreInteractors = false;
            if (!checkComplexesAsInteractors){
                if (checkIfComplexAlreadyProcessed(bioParticipant1, bioParticipant2, processedComplexes)
                        || checkIfComplexAlreadyProcessed(bioParticipant2, bioParticipant1, processedComplexes)){
                    ignoreInteractors = true;
                }
            }

            if (!DefaultEntityBaseComparator.areEquals(bioParticipant1, bioParticipant2, ignoreInteractors)){
                return false;
            }

            // then compares the features
            Collection<ModelledFeature> features1 = bioParticipant1.getFeatures();
            Collection<ModelledFeature> features2 = bioParticipant2.getFeatures();

            if (features1.size() != features2.size()){
                 return false;
            }
            else {
                Iterator<ModelledFeature> f1Iterator = new ArrayList<ModelledFeature>(features1).iterator();
                Collection<ModelledFeature> f2List = new ArrayList<ModelledFeature>(features2);

                while (f1Iterator.hasNext()){
                    ModelledFeature f1 = f1Iterator.next();
                    ModelledFeature f2ToRemove = null;
                    for (ModelledFeature f2 : f2List){
                        if (DefaultModelledFeatureComparator.areEquals(f1, f2)){
                            f2ToRemove = f2;
                            break;
                        }
                    }
                    if (f2ToRemove != null){
                        f2List.remove(f2ToRemove);
                        f1Iterator.remove();
                    }
                    else {
                        return false;
                    }
                }

                if (f1Iterator.hasNext() || !f2List.isEmpty()){
                    return false;
                }
                else{
                    return true;
                }
            }
        }
    }

    private static boolean checkIfComplexAlreadyProcessed(ModelledEntity bioParticipant1, ModelledEntity bioParticipant2, Map<Complex, Set<Interactor>> processedComplexes) {
        Complex complex = null;
        if (bioParticipant1.getInteractor() instanceof Complex){
            complex = (Complex) bioParticipant1.getInteractor();
        }

        // we already processed complex1 as first interactor
        if (complex != null && processedComplexes.containsKey(complex)){
            Set<Interactor> interactorSet = processedComplexes.get(complex);
            // already processed this pair
            if (interactorSet.contains(bioParticipant2.getInteractor())){
                return true;
            }
            else{
                interactorSet.add(bioParticipant2.getInteractor());
            }
        }

        return false;
    }
}
