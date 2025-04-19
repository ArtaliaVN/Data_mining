package Data.Processing.DataMining.Service;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class DataTestingService {

    public Evaluation evaluation(Instances trainingDataset, Instances testingDataset, Classifier classifier) throws Exception{
        trainingDataset.setClassIndex(trainingDataset.numAttributes() - 1);
        testingDataset.setClassIndex(testingDataset.numAttributes() - 1);
        Evaluation eval = new Evaluation(trainingDataset);
        eval.evaluateModel(classifier, testingDataset);
        return eval;
    }

}