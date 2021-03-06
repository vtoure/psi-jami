package psidev.psi.mi.jami.utils.comparator.cooperativity;

import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.comparator.cv.DefaultCvTermComparator;
import psidev.psi.mi.jami.utils.comparator.participant.DefaultExactModelledEntityComparator;

/**
 * Default exact comparator for Allostery
 *
 * It will first compare basic cooperative effect properties using DefaultExactCooperativeEffectBaseComparator.
 * Then, it will compare the allosteric effector types :
 * - molecule effector types always come first
 * - if both allosteric effector are molecule effectors, it will use DefaultExactMoleculeEffectorComparator to compare them
 * - if both allosteric effector are feature modification effectors, it will use DefaultFeatureModificationEffectorComparator to compare them
 * Then, it will compare the allosteric mechanisms using DefaultCvTermComparator
 * Then, it will compare the allostery type using DefaultCvTermComparator
 * Finally, it will compare the allosteric molecule using DefaultExactModelledParticipantComparator
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>31/05/13</pre>
 */
public class DefaultExactAllosteryComparator {

    /**
     * Use DefaultExactAllosteryComparator to know if two allostery are equals.
     *
     * @param allostery1 a {@link psidev.psi.mi.jami.model.Allostery} object.
     * @param allostery2 a {@link psidev.psi.mi.jami.model.Allostery} object.
     * @return true if the two Allostery are equal
     */
    public static boolean areEquals(Allostery allostery1, Allostery allostery2){
        if (allostery1 == allostery2){
            return true;
        }
        else if (allostery1 == null || allostery2 == null){
            return false;
        }
        else {
            if (!DefaultCooperativeEffectBaseComparator.areEquals(allostery1, allostery2)){
                return false;
            }

            // first compare allosteric effector
            AllostericEffector effector1 = allostery1.getAllostericEffector();
            AllostericEffector effector2 = allostery2.getAllostericEffector();

            // both effectors are molecules
            if (effector1.getEffectorType().equals(AllostericEffectorType.molecule) && effector2.getEffectorType().equals(AllostericEffectorType.molecule)){
                if (!DefaultExactMoleculeEffectorComparator.areEquals((MoleculeEffector) effector1, (MoleculeEffector) effector2)){
                    return false;
                }
            }
            // effector 1 is molecule, comes first
            else if (effector1.getEffectorType().equals(AllostericEffectorType.molecule) || effector2.getEffectorType().equals(AllostericEffectorType.molecule)){
                return false;
            }
            else {
                if (!DefaultFeatureModificationEffectorComparator.areEquals((FeatureModificationEffector) effector1, (FeatureModificationEffector) effector2)){
                    return false;
                }
            }

            // same allosteric effector, compare mechanism
            CvTerm mechanism1 = allostery1.getAllostericMechanism();
            CvTerm mechanism2 = allostery2.getAllostericMechanism();
            if (!DefaultCvTermComparator.areEquals(mechanism1, mechanism2)){
                return false;
            }

            // compare allostery type
            CvTerm type1 = allostery1.getAllosteryType();
            CvTerm type2 = allostery2.getAllosteryType();
            if (!DefaultCvTermComparator.areEquals(type1, type2)){
                return false;
            }

            // compare allosteric molecule
            ModelledEntity mol1 = allostery1.getAllostericMolecule();
            ModelledEntity mol2 = allostery2.getAllostericMolecule();
            return DefaultExactModelledEntityComparator.areEquals(mol1, mol2, true);
        }
    }
}
