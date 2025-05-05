package Data.Processing.DataMining.Service;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class DataTestingService {

    public Evaluation evaluation(Instances trainingDataset, Instances testingDataset, Classifier classifier, String[] options) throws Exception{
        Evaluation eval = new Evaluation(trainingDataset);
        eval.evaluateModel(classifier, testingDataset);
        return eval;
    }

}
